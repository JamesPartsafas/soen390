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
    private AppUser recruiter;
    private AppUser candidate;
    private AppUser company;

    @BeforeEach
    void setUp() {
        recruiter = new AppUser(1L, "Joe Man1", "1234", "joerecruitertest@mail.com", Role.RECRUITER);
        candidate = new AppUser(2L, "Joe Man2", "1234", "joecandidatetest@mail.com", Role.CANDIDATE);
        company = new AppUser(3L, "Joe Company", "1234", "joecompanyunittest@mail.com", Role.COMPANY);
        appUserRepository.saveAll(Arrays.asList(recruiter, candidate, company));

        Job job1 = new Job(1L, recruiter, "Software Developer", company, "123 Test Street", "description", JobType.FULLTIME, 1, 1, false, "", true, true);
        Job job2 = new Job(2L, recruiter, "QA Tester", company, "123 Test Street", "description", JobType.FULLTIME, 1, 1, false, "", true, true);
        Job job3 = new Job(3L, recruiter, "VBA Tester", company, "123 Test Street", "description", JobType.FULLTIME, 1, 1, true, "https://google.com", true, true);
        underTest.saveAll(Arrays.asList(job1, job2, job3));

        JobApplication jobApplication1 = new JobApplication(1L, job1, candidate, new Timestamp(System.currentTimeMillis()), JobApplicationStatus.IN_REVIEW, candidate.getEmail(), "Joe", "User", "5144444444", "123 Test Street", "Montreal", "Canada", "", "", true, "");
        JobApplication jobApplication2 = new JobApplication(2L, job2, candidate, new Timestamp(System.currentTimeMillis()), JobApplicationStatus.IN_REVIEW, candidate.getEmail(), "Joe", "User", "5144444444", "123 Test Street", "Montreal", "Canada", "", "", true, "");
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
    void itShouldFindAllMyJobsByCreator() {
        assertEquals(3, underTest.findJobsByCreatorEquals(recruiter).size());
    }

    @Test
    void itShouldFindAllJobsBySearch() {
        String searchTerm = "Joe Company";

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
        String searchTerm = "Joe Company";

        assertEquals(0, underTest.findInternalJobsByJobTypeAndSearchTerm(JobType.PARTTIME, searchTerm).size());
    }

    @Test
    void itShouldFindAllExternalJobsByJobTypeSearchTerm() {
        String searchTerm = "Joe Company";

        assertEquals(0, underTest.findExternalJobsByJobTypeAndSearchTerm(JobType.FULLTIME, searchTerm).size());
    }

    @Test
    void itShouldFindAllInternalJobsBySearchTerm() {
        assertEquals(0, underTest.findInternalJobsBySearchTerm("engineer").size());
    }

    @Test
    void itShouldFindAllExternalJobsBySearchTerm() {
        assertEquals(0, underTest.findExternalJobsBySearchTerm("engineer").size());
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
