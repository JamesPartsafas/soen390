package com.soen.synapsis.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Chat chat, AppUser sender, String content) {
        Message message = new Message(chat, content, sender);
        messageRepository.save(message);
        return message;
    }

    public void updateRead(Long chatId, Long senderId, Long messageId) {
        List<Message> messagesToUpdate = messageRepository.getMessagesByChatAndSenderAndIdLessThanEqual(chatId, senderId, messageId);

        for (Message message : messagesToUpdate) {
            message.setRead(true);
        }

        messageRepository.saveAll(messagesToUpdate);
    }
}
