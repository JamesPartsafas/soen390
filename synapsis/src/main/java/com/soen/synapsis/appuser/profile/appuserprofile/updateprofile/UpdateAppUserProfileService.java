package com.soen.synapsis.appuser.profile.appuserprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A service class to update the profile of an app user.
 */
@Service
public class UpdateAppUserProfileService {
    private AppUserProfileRepository appUserProfileRepository;

    @Autowired
    public UpdateAppUserProfileService(AppUserProfileRepository appUserProfileRepository) {
        this.appUserProfileRepository = appUserProfileRepository;
    }

    /**
     * Retrieve the profile of an app user.
     * @param appUser the object representing the app user.
     * @return the AppUserProfile of an app user.
     */
    public AppUserProfile findAppUserProfile(AppUser appUser) {
        return appUserProfileRepository.findByAppUser(appUser);
    }

    /**
     * Update the profile of the app user based on his request.
     * @param request an object carrying the request of an app user to update their profile.
     * @param appUser the object representing the app user.
     * @return the app user profile page.
     */
    public String updateProfile(UpdateAppUserProfileRequest request, AppUser appUser) {
        AppUserProfile profile = findAppUserProfile(appUser);

        if (profile == null) {
            throw new IllegalStateException("The user does not have a profile.");
        }

        profile.setDescription(request.getDescription());
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
