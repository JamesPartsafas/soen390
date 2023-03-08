package com.soen.synapsis.unit.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.ProfilePicture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProfilePictureTest {

    private Long id;
    private AppUser appUser;
    private String image;
    private ProfilePicture underTest;

    @BeforeEach
    void setUp() {
        id = 1L;
        appUser = new AppUser(1L, "Joe Man", "1234", "joecompany@mail.com", Role.COMPANY);
        image = "data";
        underTest = new ProfilePicture(id, appUser, image);
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
    void getImage() {
        assertEquals(image, underTest.getImage());
    }

    @Test
    void setImage() {
        String newImage = "newImage";

        underTest.setImage(newImage);

        assertEquals(newImage, underTest.getImage());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}