package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.ConnectionController;
import com.soen.synapsis.appuser.connection.ConnectionService;
import com.soen.synapsis.utilities.SecurityUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectionControllerTest {
    @Mock
    private ConnectionService connectionService;
    @Mock
    private AuthService authService;
    private ConnectionController underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ConnectionController(connectionService, authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        autoCloseable.close();
    }

    @Test
    void viewConnectionPage() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        SecurityUtilities.authenticateAnonymousUser();
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(loggedInAppUser);

        String returnedPage = underTest.viewNetwork(mock(Model.class));

        assertEquals("pages/network", returnedPage);
    }

    @Test
    void rejectConnectionRequestPage() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        SecurityUtilities.authenticateAnonymousUser();
        when(authService.getAuthenticatedUser()).thenReturn(loggedInAppUser);

        String returnedPage = underTest.rejectConnection(2L, mock(Model.class));

        assertEquals(null, returnedPage);
    }

    @Test
    void acceptConnectionRequest() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        SecurityUtilities.authenticateAnonymousUser();
        when(authService.getAuthenticatedUser()).thenReturn(loggedInAppUser);

        String returnedPage = underTest.acceptConnection(3L, mock(Model.class));

        assertEquals(null, returnedPage);
    }

    @Test
    void connectWithUser() {
        AppUser appUser = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);

        String returnedPage = underTest.connectWithUser(appUser.getId(), mock(Model.class));

        assertEquals("redirect:/user/" + appUser.getId(), returnedPage);
    }

    @Test
    void connectWithUserThrowsExceptionWhenRoleIsAdmin() {
        AppUser appUser = new AppUser("Joe Admin", "1234", "joeman@email.com", Role.ADMIN);

        String returnedPage = underTest.connectWithUser(appUser.getId(), mock(Model.class));

        assertEquals("redirect:/user/" + appUser.getId(), returnedPage);
    }

    @Test
    void disconnectFromUser() {
        AppUser appUser = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        String returnedPage = underTest.disconnectFromUser(2L);

        assertEquals("redirect:/user/2", returnedPage);
    }
}
