package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;

    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public String makeConnection(AppUser requester, AppUser receiver) {
        if (requester.getRole() == Role.ADMIN) {
            throw new IllegalStateException("Admins cannot make connections.");
        }
        if (receiver.getRole() == Role.ADMIN) {
            throw new IllegalStateException("Admins cannot receive connections.");
        }

        ConnectionKey cKey1 = new ConnectionKey(requester.getId(), receiver.getId());
        ConnectionKey cKey2 = new ConnectionKey(receiver.getId(), requester.getId());

        boolean connection1Exists = connectionRepository.existsById(cKey1);
        boolean connection2Exists = connectionRepository.existsById(cKey2);

        if (connection1Exists || connection2Exists) {
            throw new IllegalStateException("Connection has already been made.");
        }

        Connection connection = new Connection(cKey1, requester, receiver, true);
        connectionRepository.save(connection);

        return null; // Should return to "pages/networking" after it is implemented
    }
}
