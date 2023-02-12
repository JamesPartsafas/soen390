package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.Connection;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import com.soen.synapsis.appuser.connection.ConnectionRepository;
import com.soen.synapsis.appuser.connection.ConnectionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class ConnectionServiceTest {

    private ConnectionService underTest;
    @Mock
    private ConnectionRepository connectionRepository;
    @Mock
    private AppUserRepository appUserRepository;
    private AutoCloseable autoCloseable;
    private AppUser candidateUser1;
    private AppUser candidateUser2;
    private AppUser companyUser;
    private AppUser recruiterUser;
    private AppUser adminUser;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ConnectionService(connectionRepository, appUserRepository);

        candidateUser1 = new AppUser(1L, "Joe Man", "1234", "joeman@email.com", Role.CANDIDATE);
        candidateUser2 = new AppUser(2L,"Joe Woman", "1234", "joewoman@email.com", Role.CANDIDATE);
        companyUser = new AppUser(3L,"Company", "1234", "company@email.com", Role.COMPANY);
        recruiterUser = new AppUser(4L,"Recruiter", "1234", "recruiter@email.com", Role.RECRUITER);
        adminUser = new AppUser(5L,"Admin", "1234", "admin@email.com", Role.ADMIN);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void makeConnectionSuccessfulWithCandidateRoles() {
        String returnValue = underTest.makeConnection(candidateUser1, candidateUser2);

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(connectionRepository).save(connectionArgumentCaptor.capture());

        assertEquals("pages/network", returnValue);
    }

    @Test
    void makeConnectionSuccessfulWithCompanyRoles() {
        String returnValue = underTest.makeConnection(companyUser, candidateUser1);

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(connectionRepository).save(connectionArgumentCaptor.capture());

        assertEquals("pages/network", returnValue);
    }

    @Test
    void makeConnectionSuccessfulWithRecruiterRoles() {
        String returnValue = underTest.makeConnection(recruiterUser, candidateUser1);

        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(connectionRepository).save(connectionArgumentCaptor.capture());

        assertEquals("pages/network", returnValue);
    }

    @Test
    void makeConnectionFailWithAdminRequesterRole() {
        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(connectionRepository, never()).save(connectionArgumentCaptor.capture());

        assertThrows(IllegalStateException.class,
                () -> underTest.makeConnection(adminUser, candidateUser1),
                "Admins cannot make connections.");
    }

    @Test
    void makeConnectionFailWithAdminReceiverRole() {
        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);
        verify(connectionRepository, never()).save(connectionArgumentCaptor.capture());

        assertThrows(IllegalStateException.class,
                () -> underTest.makeConnection(candidateUser1, adminUser),
                "Admins cannot receive connections.");
    }

    @Test
    void willThrowWhenConnectionExists() {
        given(connectionRepository.existsById(Mockito.any(ConnectionKey.class))).willReturn(true);

        verify(connectionRepository, never()).save(any());

        assertThrows(IllegalStateException.class,
                () -> underTest.makeConnection(candidateUser1, candidateUser2),
                "Connection has already been made.");
    }

}
