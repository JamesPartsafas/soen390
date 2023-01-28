package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.AppUserController;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.RegistrationController;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import com.soen.synapsis.appuser.registration.RegistrationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;
    private AutoCloseable autoCloseable;
    private RegistrationController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new RegistrationController(registrationService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewRegisterPage() {
        String returnedPage = underTest.register(Mockito.mock(Model.class));

        assertEquals("pages/register", returnedPage);
    }

    @Test
    void sendValidRegisterInfo() {
        RegistrationRequest request = new RegistrationRequest("joe", "joe@maul.com", "1234", Role.CANDIDATE);

        underTest.register(request,
                mock(BindingResult.class),
                mock(HttpServletRequest.class),
                mock(Model.class));

        verify(registrationService).register(request);
    }

    @Test
    void registerWithBindingErrors() {
        RegistrationRequest request = new RegistrationRequest("joe", "joe@maul.com", "1234", Role.CANDIDATE);
        HttpServletRequest servlet = mock(HttpServletRequest.class);
        Model model = mock(Model.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.register(request,
                bindingResult,
                servlet,
                model);

        verify(model).addAttribute(anyString(), anyString());
    }

    @Test
    void viewLoginPage() {
        String returnedPage = underTest.viewLoginPage();

        assertEquals("pages/login", returnedPage);
    }

    @Test
    void viewLogoutPage() {
        String returnedPage = underTest.viewLogoutPage();

        assertEquals("pages/logout", returnedPage);
    }
}