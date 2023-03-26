package com.soen.synapsis.unit.utility;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.utility.GlobalControllerAdvice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalControllerAdviceTest {

    @Mock
    private AuthService authService;
    private AutoCloseable autoCloseable;
    private GlobalControllerAdvice underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new GlobalControllerAdvice(authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    @Test
    void getUserIdWithoutAuthReturnsNull() {
        when(authService.isUserAuthenticated()).thenReturn(false);

        Long id = underTest.getUserId();

        assertNull(id);
    }

    @Test
    void getUserIdReturnsId() {
        Long id = 1L;
        AppUser appUser = new AppUser(id, "name", "pass", "email", Role.CANDIDATE);

        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        Long returnedId = underTest.getUserId();

        assertEquals(id, returnedId);
    }
}