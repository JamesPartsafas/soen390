package com.soen.synapsis.appuser.profile.appuserprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * A controller class to update the profile of an app user.
 */
@Controller
public class UpdateAppUserProfileController {
    private UpdateAppUserProfileService updateAppUserProfileService;
    private AuthService authService;
    private AppUserService appUserService;

    @Autowired
    public UpdateAppUserProfileController(UpdateAppUserProfileService updateAppUserProfileService, AppUserService appUserService) {
        this.updateAppUserProfileService = updateAppUserProfileService;
        this.authService = new AuthService();
        this.appUserService = appUserService;
    }

    public UpdateAppUserProfileController(UpdateAppUserProfileService updateAppUserProfileService,
                                          AuthService authService, AppUserService appUserService) {
        this.updateAppUserProfileService = updateAppUserProfileService;
        this.authService = authService;
        this.appUserService = appUserService;
    }

    /**
     * Retrieve the updated app user profile page.
     *
     * @param model the object carrying data attributes passed to the view.
     * @return the app user profile page.
     */
    @GetMapping("/user/update")
    public String updateAppUserProfile(Model model) {
        if (!authService.doesUserHaveRole(Role.CANDIDATE, Role.RECRUITER)) {
            return "redirect:/";
        }

        AppUserProfile profile = updateAppUserProfileService.findAppUserProfile(authService.getAuthenticatedUser());
        if (profile == null)
            profile = new AppUserProfile();

        model.addAttribute("updateAppUserProfileRequest", new UpdateAppUserProfileRequest());
        model.addAttribute("profile", profile);
        if (profile.getAppUser() != null && profile.getAppUser().getResume() != null) {
            model.addAttribute("default_resume", profile.getAppUser().getResume().getFileName());
        }
        if (profile.getAppUser() != null && profile.getAppUser().getCoverLetter() != null) {
            model.addAttribute("default_cover_letter", profile.getAppUser().getCoverLetter().getFileName());
        }

        return "pages/updateuserpage";
    }

    /**
     * Updating the profile and uploading the profile picture of an app user.
     *
     * @param request        an object carrying the request of an app user to update their profile.
     * @param profilePicture the file image for the profile picture of an app user.
     * @param coverPicture   the file image for the profile picture of an app user.
     * @param bindingResult  the binding result of all controller fields and controller method parameters.
     * @param model          the object carrying data attributes passed to the view.
     * @return the app user profile page.
     */
    @PostMapping("/user/update")
    public String updateAppUserProfile(UpdateAppUserProfileRequest request,
                                       @RequestParam("image") MultipartFile profilePicture,
                                       @RequestParam("coverImage") MultipartFile coverPicture,
                                       @RequestParam("defaultResume") MultipartFile defaultResume,
                                       @RequestParam("defaultCoverLetter") MultipartFile defaultCoverLetter,
                                       BindingResult bindingResult, Model model) {
        if (!authService.doesUserHaveRole(Role.CANDIDATE, Role.RECRUITER)) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            AppUser appUser = authService.getAuthenticatedUser();
            appUserService.uploadProfilePicture(profilePicture, appUser);
            appUserService.uploadCoverPicture(coverPicture, appUser);

            appUserService.uploadDefaultResume(defaultResume, appUser);
            appUserService.uploadDefaultCoverLetter(defaultCoverLetter, appUser);

            return updateAppUserProfileService.updateProfile(request, appUser);
        } catch (Exception e) {
            model.addAttribute("error", "There was an error updating. " + e.getMessage());
            return updateAppUserProfile(model);
        }
    }
}
