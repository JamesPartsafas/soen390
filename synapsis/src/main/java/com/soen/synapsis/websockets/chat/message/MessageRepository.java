package com.soen.synapsis.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.sender.id = :appUserId AND m.id <= :id")
    List<Message> getMessagesByChatAndSenderAndIdLessThanEqual(@Param("chatId") Long chatId, @Param("appUserId") Long appUserId, @Param("id") Long id);
}
