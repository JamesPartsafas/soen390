package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
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

public class ConnectionControllerTest {
    @Mock
    private ConnectionService connectionService;
    private ConnectionController underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ConnectionController(connectionService);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        autoCloseable.close();
    }

    @Test
    void viewConnectionPage() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);
        SecurityUtilities.authenticateAnonymousUser();

        String returnedPage = underTest.viewNetwork(appUserDetails, mock(Model.class));

        assertEquals("pages/network", returnedPage);
    }

    @Test
    void rejectConnectionRequestPage() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);
        SecurityUtilities.authenticateAnonymousUser();

        String returnedPage = underTest.rejectConnection(appUserDetails, 2L, mock(Model.class));

        assertEquals(null, returnedPage);
    }

    @Test
    void acceptConnectionRequest() {
        AppUser loggedInAppUser = new AppUser(1L, "Joe Man", "1234", "joerecruiter@mail.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(loggedInAppUser);
        SecurityUtilities.authenticateAnonymousUser();

        String returnedPage = underTest.acceptConnection(appUserDetails, 3L, mock(Model.class));

        assertEquals(null, returnedPage);
    }

    @Test
    void makeConnectionTest() {
        AppUser appUser1 = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        AppUser appUser2 = new AppUser("Joe Woman", "1234", "joewoman@email.com", Role.CANDIDATE);
        Model model = mock(Model.class);

        String returnedPage = underTest.makeConnection(appUser1, appUser2, model);

        assertEquals(null, returnedPage);
    }

    @Test
    void disconnectFromUser() {
        AppUser appUser = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        AppUserDetails appUserDetails = new AppUserDetails(appUser);

        String returnedPage = underTest.disconnectFromUser(appUserDetails,2L);

        assertEquals("redirect:/user/2", returnedPage);
    }
}
