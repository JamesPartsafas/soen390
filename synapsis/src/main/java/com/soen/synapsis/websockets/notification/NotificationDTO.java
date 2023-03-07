package com.soen.synapsis.websockets.notification;

public class NotificationDTO {
    private Long id;
    private Long recipient_id;
    private NotificationType type;
    private String text;
    private String url;
    private boolean seen;

    public NotificationDTO() {
    }

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
