package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, AppUserRepository appUserRepository, NotificationService notificationService) {
        this.connectionRepository = connectionRepository;
        this.appUserRepository = appUserRepository;
        this.notificationService = notificationService;
    }

    public List<AppUser> getConnections(AppUser appUser) {
        List<Connection> connectionsRequesters = connectionRepository.findAcceptedConnectionsByRequesterID(appUser.getId());
        List<Connection> connectionsReceivers = connectionRepository.findAcceptedConnectionsByReceiverID(appUser.getId());

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

    public List<AppUser> getPendingConnectionRequest(AppUser user) {
        return connectionRepository.findPendingConnectionsByReceiverID(user.getId());
    }

    public String rejectConnection(AppUser user, Long id) {
        ConnectionKey connectionKey = new ConnectionKey(id, user.getId());
        Optional<Connection> retrievedConnection = connectionRepository.findById(connectionKey);

        if (!retrievedConnection.isPresent()) {
            throw new IllegalStateException("Connection does not exists.");
        }

        Connection connection = retrievedConnection.get();

        if (!connection.isPending()) {
            throw new IllegalStateException("Connection already accepted.");
        }

        connectionRepository.deleteById(connectionKey);

        return "redirect:/network";
    }

    public String acceptConnection(AppUser user, Long id) {
        ConnectionKey connectionKey = new ConnectionKey(id, user.getId());
        Optional<Connection> retrievedConnection = connectionRepository.findById(connectionKey);

        if (!retrievedConnection.isPresent()) {
            throw new IllegalStateException("Connection does not exists.");
        }

        Connection connection = retrievedConnection.get();

        if (!connection.isPending()) {
            throw new IllegalStateException("Connection already accepted.");
        }

        connection.setPending(false);
        connectionRepository.save(connection);

        AppUser requester = connection.getRequester();
        AppUser accepter = connection.getReceiver();

        NotificationDTO requesterNotificationDTO = new NotificationDTO(0L, requester.getId(), NotificationType.CONNECTION, "Your connection request was accepted!", "/network", false, "");
        notificationService.saveNotification(requesterNotificationDTO, requester);

        NotificationDTO accepterNotificationDTO = new NotificationDTO(0L, accepter.getId(), NotificationType.CONNECTION, "You accepted a new connection!", "/network", false, "");
        notificationService.saveNotification(accepterNotificationDTO, accepter);

        return "redirect:/network";
    }

    public void connect(Long requesterId, Long receiverId) {
        AppUser requester = appUserRepository.getReferenceById(requesterId);
        AppUser receiver = appUserRepository.getReferenceById(receiverId);

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

        NotificationDTO notificationDTO = new NotificationDTO(0L, receiver.getId(), NotificationType.CONNECTION, "You have a new connection request!", "/network", false, "");
        notificationService.saveNotification(notificationDTO, receiver);
    }

    public void disconnect(Long requesterId, Long receiverId) {
        ConnectionKey connectionKey = new ConnectionKey(requesterId, receiverId);
        connectionRepository.deleteById(connectionKey);
    }

    public boolean isConnectedWith(Long requesterId, Long receiverId) {
        Optional<Connection> connectionWhenSentConnection = connectionRepository.findAcceptedConnectionsByRequesterIDAndReceiverID(requesterId, receiverId);
        Optional<Connection> connectionWhenReceivedConnection = connectionRepository.findAcceptedConnectionsByRequesterIDAndReceiverID(receiverId, requesterId);

        if (connectionWhenSentConnection.isEmpty() && connectionWhenReceivedConnection.isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean isPendingConnectionWith(Long requesterId, Long receiverId) {
        Optional<Connection> retrievedConnection = connectionRepository.findPendingConnectionsByRequesterIDAndReceiverID(requesterId, receiverId);
        if (retrievedConnection.isEmpty()) {
            return false;
        }
        return true;
    }

}

