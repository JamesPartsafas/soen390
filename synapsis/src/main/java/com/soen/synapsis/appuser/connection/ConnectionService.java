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
        if (requester.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("Only candidates can make connections.");
        }
        if (receiver.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("Only candidates can receive connections.");
        }

        ConnectionKey cKey1 = new ConnectionKey(requester.getId(), receiver.getId());
        ConnectionKey cKey2 = new ConnectionKey(receiver.getId(), requester.getId());

        boolean connection1Exists = connectionRepository.existsById(cKey1);
        boolean connection2Exists = connectionRepository.existsById(cKey2);

        if (connection1Exists || connection2Exists) {
            throw new IllegalStateException("Connection has already been made.");
        }

        /*
        An alternative to this ugly solution is to save in a record the connection in the other way around:
        The requester would be the receiver and vice-versa.
        So, in the database the following two records would be saved for a connection:

        | receiver_id | requester_id | pending |
        | 1           | 2            |   true  |
        | 2           | 1            |   true  |

        I'm not sure which solution is better.
         */

/*
        boolean connectionExists = connectionRepository.findById(cKey);

        if(connectionExists){
            throw new IllegalStateException("Connection has already been made.");
        }
*/

        Connection connection = new Connection(cKey1, requester, receiver, true);
        connectionRepository.save(connection);

        return "pages/home";
    }
}
