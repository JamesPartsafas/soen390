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
import java.util.NoSuchElementException;

/**
 * Service layer for Notification-related functionality.
 */
@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    private AppUserService appUserService;
    private SimpMessagingTemplate simpMessagingTemplate;
    private EmailService emailService;

    /**
     * Constructor to create an instance of the MessageService.
     * This is annotated by autowired for automatic dependency injection
     *
     * @param notificationRepository Used to interact with the Notification table in the database
     * @param appUserService         Used to interact with the AppUser service layer
     * @param simpMessagingTemplate  Used to send notification to a user via the WebSocket connection.
     * @param emailService           Used to send emails to a user
     */
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

    /**
     * Saves the notification in the database for the given recipient.
     *
     * @param notificationDTO The DTO representation of the notification to be saved.
     */
    public void saveNotification(NotificationDTO notificationDTO) {
        AppUser appUser;
        try {
            appUser = appUserService.getAppUser(notificationDTO.getRecipientId()).get();
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Recipient ID not valid");
        }

        saveNotification(notificationDTO, appUser);
    }

    /**
     * Saves a notification for a given AppUser using a NotificationDTO object.
     * Also sends the notification to the user via web socket and,
     * if the user has email notifications on, sends an email to the user's email address.
     *
     * @param notificationDTO the NotificationDTO object representing the notification to be saved
     * @param appUser         the AppUser who should receive the notification
     */
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

    /**
     * Update the seen flag of a notification and send the updated notification object to the user's specific channel.
     *
     * @param notificationDTO the NotificationDTO object containing the id of the notification to be updated
     * @param seenValue       the new seen flag value to be set for the notification
     */
    public void updateSeen(NotificationDTO notificationDTO, boolean seenValue) {
        Notification notification = notificationRepository.getNotificationById(notificationDTO.getId());

        notification.setSeen(seenValue);
        notificationRepository.save(notification);

        String recipientId = notification.getRecipient().getId().toString();
        simpMessagingTemplate.convertAndSendToUser(recipientId.toString(), "/specific/notification/" + recipientId, notificationDTO);
    }

    /**
     * Get the list of 5 most recent notifications for a specific user from the past 7 days.
     *
     * @param userId the id of the user whose notifications are to be fetched
     * @return a list of NotificationDTO objects representing the notifications
     */
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
