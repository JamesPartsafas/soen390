package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.connection.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        return "pages/userpage";
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
