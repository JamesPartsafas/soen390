package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.job.Job;
import com.soen.synapsis.appuser.job.JobRepository;
import com.soen.synapsis.appuser.profile.*;
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
    private ResumeRepository defaultResumeRepository;
    private CoverLetterRepository defaultCoverLetterRepository;
    private final BCryptPasswordEncoder encoder;
    private final JobRepository jobRepository;

    public AppUserService(AppUserRepository appUserRepository, JobRepository jobRepository) {
        this.appUserRepository = appUserRepository;
        this.encoder = new BCryptPasswordEncoder();
        this.jobRepository = jobRepository;
    }

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserProfileRepository appUserProfileRepository,
                          CompanyProfileRepository companyProfileRepository, ProfilePictureRepository profilePictureRepository, ResumeRepository defaultResumeRepository, CoverLetterRepository defaultCoverLetterRepository, JobRepository jobRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserProfileRepository = appUserProfileRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.defaultResumeRepository = defaultResumeRepository;
        this.defaultCoverLetterRepository = defaultCoverLetterRepository;
        this.encoder = new BCryptPasswordEncoder();
        this.jobRepository = jobRepository;
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
     *
     * @param name Name to search for
     * @param id   ID to exclude
     * @return List of AppUsers meeting search requirements.
     */
    public List<AppUser> getRegularUsersLikeName(String name, Long id) {
        return appUserRepository.findByNameContainingIgnoreCaseAndIdNotAndRoleNot(name, id, Role.ADMIN);
    }

    /**
     * Signs up an AppUser by adding their data to the database, after verifying the
     * uniqueness of their email and validity of their role. Additionally, encodes the
     * password and security answers.
     *
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
     *
     * @param file    Image to be saved.
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
            newPicture = new ProfilePicture(appUser, encodedImage, "");
        }

        profilePictureRepository.save(newPicture);
    }

    /**
     * Saves a cover picture to the database
     *
     * @param file    Image to be saved.
     * @param appUser The user the image is associated to.
     * @throws IOException Thrown if the file is corrupted and cannot be correctly converted to a byte stream.
     */
    public void uploadCoverPicture(MultipartFile file, AppUser appUser) throws IOException {
        String encodedImage = Base64.getEncoder().encodeToString(file.getBytes());
        if (encodedImage.isEmpty())
            return;

        ProfilePicture newPicture = profilePictureRepository.findByAppUser(appUser);

        if (newPicture != null) {
            newPicture.setCoverImage(encodedImage);
        } else {
            newPicture = new ProfilePicture(appUser, "", encodedImage);
        }

        profilePictureRepository.save(newPicture);
    }

    /**
     * Marks candidates to recruiters for a specific company, and adds that recruiter to the company's recruiter list.
     *
     * @param appUser     The user to be made a recruiter.
     * @param companyUser The company the recruiter is to be associated to.
     */
    public void markCandidateToRecruiter(AppUser appUser, AppUser companyUser) {

        appUser.setRole(Role.RECRUITER);
        appUser.setCompany(companyUser);
        appUser.setVerificationStatus(true);
        companyUser.addRecruiter(appUser);

        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);
    }

    /**
     * Signs up a new user with the admin role
     *
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
     *
     * @param appUser  The user whose password is to be updated.
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
     *
     * @param appUser     The recruiter to be made a candidate.
     * @param companyUser The company the recruiter is associated to.
     */
    public void unmarkRecruiterToCandidate(AppUser appUser, AppUser companyUser) {
        appUser.setCompany(null);
        appUser.setRole(Role.CANDIDATE);
        appUser.setVerificationStatus(false);
        companyUser.removeRecruiter(appUser);

        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);

    }

    /**
     * Verifies if security answers match stored answers.
     *
     * @param appUser         The user who is verifying their answers.
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
     * Allows users to save a job.
     *
     * @param jid     The id of job being saved.
     * @param appUser The currently authenticated user.
     * @return View containing saved jobs.
     */
    public String saveJob(Long jid, AppUser appUser) {
        appUser.addSavedJob(jid);
        appUserRepository.save(appUser);
        return "redirect:/savedjobs";
    }

    /**
     * Allows users to unsave a job.
     *
     * @param jid     The id of job being unsaved.
     * @param appUser The currently authenticated user.
     * @return View containing saved jobs.
     */
    public String deleteSavedJob(Long jid, AppUser appUser) {
        appUser.removeSavedJob(jid);
        appUserRepository.save(appUser);
        return "redirect:/savedjobs";
    }

    /**
     * Marks company user as verified.
     *
     * @param appUser The user to be set as verified.
     */
    public void markCompanyAsVerified(AppUser appUser) {

        appUser.setVerificationStatus(true);
        appUserRepository.save(appUser);
    }

    /**
     * Saves a default resume to the database
     *
     * @param defaultResume Resume to be saved.
     * @param appUser       The resume is associated to.
     * @throws IOException Thrown if the file is corrupted and cannot be correctly converted to a byte stream.
     */
    public void uploadDefaultResume(MultipartFile defaultResume, AppUser appUser) throws IOException {
        String encodedDefaultResume = Base64.getEncoder().encodeToString(defaultResume.getBytes());
        String fileName = defaultResume.getOriginalFilename();
        if (encodedDefaultResume.isEmpty())
            return;

        Resume newDefaultResume = defaultResumeRepository.findByAppUser(appUser);

        if (newDefaultResume != null) {
            newDefaultResume.setDefaultResume(encodedDefaultResume);
            newDefaultResume.setFileName(fileName);
        } else {
            newDefaultResume = new Resume(appUser, encodedDefaultResume, fileName);
        }

        defaultResumeRepository.save(newDefaultResume);
    }

    /**
     * Marks company user as non-verified.
     *
     * @param appUser The user to be set as non-verified.
     */
    public void markCompanyAsNonVerified(AppUser appUser) {

        appUser.setVerificationStatus(false);

        if(appUser.getRecruiter() != null) {
            for (AppUser recruiter : appUser.getRecruiter()) {
                recruiter.setVerificationStatus(false);
                recruiter.setRole(Role.CANDIDATE);
                appUserRepository.save(recruiter);
            }
        }

        List<Job> jobsToDelete = jobRepository.findJobsByCompanyEquals(appUser);
        jobRepository.deleteAll(jobsToDelete);

        appUserRepository.save(appUser);

    }

    /**
     * Ban user from site, so they can no longer log-in
     *
     * @param appUserId The ID of the user to ban
     * @return true if the user was successfully banned, false otherwise
     */
    public boolean banUser(Long appUserId) {
        Optional<AppUser> optionalAppUser = getAppUser(appUserId);

        if (optionalAppUser.isEmpty()) {
            return false;
        }

        AppUser appUser = optionalAppUser.get();

        appUser.setIsBanned(true);
        appUserRepository.save(appUser);

        return true;
    }

    /**
     * Saves a default cover letter to the database.
     *
     * @param defaultCoverLetter CoverLetter to be saved.
     * @param appUser            The cover letter is associated to.
     * @throws IOException Thrown if the file is corrupted and cannot be correctly converted to a byte stream.
     */
    public void uploadDefaultCoverLetter(MultipartFile defaultCoverLetter, AppUser appUser) throws IOException {
        String encodedDefaultCoverLetter = Base64.getEncoder().encodeToString(defaultCoverLetter.getBytes());
        String fileName = defaultCoverLetter.getOriginalFilename();
        if (encodedDefaultCoverLetter.isEmpty())
            return;

        CoverLetter newCoverLetter = defaultCoverLetterRepository.findByAppUser(appUser);

        if (newCoverLetter != null) {
            newCoverLetter.setDefaultCoverLetter(encodedDefaultCoverLetter);
            newCoverLetter.setFileName(fileName);
        } else {
            newCoverLetter = new CoverLetter(appUser, encodedDefaultCoverLetter, fileName);
        }

        defaultCoverLetterRepository.save(newCoverLetter);
    }
}
