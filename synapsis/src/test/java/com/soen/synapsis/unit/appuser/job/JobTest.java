package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.Job;
import com.soen.synapsis.appuser.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobTest {

    private Long id;
    private Job undertest;
    private AppUser creator;
    private String position;
    private String company;
    private String address;
    private String description;
    private JobType type;
    private int numAvailable;
    private int numApplicants;

    @BeforeEach
    void setUp() {
        id = 15L;
        creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        position = "Software Engineer";
        company = "Synapsis";
        address = "1 Synapsis Street, Montreal, QC, Canada";
        description = "Sample Description";
        type = JobType.FULLTIME;
        numAvailable = 5;
        numApplicants = 0;
        undertest = new Job(creator, position, company, address, description, type, numAvailable);
        undertest.setID(id);
    }

    @Test
    void getID() {
        assertEquals(id, undertest.getID());
    }

    @Test
    void setID() {
        Long newID = 10L;
        undertest.setID(newID);
        assertEquals(newID, undertest.getID());
    }

    @Test
    void getCreator() {
        assertEquals(creator, undertest.getCreator());
    }

    @Test
    void getPosition() {
        assertEquals(position, undertest.getPosition());
    }

    @Test
    void setPosition() {
        String newPosition = "Quality Assurance";
        undertest.setPosition(newPosition);
        assertEquals(newPosition, undertest.getPosition());
    }

    @Test
    void getCompany() {
        assertEquals(company, undertest.getCompany());
    }

    @Test
    void setCompany() {
        String newCompany = "Amaznot";
        undertest.setCompany(newCompany);
        assertEquals(newCompany, undertest.getCompany());
    }

    @Test
    void getAddress() {
        assertEquals(address, undertest.getAddress());
    }

    @Test
    void setAddress() {
        String newAddress = "2 Amaznot Street, Toronto, ON, Canada";
        undertest.setAddress(newAddress);
        assertEquals(newAddress, undertest.getAddress());
    }

    @Test
    void getDescription() {
        assertEquals(address, undertest.getAddress());
    }

    @Test
    void setDescription() {
        String newDescription = "New Sample Description";
        undertest.setDescription(newDescription);
        assertEquals(newDescription, undertest.getDescription());
    }

    @Test
    void getType() {
        assertEquals(type, undertest.getType());
    }

    @Test
    void setType() {
        JobType newType = JobType.PARTTIME;
        undertest.setType(newType);
        assertEquals(newType, undertest.getType());
    }

    @Test
    void getNumAvailable() {
        assertEquals(numAvailable, undertest.getNumAvailable());
    }

    @Test
    void setNumAvailable() {
        int newNumAvailable = 2;
        undertest.setNumAvailable(newNumAvailable);
        assertEquals(newNumAvailable, undertest.getNumAvailable());
    }

    @Test
    void getNumApplicants() {
        assertEquals(numApplicants, undertest.getNumApplicants());
    }

    @Test
    void setNumApplicants() {
        int newNumApplicants = 13;
        undertest.setNumApplicants(newNumApplicants);
        assertEquals(newNumApplicants, undertest.getNumApplicants());
    }
}