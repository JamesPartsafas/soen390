package com.soen.synapsis.appuser.oauth;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * Wrapper for AppUser to interface with OAuth authentication framework
 */
public class CustomOAuth2User implements OAuth2User, AppUserAuth {
    private AppUser appUser;

    public CustomOAuth2User(AppUser appUser) {
        this.appUser = appUser;
    }

    /**
     * Retrieves attributes of OAuth2 user
     * @return Map containing name, email, and role
     */
    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("name", appUser.getName());
        attributes.put("email", appUser.getEmail());
        attributes.put("role", appUser.getRole().toString());

        return attributes;
    }

    /**
     * Retrieves authorities based on role of AppUser
     * @return Collection of authorities containing 1 authority only
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().toString()));

        return authorities;
    }

    @Override
    public String getName() {
        return appUser.getId().toString();
    }

    public String getEmail() {
        return appUser.getEmail();
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
