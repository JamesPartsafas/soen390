package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void makeConnection(@RequestBody AppUser appUser1, @RequestBody AppUser appUser2) {
        connectionService.makeConnection(appUser1, appUser2);
    }


}
