package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * A controller class to update the profile of a company user.
 */
@Controller
public class UpdateCompanyProfileController {

    private UpdateCompanyProfileService updateCompanyProfileService;
    private AppUserService appUserService;
    private AuthService authService;

    @Autowired
    public UpdateCompanyProfileController(UpdateCompanyProfileService updateCompanyProfileService, AppUserService appUserService) {
        this.updateCompanyProfileService = updateCompanyProfileService;
        this.authService = new AuthService();
        this.appUserService = appUserService;
    }

    /**
     * Retrieve the updated company user profile page.
     *
     * @param model the object carrying data attributes passed to the view.
     * @return the company user profile page.
     */
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

    /**
     * Updating the company profile and uploading the profile picture of a company user.
     *
     * @param request        an object carrying the request of a company user to update their profile.
     * @param profilePicture the file image for the profile picture of a company user.
     * @param coverPicture   the file image for the cover picture of a company user.
     * @param bindingResult  the binding result of all controller fields and controller method parameters.
     * @param model          the object carrying data attributes passed to the view.
     * @return the company user profile page.
     */
    @PostMapping("/company/update")
    public String updateAppUserProfile(UpdateCompanyProfileRequest request,
                                       @RequestParam("image") MultipartFile profilePicture,
                                       @RequestParam("coverImage") MultipartFile coverPicture,
                                       BindingResult bindingResult, Model model) {
        if (!authService.doesUserHaveRole(Role.COMPANY)) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            AppUser appUser = authService.getAuthenticatedUser();
            appUserService.uploadProfilePicture(profilePicture, appUser);
            appUserService.uploadCoverPicture(coverPicture, appUser);

            return updateCompanyProfileService.updateProfile(request, authService.getAuthenticatedUser());
        } catch (Exception e) {
            model.addAttribute("error", "There was an error updating. " + e.getMessage());
            return updateAppUserProfile(model);
        }
    }
}
