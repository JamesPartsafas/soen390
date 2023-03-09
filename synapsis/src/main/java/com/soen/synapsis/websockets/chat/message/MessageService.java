package com.soen.synapsis.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for Message-related functionality.
 */
@Service
public class MessageService {

    private MessageRepository messageRepository;

    /**
     * Constructor to create an instance of the MessageService.
     * This is annotated by autowired for automatic dependency injection
     * @param messageRepository Used to interact with the message table in the database
     */
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates and saves an instance of the message in the database
     * @param chat Used to identify the chat the message was sent in
     * @param sender Used to identify the sender of the message
     * @param content Represents the content of the message
     * @return The created message
     */
    public Message saveMessage(Chat chat, AppUser sender, String content) {
        Message message = new Message(chat, content, sender);
        messageRepository.save(message);
        return message;
    }

    /**
     * Sets all the read flag to true in all messages thar are sent before a specific message,
     * sent by a particular user, and in a particular chat.
     * @param chatId Used to search for the messages in a particular chat
     * @param senderId Used to search for messages created by a particular sender
     * @param messageId Used to search for messages that are sent before this message Id
     */
    public void updateRead(Long chatId, Long senderId, Long messageId) {
        List<Message> messagesToUpdate = messageRepository.getMessagesByChatAndSenderAndIdLessThanEqual(chatId, senderId, messageId);

        for (Message message : messagesToUpdate) {
            message.setRead(true);
        }

        messageRepository.saveAll(messagesToUpdate);
    }
}
