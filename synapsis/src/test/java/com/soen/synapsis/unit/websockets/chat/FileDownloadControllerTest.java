package com.soen.synapsis.unit.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.chat.ChatController;
import com.soen.synapsis.websockets.chat.ChatService;
import com.soen.synapsis.websockets.chat.FileDownloadController;
import com.soen.synapsis.websockets.chat.FileHolder;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FileDownloadControllerTest {
    private FileDownloadController underTest;
    @Mock
    private AuthService authService;
    @Mock
    private MessageService messageService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new FileDownloadController(messageService, authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void anonymousUserReturnsNull() {
        when(authService.isUserAuthenticated()).thenReturn(false);

        ResponseEntity<Resource> response = underTest.downloadFile(1L);

        assertNull(response);
    }

    @Test
    void emptyFileReturnsNull() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(new AppUser("joe", "pass", "email", Role.CANDIDATE));
        when(messageService.retrieveFileHolderFromMessage(any(Long.class), any(AppUser.class))).thenReturn(new FileHolder(null, null));

        ResponseEntity<Resource> response = underTest.downloadFile(1L);

        assertNull(response);
    }

    @Test
    void authedUserReturnsFile() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(new AppUser("joe", "pass", "email", Role.CANDIDATE));
        when(messageService.retrieveFileHolderFromMessage(any(Long.class), any(AppUser.class))).thenReturn(new FileHolder("name", "data,123"));

        ResponseEntity<Resource> response = underTest.downloadFile(1L);

        assertNotNull(response);
    }
}