package com.soen.synapsis.appuser.profile.appuserprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateAppUserProfileService {
    private AppUserProfileRepository appUserProfileRepository;

    @Autowired
    public UpdateAppUserProfileService(AppUserProfileRepository appUserProfileRepository) {
        this.appUserProfileRepository = appUserProfileRepository;
    }

    public String updateProfile(UpdateAppUserProfileRequest request, AppUser appUser) {
        AppUserProfile profile = appUserProfileRepository.findByAppUser(appUser);

        if (profile == null) {
            throw new IllegalStateException("The user does not have a profile.");
        }

        profile.setWork(request.getWork());
        profile.setEducation(request.getEducation());
        profile.setSkill(request.getSkill());
        profile.setProject(request.getProject());
        profile.setAward(request.getAward());
        profile.setCourse(request.getCourse());
        profile.setPhone(request.getPhone());
        profile.setVolunteering(request.getVolunteering());
        profile.setLanguage(request.getLanguage());

        appUserProfileRepository.save(profile);
        return "redirect:/user/" + appUser.getId();
    }
}
