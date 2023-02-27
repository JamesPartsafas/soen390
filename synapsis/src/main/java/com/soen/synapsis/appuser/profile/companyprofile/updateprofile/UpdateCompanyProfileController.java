package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UpdateCompanyProfileController {

    private UpdateCompanyProfileService updateCompanyProfileService;
    private AuthService authService;

    @Autowired
    public UpdateCompanyProfileController(UpdateCompanyProfileService updateCompanyProfileService) {
        this.updateCompanyProfileService = updateCompanyProfileService;
        this.authService = new AuthService();
    }

    @GetMapping("/company/update")
    public String updateAppUserProfile(Model model) {
        if (!authService.doesUserHaveRole(Role.COMPANY)) {
            return "redirect:/";
        }

        CompanyProfile profile = updateCompanyProfileService.findAppUserProfile(authService.getAuthenticatedUser());
        if (profile == null)
            profile = new CompanyProfile();

        model.addAttribute("updateCompanyProfileRequest", new UpdateCompanyProfileRequest());
        model.addAttribute("profile", profile);

        return "pages/updatecompanypage";
    }

    @PostMapping("/company/update")
    public String updateAppUserProfile(UpdateCompanyProfileRequest request, BindingResult bindingResult, Model model) {
        if (!authService.doesUserHaveRole(Role.COMPANY)) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            return updateCompanyProfileService.updateProfile(request, authService.getAuthenticatedUser());
        } catch (Exception e) {
            model.addAttribute("error", "There was an error updating. " + e.getMessage());
            return updateAppUserProfile(model);
        }
    }
}
