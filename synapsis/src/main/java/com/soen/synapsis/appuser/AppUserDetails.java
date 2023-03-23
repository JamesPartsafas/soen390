package com.soen.synapsis.appuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Wrapper for AppUser used by authentication framework. This wrapper
 * is used for users logging in or registering through internal Synapsis system,
 * and not an external SSO provider.
 */
public class AppUserDetails implements UserDetails, AppUserAuth {

    private AppUser appUser;

    public AppUserDetails(AppUser appUser) {
        super();
        this.appUser = appUser;
    }

    /**
     * Returns authorities of user based on their role.
     * @return A collection of authorities containing 1 element.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(appUser.getRole().toString()));
    }

    public Long getID() {
        return appUser.getId();
    }

    public AppUser getAppUser() {
        return appUser;
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() { return appUser.getId().toString(); }

    /**
     * Verifies if account has not yet reached expiration date.
     * Used by authentication framework.
     * @return Always returns true, meaning account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Verifies if account has not been locked..
     * Used by authentication framework.
     * @return Always returns true, meaning account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifies if credentials has not yet reached expiration date.
     * Used by authentication framework.
     * @return Always returns true, meaning credentials is not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifies if account is enabled for use.
     * Used by authentication framework.
     * @return The opposite of the user's isBanned value.
     */
    @Override
    public boolean isEnabled() {
        if (appUser.getIsBanned() == null)
            return true;

        return !appUser.getIsBanned();
    }

    public Role getRole() {
        return appUser.getRole();
    }

    public Long getId() {
        return appUser.getId();
    }
}
