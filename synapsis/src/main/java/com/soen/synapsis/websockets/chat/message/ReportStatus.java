package com.soen.synapsis.websockets.chat.message;

/**
 * The ReportStatus enum represents the different statuses a message can have.
 * REPORTED: means the message has been submitted for admin review
 * UNREPORTED: means the message has not been submitted for admin review
 * REVIEWED: means the message has been reviewed by an admin.
 */
public enum ReportStatus {
    REPORTED,
    UNREPORTED,
    REVIEWED
}
