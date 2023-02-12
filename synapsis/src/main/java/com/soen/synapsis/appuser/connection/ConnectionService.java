package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final AppUserRepository appUserRepository;

    public ConnectionService(ConnectionRepository connectionRepository, AppUserRepository appUserRepository) {
        this.connectionRepository = connectionRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<AppUser> getConnections(Long user_uid) {
        List<Connection> connectionsRequesters = connectionRepository.findAcceptedConnectionsByRequesterID(user_uid);
        List<Connection> connectionsReceivers = connectionRepository.findAcceptedConnectionsByReceiverID(user_uid);

        List<Long> allConnectionIDs = new ArrayList<>();

        // Get all the receiver IDs
        if (connectionsRequesters != null) {
            for (Connection c : connectionsRequesters) {
                Long receiverID = c.getReceiver().getId();
                allConnectionIDs.add(receiverID);
            }
        }
        // Get all the requester IDs
        if (connectionsReceivers != null) {
            for (Connection c : connectionsReceivers) {
                Long requesterID = c.getRequester().getId();
                allConnectionIDs.add(requesterID);
            }
        }

        List<AppUser> allConnections = new ArrayList<>();
        for (Long l : allConnectionIDs) {
            allConnections.add(appUserRepository.getReferenceById(l));
        }

        return allConnections;
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

        return "pages/network";
    }
}
