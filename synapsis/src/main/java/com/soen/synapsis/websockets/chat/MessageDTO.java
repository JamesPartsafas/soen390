package com.soen.synapsis.websockets.chat;

/**
 * This class represents a message data transfer object,
 * used to transfer message data between the client and server.
 */
public class MessageDTO {

    private Long id;
    private String content;
    private MessageType type;
    private Long receiverId;
    private Long senderId;
    private String fileName;
    private String file;

    /**
     * Default constructor
     * Assigns the default values to all the instance variables
     */
    public MessageDTO() {
    }

    /**
     * Creates a new chat instance from the given data.
     * @param id The ID of the read message
     * @param content The content of the message
     * @param type The type of the message
     * @param receiverId The ID of the user receiving the message
     * @param senderId The ID of the user sending the message
     * @param fileName The name of the file included in the message
     * @param file The base64 of the file included in the message
     */
    public MessageDTO(Long id, String content, MessageType type, Long receiverId, Long senderId, String fileName, String file) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.fileName = fileName;
        this.file = file;
    }

    /**
     * Creates a new chat instance from the given data.
     * Both file and filename are set to null.
     * @param id The ID of the read message
     * @param content The content of the message
     * @param type The type of the message
     * @param receiverId The ID of the user receiving the message
     * @param senderId The ID of the user sending the message
     */
    public MessageDTO(Long id, String content, MessageType type, Long receiverId, Long senderId) {
        this(id, content, type, receiverId, senderId, null, null);
    }

    /**
     * Creates a new chat instance from the given data.
     * The id is set to 0
     * Both file and filename are set to null
     * @param content The content of the message
     * @param type The type of the message
     * @param receiverId The ID of the user receiving the message
     * @param senderId The ID of the user sending the message
     */
    public MessageDTO(String content, MessageType type, Long receiverId, Long senderId) {
        this(0L, content, type, receiverId, senderId);
    }

    /**
     * Creates a new chat instance from the given data.
     * The id is set to 0
     * @param content The content of the message
     * @param type The type of the message
     * @param receiverId The ID of the user receiving the message
     * @param senderId The ID of the user sending the message
     * @param fileName The name of the file included in the message
     * @param file The base64 of the file included in the message
     */
    public MessageDTO(String content, MessageType type, Long receiverId, Long senderId, String fileName, String file) {
        this(0L, content, type, receiverId, senderId, fileName, file);
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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
