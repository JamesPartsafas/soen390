package com.soen.synapsis.appuser.settings;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * Contains user settings data.
 * All AppUsers have a settings entity which is
 * mapped through one-to-one relationship.
 */
@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(columnDefinition = "boolean default true")
    private boolean jobEmailNotificationsOn;

    @Column(columnDefinition = "boolean default true")
    private boolean messageEmailNotificationsOn;

    @Column(columnDefinition = "boolean default true")
    private boolean connectionEmailNotificationsOn;

    protected Settings() {
    }

    public Settings(Long id, AppUser appUser, boolean jobEmailNotificationsOn, boolean messageEmailNotificationsOn, boolean connectionEmailNotificationsOn) {
        this.id = id;
        this.appUser = appUser;
        this.jobEmailNotificationsOn = jobEmailNotificationsOn;
        this.messageEmailNotificationsOn = messageEmailNotificationsOn;
        this.connectionEmailNotificationsOn = connectionEmailNotificationsOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public boolean isJobEmailNotificationsOn() {
        return jobEmailNotificationsOn;
    }

    public void setJobEmailNotificationsOn(boolean emailNotificationsOn) {
        this.jobEmailNotificationsOn = emailNotificationsOn;
    }

    public boolean isMessageEmailNotificationsOn() {
        return messageEmailNotificationsOn;
    }

    public void setMessageEmailNotificationsOn(boolean messageEmailNotificationsOn) {
        this.messageEmailNotificationsOn = messageEmailNotificationsOn;
    }

    public boolean isConnectionEmailNotificationsOn() {
        return connectionEmailNotificationsOn;
    }

    public void setConnectionEmailNotificationsOn(boolean connectionEmailNotificationsOn) {
        this.connectionEmailNotificationsOn = connectionEmailNotificationsOn;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", appUser=" + appUser +
                ", jobEmailNotificationsOn=" + jobEmailNotificationsOn +
                ", messageEmailNotificationsOn=" + messageEmailNotificationsOn +
                ", connectionEmailNotificationsOn=" + connectionEmailNotificationsOn +
                '}';
    }
}
