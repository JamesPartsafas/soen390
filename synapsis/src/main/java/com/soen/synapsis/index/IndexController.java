package com.soen.synapsis.index;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Controller
@RequestMapping(path = "/")
public class IndexController {
    private AppUser appUser;
    private final IndexService indexService;

    @Autowired
    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("attr", "This is an attribute for thymeleaf");
        return indexService.getHomePage();
    }


    @GetMapping(value= "/admincreationpage")
    public String getAdminPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return indexService.getAdminPage();
    }

    @GetMapping(value= "/passwordresetpage")
    public String getPasswordResetPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return indexService.getPasswordResetPage();
    }

    @GetMapping(value= "/accessDenied")
    public String getAccessDeniedPage(Model model) {
        return indexService.getAccessDeniedPage();
    }

}
