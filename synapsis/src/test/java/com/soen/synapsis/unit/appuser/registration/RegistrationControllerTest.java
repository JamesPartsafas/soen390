package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.PasswordUpdateRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;
    @Mock
    private AuthService authService;

    private AutoCloseable autoCloseable;
    private RegistrationController underTest;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new RegistrationController(registrationService, authService);

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
    void viewRegisterAdminPage() {
        String returnedPage = underTest.registerAdmin(Mockito.mock(Model.class));
        assertEquals(null, returnedPage);
    }


    @Test
    void viewLoginPage() {
        String returnedPage = underTest.viewLoginPage();
        assertEquals("pages/login", returnedPage);
    }


    @Test
    void sendValidPasswordUpdateInfo() throws Exception {
        AppUser appUser = new AppUser("joe", "joeadmin@gmail.com", "12345678", Role.ADMIN);
        PasswordUpdateRequest request = new PasswordUpdateRequest("12345678", "12345679");
        BindingResult bindingResult = mock(BindingResult.class);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        underTest.passwordUpdate(request,
                bindingResult,
                Mockito.mock(Model.class));

        verify(registrationService).updateUserPassword(appUser, request.getOldPassword(), request.getNewPassword());
    }

    @Test
    void sendPasswordUpdateInfoWithBindingError() {
        RegistrationRequest request = new RegistrationRequest("joe", "joeadmin@gmail.com", "1234", Role.ADMIN);
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.registerAdmin(request,
                bindingResult,
                model);

        verify(model).addAttribute(anyString(), anyString());
    }

    @Test
    void sendValidPasswordResetInfo() throws Exception {
        RegistrationRequest request = new RegistrationRequest("joe", "joeadmin@mail.com", "12345678", Role.CANDIDATE, "a", "a", "a");
        BindingResult bindingResult = mock(BindingResult.class);
        when(registrationService.resetUserPassword(request)).thenReturn("pages/login");
        String result = underTest.passwordReset(request,
                bindingResult,
                Mockito.mock(Model.class));
        assertEquals("pages/login", result);
    }
}