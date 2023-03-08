package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;

/**
 * This Job class serves as the entity to store job postings.
 */
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

    @Column(nullable = false, columnDefinition="TEXT")
    private String description;

    @Column(nullable = false)
    private JobType type;

    @Column(name = "num_available", nullable = false)
    private int numAvailable;

    @Column(name = "num_applicants", nullable = false)
    private int numApplicants;

    @Column(name = "is_external", nullable = false)
    private boolean isExternal;

    @Column(name = "external_link")
    private String externalLink;

    @Column(name = "need_resume", nullable = false)
    private boolean needResume;

    @Column(name = "need_cover", nullable = false)
    private boolean needCover;

    @Column(name = "need_portfolio", nullable = false)
    private boolean needPortfolio;

    /**
     * Empty constructor.
     */
    protected Job() {}

    /**
     * Create a new job posting given the inputs data.
     *
     * @param creator the recruiter who creates the job.
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
     * @param needPortfolio true if the job application requires a portfolio; otherwise false.
     */
    public Job(AppUser creator, String position, String company, String address, String description, JobType type, int numAvailable, boolean isExternal, String externalLink, boolean needResume, boolean needCover, boolean needPortfolio) {
        this.creator = creator;
        this.position = position;
        this.company = company;
        this.address = address;
        this.description = description;
        this.type = type;
        this.numAvailable = numAvailable;
        this.numApplicants = 0;
        this.isExternal = isExternal;
        this.externalLink = externalLink;
        this.needResume = needResume;
        this.needCover = needCover;
        this.needPortfolio = needPortfolio;
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", company='" + company + '\'' +
                ", address=" + address +
                ", description=" + description +
                ", type=" + type +
                ", numAvailable=" + numAvailable +
                ", numApplicants=" + numApplicants +
                '}';
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

    public boolean getNeedPortfolio() {
        return needPortfolio;
    }

    public void setNeedPortfolio(boolean needPortfolio) {
        this.needPortfolio = needPortfolio;
    }
}
