package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.ChatRepository;
import com.soen.synapsis.websockets.chat.message.MessageRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatRepositoryTest {

    @Autowired
    private ChatRepository underTest;
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        AppUser user1 = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        AppUser user2 = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        AppUser user3 = new AppUser(3L, "Joe Man3", "1234", "joecandidate3@mail.com", Role.CANDIDATE);
        appUserRepository.saveAllAndFlush(Arrays.asList(user1, user2, user3));

        Chat chat1 = new Chat(user1, user2);
        Chat chat2 = new Chat(user2, user3);
        Chat chat3 = new Chat(user3, user1);
        underTest.saveAllAndFlush(Arrays.asList(chat1, chat2, chat3));
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void findAllByUserIdReturnsListChatsAccessibleByTheUser() {
        assertEquals(2, underTest.findAllByUserId(2L).size());
    }

    @Test
    void findByUserIdsReturnsChatBetweenTwoUsersIfChatExist() {
        assertEquals(true, underTest.findByUserIds(3L, 2L).isPresent());
    }

    @Test
    void findByUserIdsDoesNotReturnChatBetweenTwoUsersIfChatNotExist() {
        assertEquals(false, underTest.findByUserIds(3L, 4L).isPresent());
    }
}