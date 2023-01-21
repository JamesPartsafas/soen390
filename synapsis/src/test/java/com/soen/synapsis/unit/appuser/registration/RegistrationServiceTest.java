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
        String password = "1234";
        RegistrationRequest request = new RegistrationRequest(name, email, password);

        underTest.register(request);

        verify(appUserService, times(1))
                .signUpUser(Mockito.any(AppUser.class));
    }

    @Test
    void throwOnInvalidEmail() {
        String email = "joemail.com";
        String name = "joe";
        String password = "1234";
        RegistrationRequest request = new RegistrationRequest(name, email, password);
        String expectedMessage = "Provided email is not valid";

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.register(request));

        assertTrue(exception.getMessage().contentEquals(expectedMessage));
    }
}