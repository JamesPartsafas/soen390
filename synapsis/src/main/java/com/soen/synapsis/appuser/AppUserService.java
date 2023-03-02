package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.ProfilePictureRepository;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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

    public List<AppUser> getRegularUsersLikeName(String name, Long id) {
        return appUserRepository.findByNameContainingIgnoreCaseAndIdNotAndRoleNot(name, id, Role.ADMIN);
    }

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

    public void markCandidateToRecruiter(AppUser appUser, AppUser companyUser) {
        appUser.setRole(Role.RECRUITER);
        appUser.setCompany(companyUser);

        try {
            companyUser.addRecruiter(appUser);
        } catch (IllegalStateException e) {
            e.getMessage();
        }
        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);
    }

    public String signUpAdmin(AppUser appUser) {
        boolean appUserExists = appUserRepository.findByEmail(appUser.getEmail()) != null;

        if (appUserExists) {
            throw new IllegalStateException("This email is already taken.");
        }

        appUser.setPassword(encoder.encode(appUser.getPassword()));

        appUserRepository.save(appUser);

        return "pages/adminCreationSuccess";
    }

    public String updatePassword(AppUser appUser, String password) {
        if (appUser != null) {
            appUser.setPassword(encoder.encode(password));
            appUserRepository.save(appUser);
            return "pages/login";
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    public void unmarkRecruiterToCandidate(AppUser appUser, @AuthenticationPrincipal AppUser companyUser) {

        if (appUser.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("The user must be a recruiter to be unmark as a candidate.");
        }
        if (companyUser != appUser.getCompany()) {
            throw new IllegalStateException("The recruiter is not part of your company.");
        }

        appUser.setCompany(null);
        appUser.setRole(Role.CANDIDATE);

        try {
            companyUser.removeRecruiter(appUser);
        } catch (IllegalStateException e) {
            e.getMessage();
        }
        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);

    }

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
}
