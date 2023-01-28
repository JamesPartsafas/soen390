package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.EmailValidator;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import com.soen.synapsis.appuser.registration.RegistrationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static com.soen.synapsis.utility.Constants.MIN_PASSWORD_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RegistrationServiceTest {

    private RegistrationService underTest;
    @Mock
    private AppUserService appUserService;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new RegistrationService(appUserService, new EmailValidator());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void registerValidUser() {
        String email = "joe@mail.com";
        String name = "joe";
        String password = "12345678";
        Role role = Role.CANDIDATE;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);

        underTest.register(request);

        verify(appUserService, times(1))
                .signUpUser(Mockito.any(AppUser.class));
    }

    @Test
    void throwOnInvalidEmail() {
        String email = "joemail.com";
        String name = "joe";
        String password = "12345678";
        Role role = Role.CANDIDATE;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);
        String expectedMessage = "The provided email is not valid.";

        assertThrows(IllegalStateException.class,
                () -> underTest.register(request), expectedMessage);
    }

    @Test
    void throwOnInvalidRole() {
        String email = "joemail.com";
        String name = "joe";
        String password = "12345678";
        Role role = Role.ADMIN;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);
        String expectedMessage = "The requested role is not valid.";

        assertThrows(IllegalStateException.class,
                () -> underTest.register(request), expectedMessage);
    }

    @Test
    void throwOnInvalidPassword() {
        String email = "joemail.com";
        String name = "joe";
        String password = "1234";
        Role role = Role.CANDIDATE;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);
        String expectedMessage = "The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.";

        assertThrows(IllegalStateException.class,
                () -> underTest.register(request), expectedMessage);
    }
}