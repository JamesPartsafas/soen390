package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.registration.RegistrationController;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import com.soen.synapsis.appuser.registration.RegistrationService;
import org.junit.jupiter.api.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void viewRegisterPage() {
        String returnedPage = underTest.register(Mockito.mock(Model.class));

        assertEquals("pages/register", returnedPage);
    }

    @Test
    @Order(2)
    void sendValidRegisterInfo() {
        RegistrationRequest request = new RegistrationRequest("joe", "joe@maul.com", "1234", Role.CANDIDATE);

        underTest.register(request,
                mock(BindingResult.class),
                mock(HttpServletRequest.class),
                mock(Model.class));

        verify(registrationService).register(request);
    }

    @Test
    @Order(3)
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
    @Order(4)
    void viewRegisterAdminPage() {
        String returnedPage = underTest.registerAdmin(Mockito.mock(Model.class));
        assertEquals(null, returnedPage);
    }


    @Test
    @Order(5)
    void viewLoginPage() {
        String returnedPage = underTest.viewLoginPage();
        assertEquals("pages/login", returnedPage);
    }

    @Test
    @Order(6)
    void viewLogoutPage() {
        String returnedPage = underTest.viewLogoutPage();

        assertEquals("pages/logout", returnedPage);
    }

    @Test
    @Order(7)
    void sendValidAdminRegisterInfo() throws Exception {

        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        RegistrationRequest request = new RegistrationRequest("joe", "joeadmin@mail.com", "1234", Role.ADMIN);
        BindingResult bindingResult = mock(BindingResult.class);
        underTest.registerAdmin(request,
                bindingResult,
                Mockito.mock(Model.class));

        verify(registrationService).registerAdmin(request);
    }

    @Test
    @Order(8)
    void sendAdminInfoWithBindingError(){
        RegistrationRequest request = new RegistrationRequest("joe", "joeadmin@gmail.com", "1234", Role.ADMIN);
        Model model = mock(Model.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.registerAdmin(request,
                bindingResult,
                model);

        verify(model).addAttribute(anyString(), anyString());
    }
}