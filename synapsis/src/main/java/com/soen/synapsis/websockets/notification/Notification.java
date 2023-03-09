package com.soen.synapsis.websockets.notification;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Contains data on all the notification
 */
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

    /**
     * Default Constructor.
     * Assigns the default values to all the instance variables
     */
    public Notification() {
    }

    /**
     * Creates a new notification instance from the given data.
     * The ID of the chat is set automatically.
     * @param recipient Represent the AppUser that the notification will be sent to
     * @param type Represents the type of notification
     * @param text Represents the content of the notification
     * @param url Represents the URL where the notification will redirect to in case if it is clicked
     * @param seen Flag represents if the notification have been viewed
     * @param creationTime Timestamp representing the time the message is created
     */
    public Notification(AppUser recipient, NotificationType type, String text, String url, boolean seen, Timestamp creationTime) {
        this.recipient = recipient;
        this.type = type;
        this.text = text;
        this.url = url;
        this.seen = seen;
        this.creationTime = creationTime;

    }

    /**
     * Creates a new notification instance from the given data.
     * The constructor should only be used in testing. ID should be set automatically.
     * @param id the ID of the Message. This should be unique
     * @param recipient Represent the AppUser that the notification will be sent to
     * @param type Represents the type of notification
     * @param text Represents the content of the notification
     * @param url Represents the URL where the notification will redirect to in case if it is clicked
     * @param seen Flag represents if the notification have been viewed
     * @param creationTime Timestamp representing the time the message is created
     */
    public Notification(Long id, AppUser recipient, NotificationType type, String text, String url, boolean seen, Timestamp creationTime) {
        this(recipient, type, text, url, seen, creationTime);
        this.id = id;
    }

    /**
     * Creates a new notification instance from the given data.
     * The ID of the chat is set automatically.
     * The createdAt timestamp is set to the current timestamp in milliseconds.
     * @param recipient Represent the AppUser that the notification will be sent to
     * @param type Represents the type of notification
     * @param text Represents the content of the notification
     * @param url Represents the URL where the notification will redirect to in case if it is clicked
     * @param seen Flag represents if the notification have been viewed
     */
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
