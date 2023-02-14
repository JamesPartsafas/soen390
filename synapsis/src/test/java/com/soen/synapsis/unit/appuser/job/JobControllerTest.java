package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;
    private AppUserService appUserService;
    private AutoCloseable autoCloseable;
    private JobController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobController(jobService, appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getJobReturnsJobInfo() {
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        AppUser company = new AppUser(30L, "Synapsis", "1234", "synapsis@mail.com", Role.COMPANY, AuthProvider.LOCAL);;

        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);

        when(jobService.getJob(job.getID())).thenReturn(Optional.of(job));

        String returnValue = underTest.getJob(job.getID(),mock(Model.class));

        assertEquals("pages/job", returnValue);
    }
}