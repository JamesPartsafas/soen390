package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AppUserController {

    private final AppUserService appUserService;
    private final ConnectionService connectionService;
    private final AuthService authService;

    @Autowired
    public AppUserController(AppUserService appUserService, ConnectionService connectionService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = new AuthService();
    }

    public AppUserController(AppUserService appUserService, ConnectionService connectionService, AuthService authService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.authService = authService;
    }

    @GetMapping("/user")
    public String redirectToAuthenticatedUserProfile() {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        String userProfileURL = "redirect:/user/" + authService.getAuthenticatedUser().getId();

        return userProfileURL;
    }

    @GetMapping("/user/{uid}")
    public String getAppUser(@PathVariable Long uid, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();
        if (appUser.getRole() == Role.ADMIN) {
            return "redirect:/";
        }

        boolean isConnectedWith = connectionService.isConnectedWith(authService.getAuthenticatedUser().getId(), uid);

        model.addAttribute("id", appUser.getId());
        model.addAttribute("name", appUser.getName());
        model.addAttribute("email", appUser.getEmail());
        model.addAttribute("isConnectedWith", isConnectedWith);

        if (authService.getAuthenticatedUser().getId() == uid)
            model.addAttribute("showControls", true);

        if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = appUser.getCompanyProfile();
            if (companyProfile == null)
                companyProfile = new CompanyProfile();

            model.addAttribute("description", companyProfile.getDescription());
            model.addAttribute("website", companyProfile.getWebsite());
            model.addAttribute("industry", companyProfile.getIndustry());
            model.addAttribute("companySize", companyProfile.getCompanySize());
            model.addAttribute("location", companyProfile.getLocation());
            model.addAttribute("speciality", companyProfile.getSpeciality());
            return "pages/companypage";
        }

        AppUserProfile profile = appUser.getAppUserProfile();
        if (profile == null)
            profile = new AppUserProfile();

        model.addAttribute("description", profile.getDescription());
        model.addAttribute("education", profile.getEducation());
        model.addAttribute("skill", profile.getSkill());
        model.addAttribute("work", profile.getWork());
        model.addAttribute("course", profile.getCourse());
        model.addAttribute("phone", profile.getPhone());
        model.addAttribute("volunteering", profile.getVolunteering());
        model.addAttribute("project", profile.getProject());
        model.addAttribute("award", profile.getAward());
        model.addAttribute("language", profile.getLanguage());

        return "pages/userpage";

    }

    @GetMapping("/search")
    public String getUsersLikeName(@RequestParam String name, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<AppUser> users = appUserService.getUsersLikeName(name, authService.getAuthenticatedUser().getId());
        model.addAttribute("users", users);
        return "pages/usersearchpage";
    }

    @GetMapping("/privateuser")
    @ResponseBody
    public String getUserData() {
        return "This is the user page";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String getAdminData() {
        return "This is the admin page";
    }

    @PutMapping("/company/markCandidateToRecruiter")
    @ResponseBody
    public String markCandidateToRecruiter(AppUser appUser) {
        if (!authService.doesUserHaveRole(Role.COMPANY)) {
            return "You must be a company to mark candidates as recruiters.";
        }
        try {
            appUserService.markCandidateToRecruiter(appUser, authService.getAuthenticatedUser());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        return "pages/userpage";
    }

    @PutMapping("/company/unmarkRecruiterToCandidate")
    @ResponseBody
    public String unmarkRecruiterToCandidate(AppUser appUser) {
        if (!authService.doesUserHaveRole(Role.COMPANY)) {
            return "You must be a company to unmark recruiters as candidates.";
        }
        try {
            appUserService.unmarkRecruiterToCandidate(appUser, authService.getAuthenticatedUser());
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        return "pages/userpage";
    }

    @GetMapping("/updateuserpage")
    public String getUpdateUserProfile() {
        return "pages/updateuserpage";
    }
}
