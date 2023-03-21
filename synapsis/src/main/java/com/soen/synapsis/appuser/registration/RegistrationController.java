package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Entry point for user requests to register to Synapsis or modify account details
 */
@Controller
public class RegistrationController {

    private RegistrationService registrationService;
    private AuthService authService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
        this.authService = new AuthService();
    }

    public RegistrationController(RegistrationService registrationService, AuthService authService) {
        this.registrationService = registrationService;
        this.authService = authService;
    }

    /**
     * Allows user to connect to registration page
     * @param model used to map DTOs to page
     * @return Path to the view containing the registration page
     */
    @GetMapping("/register")
    public String register(Model model) {
        if (authService.isUserAuthenticated())
            return "redirect:/";

        model.addAttribute("registrationRequest", new RegistrationRequest());

        return "pages/register";
    }

    /**
     * Processes request data to store a new user in the database
     * @param request Contains request information from user
     * @param bindingResult Framework specific object to verify integrity of model binding
     * @param servlet Framework specific object tracking login status of user
     * @param model Contains data to be passed to rendered view
     * @return View to homepage if registration is successful, else returns view to registration page
     */
    @PostMapping("/register")
    public String register(RegistrationRequest request,
                           BindingResult bindingResult,
                           HttpServletRequest servlet,
                           Model model) {
        if (authService.isUserAuthenticated())
            return "redirect:/";

        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            String response = registrationService.register(request);
            servlet.login(request.getEmail(), request.getPassword());

            return response;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error registering you. " + e.getMessage());
            return register(model);
        }
    }

    /**
     * Allows user to connect to admin creation page
     * @param model Allows data to be passed to view
     * @return View of admin creation page
     */
    @GetMapping(value = "/admincreation")
    public String registerAdmin(Model model) {
        if (authService.isUserAuthenticated()) {
            model.addAttribute("registrationRequest", new RegistrationRequest());

            return "pages/adminCreationPage";
        }
        return null;
    }

    /**
     * Processes admin request to create a new admin
     * @param request Contains request data
     * @param bindingResult Framework specific object to verify integrity of model binding
     * @param model Used to pass data to view
     * @return Returns confirmation page if successful, else returns admin creation page
     */
    @PostMapping(value = "/admincreation")
    public String registerAdmin(RegistrationRequest request,
                                BindingResult bindingResult,
                                Model model) {

        try {

            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            if (authService.isUserAuthenticated()) {
                String response = registrationService.registerAdmin(request);
                return response;
            }
        } catch (Exception e) {
            model.addAttribute("error", "There was an error registering the ADMIN. " + e.getMessage());
            return registerAdmin(model);
        }
        return null;
    }

    /**
     * Allows user to view password update page
     * @param model Allows data to be passed to view
     * @return View containing password update page
     */
    @GetMapping("/passwordupdate")
    public String passwordUpdate(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("passwordUpdateRequest", new PasswordUpdateRequest());

        return "pages/passwordUpdateForm";

    }

    /**
     * Processes request for password update
     * @param request Contains request data
     * @param bindingResult Framework specific object to verify integrity of model binding
     * @param model Allows data to be passed to view
     * @return View containing home page if successful, else view containing password update page
     */
    @PostMapping("/passwordupdate")
    public String passwordUpdate(PasswordUpdateRequest request,
                                 BindingResult bindingResult,
                                 Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }
            AppUser appUser = authService.getAuthenticatedUser();

            String response = registrationService.updateUserPassword(appUser, request.getOldPassword(), request.getNewPassword());
            return response;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error resetting your password. " + e.getMessage());
            return passwordUpdate(model);
        }
    }

    /**
     * Allows user to view password reset page
     * @param model Allows data to be passed to view
     * @return View containing password reset page
     */
    @GetMapping("/passwordreset")
    public String passwordReset(Model model) {

        model.addAttribute("registrationRequest", new RegistrationRequest());

        return "pages/passwordResetForm";

    }

    /**
     * Processes password reset request
     * @param request Contains request data
     * @param bindingResult Framework specific object to verify integrity of model binding
     * @param model Allows data to be passed to view
     * @return View containing home page if successful, else returns password reset page
     */
    @PostMapping("/passwordreset")
    public String passwordReset(RegistrationRequest request,
                                BindingResult bindingResult,
                                Model model) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            String response = registrationService.resetUserPassword(request);
            return response;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error resetting your password. " + e.getMessage());
            return passwordReset(model);
        }
    }

    /**
     * Allows user to view login page
     * @return View containing login page
     */
    @GetMapping("/login")
    public String viewLoginPage() {
        if (authService.isUserAuthenticated())
            return "redirect:/";

        return "pages/login";
    }
}
