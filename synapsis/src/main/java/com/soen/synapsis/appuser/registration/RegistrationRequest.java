package com.soen.synapsis.appuser.registration;

import com.soen.synapsis.appuser.Role;

public class RegistrationRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
    private String securityAnswer1;
    private String securityAnswer2;
    private String securityAnswer3;

    public RegistrationRequest() {
        role = Role.CANDIDATE;
    }

    public RegistrationRequest(String name, String email, String password, Role role, String securityAnswer1, String securityAnswer2, String securityAnswer3) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.securityAnswer1 = securityAnswer1;
        this.securityAnswer2 = securityAnswer2;
        this.securityAnswer3 = securityAnswer3;
    }

    public RegistrationRequest(String name, String email, String password, Role role) {
        this(name, email, password, role, null, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", securityAnswer1='" + securityAnswer1 + '\'' +
                ", securityAnswer2='" + securityAnswer2 + '\'' +
                ", securityAnswer3='" + securityAnswer3 + '\'' +
                '}';
    }
}
