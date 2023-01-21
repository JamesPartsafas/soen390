package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.index.IndexController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (isUserAuthenticated())
            return "redirect:/";

        model.addAttribute("registrationRequest", new RegistrationRequest());

        return "pages/register";
    }

    @PostMapping("/register")
    public String register(RegistrationRequest request,
                           BindingResult bindingResult,
                           HttpServletRequest servlet,
                           Model model) {
        if (isUserAuthenticated())
            return "redirect:/";

        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            String response = registrationService.register(request);
            servlet.login(request.getEmail(), request.getPassword());

            return response;
        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error registering you. Ensure the entered email is valid.");
            return register(model);
        }
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        if (isUserAuthenticated())
            return "redirect:/";

        return "pages/login";
    }

    @GetMapping("/logout")
    public String viewLogoutPage() {
        return "pages/logout";
    }

    private boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return false;

        return true;
    }
}
