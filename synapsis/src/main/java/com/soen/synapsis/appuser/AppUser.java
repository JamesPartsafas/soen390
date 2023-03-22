package com.soen.synapsis.appuser;

import com.soen.synapsis.appuser.profile.Resume;
import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.settings.Settings;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains data on all users of any role.
 * All registered users are instances of AppUser, which maps
 * to a database table.
 */
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
    private boolean verificationStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column
    private Boolean isBanned;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private AppUser company;

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    private Set<AppUser> recruiters;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private AppUserProfile profile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private CompanyProfile companyProfile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private ProfilePicture profilePicture;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Resume defaultResume;
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Settings settings;

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

    public AppUser(Long id, String name, String password, String email, Role role, AuthProvider authProvider, String securityAnswer1, String securityAnswer2, String securityAnswer3, Boolean isBanned) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.securityAnswer1 = securityAnswer1;
        this.securityAnswer2 = securityAnswer2;
        this.securityAnswer3 = securityAnswer3;
        this.isBanned = isBanned;
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

    public AppUser(Long id, String name, String password, String email, Role role, boolean verificationStatus) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.verificationStatus = verificationStatus;
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

    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public boolean getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    /**
     * Retrieves the recruiters associated to this AppUser. Only AppUsers
     * with the role COMPANY may have recruiters.
     *
     * @return Set of AppUsers with the role RECRUITER.
     */
    public Set<AppUser> getRecruiter() {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be company to have recruiters.");
        }
        return recruiters;
    }

    /**
     * Add recruiter to AppUser of role COMPANY.
     *
     * @param recruiter AppUser to be added to recruiters list.
     */
    public void addRecruiter(AppUser recruiter) {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be a company to add a recruiter.");
        }
        if (this.recruiters == null) {
            this.recruiters = new HashSet<AppUser>();
        }
        recruiters.add(recruiter);
    }

    /**
     * Remove recruiter from AppUser's recruiter list.
     * Only AppUsers of role COMPANY may have recruiters.
     *
     * @param recruiter The recruiter to remove from the company's
     *                  recruiter list.
     */
    public void removeRecruiter(AppUser recruiter) {
        if (this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be a company to remove a recruiter.");
        }
        if (this.recruiters == null) {
            throw new IllegalStateException("Your company does not have recruiters to be removed.");
        }
        recruiters.remove(recruiter);
    }

    /**
     * Gets the company a user of role RECRUITER works for.
     *
     * @return An AppUser of role COMPANY.
     */
    public AppUser getCompany() {
        if (this.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("You must be a recruiter to belong to a company.");
        }
        return company;
    }

    /**
     * Sets the company a user of role RECRUITER works for.
     *
     * @param company The AppUser of role COMPANY to work for.
     */
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

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Resume getResume() {
        return defaultResume;
    }

    public void setResume(Resume defaultResume) {
        this.defaultResume = defaultResume;
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
                '}';
    }
}
