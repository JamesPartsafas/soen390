package com.soen.synapsis.utilities;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilities {
    public static void authenticateAnonymousUser() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("TEST_USER", "credentials", "authority"));
    }
}
