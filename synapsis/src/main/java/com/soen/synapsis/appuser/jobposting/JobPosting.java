package com.soen.synapsis.appuser.jobposting;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;

public class JobPosting {

    private final AppUser creator;
    private String position;
    private String company;
    private String address;
    private String description;
    private JobType type;
    private int numAvailable;
    private int numApplicants;

    public JobPosting (AppUser creator, String position, String company, String address, String description, JobType type, int numAvailable, int numApplicants) {
        if (creator.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("This user is not a recruiter");
        }
        this.creator = creator;
        this.position = position;
        this.company = company;
        this.address = address;
        this.description = description;
        this.type = type;
        this.numAvailable = numAvailable;
        this.numApplicants = numApplicants;
    }

    public AppUser getCreator() {
        return creator;
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

    public int getNumApplicants() {
        return numApplicants;
    }

    public void setNumApplicants(int numApplicants) {
        this.numApplicants = numApplicants;
    }
}
