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

    /**
     * The run method contains the sendJobNotifications code to be executed in a separate thread, independently of the main thread of the application.
     */
    @Override
    public void run() {
        sendJobNotifications(this.job);
    }

    /**
     * Send a notification about a new job that matches the users' filter preferences.
     *
     * @param job the new created or edited job.
     */
    @Async
    public void sendJobNotifications(Job job) {
        List<JobFilter> jobFilters;
        if (job.getIsExternal()) {
            jobFilters = jobFilterRepository.findAllExternalJobFiltersMatchingJobTypeAndSearchTerm(job.getType(), job.getPosition().toLowerCase(), job.getCompany().toLowerCase());
        } else {
            jobFilters = jobFilterRepository.findAllInternalJobFiltersMatchingJobTypeAndSearchTerm(job.getType(), job.getPosition().toLowerCase(), job.getCompany().toLowerCase());
        }

        for (JobFilter jobFilter : jobFilters) {
            AppUser appUser = jobFilter.getAppUser();
            NotificationDTO notificationDTO = new NotificationDTO(
                    0L,
                    appUser.getId(),
                    NotificationType.JOB,
                    job.getCompany(),
                    "/job/" + job.getID(),
                    false
            );
            notificationService.saveNotification(notificationDTO, appUser);
        }
    }
}
