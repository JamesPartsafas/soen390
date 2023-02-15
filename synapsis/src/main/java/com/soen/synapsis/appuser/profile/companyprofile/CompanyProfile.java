package com.soen.synapsis.appuser.profile.companyprofile;


import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

@Entity
public class CompanyProfile {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String website;

    @Column(nullable = true)
    private String industry;

    @Column(nullable = true)
    private String companySize;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String speciality;

    public CompanyProfile() {
    }

    public CompanyProfile(AppUser appUser, Long id, String website, String industry, String companySize, String location,
                          String speciality) {
        this.appUser = appUser;

        this.id = id;
        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.speciality = speciality;
    }

    public CompanyProfile(AppUser appUser, String website, String industry, String companySize, String location,
                          String speciality) {
        this.appUser = appUser;

        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.speciality = speciality;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "CompanyProfile{" +
                "id=" + id +
                ", website ='" + website + '\'' +
                ", industry ='" + industry + '\'' +
                ", companySize ='" + companySize + '\'' +
                ", location ='" + location + '\'' +
                ", industry ='" + industry +
                '}';
    }
}
