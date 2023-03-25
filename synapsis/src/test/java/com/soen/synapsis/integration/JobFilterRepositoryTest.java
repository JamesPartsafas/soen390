package com.soen.synapsis.integration;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.JobFilter;
import com.soen.synapsis.appuser.job.JobFilterRepository;
import com.soen.synapsis.appuser.job.JobType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class JobFilterRepositoryTest {

    @Autowired
    private JobFilterRepository underTest;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser(1L, "Joe Man1", "1234", "joecandidate1@mail.com", Role.RECRUITER);

        JobFilter jobFilter = new JobFilter(appUser, JobType.FULLTIME, true, true);

        underTest.save(jobFilter);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindJobFilterByAppUser() {
        assertTrue(underTest.findJobFilterByAppUser(appUser).isPresent());
    }

    @Test
    void itShouldNotFindJobFilterByAppUserIfUserDoesNotExist() {
        AppUser appUser2 = null;
        assertTrue(underTest.findJobFilterByAppUser(appUser2).isEmpty());
    }

}
