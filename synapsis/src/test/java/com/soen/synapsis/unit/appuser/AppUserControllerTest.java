package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.utilities.SecurityUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUserInfo() {
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);
        SecurityUtilities.authenticateAnonymousUser();

        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.of(appUser));

        String returnValue = underTest.getAppUser(appUserDetails, appUser.getId(), mock(Model.class));

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
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.COMPANY);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);

        String returnValue = underTest.markCandidateToRecruiter(loggedInAppUser, appUserDetails);

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void isNotCompanyForSetCandidateToRecruiter() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);

        String returnValue = underTest.markCandidateToRecruiter(loggedInAppUser, appUserDetails);

        assertEquals("You must be a company to mark candidates as recruiters.", returnValue);
    }
}