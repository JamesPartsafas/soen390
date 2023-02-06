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

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());

        return provider;
    }

    @Override
    @ExcludeFromGeneratedTestReport
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/privateuser").hasAuthority(Role.CANDIDATE.toString())
                .antMatchers("/admin").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/admincreationpage").hasAuthority(Role.ADMIN.toString())
                .antMatchers("/**").permitAll()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .usernameParameter("email")
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint().userService(oAuth2UserService)
                    .and()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
