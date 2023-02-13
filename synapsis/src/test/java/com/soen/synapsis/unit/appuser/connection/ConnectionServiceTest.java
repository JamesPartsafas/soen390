package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.Connection;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import com.soen.synapsis.appuser.connection.ConnectionRepository;
import com.soen.synapsis.appuser.connection.ConnectionService;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void getConnections() {
        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        ConnectionKey connectionKey = new ConnectionKey(candidateUser1.getId(), candidateUser2.getId());
        Connection connection = new Connection(connectionKey, candidateUser1, candidateUser2, false);

        List<Connection> allConnectionsIDs = new ArrayList<>();
        allConnectionsIDs.add(connection);
        List<AppUser> allConnections = new ArrayList<>();
        allConnections.add(candidateUser2);

        given(connectionRepository.findAcceptedConnectionsByRequesterID(Mockito.any(Long.class))).willReturn(allConnectionsIDs);
        given(appUserRepository.getReferenceById(Mockito.any(Long.class))).willReturn(candidateUser2);

        List<AppUser> returnValue = underTest.getConnections(appUserDetails);

        verify(connectionRepository).findAcceptedConnectionsByRequesterID(candidateUser1.getId());
        verify(connectionRepository).findAcceptedConnectionsByReceiverID(candidateUser1.getId());

        assertEquals(allConnections, returnValue);
    }

    @Test
    void rejectConnectionWithNonexistentConnection() {
        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, never()).deleteById(connectionArgumentCaptor.capture());

        assertThrows(IllegalStateException.class, () -> underTest.rejectConnection(appUserDetails, candidateUser2.getId()));
    }

    @Test
    void rejectConnectionWithAcceptedConnection() {
        ConnectionKey connectionKey = new ConnectionKey(candidateUser2.getId(), candidateUser1.getId());
        Connection connection = new Connection(connectionKey, candidateUser2, candidateUser1, false);

        when(connectionRepository.findById(any(ConnectionKey.class))).thenReturn(Optional.of(connection));
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, never()).deleteById(connectionArgumentCaptor.capture());

        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        assertThrows(IllegalStateException.class, () -> underTest.rejectConnection(appUserDetails, candidateUser2.getId()));
    }

    @Test
    void rejectConnectionWithPendingConnection() {
        ConnectionKey connectionKey = new ConnectionKey(candidateUser2.getId(), candidateUser1.getId());
        Connection connection = new Connection(connectionKey, candidateUser2, candidateUser1, true);

        when(connectionRepository.findById(any(ConnectionKey.class))).thenReturn(Optional.of(connection));
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, atMostOnce()).deleteById(connectionArgumentCaptor.capture());

        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        assertEquals(underTest.rejectConnection(appUserDetails, candidateUser2.getId()), "redirect:/network");
    }

    @Test
    void acceptConnectionWithNonexistentConnection() {
        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, never()).deleteById(connectionArgumentCaptor.capture());

        assertThrows(IllegalStateException.class, () -> underTest.acceptConnection(appUserDetails, candidateUser2.getId()));
    }

    @Test
    void acceptConnectionWithAcceptedConnection() {
        ConnectionKey connectionKey = new ConnectionKey(candidateUser2.getId(), candidateUser1.getId());
        Connection connection = new Connection(connectionKey, candidateUser2, candidateUser1, false);

        when(connectionRepository.findById(any(ConnectionKey.class))).thenReturn(Optional.of(connection));
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, never()).deleteById(connectionArgumentCaptor.capture());

        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        assertThrows(IllegalStateException.class, () -> underTest.acceptConnection(appUserDetails, candidateUser2.getId()));
    }

    @Test
    void acceptConnectionWithPendingConnection() {
        ConnectionKey connectionKey = new ConnectionKey(candidateUser2.getId(), candidateUser1.getId());
        Connection connection = new Connection(connectionKey, candidateUser2, candidateUser1, true);

        when(connectionRepository.findById(any(ConnectionKey.class))).thenReturn(Optional.of(connection));
        ArgumentCaptor<ConnectionKey> connectionArgumentCaptor = ArgumentCaptor.forClass(ConnectionKey.class);
        verify(connectionRepository, atMostOnce()).deleteById(connectionArgumentCaptor.capture());

        AppUserDetails appUserDetails = new AppUserDetails(candidateUser1);
        assertEquals(underTest.acceptConnection(appUserDetails, candidateUser2.getId()), "redirect:/network");
    }

}
