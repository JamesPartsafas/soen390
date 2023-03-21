package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.ProfilePictureRepository;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for AppUser-related functionality.
 */
@Component
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private AppUserProfileRepository appUserProfileRepository;
    private CompanyProfileRepository companyProfileRepository;
    private ProfilePictureRepository profilePictureRepository;
    private final BCryptPasswordEncoder encoder;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserProfileRepository appUserProfileRepository,
                          CompanyProfileRepository companyProfileRepository, ProfilePictureRepository profilePictureRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserProfileRepository = appUserProfileRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public Optional<AppUser> getAppUser(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser getAppUser(String email) {
        return appUserRepository.findByEmail(email);
    }

    /**
     * Retrieve users whose name contains some substring. Exclude users with a certain id,
     * as well as excluding all admins.
     * @param name Name to search for
     * @param id ID to exclude
     * @return List of AppUsers meeting search requirements.
     */
    public List<AppUser> getRegularUsersLikeName(String name, Long id) {
        return appUserRepository.findByNameContainingIgnoreCaseAndIdNotAndRoleNot(name, id, Role.ADMIN);
    }

    /**
     * Signs up an AppUser by adding their data to the database, after verifying the
     * uniqueness of their email and validity of their role. Additionally, encodes the
     * password and security answers.
     * @param appUser User to be signed up.
     * @return View to homepage.
     */
    public String signUpUser(AppUser appUser) {
        boolean appUserExists = appUserRepository.findByEmail(appUser.getEmail()) != null;

        if (appUserExists) {
            throw new IllegalStateException("This email is already taken.");
        } else if (appUser.getRole() == Role.RECRUITER) {
            throw new IllegalStateException("Cannot sign up a recruiter");
        }

        appUser.setPassword(encoder.encode(appUser.getPassword()));

        if (appUser.getSecurityAnswer1() != null) {
            appUser.setSecurityAnswer1(encoder.encode(appUser.getSecurityAnswer1()));
        }
        if (appUser.getSecurityAnswer2() != null) {
            appUser.setSecurityAnswer2(encoder.encode(appUser.getSecurityAnswer2()));
        }
        if (appUser.getSecurityAnswer3() != null) {
            appUser.setSecurityAnswer3(encoder.encode(appUser.getSecurityAnswer3()));
        }

        appUserRepository.save(appUser);

        if (appUser.getRole() == Role.CANDIDATE) {
            AppUserProfile profile = new AppUserProfile();
            profile.setAppUser(appUser);
            appUserProfileRepository.save(profile);
        } else if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = new CompanyProfile();
            companyProfile.setAppUser(appUser);
            companyProfileRepository.save(companyProfile);
        }

        return "pages/home";
    }

    /**
     * Saves a profile picture to the database
     * @param file Image to be saved.
     * @param appUser The user the image is associated to.
     * @throws IOException Thrown if the file is corrupted and cannot be correctly converted to a byte stream.
     */
    public void uploadProfilePicture(MultipartFile file, AppUser appUser) throws IOException {
        String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
        if (encodedImage.isEmpty())
            return;

        ProfilePicture newPicture = profilePictureRepository.findByAppUser(appUser);

        if (newPicture != null) {
            newPicture.setImage(encodedImage);
        } else {
            newPicture = new ProfilePicture(appUser, encodedImage);
        }

        profilePictureRepository.save(newPicture);
    }

    /**
     * Marks candidates to recruiters for a specific company, and adds that recruiter to the company's recruiter list.
     * @param appUser The user to be made a recruiter.
     * @param companyUser The company the recruiter is to be associated to.
     */
    public void markCandidateToRecruiter(AppUser appUser, AppUser companyUser) {

        appUser.setRole(Role.RECRUITER);
        appUser.setCompany(companyUser);

        companyUser.addRecruiter(appUser);

        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);
    }

    /**
     * Signs up a new user with the admin role
     * @param appUser The AppUser to be signed up
     * @return View containing admin creation success page.
     */
    public String signUpAdmin(AppUser appUser) {
        boolean appUserExists = appUserRepository.findByEmail(appUser.getEmail()) != null;

        if (appUserExists) {
            throw new IllegalStateException("This email is already taken.");
        }

        appUser.setPassword(encoder.encode(appUser.getPassword()));

        appUserRepository.save(appUser);

        return "pages/adminCreationSuccess";
    }

    /**
     * Processes request to update a user's password.
     * @param appUser The user whose password is to be updated.
     * @param password The new password to be given to the user.
     * @return View containing login page.
     */
    public String updatePassword(AppUser appUser, String password) {
        if (appUser != null) {
            appUser.setPassword(encoder.encode(password));
            appUserRepository.save(appUser);
            return "pages/login";
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    /**
     * Used to set a recruiter back to a candidate and remove them from the company's recruiter list.
     * @param appUser The recruiter to be made a candidate.
     * @param companyUser The company the recruiter is associated to.
     */
    public void unmarkRecruiterToCandidate(AppUser appUser, AppUser companyUser) {
        appUser.setCompany(null);
        appUser.setRole(Role.CANDIDATE);

        companyUser.removeRecruiter(appUser);

        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);

    }

    /**
     * Verifies if security answers match stored answers.
     * @param appUser The user who is verifying their answers.
     * @param securityAnswer1 Answer to the first security question.
     * @param securityAnswer2 Answer to the second security question.
     * @param securityAnswer3 Answer to the third security question.
     * @return False if any security answers do not match, true otherwise.
     */
    public boolean checkSecurityQuestions(AppUser appUser, String securityAnswer1, String securityAnswer2, String securityAnswer3) {

        if (appUser != null) {
            if (!encoder.matches(securityAnswer1, appUser.getSecurityAnswer1()))
                return false;
            if (!encoder.matches(securityAnswer2, appUser.getSecurityAnswer2()))
                return false;
            if (!encoder.matches(securityAnswer3, appUser.getSecurityAnswer3()))
                return false;

            return true;
        } else {
            throw new IllegalStateException("This email does not belong to any user.");
        }
    }

    /**
     * Marks company user as verified.
     * @param appUser The user to be set as verified.
     */
    public void markCompanyAsVerified(AppUser appUser) {

        appUser.setVerificationStatus(true);
        appUserRepository.save(appUser);

    }

    /**
     * Marks company user as non-verified.
     * @param appUser The user to be set as non-verified.
     */
    public void markCompanyAsNonVerified(AppUser appUser) {

        appUser.setVerificationStatus(false);
        appUserRepository.save(appUser);

    }
}
