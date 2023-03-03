package com.soen.synapsis.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    private AppUserService appUserService;
    private SimpMessagingTemplate simpMessagingTemplate;
    private EmailService emailService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               AppUserService appUserService,
                               SimpMessagingTemplate simpMessagingTemplate,
                               EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.appUserService = appUserService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.emailService = emailService;
    }

    public void saveNotification(NotificationDTO notificationDTO) {
        AppUser appUser = appUserService.getAppUser(notificationDTO.getRecipient_id()).get();
        saveNotification(notificationDTO, appUser);
    }

    public void saveNotification(NotificationDTO notificationDTO, AppUser appUser) {
        Notification notification = new Notification(appUser, notificationDTO.getType(), notificationDTO.getText(), notificationDTO.getUrl(), false);
        notificationRepository.save(notification);
        notification.setId(notification.getId());

        String recipientId = notification.getRecipient().getId().toString();
        simpMessagingTemplate.convertAndSendToUser(recipientId, "/specific/notification/" + recipientId, notificationDTO);
        if (appUser.isEmailNotificationsOn()) {
            emailService.sendSimpleMessage(
                    appUser.getEmail(),
                    "You have a new " + notification.getType().toString(),
                    "Here is your new notification: \n\n" + notification.toString()
            );
        }
    }

    public void updateSeen(NotificationDTO notificationDTO, boolean seenValue) {
        Notification notification = notificationRepository.getNotificationById(notificationDTO.getId());

        notification.setSeen(seenValue);
        notificationRepository.save(notification);

        String recipientId = notification.getRecipient().getId().toString();
        simpMessagingTemplate.convertAndSendToUser(recipientId.toString(), "/specific/notification/" + recipientId, notificationDTO);
    }

    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.DAY_OF_WEEK, -7);
        time.setTime(cal.getTime().getTime());

        List<Notification> notifications = notificationRepository.getNotificationsByUserIdAfterTime(userId, time, PageRequest.of(0, 5));

        List<NotificationDTO> notificationDTOs = new ArrayList<NotificationDTO>();

        for (Notification notification : notifications) {
            notificationDTOs.add(NotificationDTO.notificationToDTO(notification));
        }

        return notificationDTOs;
    }
}
