package com.soen.synapsis.unit.appuser.profile.companyprofiletest;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.registration.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateCompanyProfileRequestTest {
    private CompanyProfile underTest;
    private AppUser appUser;
    private Long id;
    private String website;
    private String industry;
    private String companySize;
    private String location;
    private String speciality;

    @BeforeEach
    void setUp() {
        appUser = new AppUser(3L, "joe carmen", "12345678", "carmen@gmail.com", Role.COMPANY);
        id = 3L;
        website = "www.google.com";
        industry = "buisness";
        companySize = "100";
        location = "Canada";
        speciality = "Investments";
        underTest = new CompanyProfile(appUser, id, website, industry, companySize, location, speciality);
    }

    @Test
    void getWebsite() {
        assertEquals(website, underTest.getWebsite());
    }

    @Test
    void setWebsite() {
        String newWebsite = "clara@mail.com";

        underTest.setWebsite(newWebsite);

        assertEquals(newWebsite, underTest.getWebsite());
    }

    @Test
    void getIndustry() {
        assertEquals(industry, underTest.getIndustry());
    }

    @Test
    void setIndustry() {
        String newIndustry = "Marketing";

        underTest.setIndustry(newIndustry);

        assertEquals(newIndustry, underTest.getIndustry());
    }

    @Test
    void getCompanySize() {
        assertEquals(companySize, underTest.getCompanySize());
    }

    @Test
    void setCompanySize() {
        String newCompanySize = "3000";

        underTest.setCompanySize(newCompanySize);

        assertEquals(newCompanySize, underTest.getCompanySize());
    }

    @Test
    void setLocation() {
        String newLocation = "USA";

        underTest.setLocation(newLocation);

        assertEquals(newLocation, underTest.getLocation());
    }

    @Test
    void getLocation() {
        assertEquals(location, underTest.getLocation());
    }

    @Test
    void setSpeciality() {
        String newSpeciality = "Personal Banking";

        underTest.setSpeciality(newSpeciality);

        assertEquals(newSpeciality, underTest.getSpeciality());
    }

    @Test
    void getSpeciality() {
        assertEquals(speciality, underTest.getSpeciality());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}