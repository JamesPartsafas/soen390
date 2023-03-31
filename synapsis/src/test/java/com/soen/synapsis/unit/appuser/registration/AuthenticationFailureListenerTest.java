package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.registration.AuthenticationFailureListener;
import com.soen.synapsis.appuser.registration.LoginAttemptService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

public class AuthenticationFailureListenerTest {
    private AuthenticationFailureListener underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private HttpServletRequest request;
    @Mock
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AuthenticationFailureListener(request, loginAttemptService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void onApplicationEventSuccessful() {
        when(request.getHeader("X-Forwarded-For")).thenReturn("1,");
        AuthenticationFailureBadCredentialsEvent event = mock(AuthenticationFailureBadCredentialsEvent.class);
        underTest.onApplicationEvent(event);

        verify(loginAttemptService).loginFailed("1");
    }
}
