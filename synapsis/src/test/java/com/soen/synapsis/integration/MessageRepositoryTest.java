package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.ChatRepository;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageRepository;
import com.soen.synapsis.websockets.chat.message.ReportStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository underTest;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ChatRepository chatRepository;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        chatRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void getMessagesByChatAndSenderAndIdLessThanEqual() {
        AppUser sender = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        AppUser receiver = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        appUserRepository.saveAll(Arrays.asList(sender, receiver));

        Long chatId1 = 1L;
        Chat chat1 = new Chat(sender, receiver);
        chat1.setId(chatId1);

        Long chatId2 = 2L;
        Chat chat2 = new Chat(sender, receiver);
        chat2.setId(chatId2);

        chatRepository.saveAll(Arrays.asList(chat1, chat2));

        Message message1 = new Message(1L, chat1, "message 1", sender, false, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis() - 1000L)));
        Message message2 = new Message(2L, chat2, "message 2", sender, true, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis())));
        Message message3 = new Message(3L, chat1, "message 3", sender, true, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis() + 1000L)));
        Message message4 = new Message(4L, chat1, "message 4", sender, true, Timestamp.from(Instant.ofEpochSecond(System.currentTimeMillis() + 2000L)));

        underTest.saveAll(Arrays.asList(message1, message2, message3, message4));

        assertTrue(underTest.getMessagesByChatAndSenderAndIdLessThanEqual(chat1.getId(), sender.getId(), 3L).size() == 2);

    }

    @Test
    void findByReportStatus() {
        AppUser sender = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        AppUser receiver = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        appUserRepository.saveAll(Arrays.asList(sender, receiver));

        Long chatId1 = 1L;
        Chat chat1 = new Chat(sender, receiver);
        chat1.setId(chatId1);
        chatRepository.save(chat1);

        Message message1 = new Message(chat1, "message 1", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(chat1, "message 2", receiver, true, ReportStatus.REVIEWED, new Timestamp(System.currentTimeMillis()));
        Message message3 = new Message(chat1, "message 3", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));

        underTest.saveAll(Arrays.asList(message1, message2, message3));

        assertEquals(underTest.findByReportStatus(ReportStatus.REVIEWED).size(), 1);
    }

    @Test
    void findPreviousMessages() {
        AppUser sender = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        AppUser receiver = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        appUserRepository.saveAll(Arrays.asList(sender, receiver));

        Long chatId = 2L;
        Chat chat = new Chat(sender, receiver);
        chat.setId(chatId);
        chatRepository.save(chat);

        Message message1 = new Message(chat, "message 1", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message2 = new Message(chat, "message 2", receiver, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message3 = new Message(chat, "message 3", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message4 = new Message(chat, "message 4", receiver, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message5 = new Message(chat, "message 5", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message6 = new Message(chat, "message 6", sender, true, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()));
        Message message7 = new Message(chat, "message 7", sender, true, ReportStatus.REPORTED, new Timestamp(System.currentTimeMillis()));

        message1.setId(1L);
        message2.setId(2L);
        message3.setId(3L);
        message4.setId(4L);
        message5.setId(5L);
        message6.setId(6L);
        message7.setId(7L);

        underTest.saveAll(Arrays.asList(message1, message2, message3, message4, message5, message6, message7));

        assertTrue(underTest.findPreviousMessages(chatId, message7.getId(), PageRequest.of(0, 6)).size() > 1);
    }
}