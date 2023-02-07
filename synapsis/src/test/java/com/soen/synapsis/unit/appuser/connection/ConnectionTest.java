package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.Connection;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionTest {

    private Connection connection;

    private ConnectionKey id;

    private AppUser requester;

    private AppUser receiver;

    private boolean pending;

    @BeforeEach
    void setUp() {
        AppUser user1 = new AppUser("joe", "1234", "joe@mail.com", Role.CANDIDATE);
        AppUser user2 = new AppUser("jane", "1234", "jane@mail.com", Role.CANDIDATE);
        ConnectionKey cKey = new ConnectionKey(user1.getId(), user2.getId());

        id = cKey;
        requester = user1;
        receiver = user2;
        pending = true;

        connection = new Connection(cKey, user1, user2, pending);
    }

    @Test
    void getId() {
        assertEquals(id, connection.getId());
    }

    @Test
    void setId() {
        AppUser newUser1 = new AppUser("max", "1234", "max@mail.com", Role.CANDIDATE);
        AppUser newUser2 = new AppUser("john", "1234", "john@mail.com", Role.CANDIDATE);
        ConnectionKey newCKey = new ConnectionKey(newUser1.getId(), newUser2.getId());

        connection.setId(newCKey);

        assertEquals(newCKey, connection.getId());
    }

    @Test
    void getRequester() {
        assertEquals(requester, connection.getRequester());
    }

    @Test
    void setRequester() {
        AppUser newUser1 = new AppUser("max", "1234", "max@mail.com", Role.CANDIDATE);

        connection.setRequester(newUser1);

        assertEquals(newUser1, connection.getRequester());
    }

    @Test
    void getReceiver() {
        assertEquals(receiver, connection.getReceiver());
    }

    @Test
    void setReceiver() {
        AppUser newUser1 = new AppUser("max", "1234", "max@mail.com", Role.CANDIDATE);

        connection.setReceiver(newUser1);

        assertEquals(newUser1, connection.getReceiver());

    }

    @Test
    void isPending() {
        assertEquals(pending, connection.isPending());
    }

    @Test
    void setPending() {
        boolean newPending = false;

        connection.setPending(newPending);

        assertEquals(newPending, connection.isPending());
    }

    @Test
    void testToString() {
        assertThat(connection.toString()).isNotNull();
    }

}
