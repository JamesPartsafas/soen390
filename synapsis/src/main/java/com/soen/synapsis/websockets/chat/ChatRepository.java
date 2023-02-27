package com.soen.synapsis.websockets.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.creator.id =:id OR c.participant.id =: id ORDER BY c.lastUpdated DESC")
    List<Chat> findAllByUserId(@Param("id") Long id);

    @Query("SELECT c FROM Chat c WHERE (c.creator.id = :firstUserId AND c.participant.id = :secondUserId) OR (c.creator.id = :secondUserId AND c.participant.id = :firstUserId)")
    Optional<Chat> findByUserIds(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);
}
