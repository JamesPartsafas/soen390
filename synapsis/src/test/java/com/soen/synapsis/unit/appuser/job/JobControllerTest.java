package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.job.*;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private List<Job> allJobs;
    private AppUser creator;
    private AppUser candidate;
    private Job job1;
    private Job job2;
    private JobRequest request;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobController(jobService, authService);

        creator = new AppUser(1L, "joecreator", "1234", "joecreatorunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        candidate = new AppUser(2L, "joecandidate", "1234", "joecandidateunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);

        job1 = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);
        job2 = new Job(creator, "Mechanical Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5);

        allJobs = new ArrayList<>();

        allJobs.add(job1);
        allJobs.add(job2);

        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewJobPostingReturnsAllJobs() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getAllJobs()).thenReturn(allJobs);

        String returnValue = underTest.viewJobPosting(mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void viewJobPostingWhenNotAuthenticatedRedirects() {
        when(jobService.getAllJobs()).thenReturn(allJobs);

        String returnValue = underTest.viewJobPosting(mock(Model.class));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getJobReturnsJobInfo() {

        when(authService.getAuthenticatedUser()).thenReturn(creator);

        when(jobService.getJob(job1.getID())).thenReturn(Optional.of(job1));

        String returnValue = underTest.getJob(job1.getID(),mock(Model.class));

        assertEquals("pages/job", returnValue);
    }

    @Test
    void viewJobCreationPage() {
        String returnedPage = underTest.createJob(Mockito.mock(Model.class));
        assertEquals("pages/createjob", returnedPage);
    }

    @Test
    void sendValidCreateJobInfo() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        underTest.createJob(request, mock(BindingResult.class), mock(Model.class));
        verify(jobService).createJob(request);
    }

    @Test
    void createJobWithBindingErrors() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1);
        request.setCreator(creator);
        Model model = mock(Model.class);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.createJob(request, bindingResult, model);

        verify(model).addAttribute(anyString(), anyString());
    }

    @Test
    void getJobApplicationByIdRedirectToHomePageWhenUserNotAuthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);

        assertEquals("redirect:/", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getJobApplicationByIdRedirectToJobsPageWhenJobIsEmpty() {
        when(authService.isUserAuthenticated()).thenReturn(true);

        assertEquals("redirect:/jobs", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getJobApplicationByIdRedirectToJobsPageWhenUserAlreadySubmittedApplication() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.empty());
        assertEquals("redirect:/jobs", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getJobApplicationByIdRedirectToApplicationForm() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));
        when(authService.getAuthenticatedUser()).thenReturn(candidate);

        assertEquals("pages/jobapplicationform", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void createJobApplicationSendsAnErrorMessage() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));

        String expectedMessage = "There was a problem processing your application. Try again later.";

        assertThrows(Exception.class, () -> underTest.createJobApplication(mock(JobApplication.class), bindingResult, mock(Model.class), 1L), expectedMessage);
    }

    @Test
    void createJobApplicationCreatedJobApplicationSuccessfully() {
        when(authService.isUserAuthenticated()).thenReturn(true);

        assertEquals("redirect:/applicationsuccess", underTest.createJobApplication(mock(JobApplication.class), mock(BindingResult.class), mock(Model.class), 1L));
    }

    @Test
    void returnJobApplicationSuccessReturnsApplicationSuccess() {
        assertEquals("pages/applicationsuccess", underTest.returnJobApplicationSuccess());
    }

}
