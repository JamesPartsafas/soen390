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

@Controller
public class ChatController {

    private ChatService chatService;
    private AuthService authService;
    private SimpMessageSendingOperations simpMessagingTemplate;

    @Autowired
    public ChatController(ChatService chatService, AuthService authService, SimpMessageSendingOperations simpMessagingTemplate) {
        this.chatService = chatService;
        this.authService = authService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/chat/create")
    public String createChat(@RequestParam("id") Long id) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        return chatService.createChat(authService.getAuthenticatedUser(), id);
    }

    @GetMapping("/chats")
    public String getChats(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<Chat> chats = chatService.findChatsByUserId(authService.getAuthenticatedUser().getId());
        model.addAttribute("chats", chats);

        return "pages/chatPage";
    }

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

        if (!Objects.equals(chat.getFirstUser().getId(), authenticatedUserId) && !Objects.equals(chat.getSecondUser().getId(), authenticatedUserId)) {
            return "redirect:/chats";
        }

        List<Message> messages = chat.getMessages();
        Long lastMessageId = messages.size() == 0 ? 0 : messages.get(messages.size() - 1).getId();

        if (Objects.equals(chat.getFirstUser().getId(), authenticatedUserId)) {
            MessageDTO readMessage = new MessageDTO(lastMessageId,
                    "",
                    MessageType.READ,
                    chat.getSecondUser().getId(),
                    chat.getFirstUser().getId());
            this.sendRead(SecurityContextHolder.getContext().getAuthentication(), chatID, readMessage);

            model.addAttribute("senderId", chat.getFirstUser().getId());
            model.addAttribute("receiverId", chat.getSecondUser().getId());
        } else {
            MessageDTO readMessage = new MessageDTO(lastMessageId,
                    "",
                    MessageType.READ,
                    chat.getFirstUser().getId(),
                    chat.getSecondUser().getId());
            this.sendRead(SecurityContextHolder.getContext().getAuthentication(), chatID, readMessage);

            model.addAttribute("senderId", chat.getSecondUser().getId());
            model.addAttribute("receiverId", chat.getFirstUser().getId());
        }

        model.addAttribute("chatId", chat.getId());
        model.addAttribute("messages", messages);

        return "pages/messagingPage";
    }

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