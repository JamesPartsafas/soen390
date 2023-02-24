package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.utilities.SecurityUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService underTest;
    @BeforeEach
    void setUp() {
        underTest = new AuthService();
    }

    @Test
    void testIsUserAuthenticatedReturnsTrue() {
        SecurityUtilities.authenticateAnonymousUser();
        assertThat(underTest.isUserAuthenticated()).isTrue();
    }

    @Test
    void testIsUserAuthenticatedReturnsFalse() {
        SecurityUtilities.doNotAuthenticateAnonymousUser();
        assertThat(underTest.isUserAuthenticated()).isFalse();
    }

    @Test
    void getAuthenticatedUserThrowsOnUnauthedUser() {
        SecurityUtilities.doNotAuthenticateAnonymousUser();

        assertThrows(IllegalStateException.class,
                () -> underTest.getAuthenticatedUser(), "User not registered");
    }

    @Test
    void doesUserHaveRoleReturnsFalseOnUnauthedUser() {
        SecurityUtilities.doNotAuthenticateAnonymousUser();

        assertThat(underTest.doesUserHaveRole(Role.CANDIDATE)).isFalse();
    }

    @Test
    void doesUserHaveMultipleRoleReturnsFalseOnUnauthedUser() {
        SecurityUtilities.doNotAuthenticateAnonymousUser();

        assertThat(underTest.doesUserHaveRole(Role.CANDIDATE, Role.RECRUITER)).isFalse();
    }
}