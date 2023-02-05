package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import com.soen.synapsis.appuser.connection.ConnectionRepository;
import com.soen.synapsis.appuser.connection.ConnectionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

public class ConnectionServiceTest {

    @Mock
    private ConnectionService connectionService;

    @Mock
    private ConnectionRepository connectionRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        connectionService = new ConnectionService(connectionRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void makeConnectionSuccessful() {
        AppUser appUser1 = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        AppUser appUser2 = new AppUser("Joe Woman", "1234", "joewoman@email.com", Role.CANDIDATE);

        String returnValue = connectionService.makeConnection(appUser1, appUser2);

        assertEquals("pages/home", returnValue);
    }

    @Test
    void makeConnectionWithIllegalRequesterRole() {
        AppUser appUser1 = new AppUser("Joe Man", "1234", "joeman@email.com", Role.ADMIN);
        AppUser appUser2 = new AppUser("Joe Woman", "1234", "joewoman@email.com", Role.CANDIDATE);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> connectionService.makeConnection(appUser1, appUser2),
                "Only candidates can make connections.");
    }

    @Test
    void makeConnectionWithIllegalReceiverRole() {
        AppUser appUser1 = new AppUser("Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        AppUser appUser2 = new AppUser("Joe Woman", "1234", "joewoman@email.com", Role.ADMIN);

        Exception exception = assertThrows(IllegalStateException.class,
                () -> connectionService.makeConnection(appUser1, appUser2),
                "Only candidates can receive connections.");
    }

}
