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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserControllerTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private ConnectionService connectionService;
    private AutoCloseable autoCloseable;
    private AppUserController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserController(appUserService, connectionService);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUserInfo() {
        AppUser appUser = mock(AppUser.class);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        SecurityUtilities.authenticateAnonymousUser();

        when(appUserService.getAppUser(any(Long.class))).thenReturn(Optional.of(appUser));
        when(appUser.getAppUserProfile()).thenReturn(new AppUserProfile());

        String returnValue = underTest.getAppUser(appUserDetails, 1L, mock(Model.class));

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void getAppUserThatIsNoAuthenticatedRedirects() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);

        String returnValue = underTest.getAppUser(appUserDetails, appUser.getId(), mock(Model.class));
        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.of(appUser));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getAppUserThatDoesNotExistRedirects() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        SecurityUtilities.authenticateAnonymousUser();

        String returnValue = underTest.getAppUser(appUserDetails, appUser.getId(), mock(Model.class));
        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.empty());

        verify(appUserService).getAppUser(appUser.getId());
        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getUsersLikeNameUserNotSignedIn() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);
        SecurityContextHolder.clearContext();
        String returnedPage = underTest.getUsersLikeName(appUserDetails,"name", mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void getUsersLikeNameUserSignedIn() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);
        SecurityUtilities.authenticateAnonymousUser();
        String returnedPage = underTest.getUsersLikeName(appUserDetails,"name", mock(Model.class));
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
    void isCompanyForSetCandidateToRecruiter() {
        AppUser companyUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser candidateUser = new AppUser(2L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);

        String returnValue = underTest.markCandidateToRecruiter(candidateUser, companyUser);

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void isNotCompanyForSetCandidateToRecruiter() {
        AppUser notCompanyUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser candidateUser = new AppUser(2L, "Joe Man", "1234", "joecandidate@mail.com", Role.CANDIDATE);

        String returnValue = underTest.markCandidateToRecruiter(candidateUser, notCompanyUser);

        assertEquals("You must be a company to mark candidates as recruiters.", returnValue);
    }
    @Test
    void isCompanyForSetRecruiterToCandidate() {
        AppUser companyUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser recruiterUser = new AppUser(2L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);

        String returnValue = underTest.unmarkRecruiterToCandidate(recruiterUser, companyUser);

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void isNotCompanyForSetRecruiterToCandidate() {
        AppUser notCompanyUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser recruiterUser = new AppUser(2L, "Joe Man", "1234", "joecandidate@mail.com", Role.RECRUITER);

        String returnValue = underTest.unmarkRecruiterToCandidate(recruiterUser, notCompanyUser);

        assertEquals("You must be a company to unmark recruiters as candidates.", returnValue);
    }
}