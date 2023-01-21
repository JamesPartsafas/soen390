package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.registration.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    private RegistrationRequest underTest;
    private String name;
    private String password;
    private String email;

    @BeforeEach
    void setUp() {
        name = "joe";
        password = "1234";
        email = "joeunittest@mail.com";
        underTest = new RegistrationRequest(name, email, password);
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
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}