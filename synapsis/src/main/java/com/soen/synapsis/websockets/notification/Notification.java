package com.soen.synapsis.websockets.notification;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser recipient;

    @Column
    private NotificationType type;

    @Column
    private String text;

    @Column
    private String url;

    @Column
    private boolean seen;

    @Column(nullable = false)
    private Timestamp creationTime;

    public Notification() {
    }

    public Notification(AppUser recipient, NotificationType type, String text, String url, boolean seen, Timestamp creationTime) {
        this.recipient = recipient;
        this.type = type;
        this.text = text;
        this.url = url;
        this.seen = seen;
        this.creationTime = creationTime;

    }

    public Notification(Long id, AppUser recipient, NotificationType type, String text, String url, boolean seen, Timestamp creationTime) {
        this(recipient, type, text, url, seen, creationTime);
        this.id = id;
    }

    public Notification(AppUser recipient, NotificationType type, String text, String url, boolean seen) {
        this(recipient, type, text, url, seen, new Timestamp(System.currentTimeMillis()));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public AppUser getRecipient() {
        return recipient;
    }

    public void setRecipient(AppUser recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", recipient=" + recipient +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", seen=" + seen +
                ", creationTime=" + creationTime +
                '}';
    }
}
