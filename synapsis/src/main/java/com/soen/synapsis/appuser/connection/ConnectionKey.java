package com.soen.synapsis.appuser.connection;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ConnectionKey implements Serializable {
    @Column(name = "requester_id")
    private Long requesterID;

    @Column(name = "receiver_id")
    private Long receiverID;

    public ConnectionKey(Long requesterID, Long receiverID) {
        this.requesterID = requesterID;
        this.receiverID = receiverID;
    }

    protected ConnectionKey() {
    }

    public Long getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(Long requesterID) {
        this.requesterID = requesterID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    @Override
    public String toString() {
        return "ConnectionKey{" +
                "requesterID=" + requesterID +
                ", receiverID=" + receiverID +
                '}';
    }
}
