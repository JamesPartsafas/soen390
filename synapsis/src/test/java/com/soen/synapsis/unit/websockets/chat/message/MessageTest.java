package com.soen.synapsis.unit.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.ReportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MessageTest {

    private Message underTest;

    private Long id;
    private Chat chat;
    private String content;
    private AppUser sender;
    private boolean read;
    private ReportStatus reportStatus;
    private Timestamp createdAt;
    private String fileName;
    private String file;

    @BeforeEach
    void setUp() {
        id = 1L;
        chat = new Chat();
        content = "content";
        sender = new AppUser("name", "password", "email@mail.com", Role.CANDIDATE);
        read = false;
        reportStatus = ReportStatus.REVIEWED;
        createdAt = new Timestamp(System.currentTimeMillis());
        fileName = "test_file_name.png";
        file = "iVBORw0KGgoAAAANSUhEUgAAAkUAAACBCAYAAADOmM5KAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAA";
        underTest = new Message(id, chat, content, sender, read, reportStatus, createdAt, fileName, file);
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
    void getChat() {
        assertEquals(chat, underTest.getChat());
    }

    @Test
    void setChat() {
        Chat newChat = new Chat();
        underTest.setChat(newChat);
        assertEquals(newChat, underTest.getChat());
    }

    @Test
    void getContent() {
        assertEquals(content, underTest.getContent());
    }

    @Test
    void setContent() {
        underTest.setContent("newContent");
        assertEquals("newContent", underTest.getContent());
    }

    @Test
    void getSender() {
        assertEquals(sender, underTest.getSender());
    }

    @Test
    void setSender() {
        AppUser newSender = new AppUser("name1", "password1", "email1@mail.com", Role.RECRUITER);
        underTest.setSender(newSender);
        assertEquals(newSender, underTest.getSender());
    }

    @Test
    void isRead() {
        assertEquals(read, underTest.isRead());
    }

    @Test
    void setRead() {
        underTest.setRead(true);
        assertEquals(true, underTest.isRead());
    }

    @Test
    void getReportStatus() {
        assertEquals(reportStatus, underTest.getReportStatus());
    }

    @Test
    void setReportStatus() {
        underTest.setReportStatus(ReportStatus.REPORTED);
        assertEquals(ReportStatus.REPORTED, underTest.getReportStatus());
    }

    @Test
    void getCreatedAt() {
        assertEquals(createdAt, underTest.getCreatedAt());
    }

    @Test
    void setCreatedAt() {
        Timestamp newCreatedAt = new Timestamp(System.nanoTime());
        underTest.setCreatedAt(newCreatedAt);
        assertEquals(newCreatedAt, underTest.getCreatedAt());
    }

    @Test
    void getFileName() {
        assertEquals(underTest.getFileName(), fileName);
    }

    @Test
    void setFileName() {
        underTest.setFileName("new.png");
        assertEquals(underTest.getFileName(), "new.png");
    }

    @Test
    void getFile() {
        assertEquals(underTest.getFile(), file);
    }

    @Test
    void setFile() {
        underTest.setFile("123r4t5y6u7i8");
        assertEquals(underTest.getFile(), "123r4t5y6u7i8");
    }

    @Test
    void testToString() {
        assertNotNull(underTest.toString());
    }
}