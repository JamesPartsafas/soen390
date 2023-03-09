package com.soen.synapsis.websockets.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository layer for interfacing with Chat database table.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    /**
     * Retrieves all the chats for a specific AppUser.
     * The list of chats is ordered by lastUpdated timestamp in descending order.
     * @param id The ID of the user to retrieve chats for.
     * @return A list of Chat that meet the search criteria.
     */
    @Query("SELECT c FROM Chat c WHERE c.creator.id =:id OR c.participant.id =: id ORDER BY c.lastUpdated DESC")
    List<Chat> findAllByUserId(@Param("id") Long id);

    /**
     * Finds the chat between two specific users.
     * @param firstUserId The ID of the first user.
     * @param secondUserId The ID of the second user.
     * @return An Optional Chat object representing the chat between the two specified users if it exists, otherwise an empty Optional.
     */
    @Query("SELECT c FROM Chat c WHERE (c.creator.id = :firstUserId AND c.participant.id = :secondUserId) OR (c.creator.id = :secondUserId AND c.participant.id = :firstUserId)")
    Optional<Chat> findByUserIds(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);
}
