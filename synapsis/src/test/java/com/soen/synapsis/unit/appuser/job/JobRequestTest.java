package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.JobRequest;
import com.soen.synapsis.appuser.job.JobType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobRequestTest {

    private JobRequest underTest;
    private AppUser creator;
    private String position;
    private String company;
    private String address;
    private String description;
    private JobType type;
    private int numAvailable;

    @BeforeEach
    void setUp() {
        creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        position = "Software Engineer";
        company = "Synapsis";
        address = "1 Synapsis Street, Montreal, QC, Canada";
        description = "Sample Description";
        type = JobType.FULLTIME;
        numAvailable = 5;
        underTest = new JobRequest(position, company, address, description, type, numAvailable, true, "");
        underTest.setCreator(creator);
    }

    @Test
    void getCreator() {
        assertEquals(creator, underTest.getCreator());
    }

    @Test
    void setCreator() {
        AppUser newCreator = new AppUser(5L, "jane", "4321", "janeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        underTest.setCreator(newCreator);
        assertEquals(newCreator, underTest.getCreator());
    }

    @Test
    void getPosition() {
        assertEquals(position, underTest.getPosition());
    }

    @Test
    void setPosition() {
        String newPosition = "Quality Assurance";
        underTest.setPosition(newPosition);
        assertEquals(newPosition, underTest.getPosition());
    }

    @Test
    void getCompany() {
        assertEquals(company, underTest.getCompany());
    }

    @Test
    void setCompany() {
        String newCompany = "Amaznot";
        underTest.setCompany(newCompany);
        assertEquals(newCompany, underTest.getCompany());
    }

    @Test
    void getAddress() {
        assertEquals(address, underTest.getAddress());
    }

    @Test
    void setAddress() {
        String newAddress = "2 Amaznot Street, Toronto, ON, Canada";
        underTest.setAddress(newAddress);
        assertEquals(newAddress, underTest.getAddress());
    }

    @Test
    void getDescription() {
        assertEquals(address, underTest.getAddress());
    }

    @Test
    void setDescription() {
        String newDescription = "New Sample Description";
        underTest.setDescription(newDescription);
        assertEquals(newDescription, underTest.getDescription());
    }

    @Test
    void getType() {
        assertEquals(type, underTest.getType());
    }

    @Test
    void setType() {
        JobType newType = JobType.PARTTIME;
        underTest.setType(newType);
        assertEquals(newType, underTest.getType());
    }

    @Test
    void getNumAvailable() {
        assertEquals(numAvailable, underTest.getNumAvailable());
    }

    @Test
    void setNumAvailable() {
        int newNumAvailable = 2;
        underTest.setNumAvailable(newNumAvailable);
        assertEquals(newNumAvailable, underTest.getNumAvailable());
    }
}