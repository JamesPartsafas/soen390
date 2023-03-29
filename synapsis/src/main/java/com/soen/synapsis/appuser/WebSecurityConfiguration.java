package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.oauth.CustomOAuth2UserService;
import com.soen.synapsis.utility.ExcludeFromGeneratedTestReport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configures the Spring Security framework. Used for configuration
 * related to authentication and access control.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private CustomOAuth2UserService oAuth2UserService;

    public WebSecurityConfiguration(UserDetailsService userDetailsService,
                                    CustomOAuth2UserService oAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.oAuth2UserService = oAuth2UserService;
    }

    /**
     * Used to retrieve currently used authentication provider, set up with the correct
     * password encoder.
     *
     * @return The app's authentication provider, used by Spring Security.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());

        return provider;
    }

    /**
     * Specifies access control along certain routes, as well as redirect info
     * if the user is not authenticated or does not have the correct role.
     *
     * @param httpSecurity Passed in by Spring Security to be modified by the method.
     * @throws Exception Thrown if configuration is improperly formatted.
     */
    @Override
    @ExcludeFromGeneratedTestReport
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/privateuser").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers("/").permitAll()
                .antMatchers("/admin").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/admincreationpage").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/deletejob").hasAuthority(Role.RECRUITER.toString())
                .antMatchers("/editjob").hasAuthority(Role.RECRUITER.toString())
                .antMatchers("/passwordresetpage").authenticated()
                .antMatchers("/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureHandler(authenticationFailureHandler())
                .defaultSuccessUrl("/", true)
                .usernameParameter("email")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/")
                .and().exceptionHandling().accessDeniedPage("/accessDenied");

    }

    @Bean
    public CustomAuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
