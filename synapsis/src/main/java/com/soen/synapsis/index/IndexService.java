package com.soen.synapsis.index;

import org.springframework.stereotype.Component;

/**
 * A service class to work the index.
 */
@Component
public class IndexService {

    /**
     * Retrieve the homepage view.
     *
     * @return the home html page.
     */
    public String getHomePage() {
        return "pages/home";
    }

    /**
     * Retrieve the admin view page.
     *
     * @return the admin html page.
     */
    public String getAdminPage() {
        return "pages/adminCreationPage";
    }

    /**
     * Retrieve the access denied page.
     *
     * @return the access denied html page.
     */
    public String getAccessDeniedPage(){
        return "pages/accessDenied";
    }

    /**
     * Retrieve the password reset page.
     *
     * @return the password reset form html page.
     */
    public String getPasswordResetPage(){
        return "pages/passwordResetForm";
    }

    /**
     * Redirect to home page.
     *
     * @return the home html page.
     */
    public String redirectHomePage() {return "pages/home";
    }
}
