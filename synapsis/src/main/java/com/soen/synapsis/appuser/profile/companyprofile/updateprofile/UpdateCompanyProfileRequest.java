package com.soen.synapsis.appuser.profile.companyprofile.updateprofile;

public class UpdateCompanyProfileRequest {
    private String description;
    private String website;
    private String industry;
    private String companySize;
    private String location;
    private String speciality;

    public UpdateCompanyProfileRequest() {
    }

    public UpdateCompanyProfileRequest(String description, String website, String industry, String companySize, String location, String speciality) {
        this.description = description;
        this.website = website;
        this.industry = industry;
        this.companySize = companySize;
        this.location = location;
        this.speciality = speciality;
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
        return "UpdateCompanyProfileRequest{" +
                "website='" + website + '\'' +
                ", industry='" + industry + '\'' +
                ", companySize='" + companySize + '\'' +
                ", location='" + location + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
