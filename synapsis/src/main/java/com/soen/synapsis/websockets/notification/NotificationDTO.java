package com.soen.synapsis.websockets.notification;

/**
 * This class represents a notification data transfer object,
 * used to transfer notification data between the client and server.
 */
public class NotificationDTO {
    private Long id;
    private Long recipient_id;
    private NotificationType type;
    private String text;
    private String url;
    private boolean seen;

    /**
     * Default constructor
     * Assigns the default values to all the instance variables
     */
    public NotificationDTO() {
    }

    /**
     * Creates a new notification instance from the given data.
     * @param id The ID of the viewed notification
     * @param recipient_id The ID of the user that will be receiving the notification
     * @param type The type of the notification
     * @param text The content of the notification
     * @param url Represents the URL where the notification will redirect to in case if it is clicked
     * @param seen Flag represents if the notification have been viewed
     */
    public NotificationDTO(Long id, Long recipient_id, NotificationType type, String text, String url, boolean seen) {
        this.id = id;
        this.recipient_id = recipient_id;
        this.type = type;
        this.text = text;
        this.url = url;
        this.seen = seen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(Long recipient_id) {
        this.recipient_id = recipient_id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
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

    /**
     * Converts a Notification object to a NotificationDTO object.
     * @param notification the Notification object to be converted
     * @return a NotificationDTO object with the same data as the input Notification object
     */
    public static NotificationDTO notificationToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getRecipient().getId(),
                notification.getType(),
                notification.getText(),
                notification.getUrl(),
                notification.isSeen()
        );
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", recipient_id=" + recipient_id +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                ", seen=" + seen +
                '}';
    }
}
