package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    @Autowired
    public JobService(JobRepository jobRepository, AppUserRepository appUserRepository, NotificationService notificationService) {
        this.jobRepository = jobRepository;
        this.appUserRepository = appUserRepository;
        this.notificationService = notificationService;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    public String createJob(JobRequest request) {

        if (request.getCreator().getRole() != Role.RECRUITER) {
            throw new IllegalStateException("This user is not a recruiter.");
        }

        AppUser creator = request.getCreator();
        String position = request.getPosition();
        String company = request.getCompany();
        String address = request.getAddress();
        String description = request.getDescription();
        JobType type = request.getType();
        int numAvailable = request.getNumAvailable();

        Job job = new Job(creator, position, company, address, description, type, numAvailable);
        jobRepository.save(job);

        sendJobNotifications(job.getID());

        return "redirect:/job/" + job.getID();
    }

    //This should be changed to however we are implementing suggested jobs
    private void sendJobNotifications(Long jobId) {
        for (AppUser appUser : appUserRepository.findAll()) {
            NotificationDTO notificationDTO = new NotificationDTO(
                    0L,
                    appUser.getId(),
                    NotificationType.JOB,
                    "You have a new job suggestion!",
                    "/job/" + jobId,
                    false,
                    ""
            );
            notificationService.saveNotification(notificationDTO, appUser);
        }
    }
}
