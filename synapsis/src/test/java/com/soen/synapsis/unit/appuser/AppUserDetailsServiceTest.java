package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetailsService;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.LoginAttemptService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    LoginAttemptService loginAttemptService;
    private AutoCloseable autoCloseable;
    private AppUserDetailsService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserDetailsService(appUserRepository, loginAttemptService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void loadUserByUsername() {
        String email = "joeusertest@mail.com";
        when(appUserRepository.findByEmail(email)).thenReturn(new AppUser("joe", "12345678", "joe@mail.com", Role.CANDIDATE));
        when(loginAttemptService.isBlocked()).thenReturn(false);
        UserDetails returnedUserDetails = null;

        try {
            returnedUserDetails = underTest.loadUserByUsername(email);
        } catch (Exception e) {
        }

        verify(appUserRepository).findByEmail(email);
        assertNotNull(returnedUserDetails);
    }

    @Test
    void loadUserThatDoesNotExistThrowsUsernameNotFoundException() {
        String email = "doesnotexist@mail.com";
        String expectedMessage = "User with email " + email + " not found.";
        when(loginAttemptService.isBlocked()).thenReturn(false);

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> underTest.loadUserByUsername(email),
                "Expected exception was not thrown.");


        assertTrue(exception.getMessage().contentEquals(expectedMessage));
    }

    @Test
    void loadUserWhenClientBlockedThrowsRuntimeException() {
        String email = "joeusertest@mail.com";
        String expectedMessage = "IP blocked.";
        when(loginAttemptService.isBlocked()).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class,
                () -> underTest.loadUserByUsername(email),
                "Expected exception was not thrown.");


        assertEquals(exception.getMessage(), expectedMessage);
    }
}