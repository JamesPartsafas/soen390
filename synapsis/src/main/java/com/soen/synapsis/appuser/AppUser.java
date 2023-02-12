package com.soen.synapsis.appuser;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="company_id")
    private AppUser company;

    @OneToMany(mappedBy="company")
    private Set<AppUser> recruiters;

    protected AppUser() {}

    public AppUser(Long id, String name, String password, String email, Role role, AuthProvider authProvider) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
    }

    public AppUser(Long id, String name, String password, String email, Role role) {
        this(id, name, password, email, role, AuthProvider.LOCAL);
    }

    public AppUser(String name, String password, String email, Role role, AuthProvider authProvider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
    }

    public AppUser(String name, String password, String email, Role role) {
        this(name, password, email, role, AuthProvider.LOCAL);
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Set<AppUser> getRecruiter() {
        if(this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be company to have recruiters.");
        }
        return recruiters;
    }

    public void setRecruiter(AppUser recruiter) {
        if(this.getRole() != Role.COMPANY) {
            throw new IllegalStateException("You must be a company to set recruiters.");
        }
        if(this.recruiters == null) {
            this.recruiters = new HashSet<AppUser>();
        }
        if(recruiter.getRole() == Role.RECRUITER) {
            recruiters.add(recruiter);
        }
        if(recruiter.getRole() != Role.RECRUITER) {
            recruiters.remove(recruiter);
        }
    }

    public AppUser getCompany() {
        return company;
    }

    public void setCompany(AppUser company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", authProvider=" + authProvider +
                '}';
    }
}
