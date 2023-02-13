package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AppUserService(AppUserRepository appUserRepository,AppUserProfileRepository appUserProfileRepository, CompanyProfileRepository companyProfileRepository) {
        this.appUserRepository = appUserRepository;
        this.appUserProfileRepository= appUserProfileRepository;
        this.companyProfileRepository= companyProfileRepository;
        this.encoder = new BCryptPasswordEncoder();
    }



// actually update method that will be called by update of appuser controller , u gonna do appUserRepository.save(appUser);
    public Optional<AppUser> getAppUser(Long id) {
        return appUserRepository.findById(id);
    }

    public String signUpUser(AppUser appUser) {
        boolean appUserExists = appUserRepository.findByEmail(appUser.getEmail()) != null;

        if (appUserExists) {
            throw new IllegalStateException("This email is already taken.");
        }else if (appUser.getRole() == Role.RECRUITER) {
            throw new IllegalStateException("Cannot sign up a recruiter");
        }

        appUser.setPassword(encoder.encode(appUser.getPassword()));

        appUserRepository.save(appUser);

        if(appUser.getRole() == Role.CANDIDATE) {
            AppUserProfile profile = new AppUserProfile();
            profile.setAppUser(appUser);
            appUserProfileRepository.save(profile);
        } else if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile  = new CompanyProfile();
            companyProfile.setAppUser(appUser);
            companyProfileRepository.save(companyProfile);
        }

        return "pages/home";
    }








//    public Optional<AppUserProfile> getAppUserProfile(Long id) {
//        return appUserProfileRepository.findById(id);
//    }


//    public String UpdateProfile(AppUserProfile profile) {
////        userProfile.setName(userProfile.getName());
//
//        appUserProfileRepository.save(profile);
//
//        return "pages/userpage";
//    }

//    public String UpdateProfile(Long id){
//        AppUserProfile profile = new AppUserProfile()ppUserRepository.findById(id);
//
//        profile.setName(profile.getName());
//            appUserRepository.save(appUser);
//            return "pages/login";
//        }
//        else{
//            throw new IllegalStateException("This email does not belong to any user.");
//        }

//    }


}
