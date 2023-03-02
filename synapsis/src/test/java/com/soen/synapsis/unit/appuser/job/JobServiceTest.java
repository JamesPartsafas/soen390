package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobApplicationRepository jobApplicationRepository;
    private AutoCloseable autoCloseable;
    private JobService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobService(jobRepository, jobApplicationRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllJobsReturnsAllJobs() {
        underTest.getAllJobs();
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void getJobReturnsJob() {
        Long id = 1L;

        Optional<Job> optionalJob = underTest.getJob(id);

        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void createValidJob() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);

        String returnValue = underTest.createJob(request);

        assertEquals("redirect:/job/null", returnValue);
    }

    @Test
    void nonRecruiterCreateJobThrows() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        request.setCreator(creator);

        Exception exception = assertThrows(IllegalStateException.class, () -> underTest.createJob(request), "This user is not a recruiter.");
    }

    @Test
    void createJobApplicationThrowsWhenRoleIsNotCandidate() {
        AppUser notCandidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL);

        assertThrows(IllegalStateException.class,
                () -> underTest.createJobApplication(mock(JobApplication.class), notCandidate, 1L));
    }

    @Test
    void createJobApplicationCreatesJobApplicationSuccessfully() {
        AppUser candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);

        underTest.createJobApplication(mock(JobApplication.class), candidate, 1L);

        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

}
