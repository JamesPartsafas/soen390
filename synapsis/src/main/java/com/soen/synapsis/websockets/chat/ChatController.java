package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserAuth;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.websockets.chat.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entry point for user requests to interface with Chat-related functionality.
 */
@Controller
public class ChatController {

    private ChatService chatService;
    private AuthService authService;
    private SimpMessageSendingOperations simpMessagingTemplate;

    /**
     * Constructor to create an instance of the ChatController.
     * This is annotated by autowired for automatic dependency injection
     * @param chatService Used to interact with the Chat service layer
     * @param authService  Used to fetch with the authenticated user
     * @param simpMessagingTemplate  Used to send messages to a user via the WebSocket connection.
     */
    @Autowired
    public ChatController(ChatService chatService, AuthService authService, SimpMessageSendingOperations simpMessagingTemplate) {
        this.chatService = chatService;
        this.authService = authService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Allows users to create a new chat
     * @param id The ID of the participant AppUser
     * @return The view of the chat if the user is authenticated else redirects to the home page.
     */
    @PostMapping("/chat/create")
    public String createChat(@RequestParam("id") Long id) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        return chatService.createChat(authService.getAuthenticatedUser(), id);
    }

    /**
     * Retrieves all the chats that the authenticated user have
     * @param model Allows for data to be passed to view.
     * @return The view containing all the chats if the user is authenticated else redirects to the home page.
     */
    @GetMapping("/chats")
    public String getChats(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<Chat> chats = chatService.findChatsByUserId(authService.getAuthenticatedUser().getId());
        model.addAttribute("chats", chats);

        return "pages/chatPage";
    }

    /**
     * Retrieves all the messages for a specific chat for the authenticated user have
     * @param chatID The chat's page to retrieve
     * @param model Allows for data to be passed to view.
     * @return The view for a specific chat showing all its messages if the user is authenticated else redirects to the home page.
     */
    @GetMapping("/chat/{chatID}")
    public String getChatById(@PathVariable Long chatID, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<Chat> retrievedChat = chatService.findChatById(chatID);

        if (retrievedChat.isEmpty()) {
            return "redirect:/chats";
        }

        Chat chat = retrievedChat.get();
        Long authenticatedUserId = authService.getAuthenticatedUser().getId();

        if (!Objects.equals(chat.getCreator().getId(), authenticatedUserId) && !Objects.equals(chat.getParticipant().getId(), authenticatedUserId)) {
            return "redirect:/chats";
        }

        List<Message> messages = chat.getMessages();
        Long lastMessageId = messages.size() == 0 ? 0 : messages.get(messages.size() - 1).getId();

        if (Objects.equals(chat.getCreator().getId(), authenticatedUserId)) {
            MessageDTO readMessage = new MessageDTO(lastMessageId,
                    "",
                    MessageType.READ,
                    chat.getParticipant().getId(),
                    chat.getCreator().getId());
            this.sendRead(SecurityContextHolder.getContext().getAuthentication(), chatID, readMessage);

            model.addAttribute("senderId", chat.getCreator().getId());
            model.addAttribute("receiverId", chat.getParticipant().getId());
        } else {
            MessageDTO readMessage = new MessageDTO(lastMessageId,
                    "",
                    MessageType.READ,
                    chat.getCreator().getId(),
                    chat.getParticipant().getId());
            this.sendRead(SecurityContextHolder.getContext().getAuthentication(), chatID, readMessage);

            model.addAttribute("senderId", chat.getParticipant().getId());
            model.addAttribute("receiverId", chat.getCreator().getId());
        }

        model.addAttribute("chatId", chat.getId());
        model.addAttribute("messages", messages);

        return "pages/messagingPage";
    }

    /**
     * This method handles sending a message in a chat to the receiving user.
     * If the message type is not TYPE, the sending user will receive an ERROR message.
     * @param authentication an instance of the Authentication interface representing the user's authentication information
     * @param chatID a Long representing the ID of the chat
     * @param message a MessageDTO object representing the content of the message to be sent and saved
     */
    @MessageMapping("/chat/{chatID}")
    public void sendMessage(Authentication authentication, @DestinationVariable Long chatID, MessageDTO message) {
        try {
            if (message.getType() != MessageType.TEXT) {
                throw new IllegalStateException("Message Type should only be TEXT");
            }

            AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
            AppUser appUser = appUserAuth.getAppUser();

            if (appUser == null || !Objects.equals(message.getSenderId(), appUser.getId())) {
                throw new IllegalStateException("Sender ID is not valid");
            }

            Long messageId = chatService.saveMessage(chatID, appUser, message.getContent());
            message.setId(messageId);
            simpMessagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/chat/" + chatID, message);
        } catch (Exception exception) {
            MessageDTO returnMessage = new MessageDTO(exception.getMessage(), MessageType.ERROR, message.getSenderId(), 0L);
            simpMessagingTemplate.convertAndSendToUser(message.getSenderId().toString(), "/queue/chat/" + chatID, returnMessage);
        }
    }

    /**
     * This method handles sending a READ message in a chat to the receiving user.
     * If the message type is not READ, the sending user will receive an ERROR message.
     * @param authentication an instance of the Authentication interface representing the user's authentication information
     * @param chatID a Long representing the ID of the chat
     * @param message a MessageDTO object representing the READ message to be sent
     */
    @MessageMapping("/chat/{chatID}/read")
    public void sendRead(Authentication authentication, @DestinationVariable Long chatID, MessageDTO message) {
        try {
            if (message.getType() != MessageType.READ) {
                throw new IllegalStateException("Message Type should only be READ");
            }

            AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
            AppUser appUser = appUserAuth.getAppUser();

            if (appUser == null || !Objects.equals(message.getSenderId(), appUser.getId())) {
                throw new IllegalStateException("Sender ID not valid");
            }

            chatService.updateRead(chatID, message.getReceiverId(), message.getId());
            simpMessagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/chat/" + chatID, message);
        } catch (Exception e) {
            MessageDTO returnMessage = new MessageDTO(e.getMessage(), MessageType.ERROR, message.getSenderId(), 0L);
            simpMessagingTemplate.convertAndSendToUser(message.getSenderId().toString(), "/queue/chat/" + chatID, returnMessage);
        }
    }

}