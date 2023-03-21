package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * This Job Application class serves as the entity to store job applications.
 */
@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AppUser applicant;

    @Column(nullable = false)
    private Timestamp dateApplied;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobApplicationStatus status;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "TEXT")
    private String resume;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Column(nullable = false)
    private boolean legallyAllowedToWork;

    @Column(nullable = true, columnDefinition="TEXT")
    private String links;

    /**
     * Empty constructor.
     */
    public JobApplication() {

    }

    /**
     * Create a job application given the inputs data.
     *
     * @param id unique id of the job application
     * @param job the job that the job application is referring to
     * @param applicant the user creating the job application
     * @param dateApplied the date at which the job application was created
     * @param status submitted, in review, selected, interviewed, offered, hired, rejected
     * @param email the email of the user submitting the job application
     * @param firstName the first name of the user submitting the job application
     * @param lastName the last name of the user submitting the job application
     * @param phone the phone number of the user submitting the job application
     * @param address the address of the user submitting the job application
     * @param city the city of the user submitting the job application
     * @param country the country of the user submitting the job application
     * @param resume the resume of the user submitting the job application
     * @param coverLetter the cover letter of the user submitting the job application
     * @param legallyAllowedToWork true if the user is allowed to work in Canada; otherwise false
     * @param links any additional links from the user submitting the job application
     */
    public JobApplication(Long id, Job job, AppUser applicant, Timestamp dateApplied, JobApplicationStatus status, String email, String firstName, String lastName, String phone, String address, String city, String country, String resume, String coverLetter, boolean legallyAllowedToWork, String links) {
        this.id = id;
        this.job = job;
        this.applicant = applicant;
        this.dateApplied = dateApplied;
        this.status = status;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.resume = resume;
        this.coverLetter = coverLetter;
        this.legallyAllowedToWork = legallyAllowedToWork;
        this.links = links;
    }

    /**
     * Create a job application given the inputs data.
     *
     * @param job the job that the job application is referring to
     * @param applicant the user creating the job application
     * @param dateApplied the date at which the job application was created
     * @param status submitted, in review, selected, interviewed, offered, hired, rejected
     * @param email the email of the user submitting the job application
     * @param firstName the first name of the user submitting the job application
     * @param lastName the last name of the user submitting the job application
     * @param phone the phone number of the user submitting the job application
     * @param address the address of the user submitting the job application
     * @param city the city of the user submitting the job application
     * @param country the country of the user submitting the job application
     * @param resume the resume of the user submitting the job application
     * @param coverLetter the cover letter of the user submitting the job application
     * @param legallyAllowedToWork true if the user is allowed to work in Canada; otherwise false
     * @param links any additional links from the user submitting the job application
     */
    public JobApplication(Job job, AppUser applicant, Timestamp dateApplied, JobApplicationStatus status, String email, String firstName, String lastName, String phone, String address, String city, String country, String resume, String coverLetter, boolean legallyAllowedToWork, String links) {
        this.job = job;
        this.applicant = applicant;
        this.dateApplied = dateApplied;
        this.status = status;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.resume = resume;
        this.coverLetter = coverLetter;
        this.legallyAllowedToWork = legallyAllowedToWork;
        this.links = links;
    }

    /**
     * Create a new job application from the inputs data.
     *
     * @param job the job that the job application is referring to
     * @param applicant the user creating the job application
     * @param dateApplied the date at which the job application was created
     * @param status submitted, in review, selected, interviewed, offered, hired, rejected
     * @param email the email of the user submitting the job application
     * @param firstName the first name of the user submitting the job application
     * @param lastName the last name of the user submitting the job application
     * @param phone the phone number of the user submitting the job application
     * @param address the address of the user submitting the job application
     * @param city the city of the user submitting the job application
     * @param country the country of the user submitting the job application
     * @param legallyAllowedToWork true if the user is allowed to work in Canada; otherwise false
     * @param links any additional links from the user submitting the job application
     */
    public JobApplication(Job job, AppUser applicant, Timestamp dateApplied, JobApplicationStatus status, String email, String firstName, String lastName, String phone, String address, String city, String country, boolean legallyAllowedToWork, String links) {
        this.job = job;
        this.applicant = applicant;
        this.dateApplied = dateApplied;
        this.status = status;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.legallyAllowedToWork = legallyAllowedToWork;
        this.links = links;
    }

    /**
     * Create a job application given the inputs data.
     *
     * @param job the job that the job application is referring to
     * @param applicant the user creating the job application
     * @param dateApplied the date at which the job application was created
     * @param status submitted, in review, selected, interviewed, offered, hired, rejected
     * @param email the email of the user submitting the job application
     * @param firstName the first name of the user submitting the job application
     * @param lastName the last name of the user submitting the job application
     * @param phone the phone number of the user submitting the job application
     * @param address the address of the user submitting the job application
     * @param city the city of the user submitting the job application
     * @param country the country of the user submitting the job application
     * @param legallyAllowedToWork true if the user is allowed to work in Canada; otherwise false
     */
    public JobApplication(Job job, AppUser applicant, Timestamp dateApplied, JobApplicationStatus status, String email, String firstName, String lastName, String phone, String address, String city, String country, boolean legallyAllowedToWork) {
        this.job = job;
        this.applicant = applicant;
        this.dateApplied = dateApplied;
        this.status = status;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.country = country;
        this.legallyAllowedToWork = legallyAllowedToWork;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public AppUser getApplicant() {
        return applicant;
    }

    public void setApplicant(AppUser applicant) {
        this.applicant = applicant;
    }

    public Timestamp getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Timestamp dateApplied) {
        this.dateApplied = dateApplied;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public boolean isLegallyAllowedToWork() {
        return legallyAllowedToWork;
    }

    public void setLegallyAllowedToWork(boolean legallyAllowedToWork) {
        this.legallyAllowedToWork = legallyAllowedToWork;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", job=" + job +
                ", applicant=" + applicant +
                ", dateApplied=" + dateApplied +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", resume=" + resume +
                ", coverLetter=" + coverLetter +
                ", legallyAllowedToWork=" + legallyAllowedToWork +
                ", links='" + links + '\'' +
                '}';
    }
}
