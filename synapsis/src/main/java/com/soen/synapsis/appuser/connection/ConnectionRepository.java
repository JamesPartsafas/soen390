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
    @Query(value = "SELECT * FROM connection WHERE requester_id = :requesterID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByRequesterID(@Param("requesterID") Long requesterID);

    @Query(value = "SELECT * FROM connection WHERE receiver_id = :receiverID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByReceiverID(@Param("receiverID") Long receiverID);

    @Query(value = "SELECT a FROM AppUser a JOIN Connection c ON c.id.requesterID = a.id WHERE c.id.receiverID = :receiverID AND c.pending = true ")
    List<AppUser> findPendingConnectionsByReceiverID(@Param("receiverID") Long receiverID);

    @Query(value = "SELECT * FROM Connection c WHERE c.requester_id = :requesterID AND c.receiver_id = :receiverID AND c.pending = true", nativeQuery = true)
    Optional<Connection> findPendingConnectionsByRequesterIDAndReceiverID(@Param("requesterID") Long requesterID, @Param("receiverID") Long receiverID);
}