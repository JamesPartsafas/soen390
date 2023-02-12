package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        AppUser candidateUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        appUserRepository.save(candidateUser);
        appUserRepository.save(companyUser);

        underTest.markCandidateToRecruiter(candidateUser, companyUser);

        assertEquals(Role.RECRUITER, candidateUser.getRole());
        assertEquals(companyUser,candidateUser.getCompany());
    }

    @Test
    void markCandidateToRecruiterFails() {
        AppUser notCandidateUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        appUserRepository.save(notCandidateUser);
        appUserRepository.save(companyUser);

        assertThrows(IllegalStateException.class,
                () -> underTest.markCandidateToRecruiter(notCandidateUser, companyUser),
                "The user must be a candidate to be marked as a recruiter.");

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
    @Test
    void unmarkRecruiterToCandidateSucceeds() {
        AppUser recruiterUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        appUserRepository.save(recruiterUser);
        appUserRepository.save(companyUser);

        underTest.unmarkRecruiterToCandidate(recruiterUser, companyUser);

        assertEquals(Role.CANDIDATE, recruiterUser.getRole());
        assertEquals(null,recruiterUser.getCompany());
    }

    @Test
    void unmarkCandidateToRecruiterFails() {
        AppUser notRecruiterUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        appUserRepository.save(notRecruiterUser);
        appUserRepository.save(companyUser);

        assertThrows(IllegalStateException.class,
                () -> underTest.unmarkRecruiterToCandidate(notRecruiterUser, companyUser),
                "The user must be a recruiter to be unmark as a candidate.");

    }
}
