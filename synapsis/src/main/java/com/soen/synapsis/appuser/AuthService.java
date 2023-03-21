package com.soen.synapsis.appuser;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * A service to quickly verify a user's login status and role, as well as retrieving additional details from them.
 */
@Component
public class AuthService {

    /**
     * Verifies if requesting user is logged in or not.
     * @return True if user is logged in, false otherwise.
     */
    public boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return false;

        return true;
    }

    /**
     * Retrieve the authenticated user.
     * @return Instance of AppUser of the authenticated user.
     */
    public AppUser getAuthenticatedUser() {
        if (!this.isUserAuthenticated()) {
            throw new IllegalStateException("User not registered");
        }

        AppUserAuth appUserAuth = (AppUserAuth) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return appUserAuth.getAppUser();
    }

    /**
     * verifies if user has specified role.
     * @param role Role to check for.
     * @return True if user is both authenticated and has the role. False otherwise.
     */
    public boolean doesUserHaveRole(Role role) {
        // User is not logged in, return false
        if (!this.isUserAuthenticated()) {
            return false;
        }

        // User logged in with correct role, return true
        Role authedUserRole = this.getAuthenticatedUser().getRole();
        if (authedUserRole == role) {
            return true;
        }

        // User logged in with incorrect role, return false
        return false;
    }

    /**
     * verifies if user has either specified role.
     * @param role1 First role to check for.
     * @param role2 Second role to check for.
     * @return True if user is both authenticated and has either role. False otherwise.
     */
    public boolean doesUserHaveRole(Role role1, Role role2) {
        if (this.doesUserHaveRole(role1) || this.doesUserHaveRole(role2))
            return true;

        return false;
    }
}
