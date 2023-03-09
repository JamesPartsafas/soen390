package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * A service class to work with jobs.
 */
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, AppUserRepository appUserRepository, NotificationService notificationService) {
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.appUserRepository = appUserRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retrieve all the jobs.
     *
     * @return a list of jobs.
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Retrieve all the jobs submitted by a user.
     *
     * @param user the appuser.
     * @return a list of jobs.
     */
    public List<Job> getAllJobsAlreadySubmittedByUser(AppUser user) {
        return jobRepository.findAllJobApplicationsSubmittedByUserID(user.getId());
    }

    /**
     * Retrieve a job given an id.
     *
     * @param id the job id.
     * @return the job.
     */
    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    /**
     * Create a new job posting.
     *
     * @param request the job application request.
     * @return the job page.
     */
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
        boolean isExternal = request.getIsExternal();
        String externalLink = request.getExternalLink();
        boolean needResume = request.getNeedResume();
        boolean needCover = request.getNeedCover();
        boolean needPortfolio = request.getNeedPortfolio();


        Job job = new Job(creator, position, company, address, description, type, numAvailable, isExternal, externalLink, needResume, needCover, needPortfolio);
        jobRepository.save(job);

        return "redirect:/job/" + job.getID();
    }

    /**
     * Create a new job application.
     *
     * @param request the job application request.
     * @param applicant the user submitting the application.
     * @param jobId the job id.
     */
    public void createJobApplication(JobApplicationRequest request, AppUser applicant, Long jobId, MultipartFile resume, MultipartFile coverLetter) throws IOException {

        checkIfUserAlreadySubmittedApplication(applicant, jobId);

        if (applicant.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("You are not a candidate. Only candidates can submit application forms.");
        }

        Job job = jobRepository.getReferenceById(jobId);

        JobApplication jobApplication = new JobApplication(
                job,
                applicant,
                new Timestamp(System.currentTimeMillis()),
                JobApplicationStatus.SUBMITTED,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone(),
                request.getAddress(),
                request.getCity(),
                request.getCountry(),
                request.isLegallyAllowedToWork(),
                request.getLinks()
        );

        String encodedResume = Base64.getEncoder().encodeToString(resume.getBytes());
        if(encodedResume.isEmpty()) {
            throw new IllegalStateException("It is mandatory to upload your resume.");
        }
        jobApplication.setResume(encodedResume);

        if(job.getNeedCover()) {
            String encodedCoverLetter = Base64.getEncoder().encodeToString(coverLetter.getBytes());
            if (encodedCoverLetter.isEmpty()) {
                throw new IllegalStateException("It is mandatory to upload your cover letter.");
            }
            jobApplication.setCoverLetter(encodedCoverLetter);
        }

        job.setNumApplicants(job.getNumApplicants() + 1);
        jobRepository.save(job);

        jobApplicationRepository.save(jobApplication);
    }

    /**
     * Check if user already submitted the job application form.
     *
     * @param applicant the user submitting the application.
     * @param jobId the job id.
     */
    public void checkIfUserAlreadySubmittedApplication(AppUser applicant, Long jobId) {
        List<Job> jobsSubmitted = getAllJobsAlreadySubmittedByUser(applicant);
        for (Job job : jobsSubmitted) {
            if (job.getID() == jobId) {
                throw new IllegalStateException("You already submitted an application form for this job.");
            }
        }
    }

    /**
     * Delete a job.
     *
     * @param id the job id.
     * @return the jobs page.
     */
    public String deleteJob(Long id) {
        jobRepository.deleteById(id);
        return "redirect:/jobs";
    }

    /**
     * Edit a job posting.
     *
     * @param optionalJob the job to be edited.
     * @param request the job request.
     * @return the job page.
     */
    public String editJob(Optional<Job> optionalJob, JobRequest request) {

        Job job = optionalJob.get();

        if (job == null)
            return "redirect:/";

        job.setPosition(request.getPosition());
        job.setCompany(request.getCompany());
        job.setAddress(request.getAddress());
        job.setDescription(request.getDescription());
        job.setNumAvailable(request.getNumAvailable());
        job.setIsExternal(request.getIsExternal());
        job.setExternalLink(request.getExternalLink());
        job.setType(request.getType());
        job.setNeedResume(request.getNeedResume());
        job.setNeedCover(request.getNeedCover());
        job.setNeedPortfolio(request.getNeedPortfolio());

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
                    false
            );
            notificationService.saveNotification(notificationDTO, appUser);
        }
    }

}
