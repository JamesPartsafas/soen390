package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.JobFilter;
import com.soen.synapsis.appuser.job.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JobFilterTest {

    private JobFilter underTest;
    private Long id;
    private AppUser appUser;
    private JobType jobType;
    private boolean showInternalJobs;
    private boolean showExternalJobs;
    private String searchTerm;

    @BeforeEach
    void setUp() {
        id = 10L;
        jobType = JobType.FULLTIME;
        showInternalJobs = true;
        showExternalJobs = true;
        appUser = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.CANDIDATE);
        searchTerm = "";
        underTest = new JobFilter(id, appUser, jobType, showInternalJobs, showExternalJobs, searchTerm);
    }

    @Test
    void getID() {
        assertEquals(id, underTest.getId());
    }

    @Test
    void setID() {
        Long newID = 11L;
        underTest.setId(newID);
        assertEquals(newID, underTest.getId());
    }

    @Test
    void getAppUser() {
        assertEquals(appUser, underTest.getAppUser());
    }

    @Test
    void setAppUser() {
        AppUser newAppUser = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        underTest.setAppUser(newAppUser);
        assertEquals(newAppUser, underTest.getAppUser());
    }

    @Test
    void getJobType() {
        assertEquals(jobType, underTest.getJobType());
    }

    @Test
    void setJobType() {
        JobType newJobType = JobType.CONTRACT;
        underTest.setJobType(newJobType);
        assertEquals(newJobType, underTest.getJobType());
    }

    @Test
    void isShowInternalJobs() {
        assertEquals(showInternalJobs, underTest.isShowInternalJobs());
    }

    @Test
    void setShowInternalJobs() {
        underTest.setShowInternalJobs(true);
        assertEquals(true, underTest.isShowInternalJobs());
    }

    @Test
    void isShowExternalJobs() {
        assertEquals(showExternalJobs, underTest.isShowExternalJobs());
    }

    @Test
    void setShowExternalJobs() {
        underTest.setShowExternalJobs(true);
        assertEquals(true, underTest.isShowExternalJobs());
    }

    @Test
    void getSearchTerm() { assertEquals(searchTerm, underTest.getSearchTerm()); }

    @Test
    void setSearchTerm() {
        String newSearchTerm = "developer";
        underTest.setSearchTerm(newSearchTerm);
        assertEquals(newSearchTerm, underTest.getSearchTerm());
    }

    @Test
    void testToString() {
        assertNotNull(underTest.toString());
    }

}
