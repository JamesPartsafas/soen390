package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.ProfilePictureRepository;
import com.soen.synapsis.appuser.profile.ResumeRepository;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    private AppUserService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserProfileRepository appUserProfileRepository;
    @Mock
    private CompanyProfileRepository companyProfileRepository;
    @Mock
    private ProfilePictureRepository profilePictureRepository;
    @Mock
    private ResumeRepository resumeRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserService(appUserRepository, appUserProfileRepository,
                companyProfileRepository, profilePictureRepository, resumeRepository);
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
    void getRegularUsersLikeNameReturnsListOfAppUsers() {
        String name = "name";
        Long id = 1L;

        List<AppUser> users = underTest.getRegularUsersLikeName(name, id);

        verify(appUserRepository, times(1)).findByNameContainingIgnoreCaseAndIdNotAndRoleNot(name, id, Role.ADMIN);
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
        assertEquals(companyUser, appUser.getCompany());
        verify(appUserRepository).save(appUser);
        verify(appUserRepository).save(companyUser);
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
    void updatePasswordWithExistingEmail() {
        String password = "abcd";
        String email = "newjoe@mail.com";
        String newPassword = "abcde";
        AppUser appUser = new AppUser("New Joe", password, email, Role.CANDIDATE);
        String returnValue = underTest.updatePassword(appUser, newPassword);
        assertEquals("pages/login", returnValue);
    }

    @Test
    void updatePasswordWithNewEmail() {
        Exception exception = assertThrows(IllegalStateException.class,
                () -> underTest.updatePassword(null, ""),
                "This email does not belong to any user.");
    }


    @Test
    void checkSecurityQuestionsSuccessful() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securityAnswer = encoder.encode("a");
        String email = "newjoe@mail.com";
        AppUser appUser = new AppUser("New Joe", "1234", email, Role.CANDIDATE, AuthProvider.LOCAL, securityAnswer, securityAnswer, securityAnswer);
        boolean result = underTest.checkSecurityQuestions(appUser, "a", "a", "a");

        assertTrue(result);
    }

    @Test
    void checkSecurityQuestionsFail1st() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securityAnswer = encoder.encode("a");
        String email = "newjoe@mail.com";
        AppUser appUser = new AppUser("New Joe", "1234", email, Role.CANDIDATE, AuthProvider.LOCAL, securityAnswer, securityAnswer, securityAnswer);
        boolean result = underTest.checkSecurityQuestions(appUser, "b", "a", "a");

        assertFalse(result);
    }

    @Test
    void checkSecurityQuestionsFail2nd() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securityAnswer = encoder.encode("a");
        String email = "newjoe@mail.com";
        AppUser appUser = new AppUser("New Joe", "1234", email, Role.CANDIDATE, AuthProvider.LOCAL, securityAnswer, securityAnswer, securityAnswer);
        boolean result = underTest.checkSecurityQuestions(appUser, "a", "b", "a");

        assertFalse(result);
    }

    @Test
    void checkSecurityQuestionsFail3rd() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String securityAnswer = encoder.encode("a");
        String email = "newjoe@mail.com";
        AppUser appUser = new AppUser("New Joe", "1234", email, Role.CANDIDATE, AuthProvider.LOCAL, securityAnswer, securityAnswer, securityAnswer);
        boolean result = underTest.checkSecurityQuestions(appUser, "a", "a", "b");

        assertFalse(result);
    }

    @Test
    void unmarkRecruiterToCandidateSucceeds() {
        AppUser companyUser = new AppUser(2L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);
        AppUser appUser = new AppUser(1L, "Joe Recruiter", "1234", "joerecruiter@mail.com", Role.RECRUITER);
        companyUser.addRecruiter(appUser);

        underTest.unmarkRecruiterToCandidate(appUser, companyUser);

        assertEquals(Role.CANDIDATE, appUser.getRole());
        verify(appUserRepository).save(appUser);
        verify(appUserRepository).save(companyUser);

    }

    @Test
    void emptyImageUploadReturns() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});

        underTest.uploadProfilePicture(file, mock(AppUser.class));

        verify(file).getBytes();
    }

    @Test
    void markCompanyAsVerifiedSucceeds() {
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY, false);

        underTest.markCompanyAsVerified(companyUser);

        assertEquals(true, companyUser.getVerificationStatus());
        verify(appUserRepository).save(companyUser);
    }

    @Test
    void emptyResumeUploadReturns() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});

        underTest.uploadDefaultResume(file, mock(AppUser.class));

        verify(file).getBytes();
    }

    @Test
    void markCompanyAsNonVerifiedSucceeds() {
        AppUser companyUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY, false);

        underTest.markCompanyAsNonVerified(companyUser);

        assertEquals(false, companyUser.getVerificationStatus());
        verify(appUserRepository).save(companyUser);
    }

    @Test
    void banUserThatIsNotFoundReturnsFalse() {
        Long id = 1L;
        when(appUserRepository.findById(id)).thenReturn(Optional.empty());

        Boolean returnValue = underTest.banUser(id);

        assertEquals(false, returnValue);
    }

    @Test
    void banUserThatIsFoundReturnsTrue() {
        Long id = 1L;
        AppUser appUser = new AppUser("Joe Man", "1234", "email@mail.com", Role.CANDIDATE);
        when(appUserRepository.findById(id)).thenReturn(Optional.of(appUser));

        Boolean returnValue = underTest.banUser(id);

        assertEquals(true, appUser.getIsBanned());
        assertEquals(true, returnValue);
    }
}
