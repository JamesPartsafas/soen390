package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.message.Message;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser firstUser;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser secondUser;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    @Column(nullable = false)
    @OrderBy("createdAt ASC")
    private List<Message> messages;

    @Column(name = "last_updated", nullable = false)
    private Timestamp lastUpdated;

    public Chat() {
    }

    public Chat(AppUser firstUser, AppUser secondUser) {
        this(firstUser, secondUser, new Timestamp(System.currentTimeMillis()));
    }

    public Chat(AppUser firstUser, AppUser secondUser, Timestamp lastUpdated) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.lastUpdated = lastUpdated;
    }

    public Chat(Long id, AppUser firstUser, AppUser secondUser, Timestamp lastUpdated) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(AppUser firstUser) {
        this.firstUser = firstUser;
    }

    public AppUser getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(AppUser secondUser) {
        this.secondUser = secondUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", firstUser=" + firstUser +
                ", secondUser=" + secondUser +
                ", messages=" + messages +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
