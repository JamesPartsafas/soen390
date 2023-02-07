package com.soen.synapsis.unit.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.connection.Connection;
import com.soen.synapsis.appuser.connection.ConnectionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionKeyTest {

    private ConnectionKey connectionKey;
    private Long requesterID;
    private Long receiverID;

    @BeforeEach
    void setUp() {
        requesterID = 10L;
        receiverID = 20L;

        //autoCloseable = MockitoAnnotations.openMocks(this);
        connectionKey = new ConnectionKey(requesterID, receiverID);
    }

    @Test
    void getRequesterID() {
        assertEquals(requesterID, connectionKey.getRequesterID());
    }

    @Test
    void setRequesterID() {
        Long newRequesterID = 30L;

        connectionKey.setRequesterID(newRequesterID);

        assertEquals(newRequesterID, connectionKey.getRequesterID());
    }

    @Test
    void getReceiverID() {
        assertEquals(receiverID, connectionKey.getReceiverID());
    }

    @Test
    void setReceiverID() {
        Long newReceiverID = 40L;

        connectionKey.setReceiverID(newReceiverID);

        assertEquals(newReceiverID, connectionKey.getReceiverID());
    }

    @Test
    void testToString() {
        assertThat(connectionKey.toString()).isNotNull();
    }


}
