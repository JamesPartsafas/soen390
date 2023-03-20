package com.soen.synapsis.websockets.chat.message;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.websockets.chat.Chat;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Contains data on all the messages including the related chat
 */
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
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String file;

    @Column(nullable = false)
    private Timestamp createdAt;

    /**
     * Default Constructor.
     * Assigns the default values to all the instance variables
     */
    public Message() {
    }

    /**
     * Creates a new message instance from the given data.
     * The constructor should only be used in testing. ID should be set automatically.
     * @param id the ID of the Message. This should be unique
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param read a flag to indicate whether the receiver viewed the message
     * @param createdAt a timestamp representing the time the message is created
     * @param fileName The name of the file included in the message
     * @param file The base64 of the file included in the message
     */
    public Message(Long id, Chat chat, String content, AppUser sender, boolean read, ReportStatus reportStatus, Timestamp createdAt, String fileName, String file) {
        this(chat, content, sender, read, reportStatus, createdAt, fileName, file);
        this.id = id;
    }

    /**
     * Creates a new message instance from the given data.
     * The constructor should only be used in testing. ID should be set automatically.
     * Both file and filename are set to null
     * @param id the ID of the Message. This should be unique
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param read a flag to indicate whether the receiver viewed the message
     * @param createdAt a timestamp representing the time the message is created
     */
    public Message(Long id, Chat chat, String content, AppUser sender, boolean read, ReportStatus reportStatus, Timestamp createdAt) {
        this(chat, content, sender, read, reportStatus, createdAt, null, null);
        this.id = id;
    }

    /**
     * Creates a new message instance from the given data.
     * The constructor should only be used in testing. ID should be set automatically.
     * The reportStatus is set to UNREPORTED
     * Both file and filename are set to null
     * @param id the ID of the Message. This should be unique
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param read a flag to indicate whether the receiver viewed the message
     * @param createdAt a timestamp representing the time the message is created
     */
    public Message(Long id, Chat chat, String content, AppUser sender, boolean read, Timestamp createdAt) {
        this(chat, content, sender, read, ReportStatus.UNREPORTED, createdAt, null, null);
        this.id = id;
    }


    /**
     * Creates a new message instance from the given data.
     * The ID of the message is set automatically.
     * Both file and filename are set to null
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param read a flag to indicate whether the receiver viewed the message
     * @param reportStatus an indicator whether the message has been reported, unreported or reviewed
     * @param createdAt a timestamp representing the time the message is created
     */
    public Message(Chat chat, String content, AppUser sender, boolean read, ReportStatus reportStatus, Timestamp createdAt) {
        this(0L, chat, content, sender, read, reportStatus, createdAt, null, null);
    }

    /**
     * Creates a new message instance from the given data.
     * The ID of the message is set automatically.
     * The read flag is set to false.
     * The reportStatus is set to UNREPORTED
     * The createdAt timestamp is set to the current timestamp in milliseconds.
     * Both file and filename are set to null
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     */
    public Message(Chat chat, String content, AppUser sender) {
        this(chat, content, sender, false, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()), null, null);
    }

    /**
     * Creates a new message instance from the given data.
     * The ID of the message is set automatically.
     * The read flag is set to false.
     * The reportStatus is set to UNREPORTED
     * The createdAt timestamp is set to the current timestamp in milliseconds.
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param fileName The name of the file included in the message
     * @param file The base64 of the file included in the message
     */
    public Message(Chat chat, String content, AppUser sender, String fileName, String file) {
        this(chat, content, sender, false, ReportStatus.UNREPORTED, new Timestamp(System.currentTimeMillis()), fileName, file);
    }

    /**
     * Creates a new message instance from the given data.
     * The ID of the message is set automatically.
     * @param chat the instance of the Chat that the message is related to
     * @param content the content of the message in a string format
     * @param sender the instance of the sender user
     * @param read a flag to indicate whether the receiver viewed the message
     * @param reportStatus an indicator whether the message has been reported, unreported or reviewed
     * @param createdAt a timestamp representing the time the message is created
     * @param fileName The name of the file included in the message
     * @param file The base64 of the file included in the message
     */
    public Message(Chat chat, String content, AppUser sender, boolean read, ReportStatus reportStatus, Timestamp createdAt, String fileName, String file) {
        this.chat = chat;
        this.content = content;
        this.sender = sender;
        this.read = read;
        this.reportStatus = reportStatus;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.file = file;
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

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
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
