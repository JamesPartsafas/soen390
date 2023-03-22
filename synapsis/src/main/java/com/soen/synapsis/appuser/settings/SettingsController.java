package com.soen.synapsis.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Entry point for users to interact with their settings
 */
@Controller
public class SettingsController {
    private final AuthService authService;
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.authService = new AuthService();
        this.settingsService = settingsService;
    }

    public SettingsController(SettingsService settingsService, AuthService authService) {
        this.authService = authService;
        this.settingsService = settingsService;
    }

    /**
     * @param model Used to map data to page
     * @return Path to settings page or redirect to home if errors exist
     */
    @GetMapping("/settings")
    public String getUserSettings(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        AppUser appUser = authService.getAuthenticatedUser();
        if (appUser.getRole() == Role.ADMIN) {
            return "redirect:/";
        }

        model.addAttribute("settings", appUser.getSettings());
        model.addAttribute("updateSettingsRequest", new UpdateSettingsRequest());

        return "pages/settings";
    }

    /**
     * @param updateSettingsRequest Request object containing settings data
     * @param bindingResult         Framework specific object to verify integrity of model binding
     * @param model                 Used to map data to page
     * @return
     */
    @PostMapping("/updateSettings")
    public String updateUserSettings(UpdateSettingsRequest updateSettingsRequest,
                                     BindingResult bindingResult,
                                     Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        AppUser appUser = authService.getAuthenticatedUser();
        if (appUser.getRole() == Role.ADMIN) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            throw new IllegalStateException("Problem with form submission");
        }

        settingsService.updateSettings(updateSettingsRequest, appUser);

        model.addAttribute("settings", appUser.getSettings());

        return "pages/settings";
    }
}
