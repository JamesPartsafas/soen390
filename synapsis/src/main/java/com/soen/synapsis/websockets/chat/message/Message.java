package com.soen.synapsis.websockets.chat.message;

import antlr.debug.MessageListener;
import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.Chat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Chat chat;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser sender;

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false)
    private Timestamp createdAt;

    public Message() {
    }

    public Message(Long id, Chat chat, String content, AppUser sender, boolean read, Timestamp createdAt) {
        this(chat, content, sender, read, createdAt);
        this.id = id;
    }

    public Message(Chat chat, String content, AppUser sender, boolean read, Timestamp createdAt) {
        this.chat = chat;
        this.content = content;
        this.sender = sender;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Message(Chat chat, String content, AppUser sender) {
        this(chat, content, sender, false, new Timestamp(System.currentTimeMillis()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppUser getSender() {
        return sender;
    }

    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chat=" + chat +
                ", content='" + content + '\'' +
                ", sender=" + sender +
                ", createdAt=" + createdAt +
                '}';
    }
}
