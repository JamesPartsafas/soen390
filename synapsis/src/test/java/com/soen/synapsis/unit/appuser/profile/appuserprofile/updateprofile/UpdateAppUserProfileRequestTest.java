package com.soen.synapsis.unit.appuser.profile.appuserprofile.updateprofile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UpdateAppUserProfileRequestTest {

    private UpdateAppUserProfileRequest underTest;
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
        education = "McGill";
        skill = "Programming";
        work = "Synapsis Programmer";
        phone = "514-123-4567";
        volunteering = "Food Drives";
        course = "Java";
        project = "LinkedIn Clone";
        award = "Best Programmer";
        language = "English";
        underTest = new UpdateAppUserProfileRequest(education, skill, work, phone,
                volunteering, course, project, award, language);
    }

    @Test
    void getEducation() {
        assertEquals(education, underTest.getEducation());
    }

    @Test
    void setEducation() {
        String newEducation = "Concordia";

        underTest.setEducation(newEducation);

        assertEquals(newEducation, underTest.getEducation());
    }

    @Test
    void getSkill() {
        assertEquals(skill, underTest.getSkill());
    }

    @Test
    void setSkill() {
        String newSkill = "Running";

        underTest.setSkill(newSkill);

        assertEquals(newSkill, underTest.getSkill());
    }

    @Test
    void getWork() {
        assertEquals(work, underTest.getWork());
    }

    @Test
    void setWork() {
        String newWork = "Walmart";

        underTest.setWork(newWork);

        assertEquals(newWork, underTest.getWork());
    }

    @Test
    void getPhone() {
        assertEquals(phone, underTest.getPhone());
    }

    @Test
    void setPhone() {
        String newPhone = "514-999-9999";

        underTest.setPhone(newPhone);

        assertEquals(newPhone, underTest.getPhone());
    }

    @Test
    void getVolunteering() {
        assertEquals(volunteering, underTest.getVolunteering());
    }

    @Test
    void setVolunteering() {
        String newVolunteering = "Teaching";

        underTest.setVolunteering(newVolunteering);

        assertEquals(newVolunteering, underTest.getVolunteering());
    }

    @Test
    void getCourse() {
        assertEquals(course, underTest.getCourse());
    }

    @Test
    void setCourse() {
        String newCourse = "French";

        underTest.setCourse(newCourse);

        assertEquals(newCourse, underTest.getCourse());
    }

    @Test
    void getProject() {
        assertEquals(project, underTest.getProject());
    }

    @Test
    void setProject() {
        String newProject = "Moodle Clone";

        underTest.setProject(newProject);

        assertEquals(newProject, underTest.getProject());
    }

    @Test
    void getAward() {
        assertEquals(award, underTest.getAward());
    }

    @Test
    void setAward() {
        String newAward = "High school award";

        underTest.setAward(newAward);

        assertEquals(newAward, underTest.getAward());
    }

    @Test
    void getLanguage() {
        assertEquals(language, underTest.getLanguage());
    }

    @Test
    void setLanguage() {
        String newLanguage = "French";

        underTest.setLanguage(newLanguage);

        assertEquals(newLanguage, underTest.getLanguage());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}