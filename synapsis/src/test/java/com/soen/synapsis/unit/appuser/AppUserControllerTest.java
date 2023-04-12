package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.utilities.SecurityUtilities;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.websockets.chat.ChatService;
import org.aspectj.weaver.ast.Not;
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
    @Mock
    private AuthService authService;
    @Mock
    private ChatService chatService;
    private AutoCloseable autoCloseable;
    private AppUserController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserController(appUserService, connectionService, authService, chatService);
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
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY,true);
        AppUser candidateUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(appUserService.getAppUser(candidateUser.getId())).thenReturn(Optional.of(candidateUser));

        String returnValue = underTest.markCandidateToRecruiter(candidateUser.getId());

        assertEquals("redirect:/user/" + candidateUser.getId(), returnValue);
    }

    @Test
    void isNotCompanyForMarkingCandidateToRecruiter() {
        AppUser NotCompanyUser = new AppUser(1L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser candidateUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(NotCompanyUser);

        assertEquals("redirect:/", underTest.markCandidateToRecruiter(candidateUser.getId()));
    }

    @Test
    void isCompanyForMarkingIsNotCandidateToRecruiter() {
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser NotCandidateUser = new AppUser(2L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(NotCandidateUser.getId())).thenReturn(Optional.of(NotCandidateUser));

        assertEquals("redirect:/", underTest.markCandidateToRecruiter(NotCandidateUser.getId()));
    }

    @Test
    void isCompanyForUnmarkingIsRecruiterToCandidate() {
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY, true);
        AppUser recruiterUser = new AppUser(2L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER, companyUser);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(appUserService.getAppUser(recruiterUser.getId())).thenReturn(Optional.of(recruiterUser));

        String returnValue = underTest.unmarkRecruiterToCandidate(recruiterUser.getId());

        assertEquals("redirect:/user/" + recruiterUser.getId(), returnValue);
    }

    @Test
    void isNotCompanyForUnmarkingRecruiterToCandidate() {
        AppUser NotCompanyUser = new AppUser(1L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser recruiterUser = new AppUser(2L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        when(authService.getAuthenticatedUser()).thenReturn(NotCompanyUser);

        assertEquals("redirect:/", underTest.unmarkRecruiterToCandidate(recruiterUser.getId()));
    }

    @Test
    void isCompanyForUnmarkingIsNotRecruiterToCandidate() {
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser NotRecruiterUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(companyUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(NotRecruiterUser.getId())).thenReturn(Optional.of(NotRecruiterUser));

        assertEquals("redirect:/", underTest.unmarkRecruiterToCandidate(NotRecruiterUser.getId()));

    }

    @Test
    void isAdminMarkingIsCompanyAsVerified() {
        AppUser adminUser = new AppUser(2L, "Joe Admin", "1234", "joeadmin@mail.com", Role.ADMIN);
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        when(authService.getAuthenticatedUser()).thenReturn(adminUser);
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(appUserService.getAppUser(companyUser.getId())).thenReturn(Optional.of(companyUser));

        String returnValue = underTest.markCompanyAsVerified(companyUser.getId());

        assertEquals("redirect:/user/" + companyUser.getId(), returnValue);
    }

    @Test
    void isNotAdminMarkingIsCompanyAsVerified() {
        AppUser NotAdminUser = new AppUser(1L, "Joe Admin", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser companyUser = new AppUser(2L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        when(authService.getAuthenticatedUser()).thenReturn(NotAdminUser);

        assertEquals("redirect:/", underTest.markCompanyAsVerified(companyUser.getId()));
    }

    @Test
    void isAdminMarkingIsNotCompanyAsVerified() {
        AppUser adminUser = new AppUser(1L, "Joe Admin", "1234", "joeadmin@mail.com", Role.ADMIN);
        AppUser NotCompanyUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(adminUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(NotCompanyUser.getId())).thenReturn(Optional.of(NotCompanyUser));

        assertEquals("redirect:/", underTest.markCompanyAsVerified(NotCompanyUser.getId()));
    }

    @Test
    void isAdminMarkingIsCompanyAsNonVerified() {
        AppUser adminUser = new AppUser(2L, "Joe Admin", "1234", "joeadmin@mail.com", Role.ADMIN);
        AppUser companyUser = new AppUser(1L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        when(authService.getAuthenticatedUser()).thenReturn(adminUser);
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(appUserService.getAppUser(companyUser.getId())).thenReturn(Optional.of(companyUser));

        String returnValue = underTest.markCompanyAsNonVerified(companyUser.getId());

        assertEquals("redirect:/user/" + companyUser.getId(), returnValue);
    }

    @Test
    void isNotAdminMarkingIsCompanyAsNonVerified() {
        AppUser NotAdminUser = new AppUser(1L, "Joe Admin", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        AppUser companyUser = new AppUser(2L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        when(authService.getAuthenticatedUser()).thenReturn(NotAdminUser);

        assertEquals("redirect:/", underTest.markCompanyAsNonVerified(companyUser.getId()));
    }

    @Test
    void isAdminMarkingIsNotCompanyAsNonVerified() {
        AppUser adminUser = new AppUser(1L, "Joe Admin", "1234", "joeadmin@mail.com", Role.ADMIN);
        AppUser NotCompanyUser = new AppUser(2L, "Joe Candidate", "1234", "joecandidate@mail.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(adminUser);
        when(authService.doesUserHaveRole(Role.COMPANY)).thenReturn(true);
        when(appUserService.getAppUser(NotCompanyUser.getId())).thenReturn(Optional.of(NotCompanyUser));

        assertEquals("redirect:/", underTest.markCompanyAsNonVerified(NotCompanyUser.getId()));
    }

    @Test
    void banUserWhenNotAdminRedirects() {
        String redirect = "redirect:/";
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(false);

        String returnValue = underTest.banUser(1L, 1L);

        assertEquals(redirect, returnValue);
    }

    @Test
    void banUserAsAdminMarksResolved() {
        String redirect = "redirect:/chats";
        Long senderId = 1L;
        Long messageId = 1L;
        when(authService.doesUserHaveRole(Role.ADMIN)).thenReturn(true);
        when(appUserService.banUser(senderId)).thenReturn(true);

        String returnValue = underTest.banUser(senderId, messageId);

        verify(appUserService, times(1)).banUser(senderId);
        verify(chatService, times(1)).resolveReport(messageId);
        assertEquals(redirect, returnValue);
    }

    @Test
    void saveJob() {
        AppUser appUser = new AppUser(2L, "joecandidate", "1234", "joecandidateunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        underTest.saveJob(1L);
        verify(appUserService, times(1)).saveJob(1L, appUser);
    }

    @Test
    void deleteJob() {
        AppUser appUser = new AppUser(2L, "joecandidate", "1234", "joecandidateunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        underTest.saveJob(1L);
        verify(appUserService, times(1)).saveJob(1L, appUser);
        underTest.deleteSavedJob(1L);
        verify(appUserService, times(1)).deleteSavedJob(1L, appUser);
    }
}