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

/**
 * A service class to work with connections.
 */
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

    /**
     * Retrieve all the connections of a user.
     *
     * @param appUser the user that we want to retrieve the connections.
     * @return a list of appuser that you are connected with.
     */
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

    /**
     * Retrieve the users that send you a connection request.
     *
     * @param user the user that we want to get the pending connection requests from.
     * @return a list of appuser where there is a pending connection.
     */
    public List<AppUser> getPendingConnectionRequest(AppUser user) {
        return connectionRepository.findPendingConnectionsByReceiverID(user.getId());
    }

    /**
     * Reject a connection request from another user.
     *
     * @param user the logged-in user.
     * @param id the user that you are rejecting the request.
     * @return the network page.
     */
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

    /**
     * Accept a connection request from another user.
     *
     * @param user the logged-in user.
     * @param id the user that you are accepting the request.
     * @return the network page.
     */
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

        NotificationDTO requesterNotificationDTO = new NotificationDTO(0L, requester.getId(), NotificationType.CONNECTION, "Your connection request was accepted!", "/network", false);
        notificationService.saveNotification(requesterNotificationDTO, requester);

        NotificationDTO accepterNotificationDTO = new NotificationDTO(0L, accepter.getId(), NotificationType.CONNECTION, "You accepted a new connection!", "/network", false);
        notificationService.saveNotification(accepterNotificationDTO, accepter);

        return "redirect:/network";
    }

    /**
     * Create a connection request with another user.
     *
     * @param requesterId the logged-in user.
     * @param receiverId the user that you are sending a request to.
     */
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

        NotificationDTO notificationDTO = new NotificationDTO(0L, receiver.getId(), NotificationType.CONNECTION, "You have a new connection request!", "/network", false);
        notificationService.saveNotification(notificationDTO, receiver);
    }

    /**
     * Disconnect with another user.
     *
     * @param requesterId the logged-in user.
     * @param receiverId the user that you are disconnecting with.
     */
    public void disconnect(Long requesterId, Long receiverId) {
        ConnectionKey connectionKey = new ConnectionKey(requesterId, receiverId);
        connectionRepository.deleteById(connectionKey);
    }

    /**
     * Check if two users are connected.
     *
     * @param requesterId the first appuser
     * @param receiverId the second appuser
     * @return true if the two users are connected; otherwise false
     */
    public boolean isConnectedWith(Long requesterId, Long receiverId) {
        Optional<Connection> connectionWhenSentConnection = connectionRepository.findAcceptedConnectionsByRequesterIDAndReceiverID(requesterId, receiverId);
        Optional<Connection> connectionWhenReceivedConnection = connectionRepository.findAcceptedConnectionsByRequesterIDAndReceiverID(receiverId, requesterId);

        if (connectionWhenSentConnection.isEmpty() && connectionWhenReceivedConnection.isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Check if there's a pending connection request between two users.
     *
     * @param requesterId the first appuser.
     * @param receiverId the second appuser.
     * @return true if there is a pending connection between the two users; otherwise false
     */
    public boolean isPendingConnectionWith(Long requesterId, Long receiverId) {
        Optional<Connection> retrievedConnection = connectionRepository.findPendingConnectionsByRequesterIDAndReceiverID(requesterId, receiverId);
        if (retrievedConnection.isEmpty()) {
            return false;
        }
        return true;
    }

}

