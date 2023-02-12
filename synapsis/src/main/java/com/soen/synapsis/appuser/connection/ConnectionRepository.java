package com.soen.synapsis.appuser.connection;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, ConnectionKey> {

    List<Connection> findAllByIdRequesterIDAndPendingIsFalse(Long requesterID);
    List<Connection> findAllByIdReceiverIDAndPendingIsFalse(Long receiverID);

}