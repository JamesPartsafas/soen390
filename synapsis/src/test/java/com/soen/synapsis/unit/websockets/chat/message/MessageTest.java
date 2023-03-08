package com.soen.synapsis.unit.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.message.Message;
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
    private Timestamp createdAt;

    @BeforeEach
    void setUp() {
        id = 1L;
        chat = new Chat();
        content = "content";
        sender = new AppUser("name", "password", "email@mail.com", Role.CANDIDATE);
        read = false;
        createdAt = new Timestamp(System.currentTimeMillis());
        underTest = new Message(id, chat, content, sender, read, createdAt);
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
    void testToString() {
        assertNotNull(underTest.toString());
    }
}