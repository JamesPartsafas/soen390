package com.soen.synapsis.unit.appuser.settings;

import com.soen.synapsis.appuser.settings.UpdateSettingsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateSettingsRequestTest {
    private UpdateSettingsRequest underTest;

    @BeforeEach
    void setUp() {
        underTest = new UpdateSettingsRequest(true, true, true);
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
