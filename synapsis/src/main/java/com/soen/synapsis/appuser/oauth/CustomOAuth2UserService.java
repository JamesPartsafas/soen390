package com.soen.synapsis.appuser.oauth;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.registration.RegistrationService;
import com.soen.synapsis.utility.ExcludeFromGeneratedTestReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Service layer to retrieve OAuth2 users, used by authentication framework
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private RegistrationService registrationService;

    @Autowired
    public CustomOAuth2UserService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * Loads the user based on their request, then store the user in DB if they do no exist yet
     * @param userRequest Contains request data. Provided by external SSO services
     * @return OAuth2User acting as a wrapper for AppUser instance
     * @throws OAuth2AuthenticationException
     */
    @Override
    @ExcludeFromGeneratedTestReport
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        AppUser appUser = registrationService.retrieveSSOUserOrRegisterIfNotExists(
                oAuth2User.getAttribute("name"), oAuth2User.getAttribute("email"));

        return new CustomOAuth2User(appUser);
    }
}
