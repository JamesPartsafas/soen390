package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.ChatRepository;
import com.soen.synapsis.websockets.chat.ChatService;
import com.soen.synapsis.websockets.chat.MessageDTO;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    private ChatService underTest;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageService messageService;
    @Mock
    private AppUserService appUserService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ChatService(chatRepository, messageService, appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findChatsByUserIdCallsChatRepositoryFindAllByUserId() {
        underTest.findChatsByUserId(1L);
        verify(chatRepository).findAllByUserId(1L);
    }

    @Test
    void findChatByIdCallsChatRepositoryFindById() {
        underTest.findChatById(1L);
        verify(chatRepository).findById(1L);
    }

    @Test
    void saveMessageThrowsWhenChatDoesNotExist() {
        when(chatRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class,
                () -> underTest.saveMessage(1L, null, new MessageDTO()));
    }

    @Test
    void saveMessageCallsMessageAndChatRepositorySaveWhenChatExist() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        Chat chat = new Chat();
        MessageDTO messageDTO = new MessageDTO();
        when(chatRepository.findById(any(Long.class))).thenReturn(Optional.of(chat));
        when(messageService.saveMessage(any(Chat.class), any(AppUser.class), any(MessageDTO.class))).thenReturn(new Message());
        underTest.saveMessage(1L, appUser, messageDTO);
        verify(messageService).saveMessage(chat, appUser, messageDTO);
        verify(chatRepository).save(chat);
    }

    @Test
    void updateReadCallsMessageServiceUpdateRead() {
        underTest.updateRead(1L, 2L, 3L);
        verify(messageService).updateRead(1L, 2L, 3L);
    }

    @Test
    void createChatReturnsRedirectHomePageWhenUserDoesNotExist() {
        when(appUserService.getAppUser(any(Long.class))).thenReturn(Optional.empty());
        assertEquals("redirect:/", underTest.createChat(null, 1L));
    }

    @Test
    void createChatReturnsRedirectChatPageWhenChatExists() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        Chat chat = new Chat();
        chat.setId(1L);
        when(appUserService.getAppUser(any(Long.class))).thenReturn(Optional.of(appUser));
        when(chatRepository.findByUserIds(any(Long.class), any(Long.class))).thenReturn(Optional.of(chat));
        assertEquals("redirect:/chat/1", underTest.createChat(appUser, 1L));
    }

    @Test
    void createChatCreateNewChatWhenChatDoesNotExist() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(appUserService.getAppUser(any(Long.class))).thenReturn(Optional.of(appUser));
        when(chatRepository.findByUserIds(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        underTest.createChat(appUser, 2L);
        verify(chatRepository).save(any(Chat.class));
    }

    @Test
    void setMessageReportStatusCallsTheMethodInMessageService() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        long messageID = 2L;
        underTest.setMessageReportStatus(appUser, messageID);
        verify(messageService).setMessageReportStatus(appUser, messageID);
    }

    @Test
    void getReportedMessagesCallsTheMethodInMessageService() {
        underTest.getReportedMessages();
        verify(messageService).getReportedMessages();
    }
}