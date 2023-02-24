package com.soen.synapsis.index;

import org.springframework.stereotype.Component;

@Component
public class IndexService {

    public String getHomePage() {
        return "pages/home";
    }

    public String getAdminPage() {
        return "pages/adminCreationPage";
    }

    public String getAccessDeniedPage(){
        return "pages/accessDenied";
    }

    public String getPasswordResetPage(){
        return "pages/passwordResetForm";
    }

    public String redirectHomePage() {return "pages/home";
    }
}
