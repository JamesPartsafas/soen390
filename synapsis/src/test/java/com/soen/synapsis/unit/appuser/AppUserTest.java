package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.utilities.SecurityUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AppUserTest {

    private AppUser underTest;

    private AppUser underTest2;
    private String name;
    private Long id;
    private String password;
    private String email;
    private Role role;
    private AuthProvider authProvider;

    @BeforeEach
    void setUp() {
        name = "joe";
        id = 10L;
        password = "1234";
        email = "joeunittest@mail.com";
        role = Role.CANDIDATE;
        authProvider = AuthProvider.LOCAL;
        underTest = new AppUser(id, name, password, email, role, authProvider);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getId() {
        assertEquals(id, underTest.getId());
    }

    @Test
    void setId() {
        Long newId = 15L;

        underTest.setId(newId);

        assertEquals(newId, underTest.getId());
    }

    @Test
    void getName() {
        assertEquals(name, underTest.getName());
    }

    @Test
    void setName() {
        String newName = "newJoe";

        underTest.setName(newName);

        assertEquals(newName, underTest.getName());
    }

    @Test
    void getPassword() {
        assertEquals(password, underTest.getPassword());
    }

    @Test
    void setPassword() {
        String newPassword = "newpass";

        underTest.setPassword(newPassword);

        assertEquals(newPassword, underTest.getPassword());
    }

    @Test
    void getEmail() {
        assertEquals(email, underTest.getEmail());
    }

    @Test
    void setEmail() {
        String newEmail = "newjoe@mail.com";

        underTest.setEmail(newEmail);

        assertEquals(newEmail, underTest.getEmail());
    }

    @Test
    void getRole() {
        assertEquals(role, underTest.getRole());
    }

    @Test
    void setRole() {
        Role newRole = Role.ADMIN;

        underTest.setRole(newRole);

        assertEquals(newRole, underTest.getRole());
    }


    @Test
    void getAuthProvider() {
        assertEquals(authProvider, underTest.getAuthProvider());
    }

    @Test
    void setAuthProvider() {
        AuthProvider newAuthProvider = AuthProvider.GOOGLE;

        underTest.setAuthProvider(newAuthProvider);

        assertEquals(newAuthProvider, underTest.getAuthProvider());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }

    @Test
    void testIsUserAuthenticatedReturnsTrue() {
        SecurityUtilities.authenticateAnonymousUser();
        assertThat(AppUser.isUserAuthenticated()).isTrue();
    }

    @Test
    void testIsUserAuthenticatedReturnsFalse() {
        assertThat(AppUser.isUserAuthenticated()).isFalse();
    }

    @Test
    void recruiterGetCompanySucceeds() {
        underTest2 = new AppUser(id, name, password, email, Role.RECRUITER, authProvider);
        assertEquals(underTest2.getCompany(), underTest2.getCompany());
    }
    @Test
    void recruiterGetCompanyFails() {
        assertThrows(IllegalStateException.class,
                () -> underTest.getCompany(),
                "You must be a recruiter to belong to a company.");
    }
    @Test
    void recruiterSetCompanySucceeds() {
        underTest2 = new AppUser(id, name, password, email, Role.RECRUITER, authProvider);
        AppUser companyUser = new AppUser(id, name, password, email, Role.COMPANY, authProvider);

        underTest2.setCompany(companyUser);

        assertEquals(companyUser, underTest2.getCompany());

    }
    @Test
    void recruiterSetCompanyFails() {
        AppUser companyUser = new AppUser(id, name, password, email, Role.COMPANY, authProvider);
        assertThrows(IllegalStateException.class,
                () -> underTest.setCompany(companyUser),
                "You must be a recruiter to be part of a company.");
    }
}