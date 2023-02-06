package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.oauth.CustomOAuth2User;
import com.soen.synapsis.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.soen.synapsis.utility.Constants.MIN_PASSWORD_LENGTH;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    @Autowired
    public RegistrationService(AppUserService appUserService, EmailValidator emailValidator) {
        this.appUserService = appUserService;
        this.emailValidator = emailValidator;
    }

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.validateEmail(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("The provided email is not valid.");
        }

        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        Role requestedRole = request.getRole();

        if (requestedRole != Role.CANDIDATE && requestedRole != Role.COMPANY) {
            throw new IllegalStateException("The requested role is not valid.");
        }

        return appUserService.signUpUser(
                new AppUser(request.getName(),
                        request.getPassword(),
                        request.getEmail(),
                        requestedRole,
                        AuthProvider.LOCAL)
        );
    }

    public AppUser retrieveUserOrRegisterSSOIfNotExists(String name, String email) {
        AppUser retrievedUser = appUserService.getAppUser(email);
        if (retrievedUser != null) {
            return retrievedUser;
        }

        AppUser createdUser = new AppUser(name, Constants.SSO_PASSWORD, email, Role.CANDIDATE, AuthProvider.GOOGLE);
        appUserService.signUpUser(createdUser);

        return createdUser;
    }
}
