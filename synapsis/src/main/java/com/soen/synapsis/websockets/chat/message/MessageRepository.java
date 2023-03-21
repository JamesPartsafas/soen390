package com.soen.synapsis.websockets.chat.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
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

    /**
     * Retrieves a list of messages with the specified report status.
     * @param reported The report status of the messages to retrieve
     * @return A list of messages with the specified report status
     */
    @Query("SELECT m FROM Message m WHERE m.reportStatus = :reported")
    List<Message> findByReportStatus(@Param("reported") ReportStatus reported);

    /**
     * Retrieves a list of previous messages in the specified chat, ordered by creation date.
     * @param chatId The ID of the chat to retrieve messages from
     * @param id The ID of the most recent message to include in the result set
     * @param pageable A Pageable object to limit and order the result set
     * @return A list of previous messages in the specified chat, ordered by creation date
     */
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.id <= :id ORDER BY m.createdAt ASC")
    List<Message> findPreviousMessages(@Param("chatId") Long chatId, @Param("id") Long id, Pageable pageable);
}
