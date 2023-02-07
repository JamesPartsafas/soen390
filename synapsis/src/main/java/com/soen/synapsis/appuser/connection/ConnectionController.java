package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/makeConnection")
    public String makeConnection(@RequestBody AppUser appUser1, @RequestBody AppUser appUser2, Model model) {
        try {
            String returnString = connectionService.makeConnection(appUser1, appUser2);
            return returnString;
        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error connecting with the user: " + e.getMessage());
            return "pages/home";
        }
    }


}
