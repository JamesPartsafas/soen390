package com.soen.synapsis.unit.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.ProfilePicture;
import com.soen.synapsis.appuser.profile.Resume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResumeTest {
    private Long id;
    private AppUser appUser;
    private String defaultResume;
    private String fileName;
    private Resume underTest;

    @BeforeEach
    void setUp() {
        id = 1L;
        appUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        defaultResume = "data";
        fileName = "test.pdf";
        underTest = new Resume(id, appUser, defaultResume, fileName);
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
        AppUser newAppUser = new AppUser(2L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);

        underTest.setAppUser(newAppUser);

        assertEquals(newAppUser, underTest.getAppUser());
    }

    @Test
    void getDefaultResume() {
        assertEquals(defaultResume, underTest.getDefaultResume());
    }

    @Test
    void setDefaultResume() {
        String newResume = "newResume";

        underTest.setDefaultResume(newResume);

        assertEquals(newResume, underTest.getDefaultResume());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
