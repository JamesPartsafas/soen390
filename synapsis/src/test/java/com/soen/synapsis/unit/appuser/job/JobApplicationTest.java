package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.Job;
import com.soen.synapsis.appuser.job.JobApplication;
import com.soen.synapsis.appuser.job.JobApplicationStatus;
import com.soen.synapsis.appuser.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JobApplicationTest {

    private JobApplication underTest;
    private Long id;
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
    private String resume;
    private String coverLetter;
    private boolean isLegallyAllowedToWork;
    private String links;

    @BeforeEach
    void setUp() {
        AppUser applicantUser = new AppUser(1L, "Joe User", "1234", "joeuser@mail.com", Role.CANDIDATE);
        AppUser companyUser = new AppUser(2L, "Joe Company", "1234", "joecompany@mail.com", Role.COMPANY);

        id = 1L;
        job = new Job(companyUser, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, false, "", true, true);
        applicant = applicantUser;
        dateApplied = new Timestamp(System.currentTimeMillis());;
        status = JobApplicationStatus.SUBMITTED;
        email = "joueuser@mail.com";
        firstName = "Joe";
        lastName = "User";
        phone = "5144444444";
        address = "555 Joe Street";
        city = "Montreal";
        country = "Canada";
        resume = null;
        coverLetter = null;
        isLegallyAllowedToWork = true;
        links = "www.links.com";

        underTest = new JobApplication(id, job, applicant, dateApplied, status, email, firstName, lastName, phone, address, city, country, resume, coverLetter, isLegallyAllowedToWork, links);
    }

    @Test
    void getId() { assertEquals(id, underTest.getId()); }

    @Test
    void setId() {
        Long newID = 2L;
        underTest.setId(newID);
        assertEquals(newID, underTest.getId());
    }

    @Test
    void getJob() { assertEquals(job, underTest.getJob()); }

    @Test
    void setJob() {
        AppUser newCompanyUser = new AppUser(3L, "Joe NewCompany", "1234", "joenewcompany@mail.com", Role.COMPANY);
        Job newJob = new Job(newCompanyUser, "Mechanical Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, false, "", true, true);

        underTest.setJob(newJob);
        assertEquals(newJob, underTest.getJob());
    }

    @Test
    void getApplicant() { assertEquals(id, underTest.getId()); }

    @Test
    void setApplicant() {
        AppUser newApplicantUser = new AppUser(4L, "Joe NewUser", "1234", "joenewuser@mail.com", Role.CANDIDATE);

        underTest.setApplicant(newApplicantUser);
        assertEquals(newApplicantUser, underTest.getApplicant());
    }

    @Test
    void getDateApplied() { assertEquals(id, underTest.getId()); }

    @Test
    void setDateApplied() {
        Timestamp newDateApplied = new Timestamp(System.nanoTime());
        underTest.setDateApplied(newDateApplied);
        assertEquals(newDateApplied, underTest.getDateApplied());
    }

    @Test
    void getStatus() { assertEquals(status, underTest.getStatus()); }

    @Test
    void setStatus() {
        underTest.setStatus(JobApplicationStatus.HIRED);
        assertEquals(JobApplicationStatus.HIRED, underTest.getStatus());
    }

    @Test
    void getEmail() { assertEquals(email, underTest.getEmail()); }

    @Test
    void setEmail() {
        String newEmail = "newemail@mail.com";
        underTest.setEmail(newEmail);
        assertEquals(newEmail, underTest.getEmail());
    }

    @Test
    void getFirstName() { assertEquals(firstName, underTest.getFirstName()); }

    @Test
    void setFirstName() {
        String newFirstName = "John";
        underTest.setFirstName(newFirstName);
        assertEquals(newFirstName, underTest.getFirstName());
    }

    @Test
    void getLastName() { assertEquals(lastName, underTest.getLastName()); }

    @Test
    void setLastName() {
        String newLastName = "Doe";
        underTest.setLastName(newLastName);
        assertEquals(newLastName, underTest.getLastName());
    }

    @Test
    void getPhone() { assertEquals(id, underTest.getId()); }

    @Test
    void setPhone() {
        String newPhone = "5145555555";
        underTest.setPhone(newPhone);
        assertEquals(newPhone, underTest.getPhone());
    }

    @Test
    void getAddress() { assertEquals(address, underTest.getAddress()); }

    @Test
    void setAddress() {
        String newAddress = "123 Street";
        underTest.setAddress(newAddress);
        assertEquals(newAddress, underTest.getAddress());
    }

    @Test
    void getCity() { assertEquals(city, underTest.getCity()); }

    @Test
    void setCity() {
        String newCity = "New York";
        underTest.setCity(newCity);
        assertEquals(newCity, underTest.getCity());
    }

    @Test
    void getCountry() { assertEquals(country, underTest.getCountry()); }

    @Test
    void setCountry() {
        String newCountry = "United States";
        underTest.setCountry(newCountry);
        assertEquals(newCountry, underTest.getCountry());
    }

    @Test
    void getResume() { assertEquals(resume, underTest.getResume()); }

    @Test
    void setResume() {
        String newResume = "1234";
        underTest.setResume(newResume);
        assertEquals(newResume, underTest.getResume());
    }

    @Test
    void getCoverLetter() { assertEquals(coverLetter, underTest.getCoverLetter()); }

    @Test
    void setCoverLetter() {
        String newCoverLetter = "1234";
        underTest.setCoverLetter(newCoverLetter);
        assertEquals(newCoverLetter, underTest.getCoverLetter());
    }

    @Test
    void isLegallyAllowedToWork() { assertEquals(isLegallyAllowedToWork, underTest.isLegallyAllowedToWork()); }

    @Test
    void setLegallyAllowedToWork() {
        boolean isNotLegallyAllowedToWork = false;
        underTest.setLegallyAllowedToWork(isNotLegallyAllowedToWork);
        assertEquals(isNotLegallyAllowedToWork, underTest.isLegallyAllowedToWork());
    }

    @Test
    void getLinks() { assertEquals(id, underTest.getId()); }

    @Test
    void setLinks() {
        String newLinks = "new links";
        underTest.setLinks(newLinks);
        assertEquals(newLinks, underTest.getLinks());
    }

    @Test
    void testToString()  {
        assertNotNull(underTest.toString());
    }

}
