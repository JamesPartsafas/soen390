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

/**
 * A controller class to work with connections.
 */
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

    /**
     * Retrieve all the connections of a given user.
     *
     * @param model an object carrying data attributes passed to the view.
     * @return the network page.
     */
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

    /**
     * Decline a connection request.
     *
     * @param id the id of the appuser that you are rejecting.
     * @param model an object carrying data attributes passed to the view.
     * @return the network page.
     */
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

    /**
     * Accept a connection request.
     *
     * @param id the id of the appuser that you are accepting.
     * @param model an object carrying data attributes passed to the view.
     * @return the network page.
     */
    @PostMapping("/connection/accept")
    public String acceptConnection(@RequestParam("id") Long id, Model model) {
        try {
            return connectionService.acceptConnection(authService.getAuthenticatedUser(), id);
        } catch (Exception e) {
            model.addAttribute("error", "There was an error accepting the connection: " + e.getMessage());
            return viewNetwork(model);
        }
    }

    /**
     * Send a connection request to another user.
     *
     * @param id the id of the appuser that you are sending a connection request to.
     * @param model an object carrying data attributes passed to the view.
     * @return the userpage of the user that you are sending a connection request to.
     */
    @PostMapping("/connect")
    public String connectWithUser(@RequestParam("id") Long id, Model model) {
        try {
            connectionService.connect(authService.getAuthenticatedUser().getId(), id);
            return "redirect:/user/" + id;

        } catch (Exception e) {
            model.addAttribute("error", "There was an error connecting with the user: " + e.getMessage());
            return "redirect:/user/" + id;
        }
    }


    /**
     * Disconnect with another user.
     *
     * @param id the id of the appuser that you are disconneting with.
     * @return the userpage of the user that you are disconneting with.
     */
    @PostMapping("/disconnect")
    public String disconnectFromUser(@RequestParam("id") Long id) {
        connectionService.disconnect(authService.getAuthenticatedUser().getId(), id);
        return "redirect:/user/" + id;
    }
}
