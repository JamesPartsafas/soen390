package com.soen.synapsis.unit.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.MessageDTO;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageRepository;
import com.soen.synapsis.websockets.chat.message.MessageService;
import com.soen.synapsis.websockets.chat.message.ReportStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        Message message = Mockito.mock(Message.class);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        underTest.saveMessage(chat, sender, new MessageDTO());

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

    @Test
    void setMessageReportStatusThrowsWhenMessageDoesNotExist() {
        when(messageRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class,
                () -> underTest.setMessageReportStatus(sender, 1L));
    }

    @Test
    void setMessageReportStatusThrowsWhenUserReportsOwnMessage() {
        Message message = new Message(chat, "message 1", sender);
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));
        assertThrows(IllegalStateException.class,
                () -> underTest.setMessageReportStatus(sender, 1L));
    }

    @Test
    void setMessageReportStatusThrowsWhenMessageIsReported() {
        AppUser receiver = new AppUser(2L, "Joe Man 2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        Message message = new Message(chat, "message 1", sender, false, ReportStatus.REPORTED, new Timestamp(System.currentTimeMillis()));
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));
        assertThrows(IllegalStateException.class,
                () -> underTest.setMessageReportStatus(receiver, 1L));
    }

    @Test
    void setMessageReportStatusSetsReportStatusToReported() {
        AppUser receiver = new AppUser(2L, "Joe Man 2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        Message message = new Message(chat, "message 1", sender, false, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        when(messageRepository.findById(anyLong())).thenReturn(Optional.of(message));
        underTest.setMessageReportStatus(receiver, 1L);
        assertEquals(message.getReportStatus(), ReportStatus.REPORTED);
        verify(messageRepository, atLeastOnce()).save(message);
    }

    @Test
    void getReportedMessagesReturnsListOfListMessages() {
        Long chatId = 1L;
        chat.setId(chatId);

        Message message1 = new Message(chat, "message 1", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(chat, "message 2", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message3 = new Message(chat, "message 3", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message4 = new Message(chat, "message 4", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message5 = new Message(chat, "message 5", sender, true, ReportStatus.REPORTED, new Timestamp(System.currentTimeMillis()));
        message5.setId(5L);

        when(messageRepository.findByReportStatus(ReportStatus.REPORTED)).thenReturn(Arrays.asList(message5));
        when(messageRepository.findPreviousMessages(eq(chatId), eq(5L), any(Pageable.class))).thenReturn(Arrays.asList(message1, message2, message3, message4, message5));

        List<List<Message>> result = underTest.getReportedMessages();
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).size(), 5);
    }
}