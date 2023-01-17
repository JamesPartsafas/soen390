package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUserDetailsService;
import com.soen.synapsis.appuser.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

class AppUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    private AutoCloseable autoCloseable;
    private AppUserDetailsService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserDetailsService(appUserRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void loadUserByUsername() {
        String email = "joeusertest@mail.com";

        try {
            underTest.loadUserByUsername(email);
        }
        catch (Exception e) {}

        verify(appUserRepository).findByEmail(email);
    }

    @Test
    void loadUserThatDoesNotExistThrowsUsernameNotFoundException() {
        String email = "doesnotexist@mail.com";
        String expectedMessage = "User with email " + email + " not found.";

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> underTest.loadUserByUsername(email),
                "Expected exception was not thrown.");


        assertTrue(exception.getMessage().contentEquals(expectedMessage));
    }
}