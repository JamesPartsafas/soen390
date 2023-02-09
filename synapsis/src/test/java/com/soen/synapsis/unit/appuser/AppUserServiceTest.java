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
    void markCandidateToRecruiterSucceeds() {
        AppUser appUserCandidate = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        AppUser appUserCompany = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUserDetails loggedInAppUser = new AppUserDetails(appUserCompany);
        appUserRepository.save(appUserCandidate);
        appUserRepository.save(appUserCompany);

        underTest.markCandidateToRecruiter(appUserCandidate, loggedInAppUser);

        assertEquals(Role.RECRUITER, appUserCandidate.getRole());
        assertEquals(loggedInAppUser.getId(),appUserCandidate.getCompanyId());
    }

    @Test
    void markCandidateToRecruiterFails() {
        AppUser appUserNotCandidate = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser appUserCompany = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUserDetails loggedInAppUser = new AppUserDetails(appUserCompany);
        appUserRepository.save(appUserNotCandidate);
        appUserRepository.save(appUserCompany);

        assertThrows(IllegalStateException.class,
                () -> underTest.markCandidateToRecruiter(appUserNotCandidate, loggedInAppUser),
                "The user must be a candidate to be marked as a recruiter.");


    }
}
