package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.websockets.chat.MessageDTO;
import com.soen.synapsis.websockets.chat.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageDTOTest {

    private MessageDTO underTest;
    private Long id;
    private String content;
    private MessageType type;
    private Long receiverId;
    private Long senderId;

    @BeforeEach
    void setUp() {
        id = 1L;
        content = "content";
        type = MessageType.TEXT;
        receiverId = 2L;
        senderId = 1L;
        underTest = new MessageDTO(id, content, type, receiverId, senderId);
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
    void getContent() {
        assertEquals(content, underTest.getContent());
    }

    @Test
    void setContent() {
        underTest.setContent("newContent");
        assertEquals("newContent", underTest.getContent());
    }

    @Test
    void getType() {
        assertEquals(type, underTest.getType());
    }

    @Test
    void setType() {
        underTest.setType(MessageType.ERROR);
        assertEquals(MessageType.ERROR, underTest.getType());
    }

    @Test
    void getReceiverId() {
        assertEquals(receiverId, underTest.getReceiverId());
    }

    @Test
    void setReceiverId() {
        underTest.setReceiverId(3L);
        assertEquals(3L, underTest.getReceiverId());
    }

    @Test
    void getSenderId() {
        assertEquals(senderId, underTest.getSenderId());
    }

    @Test
    void setSenderId() {
        underTest.setSenderId(4L);
        assertEquals(4L, underTest.getSenderId());
    }

    @Test
    void testToString() {
        assertNotNull(underTest.toString());
    }
}