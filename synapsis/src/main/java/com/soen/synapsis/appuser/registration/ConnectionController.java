package com.soen.synapsis.appuser.registration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnectionController {
    @GetMapping("/network")
    public String viewNetwork() {

        return "pages/network";
    }
}
