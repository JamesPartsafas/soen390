package com.soen.synapsis.unit.appuser.oauth;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.oauth.CustomOAuth2User;
import com.soen.synapsis.utility.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomOAuth2UserTest {

    private CustomOAuth2User underTest;
    private Long id;
    private String name;
    private String email;
    private Role role;

    @BeforeEach
    void setUp() {
        id = 6L;
        name = "Joe Smith";
        email = "joe@mail.com";
        role = Role.CANDIDATE;
        underTest = new CustomOAuth2User(new AppUser(id, name, Constants.SSO_PASSWORD, email, role));
    }

    @Test
    void getAttributes() {
        Map<String, Object> attributes = underTest.getAttributes();

        assertEquals(name, attributes.get("name"));
        assertEquals(email, attributes.get("email"));
        assertEquals(role.toString(), attributes.get("role"));
    }

    @Test
    void getAuthorities() {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) underTest.getAuthorities();

        assertEquals(role.toString(), authorities.get(0).getAuthority());
    }

    @Test
    void getName() {
        String retrievedName = underTest.getName();

        assertEquals(id.toString(), retrievedName);
    }

    @Test
    void getEmail() {
        String retrievedEmail = underTest.getEmail();

        assertEquals(email, retrievedEmail);
    }
}