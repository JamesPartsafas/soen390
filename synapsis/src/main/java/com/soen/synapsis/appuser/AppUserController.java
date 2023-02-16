package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AppUserController {

    private final AppUserService appUserService;
    private final ConnectionService connectionService;

    @Autowired
    public AppUserController(AppUserService appUserService, ConnectionService connectionService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
    }

    @GetMapping("/user/{uid}")
    public String getAppUser(@AuthenticationPrincipal AppUserDetails user, @PathVariable Long uid, Model model) {
        if (!AppUser.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();
        boolean isConnectedWith = connectionService.isConnectedWith(user.getID(), uid);

        model.addAttribute("id", appUser.getId());
        model.addAttribute("name", appUser.getName());
        model.addAttribute("email", appUser.getEmail());
        model.addAttribute("isConnectedWith", isConnectedWith);

        if (appUser.getRole() == Role.COMPANY) {
            CompanyProfile companyProfile = appUser.getCompanyProfile();
            model.addAttribute("website", companyProfile.getWebsite());
            model.addAttribute("industry", companyProfile.getIndustry());
            model.addAttribute("companySize", companyProfile.getCompanySize());
            model.addAttribute("location", companyProfile.getLocation());
            model.addAttribute("speciality", companyProfile.getSpeciality());
            return "pages/companypage";
        }

        AppUserProfile profile = appUser.getAppUserProfile();
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
    public String getUsersLikeName(@AuthenticationPrincipal AppUserDetails user, @RequestParam String name, Model model) {
        if (!AppUser.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<AppUser> users = appUserService.getUsersLikeName(name, user.getId());
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
    public String markCandidateToRecruiter(AppUser appUser, @AuthenticationPrincipal AppUser companyUser) {
        if (companyUser.getRole() != Role.COMPANY) {
            return "You must be a company to mark candidates as recruiters.";
        }
        try {
            appUserService.markCandidateToRecruiter(appUser, companyUser);
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        return "pages/userpage";
    }

    @PutMapping("/company/unmarkRecruiterToCandidate")
    @ResponseBody
    public String unmarkRecruiterToCandidate(AppUser appUser, @AuthenticationPrincipal AppUser companyUser) {
        if(companyUser.getRole() != Role.COMPANY) {
            return "You must be a company to unmark recruiters as candidates.";
        }
        try {
            appUserService.unmarkRecruiterToCandidate(appUser, companyUser);
        }
        catch(IllegalStateException e) {
            return e.getMessage();
        }
        return "pages/userpage";
    }

    @GetMapping("/updateuserpage")
    public String getUpdateUserProfile() {
        return "pages/updateuserpage";
    }

}
