package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.registration.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class handles the logic to return the correct error message to the user.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * @param request   Failed authentication request object.
     * @param response  Response object from security framework.
     * @param exception Exception object from security framework.
     * @throws IOException      Exception thrown if there is an issue sending error response.
     * @throws ServletException Servlet exception thrown by the secutiy framework.
     */
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login?error");

        super.onAuthenticationFailure(request, response, exception);
        request.getSession()
                .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "Invalid Login Credentials.");
        if (loginAttemptService.isBlocked()) {
            request.getSession()
                    .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, "Too many attempts. Try again later.");
        }
    }
}
