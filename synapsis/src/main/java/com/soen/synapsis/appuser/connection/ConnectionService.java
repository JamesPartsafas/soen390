package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;

public class ConnectionService {
    private final ConnectionRepository connectionRepository;

    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public String makeConnection(AppUser requester, AppUser receiver) {
        if (requester.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("Only candidates can make connections.");
        }
        if (receiver.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("Only candidates can receive connections.");
        }

        ConnectionKey cKey = new ConnectionKey(requester.getId(), receiver.getId());

/*
        boolean connectionExists = connectionRepository.findById(cKey);

        if(connectionExists){
            throw new IllegalStateException("Connection has already been made.");
        }
*/

        Connection connection = new Connection(cKey, requester, receiver, true);
        connectionRepository.save(connection);

        return "pages/home";
    }
}
