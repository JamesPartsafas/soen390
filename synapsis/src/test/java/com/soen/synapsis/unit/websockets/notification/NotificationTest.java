package com.soen.synapsis.unit.websockets.notification;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.Notification;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    private Notification underTest;
    private Long id;
    private AppUser recipient;
    private NotificationType type;
    private String text;
    private String url;
    private Timestamp creationTime;


    @BeforeEach
    void setUp() {
        id = 1L;
        type = NotificationType.JOB;
        text = "Test text";
        url = "Test url";
        recipient = new AppUser("name", "12345678", "name@mail.com", Role.CANDIDATE);
        creationTime = new Timestamp(2020, 1, 1, 1, 1, 1, 1);
        underTest = new Notification(id, recipient, type, text, url, false, creationTime);
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
    void getRecipient() {
        assertEquals(recipient, underTest.getRecipient());
    }

    @Test
    void setRecipient() {
        AppUser newRecipient = new AppUser("name1", "12345678", "name@mail.com", Role.CANDIDATE);
        underTest.setRecipient(newRecipient);
        assertEquals(newRecipient, underTest.getRecipient());
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
    void getCreationTime() {
        assertEquals(creationTime, underTest.getCreationTime());
    }

    @Test
    void setCreationTime() {
        Timestamp newCreationTime = new Timestamp(2020, 1, 1, 1, 1, 1, 2);
        underTest.setCreationTime(newCreationTime);
        assertEquals(newCreationTime, underTest.getCreationTime());
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
    void testToString() {
        assertNotNull(underTest.toString());
    }
}
