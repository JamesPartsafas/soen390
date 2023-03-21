package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.ProfilePicture;
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
    private Boolean isBanned;
    private AuthProvider authProvider;
    private String securityAnswer1;
    private String securityAnswer2;
    private String securityAnswer3;

    @BeforeEach
    void setUp() {
        name = "joe";
        id = 10L;
        password = "1234";
        email = "joeunittest@mail.com";
        role = Role.CANDIDATE;
        isBanned = false;
        authProvider = AuthProvider.LOCAL;
        securityAnswer1 = "a";
        securityAnswer2 = "a";
        securityAnswer3 = "a";
        underTest = new AppUser(id, name, password, email, role, authProvider, securityAnswer1, securityAnswer2, securityAnswer3, isBanned);
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
    void getIsBanned() {
        assertEquals(isBanned, underTest.getIsBanned());
    }

    @Test
    void setIsBanned() {
        Boolean newIsBanned = true;

        underTest.setIsBanned(newIsBanned);

        assertEquals(newIsBanned, underTest.getIsBanned());
    }

    @Test
    void getAuthProvider() {
        assertThat(underTest.toString()).isNotNull();
    }

    @Test
    void setAuthProvider() {
        AuthProvider newAuthProvider = AuthProvider.GOOGLE;

        underTest.setAuthProvider(newAuthProvider);

        assertEquals(newAuthProvider, underTest.getAuthProvider());
    }

    @Test
    void getSecurityAnswer1() {
        assertEquals(securityAnswer1, underTest.getSecurityAnswer1());
    }

    @Test
    void setSecurityAnswer1() {
        String newSecurityAnswer = "b";

        underTest.setSecurityAnswer1(newSecurityAnswer);

        assertEquals(newSecurityAnswer, underTest.getSecurityAnswer1());
    }

    @Test
    void getSecurityAnswer2() {
        assertEquals(securityAnswer2, underTest.getSecurityAnswer2());
    }

    @Test
    void setSecurityAnswer2() {
        String newSecurityAnswer = "b";

        underTest.setSecurityAnswer2(newSecurityAnswer);

        assertEquals(newSecurityAnswer, underTest.getSecurityAnswer2());
    }

    @Test
    void getSecurityAnswer3() {
        assertEquals(securityAnswer3, underTest.getSecurityAnswer3());
    }

    @Test
    void setSecurityAnswer3() {
        String newSecurityAnswer = "b";

        underTest.setSecurityAnswer3(newSecurityAnswer);

        assertEquals(newSecurityAnswer, underTest.getSecurityAnswer3());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
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

    @Test
    void setProfilePicture() {
        ProfilePicture profilePicture = new ProfilePicture();

        underTest.setProfilePicture(profilePicture);

        assertEquals(profilePicture, underTest.getProfilePicture());
    }
}