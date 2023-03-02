package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationRequestTest {

    private RegistrationRequest underTest;
    private String name;
    private String password;
    private String email;
    private Role role;
    private String securityAnswer1;
    private String securityAnswer2;
    private String securityAnswer3;

    @BeforeEach
    void setUp() {
        name = "joe";
        password = "1234";
        email = "joeunittest@mail.com";
        role = Role.CANDIDATE;
        securityAnswer1 = "a";
        securityAnswer2 = "a";
        securityAnswer3 = "a";
        underTest = new RegistrationRequest(name, email, password, role, securityAnswer1, securityAnswer2, securityAnswer3);
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
    void getEmail() {
        assertEquals(email, underTest.getEmail());
    }

    @Test
    void setEmail() {
        String newEmail = "newEmail@mail.com";

        underTest.setEmail(newEmail);

        assertEquals(newEmail, underTest.getEmail());
    }

    @Test
    void getPassword() {
        assertEquals(password, underTest.getPassword());
    }

    @Test
    void setPassword() {
        String newPassword = "newPassword";

        underTest.setPassword(newPassword);

        assertEquals(newPassword, underTest.getPassword());
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
}