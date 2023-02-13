package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = true)
    private String name;
    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserProfile profile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private CompanyProfile companyProfile;

    protected AppUser() {}

    public AppUser(Long id, String name, String password, String email, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public AppUser(String name, String password, String email, Role role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return false;

        return true;
    }

    public static AppUser getAuthenticatedUser() {
        if (!isUserAuthenticated()) {
            throw new IllegalStateException("User not registered");
        }

        AppUserDetails appUserDetails = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  appUserDetails.getAppUser();
    }

    public AppUserProfile  getAppUserProfile() {
        return profile ;
    }
    public void setAppUserProfile(AppUserProfile profile) {
        this.profile = profile;
    }

    public CompanyProfile  getCompanyProfile() {
        return companyProfile ;
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
