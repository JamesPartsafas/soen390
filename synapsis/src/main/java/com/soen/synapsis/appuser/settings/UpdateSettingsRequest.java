package com.soen.synapsis.appuser.settings;

/**
 * Request object containing data for changes to existing settings entity
 */
public class UpdateSettingsRequest {
    private boolean jobEmailNotificationsOn;
    private boolean messageEmailNotificationsOn;
    private boolean connectionEmailNotificationsOn;

    public UpdateSettingsRequest() {
        this.jobEmailNotificationsOn = false;
        this.messageEmailNotificationsOn = false;
        this.connectionEmailNotificationsOn = false;
    }

    public UpdateSettingsRequest(boolean jobEmailNotificationsOn, boolean messageEmailNotificationsOn, boolean connectionEmailNotificationsOn) {
        this.jobEmailNotificationsOn = jobEmailNotificationsOn;
        this.messageEmailNotificationsOn = messageEmailNotificationsOn;
        this.connectionEmailNotificationsOn = connectionEmailNotificationsOn;
    }

    public boolean isJobEmailNotificationsOn() {
        return jobEmailNotificationsOn;
    }

    public void setJobEmailNotificationsOn(boolean jobEmailNotificationsOn) {
        this.jobEmailNotificationsOn = jobEmailNotificationsOn;
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
        return "UpdateSettingsRequest{" +
                "jobEmailNotificationsOn=" + jobEmailNotificationsOn +
                ", messageEmailNotificationsOn=" + messageEmailNotificationsOn +
                ", connectionEmailNotificationsOn=" + connectionEmailNotificationsOn +
                '}';
    }
}
