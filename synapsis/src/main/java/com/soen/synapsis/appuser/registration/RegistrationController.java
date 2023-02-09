package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


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
        if (AppUser.isUserAuthenticated())
            return "redirect:/";

        model.addAttribute("registrationRequest", new RegistrationRequest());

        return "pages/register";
    }

    @PostMapping("/register")
    public String register(RegistrationRequest request,
                           BindingResult bindingResult,
                           HttpServletRequest servlet,
                           Model model) {
        if (AppUser.isUserAuthenticated())
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
            model.addAttribute("error", "There was an error registering you. " + e.getMessage());
            return register(model);
        }
    }


    @GetMapping(value ="/admincreation")
    public String registerAdmin(Model model) {
        if (isUserAuthenticated()) {
            model.addAttribute("registrationRequest", new RegistrationRequest());

            return "pages/adminCreationPage";
        }
        return null;
    }

    @PostMapping (value ="/admincreation")
    public String registerAdmin(RegistrationRequest request,
                           BindingResult bindingResult,
                           Model model) {

        try{

            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            if (isUserAuthenticated()){
                String response = registrationService.registerAdmin(request);
                return response;
            }
        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error registering the ADMIN. " + e.getMessage());
            return registerAdmin(model);
        }
        return null;
    }


    @GetMapping("/passwordreset")
    public String passwordReset(Model model) {

            model.addAttribute("registrationRequest", new RegistrationRequest());

            return "pages/passwordResetForm";

    }

    @PostMapping("/passwordreset")
    public String passwordReset(RegistrationRequest request,
                                BindingResult bindingResult,
                                Model model) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

                String response = registrationService.updateUserPassword(request);
                return response;
        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error resetting your password. " + e.getMessage());
            return passwordReset(model);
        }
    }


    @GetMapping("/login")
    public String viewLoginPage() {
        if (AppUser.isUserAuthenticated())
            return "redirect:/";

        return "pages/login";
    }

    @GetMapping("/logout")
    public String viewLogoutPage() {
        return "pages/logout";
    }
}
