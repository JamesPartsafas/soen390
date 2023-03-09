package com.soen.synapsis.websockets.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer for interfacing with Message database table.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Finds all the messages, before a specific message, that is sent by a particular user in a particular chat
     * @param chatId Used to search in a particular chat
     * @param appUserId Used to search by a particular sender
     * @param id Used to search for messages that are sent before this message
     * @return List of messages which meet the search criteria
     */
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.sender.id = :appUserId AND m.id <= :id")
    List<Message> getMessagesByChatAndSenderAndIdLessThanEqual(@Param("chatId") Long chatId, @Param("appUserId") Long appUserId, @Param("id") Long id);
}
