package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.message.Message;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    private Chat underTest;
    private Long id;
    private AppUser firstUser;
    private AppUser secondUser;
    private List<Message> messages;
    private Timestamp lastUpdated;

    @BeforeEach
    void setUp() {
        id = 1L;
        firstUser = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        secondUser =  new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        messages = new ArrayList<>();
        messages.add(new Message());
        lastUpdated = new Timestamp(System.currentTimeMillis());
        underTest = new Chat(id, firstUser, secondUser, lastUpdated);
        underTest.setMessages(messages);
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
    void getFirstUser() {
        assertEquals(firstUser, underTest.getFirstUser());
    }

    @Test
    void setFirstUser() {
        AppUser newFirstUser = new AppUser(3L, "Joe Man3", "1234", "joecandidate3@mail.com", Role.CANDIDATE);
        underTest.setFirstUser(newFirstUser);
        assertEquals(newFirstUser, underTest.getFirstUser());
    }

    @Test
    void getSecondUser() {
        assertEquals(secondUser, underTest.getSecondUser());
    }

    @Test
    void setSecondUser() {
        AppUser newSecondUser = new AppUser(3L, "Joe Man3", "1234", "joecandidate3@mail.com", Role.CANDIDATE);
        underTest.setSecondUser(newSecondUser);
        assertEquals(newSecondUser, underTest.getSecondUser());
    }

    @Test
    void getMessages() {
        assertEquals(1, underTest.getMessages().size());
    }

    @Test
    void setMessages() {
        underTest.setMessages(new ArrayList<>());
        assertEquals(0, underTest.getMessages().size());
    }

    @Test
    void getLastUpdated() {
        assertEquals(lastUpdated, underTest.getLastUpdated());
    }

    @Test
    void setLastUpdated() {
        Timestamp newLastCreated = new Timestamp(System.nanoTime());
        underTest.setLastUpdated(newLastCreated);
        assertEquals(newLastCreated, underTest.getLastUpdated());
    }

    @Test
    void testToString() {
        assertNotNull(underTest.toString());
    }
}