package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserAuth;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
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

import java.util.Arrays;
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
     * If the user is not an admin, the method retrieves all the chats that the authenticated user have
     * else redirects to the admin review page.
     * @param model Allows for data to be passed to view.
     * @return The view containing all the chats if the user is authenticated else redirects to the home page.
     */
    @GetMapping("/chats")
    public String getChats(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        if (authService.doesUserHaveRole(Role.ADMIN)) {
            return "redirect:/chat/admin";
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
        model.addAttribute("authenticatedUserID", authenticatedUserId);

        return "pages/messagingPage";
    }

    /**
     * This method handles sending a message in a chat to the receiving user.
     * If the message type is not TEXT or the message content is empty or if the message exceeds the permitted size,
     * the sending user will receive an ERROR message.
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

            String content = message.getContent();

            if (content == null) {
                throw new IllegalStateException("Message Content cannot be null");
            }

            String fileName = message.getFileName();
            String file = message.getFile();

            if ((fileName != null && fileName.length() > 50) || (file != null && file.length() > 64600) || content.length() > 255) {
                throw  new IllegalStateException("Message exceeds permitted size limit");
            }

            AppUserAuth appUserAuth = (AppUserAuth) authentication.getPrincipal();
            AppUser appUser = appUserAuth.getAppUser();

            if (appUser == null || !Objects.equals(message.getSenderId(), appUser.getId())) {
                throw new IllegalStateException("Sender ID is not valid");
            }

            Long messageId = chatService.saveMessage(chatID, appUser, message);
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

    /**
     * This method is used to report a chat message.
     * @param messageID The ID of the message to be reported.
     * @param chatID The ID of the chat to which the message belongs.
     * @param model The model object to add attributes to.
     * @return Returns the chat page with an updated model attribute to show if the process succeeded or not.
     */
    @PostMapping("/chat/report")
    public String reportMessage(@RequestParam("messageID") Long messageID, @RequestParam("chatID") Long chatID, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        try {
            chatService.setMessageReportStatus(authService.getAuthenticatedUser(), messageID);
            model.addAttribute("reportStatus", "Reported Message Successfully");
        } catch (Exception e) {
            model.addAttribute("reportStatus", e.getMessage());
        }

        return getChatById(chatID, model);
    }

    /**
     * Retrieves reported messages and displays them on the admin messages page.
     * @param model  Allows for data to be passed to view.
     * @return The view of the admin reported messages' page if the user is an admin else redirects to the home page
     */
    @GetMapping ("/chat/admin")
    public String getReportMessage(Model model) {
        if (!authService.doesUserHaveRole(Role.ADMIN)) {
            return "redirect:/";
        }

        List<List<Message>> reportedMessage = chatService.getReportedMessages();
        model.addAttribute("listOfListOfMessages", reportedMessage);
        return "pages/adminMessagesPage";
    }

    /**
     * Ignores the user report of a message and takes no action
     * @param messageId The ID of the reported message
     * @return View of admin chats page
     */
    @PostMapping("/chats/ignore")
    public String ignoreReport(@RequestParam("messageId") Long messageId) {
        if (!authService.doesUserHaveRole(Role.ADMIN)) {
            return "redirect:/";
        }

        chatService.resolveReport(messageId);

        return "redirect:/chats";
    }

    /**
     * Brings the admin to the chat page with the reported user so a warning can be written
     * @param senderId The reported user ID
     * @param messageId The reported message ID
     * @return View of the chat page with the reported user
     */
    @PostMapping("/chats/warnUser")
    public String warnUser(@RequestParam("senderId") Long senderId, @RequestParam("messageId") Long messageId) {
        if (!authService.doesUserHaveRole(Role.ADMIN)) {
            return "redirect:/";
        }

        chatService.resolveReport(messageId);

        return chatService.createChat(authService.getAuthenticatedUser(), senderId);
    }
}