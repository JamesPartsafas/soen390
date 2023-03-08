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

@Controller
public class NotificationController {

    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/notification/{recipientId}")
    public void sendNotification(Authentication authentication, @DestinationVariable Long recipientId, NotificationDTO notification) {
        AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
        AppUser appUser = appUserAuth.getAppUser();

        if (appUser == null) {
            throw new IllegalStateException("Sender ID not valid");
        }
        notificationService.saveNotification(notification);
    }

    @MessageMapping("/notification/{recipientId}/seen")
    public void sendSeen(Authentication authentication, @DestinationVariable Long recipientId, NotificationDTO notification) {
        
        AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
        AppUser appUser = appUserAuth.getAppUser();

        if (appUser == null || !Objects.equals(recipientId, appUser.getId())) {
            throw new IllegalStateException("Recipient ID not valid");
        }

        notificationService.updateSeen(notification, true);
    }

    @GetMapping("/updateNotifications/{userId}")
    public String updateNotifications(@PathVariable String userId, Model model) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(Long.parseLong(userId));
        model.addAttribute("notificationList", notifications);

        return "fragments/notifications :: notification_list";
    }
}
