package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.job.JobRepository;
import com.soen.synapsis.appuser.job.JobService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;
    private AutoCloseable autoCloseable;
    private AppUserService appUserService;
    private JobService undertest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        undertest = new JobService(jobRepository, appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getJobReturnsJob() {

    }

    @Test
    void createJobPosting() {
    }
}