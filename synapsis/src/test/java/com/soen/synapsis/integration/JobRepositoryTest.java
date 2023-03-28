package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class JobRepositoryTest {

    @Autowired
    private JobRepository underTest;
    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        AppUser user1 = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.RECRUITER);
        AppUser user2 = new AppUser(2L, "Joe Man2", "1234", "joecandidate2@mail.com", Role.CANDIDATE);
        appUserRepository.saveAll(Arrays.asList(user1, user2));

        Job job1 = new Job(1L, user1, "Software Developer", "Macrosoft", "123 Test Street", "description", JobType.FULLTIME, 1, 1, false, "", true, true, true);
        Job job2 = new Job(2L, user1, "QA Tester", "Macrosoft", "123 Test Street", "description", JobType.FULLTIME, 1, 1, false, "", true, true, true);
        Job job3 = new Job(3L, user1, "VBA Tester", "Macrosoft", "123 Test Street", "description", JobType.FULLTIME, 1, 1, true, "https://google.com", true, true, true);
        underTest.saveAll(Arrays.asList(job1, job2, job3));

        JobApplication jobApplication1 = new JobApplication(1L, job1, user2, new Timestamp(System.currentTimeMillis()), JobApplicationStatus.IN_REVIEW, user2.getEmail(), "Joe", "User", "5144444444", "123 Test Street", "Montreal", "Canada", "", "", true, "");
        JobApplication jobApplication2 = new JobApplication(2L, job2, user2, new Timestamp(System.currentTimeMillis()), JobApplicationStatus.IN_REVIEW, user2.getEmail(), "Joe", "User", "5144444444", "123 Test Street", "Montreal", "Canada", "", "", true, "");
        jobApplicationRepository.saveAll(Arrays.asList(jobApplication1, jobApplication2));
    }

    @AfterEach
    void tearDown() {
        jobApplicationRepository.deleteAll();
        underTest.deleteAll();
        appUserRepository.deleteAll();
    }

    @Test
    void itShouldFindAllJobAlreadySubmittedByUserID() {
        assertEquals(2, underTest.findAllJobsAlreadySubmittedByUserID(2L).size());
    }

    @Test
    void itShouldNotFindAllJobAlreadySubmittedByUserIDIfUserDoesNotExist() {
        Long ID = 100L;

        List<Job> result = underTest.findAllJobsAlreadySubmittedByUserID(ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void itShouldFindAllJobsBySearch() {
        String searchTerm = "Tester";

        assertEquals(2, underTest.findJobsBySearch(searchTerm.toLowerCase()).size());
    }

    @Test
    void itShouldFindAllInternalJobsByJobTypeFilter() {
        assertEquals(2, underTest.findInternalJobsByJobType(JobType.FULLTIME).size());
    }

    @Test
    void itShouldFindAllExternalJobsByJobTypeFilter() {
        assertEquals(2, underTest.findExternalJobsByJobType(JobType.FULLTIME).size());
    }

    @Test
    void itShouldFindAllInternalJobsByJobTypeAndSearchTerm() {
        String searchTerm = "tester";

        assertEquals(1, underTest.findInternalJobsByJobTypeAndSearchTerm(JobType.FULLTIME, searchTerm).size());
    }

    @Test
    void itShouldFindAllExternalJobsByJobTypeSearchTerm() {
        String searchTerm = "tester";

        assertEquals(1, underTest.findExternalJobsByJobTypeAndSearchTerm(JobType.FULLTIME, searchTerm).size());
    }

    @Test
    void itShouldFindAllInternalJobsBySearchTerm() {
        assertEquals(1, underTest.findInternalJobsBySearchTerm("tester").size());
    }

    @Test
    void itShouldFindAllExternalJobsBySearchTerm() {
        assertEquals(1, underTest.findExternalJobsBySearchTerm("tester").size());
    }

    @Test
    void itShouldFindAllInternalJobs() {
        assertEquals(3, underTest.findInternalJobs().size());
    }

    @Test
    void itShouldFindAllExternalJobs() {
        assertEquals(2, underTest.findExternalJobs().size());
    }

}
