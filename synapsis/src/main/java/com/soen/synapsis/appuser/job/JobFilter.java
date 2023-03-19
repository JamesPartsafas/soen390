package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

@Entity
public class JobFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @Column(nullable = false)
    private JobType jobType;

    @Column(nullable = false)
    private boolean showInternalJobs;

    @Column(nullable = false)
    private boolean showExternalJobs;

    protected JobFilter() {}

    public JobFilter(Long id, AppUser appUser, JobType jobType, boolean showInternalJobs, boolean showExternalJobs) {
        this.id = id;
        this.appUser = appUser;
        this.jobType = jobType;
        this.showInternalJobs = showInternalJobs;
        this.showExternalJobs = showExternalJobs;
    }

    public JobFilter(AppUser appUser, JobType jobType, boolean showInternalJobs, boolean showExternalJobs) {
        this.appUser = appUser;
        this.jobType = jobType;
        this.showInternalJobs = showInternalJobs;
        this.showExternalJobs = showExternalJobs;
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

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public boolean isShowInternalJobs() {
        return showInternalJobs;
    }

    public void setShowInternalJobs(boolean showInternalJobs) {
        this.showInternalJobs = showInternalJobs;
    }

    public boolean isShowExternalJobs() {
        return showExternalJobs;
    }

    public void setShowExternalJobs(boolean showExternalJobs) {
        this.showExternalJobs = showExternalJobs;
    }

    @Override
    public String toString() {
        return "JobFilter{" +
                "id=" + id +
                ", appUser=" + appUser +
                ", jobType=" + jobType +
                ", showExternalJobs=" + showExternalJobs +
                ", showInternalJobs=" + showInternalJobs +
                '}';
    }

}
