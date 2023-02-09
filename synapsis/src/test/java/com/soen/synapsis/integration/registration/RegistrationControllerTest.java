package com.soen.synapsis.integration.registration;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

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
