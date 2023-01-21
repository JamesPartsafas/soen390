package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.registration.EmailValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private EmailValidator underTest;

    public EmailValidatorTest() {
        underTest = new EmailValidator();
    }

    @Test
    void allowValidEmail() {
        String email = "joe@mail.com";

        boolean allowed = underTest.validateEmail(email);

        assertTrue(allowed);
    }

    @Test
    void disallowInvalidEmail() {
        String email = "joemail.com";

        boolean allowed = underTest.validateEmail(email);

        assertFalse(allowed);
    }
}