package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private AppUserProfileRepository appUserProfileRepository;
    private CompanyProfileRepository companyProfileRepository;
    private final BCryptPasswordEncoder encoder;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AppUserProfileRepository appUserProfileRepository,
            CompanyProfileRepository companyProfileRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserProfileRepository = appUserProfileRepository;
        this.companyProfileRepository = companyProfileRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    public Optional<AppUser> getAppUser(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser getAppUser(String email) {
        return appUserRepository.findByEmail(email);
    }

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

    public void markCandidateToRecruiter(AppUser appUser, @AuthenticationPrincipal AppUserDetails companyUser) {

        if (appUser.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("The user must be a candidate to be marked as a recruiter.");
        }
        appUser.setRole(Role.RECRUITER);
        appUser.setCompanyId(companyUser.getId());
        appUserRepository.save(appUser);

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
}
