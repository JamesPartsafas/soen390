package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.job.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobControllerTest {

    @Mock
    private JobService jobService;
    @Mock
    private AuthService authService;
    private AutoCloseable autoCloseable;
    private JobController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobController(jobService, authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewJobPostingReturnsAllJobs() {
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);

        Job job1 = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);
        Job job2 = new Job(creator, "Mechanical Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);

        List<Job> allJobs = new ArrayList<>();
        allJobs.add(job1);
        allJobs.add(job2);

        when(jobService.getAllJobs()).thenReturn(allJobs);

        String returnValue = underTest.viewJobPosting(mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void getJobReturnsJobInfo() {
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);

        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);

        when(jobService.getJob(job.getID())).thenReturn(Optional.of(job));

        String returnValue = underTest.getJob(job.getID(),mock(Model.class));

        assertEquals("pages/job", returnValue);
    }

    @Test
    void viewJobCreationPage() {
        String returnedPage = underTest.createJob(Mockito.mock(Model.class));
        assertEquals("pages/createjob", returnedPage);
    }

    @Test
    void sendValidCreateJobInfo() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        underTest.createJob(request, mock(BindingResult.class), mock(Model.class));
        verify(jobService).createJob(request);
    }

    @Test
    void createJobWithBindingErrors() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);
        Model model = mock(Model.class);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.createJob(request, bindingResult, model);

        verify(model).addAttribute(anyString(), anyString());
    }
}