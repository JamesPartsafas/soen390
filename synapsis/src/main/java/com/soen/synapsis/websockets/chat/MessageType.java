package com.soen.synapsis.websockets.chat;

/**
 * An enumeration type that defines the different types of messages that can be sent.
 * TEXT: a normal text message.
 * READ: a message indicating that a previous message has been read.
 * ERROR: a message indicating that an error has occurred.
 */
public enum MessageType {
    TEXT,
    READ,
    ERROR
}
