package com.soen.synapsis.unit.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.CoverLetter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverLetterTest {
    private Long id;
    private AppUser appUser;
    private String defaultCoverLetter;
    private String fileName;
    private CoverLetter underTest;

    @BeforeEach
    void setUp() {
        id = 1L;
        appUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY.COMPANY);
        defaultCoverLetter = "data";
        fileName = "test.pdf";
        underTest = new CoverLetter(id, appUser, defaultCoverLetter, fileName);
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
        assertEquals(defaultCoverLetter, underTest.getDefaultCoverLetter());
    }

    @Test
    void setDefaultResume() {
        String newDefaultCoverLetter = "newCoverLetter";

        underTest.setDefaultCoverLetter(newDefaultCoverLetter);

        assertEquals(newDefaultCoverLetter, underTest.getDefaultCoverLetter());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
