package com.soen.synapsis.appuser.connection;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

@Entity
public class Connection {

    @EmbeddedId
    private ConnectionKey id;

    @ManyToOne
    @MapsId("requesterID")
    @JoinColumn(name = "requester_id")
    private AppUser requester;

    @ManyToOne
    @MapsId("receiverID")
    @JoinColumn(name = "receiver_id")
    private AppUser receiver;

    @Column(nullable = false)
    private boolean pending;

    protected Connection() {
    }

    public Connection(ConnectionKey id, AppUser requester, AppUser receiver, boolean pending) {
        this.id = id;
        this.requester = requester;
        this.receiver = receiver;
        this.pending = pending;
    }

    public ConnectionKey getId() {
        return id;
    }

    public void setId(ConnectionKey id) {
        this.id = id;
    }

    public AppUser getRequester() {
        return requester;
    }

    public void setRequester(AppUser requester) {
        this.requester = requester;
    }

    public AppUser getReceiver() {
        return receiver;
    }

    public void setReceiver(AppUser receiver) {
        this.receiver = receiver;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }
    
    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", requester=" + requester +
                ", receiver=" + receiver +
                ", pending=" + pending +
                '}';
    }
}
