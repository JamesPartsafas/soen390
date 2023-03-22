package com.soen.synapsis.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.Chat;
import com.soen.synapsis.websockets.chat.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * @param messageDTO Represents the message sent by the sender
     * @return The created message
     */
    public Message saveMessage(Chat chat, AppUser sender, MessageDTO messageDTO) {
        Message message = new Message(chat, messageDTO.getContent(), sender, messageDTO.getFileName(), messageDTO.getFile());
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

    /**
     * Sets the report status of a message to "REPORTED" and saves the updated message to the repository.
     * @param authenticatedUser the authenticated user who is reporting the message
     * @param messageID the ID of the message to report
     * @throws IllegalStateException if the message with the given ID does not exist,
     * or if the authenticated user is the sender of the message,
     * or if the message has already been reported.
     */
    public void setMessageReportStatus(AppUser authenticatedUser, Long messageID) {
        Optional<Message> messageToReport = messageRepository.findById(messageID);

        if (messageToReport.isEmpty()) {
            throw new IllegalStateException("Message does not exist");
        }

        Message message = messageToReport.get();

        if (Objects.equals(message.getSender().getId(), authenticatedUser.getId())) {
            throw new IllegalStateException("Can not report your own message");
        }

        if (message.getReportStatus() != ReportStatus.UNREPORTED) {
            throw new IllegalStateException("Message already reported");
        }

        message.setReportStatus(ReportStatus.REPORTED);
        messageRepository.save(message);
    }


    /**
     * Retrieves a list of all reported messages and the five previous messages for each message in separate lists.
     * @return A list of lists containing the reported messages and their previous messages.
     */
    public List<List<Message>> getReportedMessages() {
        List<Message> reportedMessages = messageRepository.findByReportStatus(ReportStatus.REPORTED);
        List<List<Message>> result = new ArrayList<>();

        for (Message reportedMessage : reportedMessages) {
            List<Message> previousMessages = messageRepository.findPreviousMessages(reportedMessage.getChat().getId(),
                    reportedMessage.getId(),
                    PageRequest.of(0, 6));
            result.add(previousMessages);
        }

        return result;
    }

    /**
     * Marks a message that was reported as resolved.
     * @param messageId the message to mark as resolved.
     */
    public void resolveReport(Long messageId) {
        Optional<Message> optionalReportedMessage = messageRepository.findById(messageId);

        if (optionalReportedMessage.isEmpty()) {
            return;
        }

        Message reportedMessage = optionalReportedMessage.get();

        reportedMessage.setReportStatus(ReportStatus.REVIEWED);
        messageRepository.save(reportedMessage);
    }
}
