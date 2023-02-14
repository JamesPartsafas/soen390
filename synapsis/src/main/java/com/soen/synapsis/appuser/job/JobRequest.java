package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

public class JobRequest {

    private AppUser creator;
    private String position;
    private String company;
    private String address;
    private String description;
    private JobType type;
    private int numAvailable;

    public JobRequest() {}

    public JobRequest(String position, String company, String address, String description, JobType type, int numAvailable) {
        this.position = position;
        this.company = company;
        this.address = address;
        this.description = description;
        this.type = type;
        this.numAvailable = numAvailable;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser creator) {
        this.creator = creator;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }
}
