package com.soen.synapsis.unit.appuser.profile.userprofiletest;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.persistence.Column;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppUserProfileTest {
    private AppUserProfile underTest;
    private AppUser appUser;
    private Long id;
    private String education;
    private String skill;
    private String work;
    private String phone;
    private String volunteering;
    private String course;
    private String project;
    private String award;
    private String language;

    @BeforeEach
    void setUp() {
        appUser = new AppUser(2L, "joe george", "12345678", "george@gmail.com", Role.CANDIDATE);
        id = 3L;
        education = "engineering";
        skill = "self learner";
        work = "developer";
        phone = "5144442323";
        volunteering = "Tutoring";
        course = "machine learning";
        project = "amaznot website";
        award = "best student award";
        language = "French";
        underTest = new AppUserProfile(appUser, id, education, skill, work, phone, volunteering, course, project, award, language);
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
        AppUser newAppUser = new AppUser(2L, "joe morgan", "12345678", "morgan@gmail.com", Role.CANDIDATE);

        underTest.setAppUser(newAppUser);

        assertEquals(newAppUser, underTest.getAppUser());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
