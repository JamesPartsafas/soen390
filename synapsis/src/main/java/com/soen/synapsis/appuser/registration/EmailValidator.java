package com.soen.synapsis.appuser.registration;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Used to verify validity of email syntax
 */
@Service
public class EmailValidator {

    private final String regex;

    /**
     * Default constructor Sets regex to match to valid emails, such as joe@mail.com
     */
    public EmailValidator() {
        this.regex = "^(.+)@(.+)$";
    }

    /**
     * Checks if email is properly formatted. Returns true if formatting is correct.
     * @param email The email address to verify
     * @return True if email address format is valid, false otherwise
     */
    public boolean validateEmail(String email) {
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
