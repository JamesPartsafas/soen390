package com.soen.synapsis.appuser.connection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionKey> {
    @Query(value = "SELECT * FROM connection WHERE requester_id = :requesterID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByRequesterID(@Param("requesterID") Long requesterID);

    @Query(value = "SELECT * FROM connection WHERE receiver_id = :receiverID AND pending = FALSE", nativeQuery = true)
    List<Connection> findAcceptedConnectionsByReceiverID(@Param("receiverID") Long receiverID);

}