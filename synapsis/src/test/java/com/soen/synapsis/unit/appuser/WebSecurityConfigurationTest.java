package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUserDetailsService;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.WebSecurityConfiguration;
import com.soen.synapsis.appuser.oauth.CustomOAuth2UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WebSecurityConfigurationTest {

    private WebSecurityConfiguration underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private AppUserDetailsService appUserDetailsService;
    @Mock
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new WebSecurityConfiguration(appUserDetailsService, customOAuth2UserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void authenticationProviderReturnsValidProvider() {
        AuthenticationProvider provider = underTest.authenticationProvider();

        assertThat(provider).isNotNull();
    }
}