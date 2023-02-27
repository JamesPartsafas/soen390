package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.websockets.chat.message.Message;
import com.soen.synapsis.websockets.chat.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private ChatRepository chatRepository;
    private MessageService messageService;
    private AppUserService appUserService;

    @Autowired
    public ChatService(ChatRepository chatRepository, MessageService messageService, AppUserService appUserService) {
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.appUserService = appUserService;
    }

    public List<Chat> findChatsByUserId(Long id) {
        return chatRepository.findAllByUserId(id);
    }

    public Optional<Chat> findChatById(Long id) { return chatRepository.findById(id); }

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

    public void updateRead(Long chatID, Long senderID, Long messageID) {
        messageService.updateRead(chatID, senderID, messageID);
    }

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
}
