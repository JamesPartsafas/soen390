package com.soen.synapsis.websockets.notification;

/**
 * An enumeration of the possible types of notifications that can be sent.
 * MESSAGE: a notification for a message update.
 * CONNECTION: a notification for a connection update.
 * JOB: a notification for a job update.
 */
public enum NotificationType {
    MESSAGE,
    REQUEST_CON,
    ACCEPT_CON,
    JOB
}
