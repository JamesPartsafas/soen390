package com.soen.synapsis.appuser;

import javax.persistence.*;

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

    @Column
    private Long companyId;

    protected AppUser() {}

    public AppUser(Long id, String name, String password, String email, Role role, AuthProvider authProvider) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authProvider = authProvider;
        this.companyId = null;
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
        this.companyId = null;
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

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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
