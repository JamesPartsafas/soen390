package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.Connection;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import com.soen.synapsis.appuser.connection.ConnectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ConnectionRepositoryTest {

    @Autowired
    private ConnectionRepository underTest;
    AppUser appUser1;
    AppUser appUser2;

    @BeforeEach
    void setUp() {
        appUser1 = new AppUser(1L, "Joe User", "1234", "joeusertest@mail.com", Role.CANDIDATE);
        appUser2 = new AppUser(2L, "John User", "1234", "johnusertest@mail.com", Role.CANDIDATE);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindAcceptedConnectionsByRequesterID() {
        ConnectionKey connectionKey = new ConnectionKey(appUser1.getId(), appUser2.getId());
        Connection connection = new Connection(connectionKey, appUser1, appUser2, false);

        underTest.save(connection);

        assertThat(underTest.findAcceptedConnectionsByRequesterID(appUser2.getId())).isNotNull();
    }

    @Test
    void itShouldFindAcceptedConnectionsByReceiverID() {
        ConnectionKey connectionKey = new ConnectionKey(appUser2.getId(), appUser1.getId());
        Connection connection = new Connection(connectionKey, appUser2, appUser1, false);

        underTest.save(connection);

        assertThat(underTest.findAcceptedConnectionsByReceiverID(appUser1.getId())).isNotNull();
    }

    @Test
    void itShouldNotFindAcceptedConnectionsByRequesterIDIfUserDoesNotExist() {
        Long ID = 100L;

        List<Connection> result = underTest.findAcceptedConnectionsByRequesterID(ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void itShouldNotFindAcceptedConnectionsByReceiverIDIfUserDoesNotExist() {
        Long ID = 100L;

        List<Connection> result = underTest.findAcceptedConnectionsByReceiverID(ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void PendingConnectionsByReceiverID_SomePendingConnections() {
        ConnectionKey connectionKey = new ConnectionKey(appUser1.getId(), appUser2.getId());
        Connection connection = new Connection(connectionKey, appUser1, appUser2, true);
        underTest.save(connection);
        assertTrue(underTest.findPendingConnectionsByReceiverID(appUser2.getId()).size() == 1);
    }


    @Test
    void PendingConnectionsByReceiverID_NoPendingConnections() {
        ConnectionKey connectionKey = new ConnectionKey(appUser1.getId(), appUser2.getId());
        Connection connection = new Connection(connectionKey, appUser1, appUser2, false);
        underTest.save(connection);
        assertTrue(underTest.findPendingConnectionsByReceiverID(appUser2.getId()).size() == 0);
    }

    @Test
    void PendingConnectionsByReceiverIDAndRequesterID_SomePendingCConnections() {
        ConnectionKey connectionKey = new ConnectionKey(appUser1.getId(), appUser2.getId());
        Connection connection = new Connection(connectionKey, appUser1, appUser2, true);
        underTest.save(connection);
        assertTrue(underTest.findPendingConnectionsByRequesterIDAndReceiverID(appUser1.getId(), appUser2.getId()).size() == 1);
    }

    @Test
    void PendingConnectionsByReceiverIDAndRequesterID_NoPendingCConnections() {
        ConnectionKey connectionKey = new ConnectionKey(appUser1.getId(), appUser2.getId());
        Connection connection = new Connection(connectionKey, appUser1, appUser2, false);
        underTest.save(connection);
        assertTrue(underTest.findPendingConnectionsByRequesterIDAndReceiverID(appUser1.getId(),appUser2.getId()).size() == 0);
    }

}
