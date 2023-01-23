package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new IllegalStateException("Provided email is not valid");
        }

        return appUserService.signUpUser(
                new AppUser(request.getName(),
                        request.getPassword(),
                        request.getEmail(),
                        Role.CANDIDATE)
        );
    }
}
