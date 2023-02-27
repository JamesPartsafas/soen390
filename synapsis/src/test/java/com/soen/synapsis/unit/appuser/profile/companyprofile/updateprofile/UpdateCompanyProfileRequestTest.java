package com.soen.synapsis.unit.appuser.profile.companyprofile.updateprofile;

import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCompanyProfileRequestTest {

    private String description;
    private String website;
    private String industry;
    private String companySize;
    private String location;
    private String speciality;
    private UpdateCompanyProfileRequest underTest;
    @BeforeEach
    void setUp() {
        description = "description";
        website = "google.com";
        industry = "tech";
        companySize = "100000";
        location = "California";
        speciality = "Search engines";
        underTest = new UpdateCompanyProfileRequest(description, website, industry, companySize, location, speciality);
    }

    @Test
    void getWebsite() {
        assertEquals(website, underTest.getWebsite());
    }

    @Test
    void setWebsite() {
        String newWebsite = "google.ca";

        underTest.setWebsite(newWebsite);

        assertEquals(newWebsite, underTest.getWebsite());
    }

    @Test
    void getIndustry() {
        assertEquals(industry, underTest.getIndustry());
    }

    @Test
    void setIndustry() {
        String newIndustry = "Ads";

        underTest.setIndustry(newIndustry);

        assertEquals(newIndustry, underTest.getIndustry());
    }

    @Test
    void getCompanySize() {
        assertEquals(companySize, underTest.getCompanySize());
    }

    @Test
    void setCompanySize() {
        String newCompanySize = "100";

        underTest.setCompanySize(newCompanySize);

        assertEquals(newCompanySize, underTest.getCompanySize());
    }

    @Test
    void getLocation() {
        assertEquals(location, underTest.getLocation());
    }

    @Test
    void setLocation() {
        String newLocation = "New York";

        underTest.setLocation(newLocation);

        assertEquals(newLocation, underTest.getLocation());
    }

    @Test
    void getSpeciality() {
        assertEquals(speciality, underTest.getSpeciality());
    }

    @Test
    void setSpeciality() {
        String newSpeciality = "Tracking people";

        underTest.setSpeciality(newSpeciality);

        assertEquals(newSpeciality, underTest.getSpeciality());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}