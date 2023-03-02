package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.utilities.SecurityUtilities;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserControllerTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private ConnectionService connectionService;
    @Mock
    private AuthService authService;
    private AutoCloseable autoCloseable;
    private AppUserController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserController(appUserService, connectionService, authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUserInfo() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        when(authService.isUserAuthenticated()).thenReturn(true);

        when(appUserService.getAppUser(any(Long.class))).thenReturn(Optional.of(appUser));

        String returnValue = underTest.getAppUser(1L, mock(Model.class));

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void getAppUserThatIsNoAuthenticatedRedirects() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        String returnValue = underTest.getAppUser(appUser.getId(), mock(Model.class));
        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.of(appUser));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getAppUserThatDoesNotExistRedirects() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        SecurityUtilities.authenticateAnonymousUser();
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        when(authService.isUserAuthenticated()).thenReturn(true);

        String returnValue = underTest.getAppUser(appUser.getId(), mock(Model.class));
        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.empty());

        verify(appUserService).getAppUser(appUser.getId());
        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getUsersLikeNameUserNotSignedIn() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        SecurityContextHolder.clearContext();
        when(authService.getAuthenticatedUser()).thenReturn(loggedInAppUser);

        String returnedPage = underTest.getUsersLikeName("name", mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void getUsersLikeNameUserSignedIn() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        SecurityUtilities.authenticateAnonymousUser();
        when(authService.getAuthenticatedUser()).thenReturn(loggedInAppUser);
        when(authService.isUserAuthenticated()).thenReturn(true);

        String returnedPage = underTest.getUsersLikeName("name", mock(Model.class));
        assertEquals("pages/usersearchpage", returnedPage);
    }

    @Test
    void getUserData() {
        String expected = "This is the user page";

        assertEquals(expected, underTest.getUserData());
    }

    @Test
    void getAdminData() {
        String expected = "This is the admin page";

        assertEquals(expected, underTest.getAdminData());
    }
    @Test
    void isCompanyForMarkingIsCandidateToRecruiter() {
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser candidateUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(candidateUser.getId())).thenReturn(Optional.of(candidateUser));

        String returnValue = underTest.markCandidateToRecruiter(candidateUser.getId());

        assertEquals(Role.CANDIDATE, candidateUser.getRole());
        assertEquals("redirect:/user/" + candidateUser.getId(), returnValue);
    }

    @Test
    void isNotCompanyForMarkingCandidateToRecruiter() {
        AppUser NotCompanyUser = new AppUser(1L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser candidateUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(NotCompanyUser);

        assertEquals("You must be a company to mark candidates as recruiters.", underTest.markCandidateToRecruiter(candidateUser.getId()));
    }

    @Test
    void isCompanyForMarkingIsNotCandidateToRecruiter() {
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser recruiterUser = new AppUser(2L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(recruiterUser.getId())).thenReturn(Optional.of(recruiterUser));

        assertEquals("The user must be a candidate to be marked as a recruiter.", underTest.markCandidateToRecruiter(recruiterUser.getId()));
    }

    @Test
    void isCompanyForSetRecruiterToCandidate() {
        AppUser companyUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser recruiterUser = new AppUser(2L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);

        String returnValue = underTest.unmarkRecruiterToCandidate(recruiterUser);

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void isNotCompanyForSetRecruiterToCandidate() {
        AppUser notCompanyUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser recruiterUser = new AppUser(2L, "Joe Man", "1234", "joecandidate@mail.com", Role.RECRUITER);
        when(authService.getAuthenticatedUser()).thenReturn(notCompanyUser);

        String returnValue = underTest.unmarkRecruiterToCandidate(recruiterUser);

        assertEquals("You must be a company to unmark recruiters as candidates.", returnValue);
    }
}