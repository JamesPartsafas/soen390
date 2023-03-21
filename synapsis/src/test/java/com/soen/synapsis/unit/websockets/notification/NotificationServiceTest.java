package com.soen.synapsis.unit.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.settings.Settings;
import com.soen.synapsis.websockets.notification.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {
    private NotificationService underTest;

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private EmailService emailService;
    @Mock
    private AppUserService appUserService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new NotificationService(notificationRepository, appUserService, simpMessagingTemplate, emailService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveNotificationSuccessful() {
        AppUser appUser = new AppUser(1L, "name", "12345678", "name@mail.com", Role.CANDIDATE);
        appUser.setSettings(new Settings(1L, appUser, true, true, true));
        NotificationDTO notificationDTO = new NotificationDTO(1L, 1L, NotificationType.CONNECTION, "text", "url", false);

        underTest.saveNotification(notificationDTO, appUser);
        verify(notificationRepository).save(ArgumentMatchers.any(Notification.class));
        verify(simpMessagingTemplate).convertAndSendToUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(NotificationDTO.class));
    }

    @Test
    void saveNotificationEmailSentWhenUserHasSetting() {
        AppUser appUser = new AppUser(1L, "name", "12345678", "name@mail.com", Role.CANDIDATE);
        appUser.setSettings(new Settings(1L, appUser, true, true, true));
        NotificationDTO notification = new NotificationDTO(1L, 1L, NotificationType.CONNECTION, "text", "url", false);

        underTest.saveNotification(notification, appUser);

        verify(emailService).sendSimpleMessage(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    void saveNotificationEmailNotSentWhenUserDoesNotHaveSetting() {
        AppUser appUser = new AppUser(1L, "name", "12345678", "name@mail.com", Role.CANDIDATE);
        appUser.setSettings(new Settings(1L, appUser, true, true, false));
        NotificationDTO notification = new NotificationDTO(1L, 1L, NotificationType.CONNECTION, "text", "url", false);

        underTest.saveNotification(notification, appUser);

        verify(emailService, never()).sendSimpleMessage(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        );
    }

    @Test
    void saveNotificationFailsWhenRecipientUserIsInvalid() {
        NotificationDTO notification = new NotificationDTO(1L, 1L, NotificationType.CONNECTION, "text", "url", false);
        when(appUserService.getAppUser(1L)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(Exception.class, () -> {
            underTest.saveNotification(notification);
        });

        assertEquals("Recipient ID not valid", thrown.getMessage());
    }

    @Test
    void updateSeenSuccessful() {
        NotificationDTO notificationDTO = new NotificationDTO(1L, 1L, NotificationType.CONNECTION, "text", "url", false);
        AppUser appUser = new AppUser(1L, "name", "12345678", "name@mail.com", Role.CANDIDATE);
        Timestamp timestamp = new Timestamp(1);
        Notification notification = new Notification(1L, appUser, NotificationType.CONNECTION, "text", "url", false, timestamp);
        when(notificationRepository.getNotificationById(notificationDTO.getId())).thenReturn(notification);

        underTest.updateSeen(notificationDTO, true);
        verify(notificationRepository).save(ArgumentMatchers.any(Notification.class));
        verify(simpMessagingTemplate).convertAndSendToUser(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(NotificationDTO.class));
    }

    @Test
    void getNotificationByUserIdSuccessful() {
        underTest.getNotificationsByUserId(1L);
        verify(notificationRepository).getNotificationsByUserIdAfterTime(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Timestamp.class), ArgumentMatchers.any(PageRequest.class));
    }
}
