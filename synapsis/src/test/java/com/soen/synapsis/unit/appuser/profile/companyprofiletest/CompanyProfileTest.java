package com.soen.synapsis.unit.appuser.profile.companyprofiletest;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyProfileTest {
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
        appUser = new AppUser(1L, "joe doe", "12345678", "joe@gmail.com", Role.COMPANY);
        id = 4L;
        website = "www.google.com";
        industry = "technology";
        companySize = "1000";
        location = "Canada";
        speciality = "Speech Recognition";
        underTest = new CompanyProfile(appUser, id, website, industry, companySize, location, speciality);
    }

    @Test
    void getId() {
        assertEquals(id, underTest.getId());
    }

    @Test
    void setId() {
        Long newId = 15L;

        underTest.setId(newId);

        assertEquals(newId, underTest.getId());
    }

    @Test
    void getAppUser() {
        assertEquals(appUser, underTest.getAppUser());
    }

    @Test
    void setAppUser() {
        AppUser newAppUser = new AppUser(2L, "joe morgan", "12345678", "morgan@gmail.com", Role.COMPANY);

        underTest.setAppUser(newAppUser);

        assertEquals(newAppUser, underTest.getAppUser());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
