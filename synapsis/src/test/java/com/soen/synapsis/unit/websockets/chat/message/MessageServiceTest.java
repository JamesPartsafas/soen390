package com.soen.synapsis.unit.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageRepository;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    private MessageService underTest;
    @Mock
    private MessageRepository messageRepository;
    private AutoCloseable autoCloseable;

    private Chat chat;
    private AppUser sender;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new MessageService(messageRepository);
        chat = new Chat();
        sender = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void saveMessageCallsSaveMethod() {
        String content = "content";
        Message message = Mockito.mock(Message.class);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        underTest.saveMessage(chat, sender, content);

        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void updateRead() {
        Long chatId = 1L;
        chat.setId(chatId);

        Message message1 = new Message(chat, "message 1", sender);
        Message message2 = new Message(chat, "message 2", sender);
        Message message3 = new Message(chat, "message 3", sender);

        message1.setId(1L);
        message2.setId(2L);
        message3.setId(3L);

        List<Message> messagesToUpdate = Arrays.asList(message1, message3);
        when(messageRepository.getMessagesByChatAndSenderAndIdLessThanEqual(chatId, sender.getId(), 3L)).thenReturn(messagesToUpdate);

        underTest.updateRead(chatId, sender.getId(), 3L);

        verify(messageRepository, times(1)).saveAll(messagesToUpdate);
        assertTrue(messagesToUpdate.stream().allMatch(Message::isRead));
    }
}