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

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private RegistrationService registrationService;

    @Autowired
    public CustomOAuth2UserService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    @ExcludeFromGeneratedTestReport
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        AppUser appUser = registrationService.retrieveUserOrRegisterSSOIfNotExists(
                oAuth2User.getAttribute("name"), oAuth2User.getAttribute("email"));

        return new CustomOAuth2User(appUser);
    }
}
