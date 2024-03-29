package com.soen.synapsis.appuser.profile.companyprofile;


import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This Company class serves as the entity to store the profile of a company user.
 */
@Entity
public class CompanyProfile {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, columnDefinition="TEXT")
    private String description;

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

    /**
     * Empty constructor.
     */
    public CompanyProfile() {
    }

    /**
     * Create a new company profile given an app user object, id, description, website, industry, size of the company, location and specialty.
     * @param appUser the object representing the company user.
     * @param id the id of the company.
     * @param description the company's description on their profile.
     * @param website the company's website on their profile.
     * @param industry the company's industry on their profile.
     * @param companySize the size of the company on their profile.
     * @param location the location of the company on their profile.
     * @param speciality the specialty of the company on their profile.
     */
    public CompanyProfile(AppUser appUser, Long id, String description, String website, String industry, String companySize, String location,
                          String speciality) {
        this.appUser = appUser;
        this.id = id;
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.speciality = speciality;
    }

    /**
     * Create a new company profile given an app user object, description, website, industry, size of the company, location and specialty.
     * @param appUser the object representing the company user.
     * @param description the company's description on their profile.
     * @param website the company's website on their profile.
     * @param industry the company's industry on their profile.
     * @param companySize the size of the company on their profile.
     * @param location the location of the company on their profile.
     * @param speciality the specialty of the company on their profile.
     */
    public CompanyProfile(AppUser appUser, String description, String website, String industry, String companySize, String location,
                          String speciality) {
        this.appUser = appUser;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
