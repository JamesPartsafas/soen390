package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
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
        SecurityUtilities.authenticateAnonymousUser();

        String returnedPage = underTest.viewNetwork();

        assertEquals("pages/network", returnedPage);
    }

    @Test
    void makeConnectionTest() {
        AppUser appUser1 = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        AppUser appUser2 = new AppUser("Joe Woman", "1234", "joewoman@email.com", Role.CANDIDATE);
        Model model = mock(Model.class);

        String returnedPage = underTest.makeConnection(appUser1, appUser2, model);

        assertEquals(null, returnedPage);
    }
}
