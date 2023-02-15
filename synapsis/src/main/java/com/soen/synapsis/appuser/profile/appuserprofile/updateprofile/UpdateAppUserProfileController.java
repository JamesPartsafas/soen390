package com.soen.synapsis.appuser.profile.appuserprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UpdateAppUserProfileController {
    private UpdateAppUserProfileService updateAppUserProfileService;

    @Autowired
    public UpdateAppUserProfileController(UpdateAppUserProfileService updateAppUserProfileService) {
        this.updateAppUserProfileService = updateAppUserProfileService;
    }

    @GetMapping("/user/update")
    public String updateAppUserProfile(Model model) {
        if (!AppUser.isUserAuthenticated() || AppUser.getAuthenticatedUser().getRole() != Role.CANDIDATE) {
            return "redirect:/";
        }

        model.addAttribute("updateAppUserProfileRequest", new UpdateAppUserProfileRequest());
        return "pages/updateuserpage";
    }

    @PostMapping("/user/update")
    public String updateAppUserProfile(UpdateAppUserProfileRequest request, BindingResult bindingResult, Model model) {
        if (!AppUser.isUserAuthenticated() || AppUser.getAuthenticatedUser().getRole() != Role.CANDIDATE) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            return updateAppUserProfileService.updateProfile(request, AppUser.getAuthenticatedUser());
        } catch (Exception e) {
            model.addAttribute("error", "There was an error updating. " + e.getMessage());
            return updateAppUserProfile(model);
        }
    }
}
