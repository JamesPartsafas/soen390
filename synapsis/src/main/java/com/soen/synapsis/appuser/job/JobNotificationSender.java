package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public class JobNotificationSender implements Runnable {
    private Job job;
    private JobFilterRepository jobFilterRepository;
    private NotificationService notificationService;

    public JobNotificationSender(Job job, JobFilterRepository jobFilterRepository, NotificationService notificationService) {
        this.job = job;
        this.jobFilterRepository = jobFilterRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void run() {
        sendJobNotifications(this.job);
    }

    @Async
    public void sendJobNotifications(Job job) {
        List<JobFilter> jobFilters;
        if (job.getIsExternal()) {
            jobFilters = jobFilterRepository.findAllExternalJobFiltersMatchingJob(job.getType());
        } else {
            jobFilters = jobFilterRepository.findAllInternalJobFiltersMatchingJob(job.getType());
        }

        for (JobFilter jobFilter : jobFilters) {
            AppUser appUser = jobFilter.getAppUser();
            NotificationDTO notificationDTO = new NotificationDTO(
                    0L,
                    appUser.getId(),
                    NotificationType.JOB,
                    "You have a new job suggestion!",
                    "/job/" + job.getID(),
                    false
            );
            notificationService.saveNotification(notificationDTO, appUser);
        }
    }
}
