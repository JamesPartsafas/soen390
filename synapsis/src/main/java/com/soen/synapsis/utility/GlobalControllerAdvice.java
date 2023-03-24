package com.soen.synapsis.utility;

import com.soen.synapsis.appuser.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Middleware for all requests,
 * used to set model attributes for use in templating.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    private AuthService authService;

    @Autowired
    public GlobalControllerAdvice(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Sets the ID of the current authenticated user
     * @return The ID of the current user, mapped to globalUserId
     */
    @ModelAttribute("globalUserId")
    public Long getUserId() {
        if (!authService.isUserAuthenticated())
            return null;

        return authService.getAuthenticatedUser().getId();
    }
}
