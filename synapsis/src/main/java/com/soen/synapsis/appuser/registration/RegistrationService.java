package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.utility.Constants;
import com.soen.synapsis.utility.crypto.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.soen.synapsis.utility.Constants.MIN_PASSWORD_LENGTH;

/**
 * Service layer for processing user registration requests and interacting with repository layer
 */
@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    @Autowired
    public RegistrationService(AppUserService appUserService, EmailValidator emailValidator) {
        this.appUserService = appUserService;
        this.emailValidator = emailValidator;
    }

    /**
     * Verifies registration data to ensure email, password, and role validity, then adds user to database
     * @param request Contains registration data
     * @return View to home page
     */
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

    /**
     * Obtains information on SSO user if they are found. If not, a new entry is added to the database with
     * the user's information.
     * @param name User's name
     * @param email user's email address
     * @return Retrieved or created user
     */
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

    /**
     * Processes request to register a new admin.
     * @param request Contains registration data
     * @return View containing confirmation page
     */
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

    /**
     * Updates the user password from requested user
     * @param appUser The user whose password should be updated
     * @param oldPassword The user's previous password
     * @param newPassword The user's new password
     * @return View containing login page
     */
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

    /**
     * Processes request to update a user password
     * @param request Contains request data
     * @return View containing login page
     */
    public String resetUserPassword(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.validateEmail(request.getEmail());
        AppUser appUser = appUserService.getAppUser(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("The provided email is not valid.");
        }
        if (request.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("The chosen password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        if (!appUserService.checkSecurityQuestions(appUser, request.getSecurityAnswer1(), request.getSecurityAnswer2(), request.getSecurityAnswer3())) {
            throw new IllegalStateException("1 or more of the security questions was incorrect.");
        }

        return appUserService.updatePassword(appUser, request.getPassword());
    }
}
