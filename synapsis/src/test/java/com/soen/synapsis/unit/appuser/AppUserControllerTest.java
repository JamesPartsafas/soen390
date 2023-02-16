package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserControllerTest {

    @Mock
    private AppUserService appUserService;
    private AutoCloseable autoCloseable;
    private AppUserController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserController(appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUserInfo() {
        AppUser appUser = mock(AppUser.class);

        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.of(appUser));
        when(appUser.getAppUserProfile()).thenReturn(new AppUserProfile());

        String returnValue = underTest.getAppUser(appUser.getId(), mock(Model.class));

        assertEquals("pages/userpage", returnValue);
    }

    @Test
    void getAppUserThatDoesNotExistRedirects() {
        Long id = 1L;

        String returnValue = underTest.getAppUser(id, mock(Model.class));

        verify(appUserService).getAppUser(id);
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