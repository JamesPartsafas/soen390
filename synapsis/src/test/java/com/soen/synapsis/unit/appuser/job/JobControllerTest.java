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
        underTest.createJob(request, mock(BindingResult.class), mock(Model.class), mock(AppUserDetails.class));
        verify(jobService).createJob(request);
    }

    @Test
    void createJobWithBindingErrors() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);
        Model model = mock(Model.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.createJob(request, bindingResult, model, mock(AppUserDetails.class));

        verify(model).addAttribute(anyString(), anyString());
    }
}