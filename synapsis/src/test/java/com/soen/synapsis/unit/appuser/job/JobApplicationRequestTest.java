package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobApplicationRequestTest {

    private JobApplicationRequest underTest;
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
    @BeforeEach
    void setUp() {
        job = new Job(new AppUser(10L, "joe", "1234", "joecreator@mail.com", Role.RECRUITER, AuthProvider.LOCAL), "Software Engineer", new AppUser(1L, "joecompany", "1234", "joecompanyunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL), "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        applicant = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        dateApplied = new Timestamp(System.currentTimeMillis());
        status = JobApplicationStatus.SUBMITTED;
        email = "joeunittest@mail.com";
        firstName = "Joe";
        lastName = "Lean";
        phone = "5149948109";
        address = "1 Synapsis Street, Montreal, QC, Canada";
        city = "Montreal";
        country = "Canada";
        legallyAllowedToWork = true;
        links = "random";
        underTest = new JobApplicationRequest(job, applicant, dateApplied, status, email, firstName, lastName ,phone, address, city, country, legallyAllowedToWork,links);
        underTest.setJob(job);
    }

    @Test
    void getJob() {
        assertEquals(job, underTest.getJob());
    }

    @Test
    void setJob() {
        Job newJob = new Job(new AppUser(5L, "joe", "1234", "joecreator@mail.com", Role.RECRUITER, AuthProvider.LOCAL), "Software Engineer", new AppUser(1L, "joecompany", "1234", "joecompanyunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL), "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        underTest.setJob(newJob);
        assertEquals(newJob, underTest.getJob());
    }

    @Test
    void getApplicant() {
        assertEquals(applicant, underTest.getApplicant());
    }

    @Test
    void setApplicant() {
        AppUser newApplicant = new AppUser(4L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        underTest.setApplicant(newApplicant);
        assertEquals(newApplicant, underTest.getApplicant());
    }

    @Test
    void getDateApplied() {
        assertEquals(dateApplied, underTest.getDateApplied());
    }

    @Test
    void setDateApplied() {
        Timestamp newDateApplied = new Timestamp(System.currentTimeMillis() + System.currentTimeMillis());
        underTest.setDateApplied(newDateApplied);
        assertEquals(newDateApplied, underTest.getDateApplied());
    }

    @Test
    void getStatus() {
        assertEquals(status, underTest.getStatus());
    }

    @Test
    void setStatus() {
        JobApplicationStatus newJobApplicationStatus = JobApplicationStatus.HIRED;
        underTest.setStatus(newJobApplicationStatus);
        assertEquals(newJobApplicationStatus, underTest.getStatus());
    }

    @Test
    void getEmail() {
        assertEquals(email, underTest.getEmail());
    }

    @Test
    void setEmail() {
        String newEmail = "joeuser@mail.com";
        underTest.setEmail(newEmail);
        assertEquals(newEmail, underTest.getEmail());
    }

    @Test
    void getFirstName() {
        assertEquals(firstName, underTest.getFirstName());
    }

    @Test
    void setFirstName() {
        String newFirstName = "ZiHao";
        underTest.setFirstName(newFirstName);
        assertEquals(newFirstName, underTest.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals(lastName, underTest.getLastName());
    }

    @Test
    void setLastName() {
        String newLastName = "Tan";
        underTest.setLastName(newLastName);
        assertEquals(newLastName, underTest.getLastName());
    }

    @Test
    void getPhone() {
        assertEquals(phone, underTest.getPhone());
    }

    @Test
    void setPhone() {
        String newPhone = "5144868109";
        underTest.setPhone(newPhone);
        assertEquals(newPhone, underTest.getPhone());
    }

    @Test
    void getAddress() {
        assertEquals(address, underTest.getAddress());
    }

    @Test
    void setAddress() {
        String newAddress = "2 Synapsis Street, Montreal, QC, Canada";
        underTest.setAddress(newAddress);
        assertEquals(newAddress, underTest.getAddress());
    }

    @Test
    void getCity() {
        assertEquals(city, underTest.getCity());
    }

    @Test
    void setCity() {
        String newCity = "Toronto";
        underTest.setCity(newCity);
        assertEquals(newCity, underTest.getCity());
    }

    @Test
    void getCountry() {
        assertEquals(country, underTest.getCountry());
    }

    @Test
    void setCountry() {
        String newCountry = "China";
        underTest.setCountry(newCountry);
        assertEquals(newCountry, underTest.getCountry());
    }

    @Test
    void isLegallyAllowedToWork() {

        assertEquals(legallyAllowedToWork, underTest.isLegallyAllowedToWork());
    }

    @Test
    void setLegallyAllowedToWork() {
        boolean newLegallyAllowedToWorkState = false;
        underTest.setLegallyAllowedToWork(newLegallyAllowedToWorkState);
        assertEquals(newLegallyAllowedToWorkState, underTest.isLegallyAllowedToWork());
    }
    @Test
    void getLinks() {
        assertEquals(links, underTest.getLinks());
    }
    @Test
    void setLinks() {
        String newLink = "random1";
        underTest.setLinks(newLink);
        assertEquals(newLink, underTest.getLinks());
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
