package com.soen.synapsis.websockets.chat;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.message.Message;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser creator;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser participant;

    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    @Column(nullable = false)
    @OrderBy("createdAt ASC")
    private List<Message> messages;

    @Column(name = "last_updated", nullable = false)
    private Timestamp lastUpdated;

    public Chat() {
    }

    public Chat(AppUser creator, AppUser participant) {
        this(creator, participant, new Timestamp(System.currentTimeMillis()));
    }

    public Chat(AppUser creator, AppUser participant, Timestamp lastUpdated) {
        this.creator = creator;
        this.participant = participant;
        this.lastUpdated = lastUpdated;
    }

    public Chat(Long id, AppUser creator, AppUser participant, Timestamp lastUpdated) {
        this.id = id;
        this.creator = creator;
        this.participant = participant;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser firstUser) {
        this.creator = firstUser;
    }

    public AppUser getParticipant() {
        return participant;
    }

    public void setParticipant(AppUser secondUser) {
        this.participant = secondUser;
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
                ", firstUser=" + creator +
                ", secondUser=" + participant +
                ", messages=" + messages +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
