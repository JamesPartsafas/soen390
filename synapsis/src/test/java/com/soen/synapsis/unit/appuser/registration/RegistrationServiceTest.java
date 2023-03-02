package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.EmailValidator;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import com.soen.synapsis.appuser.registration.RegistrationService;
import com.soen.synapsis.utility.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.soen.synapsis.utility.Constants.MIN_PASSWORD_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void ssoRetrievalReturnsFoundUser() {
        String email = "joe@mail.com";
        when(appUserService.getAppUser(email)).thenReturn(new AppUser("joe", "1234", email, Role.CANDIDATE));

        AppUser retrievedUser = underTest.retrieveSSOUserOrRegisterIfNotExists("joe", email);

        assertNotNull(retrievedUser);
    }

    @Test
    void ssoRegisterSuccessfullyInsertsNewUser() {
        String name = "joe";
        String email = "joe@mail.com";

        AppUser createdUser = underTest.retrieveSSOUserOrRegisterIfNotExists(name, email);

        assertEquals(name, createdUser.getName());
        assertEquals(Constants.SSO_PASSWORD, createdUser.getPassword());
        assertEquals(email, createdUser.getEmail());
        assertEquals(Role.CANDIDATE, createdUser.getRole());
        assertEquals(AuthProvider.GOOGLE, createdUser.getAuthProvider());
    }

    @Test
    void registerValidAdmin() {
        String email = "joeadmin@mail.com";
        String name = "joe admin";
        String password = "123456789";
        Role role = Role.ADMIN;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);

        underTest.registerAdmin(request);

        verify(appUserService, times(1))
                .signUpAdmin(Mockito.any(AppUser.class));
    }

    @Test
    void throwOnInvalidAdminEmail() {
        String email = "joemail.com";
        String name = "joe";
        String password = "12345678";
        Role role = Role.ADMIN;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);
        String expectedMessage = "The provided email is not valid.";

        assertThrows(IllegalStateException.class,
                () -> underTest.registerAdmin(request), expectedMessage);
    }

    @Test
    void throwOnInvalidAdminPassword() {
        String email = "joemail.com";
        String name = "joe";
        String password = "1234";
        Role role = Role.ADMIN;
        RegistrationRequest request = new RegistrationRequest(name, email, password, role);
        String expectedMessage = "The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.";

        assertThrows(IllegalStateException.class,
                () -> underTest.registerAdmin(request), expectedMessage);
    }

    @Test
    void updateUserPasswordTest() {
        String email = "joe@mail.com";
        String name = "Joe";
        String password = "12345678";
        String newPassword = "12345679";
        Role role = Role.CANDIDATE;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AppUser appUser = new AppUser(name, encoder.encode(password), email, role);

        underTest.updateUserPassword(appUser, password, newPassword);

        verify(appUserService, times(1))
                .updatePassword(appUser, newPassword);
    }

    @Test
    void resetUserPasswordTest() {
        String email = "joe@mail.com";
        String name = "Joe";
        String password = "12345678";
        String newPassword = "12345679";
        Role role = Role.CANDIDATE;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securityAnswer = encoder.encode("a");

        AppUser appUser = new AppUser(name, password, email, role, AuthProvider.LOCAL, securityAnswer, securityAnswer, securityAnswer);
        RegistrationRequest request = new RegistrationRequest(name, email, newPassword, role, "a", "a", "a");
        when(appUserService.getAppUser(email)).thenReturn(appUser);
        when(appUserService.checkSecurityQuestions(appUser, request.getSecurityAnswer1(), request.getSecurityAnswer2(), request.getSecurityAnswer3())).thenReturn(true);

        underTest.resetUserPassword(request);

        verify(appUserService, times(1))
                .updatePassword(appUser, newPassword);
    }
}