package com.soen.synapsis.appuser;

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

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/{uid}")
    public String getAppUser(@PathVariable Long uid, Model model) {
        Optional<AppUser> optionalAppUser = appUserService.getAppUser(uid);

        if (optionalAppUser.isEmpty())
            return "redirect:/";

        AppUser appUser = optionalAppUser.get();

        model.addAttribute("id", appUser.getId());
        model.addAttribute("name", appUser.getName());
        model.addAttribute("email", appUser.getEmail());

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

    @PutMapping("/company/setCandidateToRecruiter")
    @ResponseBody
    public String markCandidateToRecruiter(AppUser appUser, @AuthenticationPrincipal AppUserDetails companyUser) {
        if(companyUser.getRole() != Role.COMPANY) {
            return "You must be a company to mark candidates as recruiters.";
        }
        try {
            appUserService.markCandidateToRecruiter(appUser, companyUser);
        }
        catch(IllegalStateException e) {
            return e.getMessage();
        }
        return "pages/userpage";
    }
}
