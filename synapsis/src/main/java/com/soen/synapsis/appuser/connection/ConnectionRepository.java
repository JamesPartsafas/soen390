package com.soen.synapsis.appuser.connection;


import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionKey> {
    /**
     * Retrieve all the connections of a given user by requester id.
     *
     * @param requesterID the id of the requester user.
     * @return a list of connections of the user.
     */
    @Query(value = "SELECT * FROM connection WHERE requester_id = :requesterID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByRequesterID(@Param("requesterID") Long requesterID);

    /**
     * Retrieve all the connections of a given user by receiver id.
     *
     * @param receiverID the id of the receiver user.
     * @return a list of connections of the user.
     */
    @Query(value = "SELECT * FROM connection WHERE receiver_id = :receiverID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByReceiverID(@Param("receiverID") Long receiverID);

    /**
     * Retrieve all the appuser of a given user by receiver id.
     *
     * @param receiverID the id of the receiver user.
     * @return a list appusers.
     */
    @Query(value = "SELECT a FROM AppUser a JOIN Connection c ON c.id.requesterID = a.id WHERE c.id.receiverID = :receiverID AND c.pending = true ")
    List<AppUser> findPendingConnectionsByReceiverID(@Param("receiverID") Long receiverID);

    /**
     * Retrieve all the pending connections between two users.
     *
     * @param requesterID the id of the requester user.
     * @param receiverID  the id of the receiver user.
     * @return a list of connections where pending is true.
     */
    @Query(value = "SELECT * FROM Connection c WHERE c.requester_id = :requesterID AND c.receiver_id = :receiverID AND c.pending = true", nativeQuery = true)
    Optional<Connection> findPendingConnectionsByRequesterIDAndReceiverID(@Param("requesterID") Long requesterID, @Param("receiverID") Long receiverID);

    /**
     * Retrieve all the accepted connections between two users.
     *
     * @param requesterID the id of the requester user.
     * @param receiverID  the id of the receiver user.
     * @return a list of connections where pending is false.
     */
    @Query(value = "SELECT * FROM Connection c WHERE c.requester_id = :requesterID AND c.receiver_id = :receiverID AND c.pending = false", nativeQuery = true)
    Optional<Connection> findAcceptedConnectionsByRequesterIDAndReceiverID(@Param("requesterID") Long requesterID, @Param("receiverID") Long receiverID);

    /**
     * Find number of connections user has.
     *
     * @param userId the id of the user.
     * @return number of connections.
     */
    @Query(value = "SELECT COUNT(c) FROM Connection c WHERE (c.requester_id = :userId OR c.receiver_id = :userId) AND c.pending = false", nativeQuery = true)
    int findNumberOfConnections(@Param("userId") Long userId);
}