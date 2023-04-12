package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Job job3;
    private JobRequest request;
    private String searchTerm;
    private JobFilter jobFilter;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobController(jobService, authService);
        searchTerm = null;

        creator = new AppUser(1L, "joecreator", "1234", "joecreatorunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        candidate = new AppUser(2L, "joecandidate", "1234", "joecandidateunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);

        job1 = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        job2 = new Job(creator, "Mechanical Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        job3 = new Job(creator, "Mechanical Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, false, "", true, true);

        job1.setID(1L);

        allJobs = new ArrayList<>();

        allJobs.add(job1);
        allJobs.add(job2);

        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);

        jobFilter = new JobFilter(candidate, JobType.FULLTIME, true, true, "");
        candidate.addSavedJob(1L);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewJobPostingReturnsAllJobs() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(candidate);
        when(jobService.getAllJobs()).thenReturn(allJobs);

        String returnValue = underTest.viewJobPosting(mock(JobType.class), true, true, false, searchTerm, mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void viewSavedJobs() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(candidate);
        String returnValue = underTest.getSavedJobs(mock(Model.class));
        assertEquals("pages/savedjobs", returnValue);
    }

    @Test
    void viewJobPostingBySearchTermReturnsAllJobs() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(candidate);
        when(jobService.getAllJobs()).thenReturn(allJobs);

        searchTerm = "software developer";

        String returnValue = underTest.viewJobPosting(mock(JobType.class), true, true, false, searchTerm, mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void viewJobPostingByFilteringJobsReturnsAllJobs() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(candidate);
        when(jobService.getAllJobsByFilter(any(JobType.class), any(boolean.class), any(boolean.class))).thenReturn(allJobs);
        when(jobService.saveJobFilter(candidate, JobType.FULLTIME, true, true, null)).thenReturn(jobFilter);

        String returnValue = underTest.viewJobPosting(JobType.FULLTIME, true, true, true, searchTerm, mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void viewJobPostingByFilteringJobsAndBySearchTermReturnsAllJobs() {
        searchTerm = "software developer";

        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(candidate);
        when(jobService.getAllJobsByFilter(any(JobType.class), any(boolean.class), any(boolean.class))).thenReturn(allJobs);
        when(jobService.saveJobFilter(candidate, JobType.FULLTIME, true, true, searchTerm)).thenReturn(jobFilter);

        String returnValue = underTest.viewJobPosting(JobType.FULLTIME, true, true, true, searchTerm, mock(Model.class));

        assertEquals("pages/jobs", returnValue);
    }

    @Test
    void viewJobPostingWhenNotAuthenticatedRedirects() {
        when(jobService.getAllJobs()).thenReturn(allJobs);

        String returnValue = underTest.viewJobPosting(mock(JobType.class), true, true, false, searchTerm, mock(Model.class));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getJobReturnsJobInfo() {

        when(authService.getAuthenticatedUser()).thenReturn(creator);

        when(jobService.getJob(job1.getID())).thenReturn(Optional.of(job1));

        String returnValue = underTest.getJob(job1.getID(), mock(Model.class));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getJobRedirects() {
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        when(jobService.getJob(job1.getID())).thenReturn(Optional.of(job1));

        String returnValue = underTest.getJob(5L, mock(Model.class));

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void getJobRedirectsJobPage() {
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job3));
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        when(authService.isUserAuthenticated()).thenReturn(true);

        String returnValue = underTest.getJob(job1.getID(), mock(Model.class));

        assertEquals("pages/job", returnValue);
    }

    @Test
    void getJobRedirectsJobApplicationExternalPage() {
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        when(authService.isUserAuthenticated()).thenReturn(true);

        String returnValue = underTest.getJob(job1.getID(), mock(Model.class));

        assertEquals("pages/jobApplicationExternal", returnValue);
    }

    @Test
    void viewJobCreationPage() {
        String returnedPage = underTest.createJob(Mockito.mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void sendValidCreateJobInfo() throws Exception {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        underTest.createJob(request, mock(BindingResult.class), mock(Model.class));
        verify(jobService).createJob(request);
    }

    @Test
    void createJobWithBindingErrors() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
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
    void getJobApplicationByIdRedirectToJobApplicationExternal() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job3));
        when(authService.getAuthenticatedUser()).thenReturn(candidate);

        assertEquals("pages/jobApplicationForm", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void getJobApplicationByIdRedirectToJobApplicationForm() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));
        when(authService.getAuthenticatedUser()).thenReturn(candidate);

        assertEquals("pages/jobApplicationExternal", underTest.getJobApplication(1L, Mockito.mock(Model.class)));
    }

    @Test
    void createJobApplicationSendsAnErrorMessage() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));

        String expectedMessage = "There was a problem processing your application. Try again later.";

        assertThrows(Exception.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), 1L, mock(MultipartFile.class), mock(MultipartFile.class), bindingResult, mock(Model.class)), expectedMessage);
    }

    @Test
    void createJobApplicationCreatedJobApplicationSuccessfully() {
        when(authService.isUserAuthenticated()).thenReturn(true);

        assertEquals("redirect:/applicationsuccess", underTest.createJobApplication(mock(JobApplicationRequest.class), 1L, mock(MultipartFile.class), mock(MultipartFile.class), mock(BindingResult.class), mock(Model.class)));
    }

    @Test
    void returnJobApplicationSuccessReturnsApplicationSuccess() {
        assertEquals("pages/applicationSuccess", underTest.returnJobApplicationSuccess());
    }


    @Test
    void deleteJobRedirects() {
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        String returnedPage = underTest.deleteJob(1L);

        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void editJobRedirects() {
        String returnedPage = underTest.editJob(1L, Mockito.mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void editJobRedirectsNullJID() {
        String returnedPage = underTest.editJob(null, Mockito.mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void editJobGet() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        when(jobService.getJob(any(Long.class))).thenReturn(Optional.of(job1));
        String returnedPage = underTest.editJob(job1.getID(), mock(Model.class));
        assertEquals("pages/editJob", returnedPage);
    }

    @Test
    void editJob() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        String returnedPage = underTest.editJob(1L, request, mock(BindingResult.class), mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void editJobRedirectsNoAuth() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);
        when(authService.getAuthenticatedUser()).thenReturn(creator);
        String returnedPage = underTest.editJob(job1.getID(), request, mock(BindingResult.class), mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void editJobWithBindingErrors() {
        request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);
        Model model = mock(Model.class);
        when(authService.getAuthenticatedUser()).thenReturn(creator);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.editJob(1L, request, bindingResult, model);

        verify(model).addAttribute(anyString(), anyString());
    }

    @Test
    void jobController() {
        underTest = new JobController(jobService);
        assertEquals(jobService, this.jobService);
        assertEquals(authService, this.authService);
    }

    @Test
    void getMyJobsReturnsAllMyJobs() {
        when(authService.doesUserHaveRole(Role.RECRUITER)).thenReturn(true);

        assertEquals("pages/myJobs", underTest.getMyJobs(Mockito.mock(Model.class)));
    }

    @Test
    void getMyJobsRedirectsToHomePageWhenUserIsNotRecruiter() {
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(Mockito.mock(AppUser.class));
        when(authService.getAuthenticatedUser().getRole()).thenReturn(Role.CANDIDATE);

        assertEquals("redirect:/", underTest.getMyJobs(Mockito.mock(Model.class)));
    }

}

