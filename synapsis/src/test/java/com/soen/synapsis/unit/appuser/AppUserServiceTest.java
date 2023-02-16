package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    private AppUserService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserProfileRepository appUserProfileRepository;
    @Mock
    private CompanyProfileRepository companyProfileRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserService(appUserRepository,appUserProfileRepository,companyProfileRepository);
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
    void getUsersLikeNameReturnsListOfAppUsers() {
        String name = "name";
        Long id = 1L;

        List<AppUser> users = underTest.getUsersLikeName(name, id);

        verify(appUserRepository, times(1)).findByNameContainingIgnoreCaseAndIdNot(name, id);
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
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);

        underTest.markCandidateToRecruiter(appUser, companyUser);

        assertEquals(Role.RECRUITER, appUser.getRole());
        assertEquals(companyUser,appUser.getCompany());
    }

    @Test
    void markCandidateToRecruiterFails() {
        AppUser notCandidateUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);

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
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);

        underTest.markCandidateToRecruiter(appUser, companyUser);
        underTest.unmarkRecruiterToCandidate(appUser, companyUser);

        assertEquals(Role.CANDIDATE, appUser.getRole());
        assertThrows(IllegalStateException.class,
                () -> appUser.getCompany(),
                "You must be a recruiter to belong to a company.");
    }

    @Test
    void unmarkCandidateToRecruiterFails() {
        AppUser notRecruiterUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);

        assertThrows(IllegalStateException.class,
                () -> underTest.unmarkRecruiterToCandidate(notRecruiterUser, companyUser),
                "The user must be a recruiter to be unmark as a candidate.");

    }
}
