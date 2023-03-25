package com.soen.synapsis.unit.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.Notification;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationDTOTest {
    private NotificationDTO underTest;
    private Long id;
    private Long recipientId;
    private NotificationType type;
    private String text;
    private String url;


    @BeforeEach
    void setUp() {
        id = 1L;
        recipientId = 1L;
        type = NotificationType.JOB;
        text = "Test text";
        url = "Test url";
        underTest = new NotificationDTO(id, 1L, type, text, url, false);
    }

    @Test
    void getId() {
        assertEquals(id, underTest.getId());
    }

    @Test
    void setId() {
        underTest.setId(2L);
        assertEquals(2L, underTest.getId());
    }

    @Test
    void getRecipientId() {
        assertEquals(recipientId, underTest.getRecipientId());
    }

    @Test
    void setRecipientId() {
        underTest.setRecipientId(2L);
        assertEquals(2L, underTest.getRecipientId());
    }

    @Test
    void getText() {
        assertEquals(text, underTest.getText());
    }

    @Test
    void setText() {
        String newText = "New test text";
        underTest.setText(newText);
        assertEquals(newText, underTest.getText());
    }

    @Test
    void getUrl() {
        assertEquals(url, underTest.getUrl());
    }

    @Test
    void setUrl() {
        String newUrl = "New test url";
        underTest.setUrl(newUrl);
        assertEquals(newUrl, underTest.getUrl());
    }

    @Test
    void isSeen() {
        assertFalse(underTest.isSeen());
    }

    @Test
    void setSeen() {
        underTest.setSeen(true);
        assertTrue(underTest.isSeen());
    }

    @Test
    void getType() {
        assertEquals(NotificationType.JOB, underTest.getType());
    }

    @Test
    void setType() {
        NotificationType newNotificationType = NotificationType.CONNECTION;
        underTest.setType(newNotificationType);
        assertEquals(newNotificationType, underTest.getType());
    }

    @Test
    void notificationToDTO() {
        AppUser recipient = new AppUser("name", "12345678", "name@mail.com", Role.CANDIDATE);
        Timestamp creationTime = new Timestamp(2020, 1, 1, 1, 1, 1, 1);
        Notification notification = new Notification(id, recipient, type, text, url, false, creationTime);

        NotificationDTO newNotificationDTO = NotificationDTO.notificationToDTO(notification);
        assertEquals(newNotificationDTO.getId(), notification.getId());
        assertEquals(newNotificationDTO.getRecipientId(), notification.getRecipient().getId());
        assertEquals(newNotificationDTO.getType(), notification.getType());
        assertEquals(newNotificationDTO.getText(), notification.getText());
        assertEquals(newNotificationDTO.getUrl(), notification.getUrl());
        assertEquals(newNotificationDTO.isSeen(), notification.isSeen());
    }

    @Test
    void testToString() {
        assertNotNull(underTest.toString());
    }
}
