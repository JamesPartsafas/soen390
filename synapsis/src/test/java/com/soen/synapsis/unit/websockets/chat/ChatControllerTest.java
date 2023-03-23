package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    private ChatController underTest;
    @Mock
    private ChatService chatService;
    @Mock
    private AuthService authService;
    @Mock
    private SimpMessageSendingOperations simpMessagingTemplate;
    private AutoCloseable autoCloseable;
    private AppUser user1;
    private AppUser user2;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ChatController(chatService, authService, simpMessagingTemplate);
        user1 = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        user2 = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createChatRedirectToHomePageWhenUserNotAuthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);
        assertEquals("redirect:/", underTest.createChat(1L));
    }

    @Test
    void createChatCallsChatServiceCreateChatWhenUserIsAuthenticated() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        underTest.createChat(1L);
        verify(chatService).createChat(appUser, 1L);
    }

    @Test
    void getChatsRedirectToHomePageWhenUserNotAuthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);
        assertEquals("redirect:/", underTest.getChats(Mockito.mock(Model.class)));
    }

    @Test
    void getChatsReturnsChatsPageWhenUserIsAuthenticated() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        assertEquals("pages/chatPage", underTest.getChats(Mockito.mock(Model.class)));
    }

    @Test
    void getChatByIdRedirectToHomePageWhenUserNotAuthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);
        assertEquals("redirect:/", underTest.getChatById(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getChatByIdRedirectToChatsPageWhenChatDoesNotExist() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(chatService.findChatById(any(Long.class))).thenReturn(Optional.empty());
        assertEquals("redirect:/chats", underTest.getChatById(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getChatByIdRedirectToChatsPageWhenChatDoesNotBelongToUser() {
        AppUser user3 = new AppUser(3L, "Joe Man3", "1234", "joecandidate3@mail.com", Role.CANDIDATE);
        Chat chat = new Chat(user1, user2);

        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(user3);
        when(chatService.findChatById(any(Long.class))).thenReturn(Optional.of(chat));
        assertEquals("redirect:/chats", underTest.getChatById(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getChatByIdCallsSendReadWithParticipantAsReceiverWhenAuthUserIsCreator() {
        Chat chat = new Chat(user1, user2);
        chat.setMessages(new ArrayList<>());

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        ChatController spyUnderTest = spy(underTest);

        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(user1);
        when(chatService.findChatById(any(Long.class))).thenReturn(Optional.of(chat));

        String resultPage = spyUnderTest.getChatById(1L, Mockito.mock(Model.class));

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(spyUnderTest).sendRead(any(Authentication.class), any(Long.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(capturedMessageDTO.getSenderId(), user1.getId());
        assertEquals(capturedMessageDTO.getReceiverId(), user2.getId());
        assertEquals("pages/messagingPage", resultPage);
    }

    @Test
    void getChatByIdCallsSendReadWithCreatorAsReceiverWhenAuthUserIsParticipant() {
        Chat chat = new Chat(user1, user2);
        chat.setMessages(new ArrayList<>());

        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user2));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        ChatController spyUnderTest = spy(underTest);

        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(user2);
        when(chatService.findChatById(any(Long.class))).thenReturn(Optional.of(chat));

        String resultPage = spyUnderTest.getChatById(1L, Mockito.mock(Model.class));

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(spyUnderTest).sendRead(any(Authentication.class), any(Long.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(capturedMessageDTO.getSenderId(), user2.getId());
        assertEquals(capturedMessageDTO.getReceiverId(), user1.getId());
        assertEquals("pages/messagingPage", resultPage);
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenMessageTypeIsNotText() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.READ, user2.getId(), user1.getId());

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenAuthUserIsNull() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(null));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.TEXT, user2.getId(), user1.getId());

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenAuthUserIsNotSenderUser() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.TEXT, user2.getId(), user2.getId());

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenContentIsNull() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO(null, MessageType.TEXT, user2.getId(), user1.getId(), null, null);

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenFileNameIsLargerThan50Bytes() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        String fileName = StringUtils.repeat("*", 51);
        MessageDTO messageDTO = new MessageDTO("uniqueContent", MessageType.TEXT, user2.getId(), user1.getId(), fileName, null);

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenFileInBase64IsLargerThan64600Bytes() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        String fileName = StringUtils.repeat("*", 49);
        String file = StringUtils.repeat("*", 64601);
        MessageDTO messageDTO = new MessageDTO("uniqueContent", MessageType.TEXT, user2.getId(), user1.getId(), fileName, file);

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageSendsAnErrorMessageWhenContentIsLargerThan255Bytes() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        String fileName = StringUtils.repeat("*", 49);
        String file = StringUtils.repeat("*", 64599);
        String content = StringUtils.repeat("*", 256);
        MessageDTO messageDTO = new MessageDTO(content, MessageType.TEXT, user2.getId(), user1.getId(), fileName, file);

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendMessageForwardsMessageWhenAuthUserIsSenderUser() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("uniqueContent", MessageType.TEXT, user2.getId(), user1.getId());

        underTest.sendMessage(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        verify(chatService).saveMessage(1L, user1, messageDTO);
        assertEquals(messageDTO.getContent(), capturedMessageDTO.getContent());
    }

    @Test
    void sendReadSendsAnErrorMessageWhenMessageTypeIsNotRead() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.TEXT, user2.getId(), user1.getId());

        underTest.sendRead(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendReadSendsAnErrorMessageWhenAuthUserIsNull() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(null));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.READ, user2.getId(), user1.getId());

        underTest.sendRead(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendReadSendsAnErrorMessageWhenAuthUserIsNotSenderUser() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO("", MessageType.READ, user2.getId(), user2.getId());

        underTest.sendRead(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        assertEquals(MessageType.ERROR, capturedMessageDTO.getType());
    }

    @Test
    void sendReadForwardsMessageWhenAuthUserIsSenderUser() {
        Authentication mockAuth = mock(Authentication.class);
        when(mockAuth.getPrincipal()).thenReturn(new AppUserDetails(user1));
        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        MessageDTO messageDTO = new MessageDTO(4L, "uniqueContent", MessageType.READ, user2.getId(), user1.getId());

        underTest.sendRead(mockAuth, 1L, messageDTO);

        ArgumentCaptor<MessageDTO> messageDTOCaptor = ArgumentCaptor.forClass(MessageDTO.class);
        verify(simpMessagingTemplate).convertAndSendToUser(any(String.class), any(String.class), messageDTOCaptor.capture());
        MessageDTO capturedMessageDTO = messageDTOCaptor.getValue();

        verify(chatService).updateRead(1L, user2.getId(), 4L);
        assertEquals(messageDTO.getContent(), capturedMessageDTO.getContent());
    }

    @Test
    void reportMessageRedirectToHomePageWhenUserIsNotAuth() {
        when(authService.isUserAuthenticated()).thenReturn(false);
        assertEquals("redirect:/", underTest.reportMessage(1L, 2L, Mockito.mock(Model.class)));
    }

    @Test
    void reportMessageCallsGetChatPageByIDWhenUserIsAuth() {
        long chatID = 1L;
        Model model = Mockito.mock(Model.class);
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        ChatController spyController = Mockito.spy(underTest);
        doReturn("").when(spyController).getChatById(Mockito.anyLong(), Mockito.any(Model.class));
        spyController.reportMessage(1L, chatID, model);
        verify(spyController, atLeastOnce()).getChatById(Mockito.anyLong(), Mockito.any(Model.class));
    }

    @Test
    void getReportMessageRedirectToHomePageWhenUserIsNotAuth() {
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(false);
        assertEquals("redirect:/", underTest.getReportMessage(Mockito.mock(Model.class)));
    }

    @Test
    void getReportMessageReturnsAdminMessagePageWhenUserIsAuth() {
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);
        assertEquals("pages/adminMessagesPage", underTest.getReportMessage(Mockito.mock(Model.class)));
    }

    @Test
    void ignoreReportWithoutAdminRedirects() {
        String redirect = "redirect:/";

        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(false);

        String returnValue = underTest.ignoreReport(1L);

        assertEquals(redirect, returnValue);
    }

    @Test
    void ignoreReportResolves() {
        String redirect = "redirect:/chats";
        Long messageId = 1L;

        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);

        String returnValue = underTest.ignoreReport(messageId);

        verify(chatService, times(1)).resolveReport(messageId);
        assertEquals(redirect, returnValue);
    }

    @Test
    void warnUserWithoutAdminRedirects() {
        String redirect = "redirect:/";

        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(false);

        String returnValue = underTest.warnUser(1L, 1L);

        assertEquals(redirect, returnValue);
    }

    @Test
    void warnUserResolvesAndCreatesChat() {
        Long senderId = 1L;
        Long messageId = 1L;

        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(user1);

        underTest.warnUser(senderId, messageId);

        verify(chatService, times(1)).resolveReport(messageId);
        verify(chatService, times(1)).createChat(user1, senderId);
    }
}