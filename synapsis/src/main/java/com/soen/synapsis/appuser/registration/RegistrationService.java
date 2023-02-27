package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
                        AuthProvider.LOCAL,
                        request.getSecurityAnswer1(),
                        request.getSecurityAnswer2(),
                        request.getSecurityAnswer3())
        );
    }

    public AppUser retrieveSSOUserOrRegisterIfNotExists(String name, String email) {
        AppUser retrievedUser = appUserService.getAppUser(email);
        if (retrievedUser != null) {
            return retrievedUser;
        }

        //User does not exist. Create the account.
        AppUser createdUser = new AppUser(name, Constants.SSO_PASSWORD, email, Role.CANDIDATE, AuthProvider.GOOGLE);
        appUserService.signUpUser(createdUser);

        return createdUser;
    }

    public String registerAdmin(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.validateEmail(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("The provided email is not valid.");
        }

        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        return appUserService.signUpAdmin(
                new AppUser(request.getName(),
                        request.getPassword(),
                        request.getEmail(),
                        Role.ADMIN,
                        AuthProvider.LOCAL,
                        request.getSecurityAnswer1(),
                        request.getSecurityAnswer2(),
                        request.getSecurityAnswer3())
        );
    }

    public String updateUserPassword(AppUser appUser, String oldPassword, String newPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, appUser.getPassword())) {
            throw new IllegalStateException("Old password is incorrect");
        }
        if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        return appUserService.updatePassword(appUser, newPassword);
    }

    public String resetUserPassword(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.validateEmail(request.getEmail());
        AppUser appUser = appUserService.getAppUser(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("The provided email is not valid.");
        }
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        if (!appUserService.checkSecurityQuestions(request.getEmail(), request.getSecurityAnswer1(), request.getSecurityAnswer2(), request.getSecurityAnswer3())) {
            throw new IllegalStateException("1 or more of the security questions was incorrect.");
        }

        return appUserService.updatePassword(appUser, request.getPassword());
    }
}
