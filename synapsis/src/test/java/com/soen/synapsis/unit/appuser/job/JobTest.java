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
    private boolean isExternal;
    private String externalLink;
    private boolean needResume;
    private boolean needCover;
    private boolean needPortfolio;

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
        isExternal = false;
        externalLink = "";
        needResume = true;
        needCover = true;
        needPortfolio = true;
        undertest = new Job(creator, position, company, address, description, type, numAvailable, isExternal, externalLink, needResume, needCover, needPortfolio);
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

    @Test
    void getIsExternal() {
        assertEquals(isExternal, undertest.getIsExternal());
    }

    @Test
    void setIsExternal() {
        boolean newIsExternal = true;
        undertest.setIsExternal(newIsExternal);
        assertEquals(newIsExternal, undertest.getIsExternal());
    }

    @Test
    void getExternalLink() {
        assertEquals(externalLink, undertest.getExternalLink());
    }

    @Test
    void setExternalLink() {
        String newExternalLink = "https://www.google.com/";
        undertest.setExternalLink(newExternalLink);
        assertEquals(newExternalLink, undertest.getExternalLink());
    }

    @Test
    void getNeedResume() {
        assertEquals(needResume, undertest.getNeedResume());
    }

    @Test
    void setNeedResume() {
        boolean newNeedResume = false;
        undertest.setNeedResume(newNeedResume);
        assertEquals(newNeedResume, undertest.getNeedResume());
    }

    @Test
    void getNeedCover() {
        assertEquals(needCover, undertest.getNeedCover());
    }

    @Test
    void setNeedCover() {
        boolean newNeedCover = false;
        undertest.setNeedCover(newNeedCover);
        assertEquals(newNeedCover, undertest.getNeedCover());
    }

    @Test
    void getNeedPortfolio() {
        assertEquals(needCover, undertest.getNeedCover());
    }

    @Test
    void setNeedPortfolio() {
        boolean newNeedPortfolio = false;
        undertest.setNeedPortfolio(newNeedPortfolio);
        assertEquals(newNeedPortfolio, undertest.getNeedPortfolio());
    }

    @Test
    void testToString() {
        String s = "Job{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", company='" + company + '\'' +
                ", address=" + address +
                ", description=" + description +
                ", type=" + type +
                ", numAvailable=" + numAvailable +
                ", numApplicants=" + numApplicants +
                '}';
        assertEquals(s, undertest.toString());
    }
}