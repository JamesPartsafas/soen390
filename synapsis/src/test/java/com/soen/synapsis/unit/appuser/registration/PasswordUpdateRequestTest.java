package com.soen.synapsis.unit.appuser.registration;

import com.soen.synapsis.appuser.registration.PasswordUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordUpdateRequestTest {
    private String oldPassword;
    private String newPassword;
    private PasswordUpdateRequest underTest;

    @BeforeEach
    void setUp() {
        oldPassword = "1234678";
        newPassword = "12345679";
        underTest = new PasswordUpdateRequest(oldPassword, newPassword);
    }

    @Test
    void getOldPassword() {
        assertEquals(oldPassword, underTest.getOldPassword());
    }

    @Test
    void setOldPassword() {
        String changedPassword = "123456789";

        underTest.setOldPassword(changedPassword);

        assertEquals(changedPassword, underTest.getOldPassword());
    }

    @Test
    void getNewPassword() {
        assertEquals(newPassword, underTest.getNewPassword());
    }

    @Test
    void setNewPassword() {
        String changedPassword = "123456789";

        underTest.setNewPassword(changedPassword);

        assertEquals(changedPassword, underTest.getNewPassword());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
