package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
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
        AppUser appUser = new AppUser(1L, "Joe Man", "1234", "joeman@mail.com", Role.CANDIDATE);

        when(appUserService.getAppUser(appUser.getId())).thenReturn(Optional.of(appUser));

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