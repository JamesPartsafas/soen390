package com.soen.synapsis.index;

import org.springframework.stereotype.Component;

@Component
public class IndexService {

    public String getHomePage() {
        return "pages/home";
    }
}
