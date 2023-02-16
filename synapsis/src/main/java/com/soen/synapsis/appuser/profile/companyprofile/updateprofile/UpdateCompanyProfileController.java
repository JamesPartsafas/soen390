package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UpdateCompanyProfileController {

    private UpdateCompanyProfileService updateCompanyProfileService;

    @Autowired
    public UpdateCompanyProfileController(UpdateCompanyProfileService updateCompanyProfileService) {
        this.updateCompanyProfileService = updateCompanyProfileService;
    }

    @GetMapping("/company/update")
    public String updateAppUserProfile(Model model) {
        if (!AppUser.isUserAuthenticated() || AppUser.getAuthenticatedUser().getRole() != Role.COMPANY) {
            return "redirect:/";
        }

        model.addAttribute("updateCompanyProfileRequest", new UpdateCompanyProfileRequest());
        return "pages/updatecompanypage";
    }

    @PostMapping("/company/update")
    public String updateAppUserProfile(UpdateCompanyProfileRequest request, BindingResult bindingResult, Model model) {
        if (!AppUser.isUserAuthenticated() || AppUser.getAuthenticatedUser().getRole() != Role.COMPANY) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            return updateCompanyProfileService.updateProfile(request, AppUser.getAuthenticatedUser());
        } catch (Exception e) {
            model.addAttribute("error", "There was an error updating. " + e.getMessage());
            return updateAppUserProfile(model);
        }
    }
}
