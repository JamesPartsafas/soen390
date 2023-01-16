package com.soen.synapsis.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public AppUser getAppUser() {
        return appUserService.getAppUser();
    }

    @GetMapping("privateuser")
    public String getUserData() {
        return "This is the user page";
    }

    @GetMapping("admin")
    public String getAdminData() {
        return "This is the admin page";
    }
}
