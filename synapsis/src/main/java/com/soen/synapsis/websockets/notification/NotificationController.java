package com.soen.synapsis.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

/**
 * Entry point for user requests to interface with Notification-related functionality.
 */
@Controller
public class NotificationController {

    private NotificationService notificationService;

    /**
     * Constructor to create an instance of the NotificationController.
     * This is annotated by autowired for automatic dependency injection
     * @param notificationService Used to interact with the Notification service layer
     */
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Handles WebSocket messages for sending notifications to a specific recipient.
     * @param authentication an instance of the Authentication interface representing the user's authentication information
     * @param recipientId The ID of the recipient user where the notification will be sent to
     * @param notification a NotificationDTO object representing the content of the notification to be sent and saved
     */
    @MessageMapping("/notification/{recipientId}")
    public void sendNotification(Authentication authentication, @DestinationVariable Long recipientId, NotificationDTO notification) {
        AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
        AppUser appUser = appUserAuth.getAppUser();

        if (appUser == null) {
            throw new IllegalStateException("Sender ID not valid");
        }
        notificationService.saveNotification(notification);
    }

    /**
     * Handles WebSocket messages for marking a notification as seen by a sender.
     * @param authentication an instance of the Authentication interface representing the user's authentication information
     * @param recipientId The ID of the user who received the notification.
     * @param notification a NotificationDTO object representing the notification that will be marked as viewed
     */
    @MessageMapping("/notification/{recipientId}/seen")
    public void sendSeen(Authentication authentication, @DestinationVariable Long recipientId, NotificationDTO notification) {
        
        AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
        AppUser appUser = appUserAuth.getAppUser();

        if (appUser == null || !Objects.equals(recipientId, appUser.getId())) {
            throw new IllegalStateException("Recipient ID not valid");
        }

        notificationService.updateSeen(notification, true);
    }

    /**
     * Returns a list of notifications for a given user ID to update the notifications UI.
     * @param userId The ID of the user to retrieve notifications for.
     * @param model Allows for data to be passed to fragment.
     * @return The notification list template fragment.
     */
    @GetMapping("/updateNotifications/{userId}")
    public String updateNotifications(@PathVariable String userId, Model model) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(Long.parseLong(userId));
        model.addAttribute("notificationList", notifications);

        return "fragments/notifications :: notification_list";
    }
}
