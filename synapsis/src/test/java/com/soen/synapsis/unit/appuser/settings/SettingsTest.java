package com.soen.synapsis.unit.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {
    private Settings underTest;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser("name", "12345678", "name@mail.com", Role.CANDIDATE);
        underTest = new Settings(1L, appUser, true, true, true);
    }

    @Test
    void getId() {
        assertEquals(1L, underTest.getId());
    }

    @Test
    void setId() {
        underTest.setId(2L);
        assertEquals(2L, underTest.getId());
    }

    @Test
    void getAppUser() {
        assertEquals(appUser, underTest.getAppUser());
    }

    @Test
    void setAppUser() {
        AppUser newAppUser = new AppUser("name1", "12345678", "name@mail.com", Role.CANDIDATE);
        underTest.setAppUser(newAppUser);
        assertEquals(newAppUser, underTest.getAppUser());
    }

    @Test
    void isJobEmailNotificationsOn() {
        assertTrue(underTest.isJobEmailNotificationsOn());
    }

    @Test
    void setJobEmailNotificationsOn() {
        underTest.setJobEmailNotificationsOn(false);
        assertFalse(underTest.isJobEmailNotificationsOn());
    }

    @Test
    void isMessageEmailNotificationsOn() {
        assertTrue(underTest.isMessageEmailNotificationsOn());
    }

    @Test
    void setMessageEmailNotificationsOn() {
        underTest.setMessageEmailNotificationsOn(false);
        assertFalse(underTest.isMessageEmailNotificationsOn());
    }

    @Test
    void isConnectionEmailNotificationsOn() {
        assertTrue(underTest.isConnectionEmailNotificationsOn());
        ;
    }

    @Test
    void setConnectionEmailNotificationsOn() {
        underTest.setConnectionEmailNotificationsOn(false);
        assertFalse(underTest.isConnectionEmailNotificationsOn());
    }

    @Test
    void toStringNotNull() {
        assertNotNull(underTest.toString());
    }
}
