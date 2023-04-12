package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

/**
 * This JobRequest class serves as the job application request.
 */
public class JobRequest {

    private AppUser creator;
    private String position;
    private String company;
    private String address;
    private String description;
    private JobType type;
    private int numAvailable;
    private boolean isExternal;
    private String externalLink;
    private boolean needResume;
    private boolean needCover;

    /**
     * Empty constructor.
     */
    public JobRequest() {}

    /**
     * Create a new job request given the inputs data.
     *
     * @param position the name of the position.
     * @param company the name of the company.
     * @param address the address of the workplace.
     * @param description the description of the job.
     * @param type fulltime, partime, contract, temporary, volunteer, internship, or other.
     * @param numAvailable the number of positions open.
     * @param isExternal true if the job application is posted externally; otherwise false.
     * @param externalLink the link of the external website.
     * @param needResume true if the job application requires a resume; otherwise false.
     * @param needCover true if the job application requires a cover letter; otherwise false.
     */
    public JobRequest(String position, String company, String address, String description, JobType type, int numAvailable, boolean isExternal, String externalLink, boolean needResume, boolean needCover) {
        this.position = position;
        this.company = company;
        this.address = address;
        this.description = description;
        this.type = type;
        this.numAvailable = numAvailable;
        this.isExternal = isExternal;
        this.externalLink = externalLink;
        this.needResume = needResume;
        this.needCover = needCover;
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

    public boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(boolean external) {
        isExternal = external;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public boolean getNeedResume() {
        return needResume;
    }

    public void setNeedResume(boolean needResume) {
        this.needResume = needResume;
    }

    public boolean getNeedCover() {
        return needCover;
    }

    public void setNeedCover(boolean needCover) {
        this.needCover = needCover;
    }
}
