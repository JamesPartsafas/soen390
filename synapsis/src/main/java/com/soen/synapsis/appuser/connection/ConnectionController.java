package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
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

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/network")
    public String viewNetwork(@AuthenticationPrincipal AppUserDetails user, Model model) {
        if (!AppUser.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<AppUser> connections = connectionService.getConnections(user);
        List<AppUser> pendingConnectionRequest = connectionService.getPendingConnectionRequest(user);

        model.addAttribute("connections", connections);
        model.addAttribute("pendingConnectionRequests", pendingConnectionRequest);

        return "pages/network";
    }

    @PostMapping("/connection/reject")
    public String rejectConnection(@AuthenticationPrincipal AppUserDetails user, @RequestParam("id") Long id, Model model) {
        try {
            String returnString = connectionService.rejectConnection(user, id);
            return returnString;
        } catch (Exception e) {
            model.addAttribute("error", "There was an error rejecting the connection: " + e.getMessage());
            return viewNetwork(user, model);
        }
    }

    @PostMapping("/connection/accept")
    public String acceptConnection(@AuthenticationPrincipal AppUserDetails user, @RequestParam("id") Long id, Model model) {
        try {
            return connectionService.acceptConnection(user, id);
        } catch (Exception e) {
            model.addAttribute("error", "There was an error accepting the connection: " + e.getMessage());
            return viewNetwork(user, model);
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
    public String disconnectFromUser(@AuthenticationPrincipal AppUserDetails user, @RequestParam("id") Long id) {
        connectionService.disconnect(user.getID(), id);
        return "redirect:/user/" + id;
    }
}
