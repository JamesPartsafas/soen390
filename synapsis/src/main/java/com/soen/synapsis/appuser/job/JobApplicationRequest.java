package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;

import javax.persistence.*;
import java.sql.Timestamp;

public class JobApplicationRequest {
    private Job job;
    private AppUser applicant;
    private Timestamp dateApplied;
    private JobApplicationStatus status;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String country;
    private boolean legallyAllowedToWork;
    private String links;

    public JobApplicationRequest() {

    }

    public JobApplicationRequest(Job job, AppUser applicant, Timestamp dateApplied, JobApplicationStatus status, String email, String firstName, String lastName, String phone, String address, String city, String country, boolean legallyAllowedToWork, String links) {
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
        return "JobApplicationRequest{" +
                "job=" + job +
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
                ", legallyAllowedToWork=" + legallyAllowedToWork +
                ", links='" + links + '\'' +
                '}';
    }
}
