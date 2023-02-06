package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    private AppUserService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserService(appUserRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUser() {
        Long id = 1L;

        Optional<AppUser> optionalAppUser = underTest.getAppUser(id);

        verify(appUserRepository, times(1)).findById(id);
    }

    @Test
    void getAppUserByEmailReturnsUser() {
        String email = "joe@mail.com";

        AppUser appUser = underTest.getAppUser(email);

        verify(appUserRepository, times(1)).findByEmail(email);
    }

    @Test
    void signUpUserWithUniqueEmail() {
        String email = "joeman@mail.com";
        AppUser appUser = new AppUser("Joe Man", "1234", email, Role.CANDIDATE);

        when(appUserRepository.findByEmail(email)).thenReturn(null);

        String returnValue = underTest.signUpUser(appUser);

        verify(appUserRepository).save(appUser);
        assertEquals("pages/home", returnValue);
    }

    @Test
    void signUpUserWithExistingEmailThrows() {
        String email = "joeman@mail.com";
        AppUser appUser = new AppUser("Joe Man", "1234", email, Role.CANDIDATE);

        when(appUserRepository.findByEmail(email)).thenReturn(appUser);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.signUpUser(appUser),
                "This email is already taken.");
    }

    @Test
    void signUpAdminWithUniqueEmail() {
        String email = "joeadmin@mail.com";
        AppUser appUser = new AppUser("Joe Admin", "1234", email, Role.ADMIN);

        when(appUserRepository.findByEmail(email)).thenReturn(null);

        String returnValue = underTest.signUpAdmin(appUser);

        verify(appUserRepository).save(appUser);
        assertEquals("pages/adminCreationSuccess", returnValue);
    }

    @Test
    void signUpAdminWithExistingEmailThrows() {
        String email = "joeadmin@mail.com";
        AppUser appUser = new AppUser("Joe Admin", "1234", email, Role.ADMIN);

        when(appUserRepository.findByEmail(email)).thenReturn(appUser);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.signUpUser(appUser),
                "This email is already taken.");
    }
    @Test
    void updatePasswordWithExistingEmail(){
        String email = "joeman@mail.com";
        String password = "abcd";
        AppUser appUser = new AppUser("Joe Man", "1234", email, Role.CANDIDATE);
        when(appUserRepository.findByEmail(email)).thenReturn(appUser);
        String returnValue = underTest.updatePassword(email,password);
        assertEquals("pages/login", returnValue);
    }
    @Test
    void updatePasswordWithNewEmail(){
        String email = "newjoe@mail.com";
        String password = "abcd";
        AppUser appUser = new AppUser("New Joe", "1234", email, Role.CANDIDATE);
        when(appUserRepository.findByEmail(email)).thenReturn(null);
        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.updatePassword(email, password),
                "This email does not belong to any user.");

    }
}
