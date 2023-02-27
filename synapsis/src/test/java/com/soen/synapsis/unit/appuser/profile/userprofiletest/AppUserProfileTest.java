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
    private String description;
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
        description = "description";
        education = "engineering";
        skill = "self learner";
        work = "developer";
        phone = "5144442323";
        volunteering = "Tutoring";
        course = "machine learning";
        project = "amaznot website";
        award = "best student award";
        language = "French";
        underTest = new AppUserProfile(appUser, id, description, education, skill, work,course, phone, volunteering,  project, award, language);
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
    void getDescription() {
        assertEquals(description, underTest.getDescription());
    }

    @Test
    void setDescription() {
        String newDescription = "new description";

        underTest.setDescription(newDescription);

        assertEquals(newDescription, underTest.getDescription());
    }

    @Test
    void getEducation() {
        assertEquals(education, underTest.getEducation());
    }

    @Test
    void setEducation() {
        String newEducation = "Chemistry";

        underTest.setEducation(newEducation);

        assertEquals(newEducation, underTest.getEducation());
    }

    @Test
    void getSkill() {
        assertEquals(skill, underTest.getSkill());
    }

    @Test
    void setSkill() {
        String newSkill = "Work under pressure";

        underTest.setSkill(newSkill);

        assertEquals(newSkill, underTest.getSkill());
    }


    @Test
    void getWork() {
        assertEquals(work, underTest.getWork());
    }

    @Test
    void setWork() {
        String newWork = "Researcher";

        underTest.setWork(newWork);

        assertEquals(newWork, underTest.getWork());
    }

    @Test
    void getPhone() {
        assertEquals(phone, underTest.getPhone());
    }

    @Test
    void setPhone() {
        String newPhone = "5144444444";

        underTest.setPhone(newPhone);

        assertEquals(newPhone, underTest.getPhone());
    }

    @Test
    void getVolunteering() {
        assertEquals(volunteering, underTest.getVolunteering());
    }

    @Test
    void setVolunteering() {
        String newVolunteering = "translator";

        underTest.setVolunteering(newVolunteering);

        assertEquals(newVolunteering, underTest.getVolunteering());
    }

    @Test
    void getCourse() {
        assertEquals(course, underTest.getCourse());
    }

    @Test
    void setCourse() {
        String newCourse = "Chemical History";

        underTest.setCourse(newCourse);

        assertEquals(newCourse, underTest.getCourse());
    }

    @Test
    void getProject() {
        assertEquals(project, underTest.getProject());
    }

    @Test
    void setProject() {
        String newProject = "Genetics";

        underTest.setProject(newProject);

        assertEquals(newProject, underTest.getProject());
    }


    @Test
    void getAward() {
        assertEquals(award, underTest.getAward());
    }

    @Test
    void setAward() {
        String newAward = "Best chemist";

        underTest.setAward(newAward);

        assertEquals(newAward, underTest.getAward());
    }

    @Test
    void getLanguage() {
        assertEquals(language, underTest.getLanguage());
    }

    @Test
    void setLanguage() {
        String newLanguage = "Arabic";

        underTest.setLanguage(newLanguage);

        assertEquals(newLanguage, underTest.getLanguage());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
