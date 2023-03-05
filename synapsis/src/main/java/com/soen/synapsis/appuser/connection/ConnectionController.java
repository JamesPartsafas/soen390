package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ConnectionController {

    private final ConnectionService connectionService;
    private final AuthService authService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
        this.authService = new AuthService();
    }

    public ConnectionController(ConnectionService connectionService, AuthService authService) {
        this.connectionService = connectionService;
        this.authService = authService;
    }

    @GetMapping("/network")
    public String viewNetwork(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        AppUser user = authService.getAuthenticatedUser();

        List<AppUser> connections = connectionService.getConnections(user);
        List<AppUser> pendingConnectionRequest = connectionService.getPendingConnectionRequest(user);

        model.addAttribute("connections", connections);
        model.addAttribute("pendingConnectionRequests", pendingConnectionRequest);

        return "pages/network";
    }

    @PostMapping("/connection/reject")
    public String rejectConnection(@RequestParam("id") Long id, Model model) {
        try {
            String returnString = connectionService.rejectConnection(authService.getAuthenticatedUser(), id);
            return returnString;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error rejecting the connection: " + e.getMessage());
            return viewNetwork(model);
        }
    }

    @PostMapping("/connection/accept")
    public String acceptConnection(@RequestParam("id") Long id, Model model) {
        try {
            return connectionService.acceptConnection(authService.getAuthenticatedUser(), id);
        } catch (Exception e) {
            model.addAttribute("error", "There was an error accepting the connection: " + e.getMessage());
            return viewNetwork(model);
        }
    }

    @PostMapping("/makeConnection")
    public String makeConnection(@RequestBody AppUser appUser1, @RequestBody AppUser appUser2, Model model) {
        try {
            String returnString = connectionService.makeConnection(appUser1, appUser2);
            return returnString;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error connecting with the user: " + e.getMessage());
            return "pages/network";
        }
    }


    @PostMapping("/disconnect")
    public String disconnectFromUser(@RequestParam("id") Long id) {
        connectionService.disconnect(authService.getAuthenticatedUser().getId(), id);
        return "redirect:/user/" + id;
    }
}
