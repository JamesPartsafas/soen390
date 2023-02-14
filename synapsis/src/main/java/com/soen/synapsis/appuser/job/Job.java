package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private AppUser creator;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private JobType type;

    @Column(nullable = false)
    private int numAvailable;

    @Column(nullable = false)
    private int numApplicants;

    protected Job() {}

    public Job(AppUser creator, String position, String company, String address, String description, JobType type, int numAvailable) {
        this.creator = creator;
        this.position = position;
        this.company = company;
        this.address = address;
        this.description = description;
        this.type = type;
        this.numAvailable = numAvailable;
        this.numApplicants = 0;
    }

    public Long getID() {
        return id;
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

    public String getDescription() { return description; }

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
