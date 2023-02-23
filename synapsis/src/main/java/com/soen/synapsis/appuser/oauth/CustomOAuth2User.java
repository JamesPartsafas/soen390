package com.soen.synapsis.appuser.oauth;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserAuth;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User implements OAuth2User, AppUserAuth {
    private AppUser appUser;

    public CustomOAuth2User(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("name", appUser.getName());
        attributes.put("email", appUser.getEmail());
        attributes.put("role", appUser.getRole().toString());

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(appUser.getRole().toString()));

        return authorities;
    }

    @Override
    public String getName() {
        return appUser.getName();
    }

    public String getEmail() {
        return appUser.getEmail();
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
