package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Message-related functionality.
 */
@Service
public class ChatService {

    private ChatRepository chatRepository;
    private MessageService messageService;
    private AppUserService appUserService;

    /**
     * Constructor to create an instance of the MessageService.
     * This is annotated by autowired for automatic dependency injection
     * @param chatRepository Used to interact with the Chat table in the database
     * @param messageService Used to interact with the Message service layer
     * @param appUserService Used to interact with the AppUser service layer
     */
    @Autowired
    public ChatService(ChatRepository chatRepository, MessageService messageService, AppUserService appUserService) {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.appUserService = appUserService;
    }

    /**
     * Retrieves all the chats for a specific AppUser
     * @param id The ID of the AppUser to retrieve the chats for
     * @return The list of all that chats for the specific AppUser
     */
    public List<Chat> findChatsByUserId(Long id) {
        return chatRepository.findAllByUserId(id);
    }

    /**
     * Retrieves a specific chat by its ID
     * @param id Represents the ID of the chat to retrieve
     * @return An Optional Chat object if it exists, otherwise an empty Optional.
     */
    public Optional<Chat> findChatById(Long id) { return chatRepository.findById(id); }

    /**
     * Saves a new message in a chat and updates the chat's last updated time.
     * @param chatID The id of the chat where the message will be saved.
     * @param appUser The user who sent the message.
     * @param content The content of the message to be saved.
     * @return The id of the saved message.
     * @throws IllegalStateException if the chat does not exist.
     */
    public Long saveMessage(Long chatID, AppUser appUser, String content) {
        Optional<Chat> retrievedChat = findChatById(chatID);

        if (retrievedChat.isEmpty()) {
            throw new IllegalStateException("Chat does not exist");
        }

        Chat chat = retrievedChat.get();
        Message savedMessage = messageService.saveMessage(chat, appUser, content);
        chat.setLastUpdated(savedMessage.getCreatedAt());
        chatRepository.save(chat);

        return savedMessage.getId();
    }

    /**
     * Update the read status of a message for a particular chat and user
     * @param chatID The ID of the chat containing the message
     * @param senderID The ID of the sender of the message
     * @param messageID The ID of the message to update
     */
    public void updateRead(Long chatID, Long senderID, Long messageID) {
        messageService.updateRead(chatID, senderID, messageID);
    }

    /**
     * Creates a new chat between the logged-in user and the user with the specified ID.
     * If a chat already exists between the two users, redirects to the existing chat page.
     * If the specified user ID is invalid, redirects to the home page.
     * @param user The logged-in user
     * @param id The ID of the user to start the chat with
     * @return The URL to redirect to, either to an existing chat page or to a new chat page
     */
    public String createChat(AppUser user, Long id) {
        Optional<AppUser> retrievedUser = appUserService.getAppUser(id);

        if (retrievedUser.isEmpty()) {
            return "redirect:/";
        }

        Optional<Chat> retrievedChat = chatRepository.findByUserIds(user.getId(), id);

        if (retrievedChat.isPresent()) {
            return "redirect:/chat/" + retrievedChat.get().getId();
        }

        Chat newChat = new Chat(user, retrievedUser.get());
        chatRepository.save(newChat);

        return "redirect:/chat/" + newChat.getId();
    }

    /**
     * Delegate the setting the message reportStatus to reported to the MessageService
     * @param authenticatedUser the authenticated user who is reporting the message
     * @param messageID the ID of the message to report
     */
    public void setMessageReportStatus(AppUser authenticatedUser, Long messageID) {
        messageService.setMessageReportStatus(authenticatedUser, messageID);
    }

    /**
     * Delegate the process of fetching the messages to the MessageService
     * @return A list of lists containing the reported messages and their previous messages.
     */
    public List<List<Message>> getReportedMessages() {
        return messageService.getReportedMessages();
    }
}
