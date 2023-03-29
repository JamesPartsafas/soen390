package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.registration.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for accessing AppUserDeatils, used by authentication
 * framework for users logging in through internal Synapsis system,
 * and not an external provider through SSO.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public AppUserDetailsService(AppUserRepository appUserRepository, LoginAttemptService loginAttemptService) {
        this.appUserRepository = appUserRepository;
        this.loginAttemptService = loginAttemptService;
    }

    /**
     * Retrieves user details by their username, which is defined as their email address.
     *
     * @param email The requested user's email address.
     * @return UserDetails wrapping the requested AppUser.
     * @throws UsernameNotFoundException Thrown if user has not yet been registered.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new RuntimeException("IP blocked.");
        }
        AppUser appUser = appUserRepository.findByEmail(email);

        if (appUser == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found.");
        }

        return new AppUserDetails(appUser);
    }
}
