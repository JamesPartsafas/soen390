package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppUserDetailsTest {

    private AppUser appUser;
    private String email;
    private String password;
    private Role role;
    private AppUserDetails underTest;

    public AppUserDetailsTest() {
        email = "joeuserdetails@mail.com";
        password = "1234";
        role = Role.CANDIDATE;
        this.appUser = new AppUser(1L, "joe", password, email, role);

        underTest = new AppUserDetails(appUser);
    }

    @Test
    void getAuthoritiesReturnsAuthorities() {
        Collection<? extends GrantedAuthority> collection = underTest.getAuthorities();

        GrantedAuthority auth = collection.iterator().next();

        assertEquals(role.toString(), auth.getAuthority());
    }

    @Test
    void getIdReturnsId() {
        assertEquals(appUser.getId(), underTest.getID());
    }

    @Test
    void getPasswordReturnsPassword() {
        assertEquals(password, underTest.getPassword());
    }

    @Test
    void getUsernameReturnsId() {
        assertEquals("1", underTest.getUsername());
    }

    @Test
    void isAccountNonExpiredReturnsTrue() {
        assertTrue(underTest.isAccountNonExpired());
    }

    @Test
    void isAccountNonLockedReturnsTrue() {
        assertTrue(underTest.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpiredReturnsTrue() {
        assertTrue(underTest.isCredentialsNonExpired());
    }

    @Test
    void isEnabledReturnsTrue() {
        assertTrue(underTest.isEnabled());
    }

    @Test
    void getRoleReturnsRole() {
        assertEquals(role, underTest.getRole());
    }
}
