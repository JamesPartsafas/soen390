package com.soen.synapsis.unit.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.NotificationController;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationControllerTest {
    @Mock
    private NotificationService notificationService;

    @Mock
    private Authentication authentication;

    private AutoCloseable autoCloseable;
    private NotificationController underTest;
    private NotificationDTO notification;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new NotificationController(notificationService);
        notification = new NotificationDTO(0L, 1L, NotificationType.JOB, "text", "url", false);
        appUser = new AppUser(1l, "name", "12345678", "name@mail.com", Role.CANDIDATE);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void sendNotificationSuccessful() {
        when(authentication.getPrincipal()).thenReturn(new AppUserDetails(appUser));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        underTest.sendNotification(authentication, 1L, notification);

        verify(notificationService).saveNotification(ArgumentMatchers.any(NotificationDTO.class));
    }

    @Test
    void sendNotificationFailsWhenAuthenticatedUserIsNull() {
        when(authentication.getPrincipal()).thenReturn(new AppUserDetails(null));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exception thrown = assertThrows(Exception.class, () -> {
            underTest.sendNotification(authentication, 1L, notification);
        });

        assertEquals("Sender ID not valid", thrown.getMessage());
    }

    @Test
    void sendSeenSuccessful() {
        when(authentication.getPrincipal()).thenReturn(new AppUserDetails(appUser));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        underTest.sendSeen(authentication, 1L, notification);

        verify(notificationService).updateSeen(ArgumentMatchers.any(NotificationDTO.class), ArgumentMatchers.anyBoolean());
    }

    @Test
    void sendSeenFailsWhenUserIsNull() {
        when(authentication.getPrincipal()).thenReturn(new AppUserDetails(null));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Exception thrown = assertThrows(Exception.class, () -> {
            underTest.sendSeen(authentication, 1L, notification);
        });

        assertEquals("Recipient ID not valid", thrown.getMessage());
    }

    @Test
    void updateNotificationsSuccessful() {
        List<NotificationDTO> notificationDTOList = new ArrayList<NotificationDTO>();
        notificationDTOList.add(new NotificationDTO(1L, appUser.getId(), NotificationType.CONNECTION, "text", "url", false));
        when(notificationService.getNotificationsByUserId(appUser.getId())).thenReturn(notificationDTOList);

        assertEquals("fragments/notifications :: notification_list", underTest.updateNotifications(appUser.getId().toString(), Mockito.mock(Model.class)));
    }
}
