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

    public List<AppUser> getUsersLikeName(String name, Long id) { return appUserRepository.findByNameContainingIgnoreCaseAndIdNot(name, id); }

    public String signUpUser(AppUser appUser) {
        boolean appUserExists = appUserRepository.findByEmail(appUser.getEmail()) != null;

        if (appUserExists) {
            throw new IllegalStateException("This email is already taken.");
        } else if (appUser.getRole() == Role.RECRUITER) {
            throw new IllegalStateException("Cannot sign up a recruiter");
        }

        appUser.setPassword(encoder.encode(appUser.getPassword()));

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
        }
        else {
            newPicture = new ProfilePicture(appUser, encodedImage);
        }

        profilePictureRepository.save(newPicture);
    }

    public void markCandidateToRecruiter(AppUser appUser, @AuthenticationPrincipal AppUser companyUser) {

        if (appUser.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("The user must be a candidate to be marked as a recruiter.");
        }
        appUser.setRole(Role.RECRUITER);
        appUser.setCompany(companyUser);
        try {
            companyUser.addRecruiter(appUser);
        }
        catch (IllegalStateException e) {
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

    public String updatePassword(String email, String password) {
        AppUser appUser = appUserRepository.findByEmail(email);
        boolean appUserExists = appUser != null;
        if (appUserExists) {
            appUser.setPassword(encoder.encode(password));
            appUserRepository.save(appUser);
            return "pages/login";
        } else {
            throw new IllegalStateException("This email does not belong to any user.");
        }
    }
    public void unmarkRecruiterToCandidate(AppUser appUser, @AuthenticationPrincipal AppUser companyUser) {

        if(appUser.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("The user must be a recruiter to be unmark as a candidate.");
        }
        if(companyUser != appUser.getCompany()) {
            throw new IllegalStateException("The recruiter is not part of your company.");
        }

        appUser.setCompany(null);
        appUser.setRole(Role.CANDIDATE);

        try{
            companyUser.removeRecruiter(appUser);
        }
        catch(IllegalStateException e) {
            e.getMessage();
        }
        appUserRepository.save(appUser);
        appUserRepository.save(companyUser);

    }
}
