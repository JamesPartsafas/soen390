package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column()
    private String securityAnswer1;

    @Column()
    private String securityAnswer2;

    @Column()
    private String securityAnswer3;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(columnDefinition = "boolean default true")
    private boolean emailNotificationsOn;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "company_id")
    private AppUser company;

    @OneToMany(mappedBy="company", fetch = FetchType.EAGER)
    private Set<AppUser> recruiters;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserProfile profile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private CompanyProfile companyProfile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private ProfilePicture profilePicture;

    protected AppUser() {
    }

    public AppUser(Long id, String name, String password, String email, Role role, AuthProvider authProvider, String securityAnswer1, String securityAnswer2, String securityAnswer3) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.securityAnswer1 = securityAnswer1;
        this.securityAnswer2 = securityAnswer2;
        this.securityAnswer3 = securityAnswer3;
    }

    public AppUser(Long id, String name, String password, String email, Role role, AuthProvider authProvider) {
        this(id, name, password, email, role, authProvider, null, null, null);
    }

    public AppUser(Long id, String name, String password, String email, Role role) {
        this(id, name, password, email, role, AuthProvider.LOCAL);
    }

    public AppUser(String name, String password, String email, Role role, AuthProvider authProvider, String securityAnswer1, String securityAnswer2, String securityAnswer3) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.securityAnswer1 = securityAnswer1;
        this.securityAnswer2 = securityAnswer2;
        this.securityAnswer3 = securityAnswer3;
    }

    public AppUser(String name, String password, String email, Role role, AuthProvider authProvider) {
        this(name, password, email, role, authProvider, null, null, null);
    }

    public AppUser(String name, String password, String email, Role role) {
        this(name, password, email, role, AuthProvider.LOCAL);
    }

    public AppUser(String name, String password, String email, Role role, AuthProvider authProvider, AppUser company) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.company = company;
    }

    public AppUser(Long id, String name, String password, String email, Role role, AppUser company) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.company = company;
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Set<AppUser> getRecruiter() {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be company to have recruiters.");
        }
        return recruiters;
    }

    public void addRecruiter(AppUser recruiter) {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be a company to add a recruiter.");
        }
        if (this.recruiters == null) {
            this.recruiters = new HashSet<AppUser>();
        }
        recruiters.add(recruiter);
    }

    public void removeRecruiter(AppUser recruiter) {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be a company to remove a recruiter.");
        }
        if (this.recruiters == null) {
            throw new IllegalStateException("Your company does not have recruiters to be removed.");
        }
        recruiters.remove(recruiter);
    }

    public AppUser getCompany() {
        if (this.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("You must be a recruiter to belong to a company.");
        }
        return company;
    }

    public void setCompany(AppUser company) {
        if (this.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("You must be a recruiter to be part of a company.");
        }
        this.company = company;
    }

    public AppUserProfile getAppUserProfile() {
        return profile;
    }

    public void setAppUserProfile(AppUserProfile profile) {
        this.profile = profile;
    }

    public CompanyProfile getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getSecurityAnswer1() {
        return securityAnswer1;
    }

    public void setSecurityAnswer1(String securityAnswer1) {
        this.securityAnswer1 = securityAnswer1;
    }

    public String getSecurityAnswer2() {
        return securityAnswer2;
    }

    public void setSecurityAnswer2(String securityAnswer2) {
        this.securityAnswer2 = securityAnswer2;
    }

    public String getSecurityAnswer3() {
        return securityAnswer3;
    }

    public void setSecurityAnswer3(String securityAnswer3) {
        this.securityAnswer3 = securityAnswer3;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public boolean isEmailNotificationsOn() {
        return emailNotificationsOn;
    }

    public void setEmailNotificationsOn(boolean emailNotificationsOn) {
        this.emailNotificationsOn = emailNotificationsOn;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", securityAnswer1='" + securityAnswer1 + '\'' +
                ", securityAnswer2='" + securityAnswer2 + '\'' +
                ", securityAnswer3='" + securityAnswer3 + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", authProvider=" + authProvider +
                ", company=" + company +
                ", recruiters=" + recruiters +
                ", profile=" + profile +
                ", companyProfile=" + companyProfile +
                ", emailNotificationsOn=" + emailNotificationsOn +
                '}';
    }
}
