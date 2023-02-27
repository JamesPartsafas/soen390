package com.soen.synapsis.websockets.chat;

public class MessageDTO {

    private Long id;
    private String content;
    private MessageType type;
    private Long receiverId;
    private Long senderId;

    public MessageDTO() {
    }

    public MessageDTO(Long id, String content, MessageType type, Long receiverId, Long senderId) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public MessageDTO(String content, MessageType type, Long receiverId, Long senderId) {
        this(0L, content, type, receiverId, senderId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", receiverEmail='" + receiverId + '\'' +
                ", senderEmail='" + senderId + '\'' +
                '}';
    }
}
