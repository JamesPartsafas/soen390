package com.soen.synapsis.appuser.registration;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidator {

    private final String regex;

    public EmailValidator() {
        this.regex = "^(.+)@(.+)$";
    }

    public boolean validateEmail(String email) {
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
