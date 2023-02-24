package com.soen.synapsis.appuser;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthService {
    public boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return false;

        return true;
    }

    public AppUser getAuthenticatedUser() {
        if (!this.isUserAuthenticated()) {
            throw new IllegalStateException("User not registered");
        }

        AppUserAuth appUserAuth = (AppUserAuth) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return appUserAuth.getAppUser();
    }

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

    public boolean doesUserHaveRole(Role role1, Role role2) {
        if (this.doesUserHaveRole(role1) || this.doesUserHaveRole(role2))
            return true;

        return false;
    }
}
