package com.soen.synapsis.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;
    private AppUserService appUserService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, AppUserService appUserService) {
        this.notificationRepository = notificationRepository;
        this.appUserService = appUserService;
    }

    public Long saveNotification(NotificationDTO notificationDTO) {
        AppUser appUser = appUserService.getAppUser(notificationDTO.getRecipient_id()).get();
        Notification notification = new Notification(appUser, notificationDTO.getText(), notificationDTO.getUrl(), false);
        notificationRepository.save(notification);
        return notification.getId();
    }

    public void updateSeen(NotificationDTO notificationDTO, boolean seenValue) {
        Notification notification = notificationRepository.getNotificationById(notificationDTO.getId());

        notification.setSeen(seenValue);
        notificationRepository.save(notification);
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
