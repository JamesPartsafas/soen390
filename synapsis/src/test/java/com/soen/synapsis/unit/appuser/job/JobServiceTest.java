package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import com.soen.synapsis.websockets.notification.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobApplicationRepository jobApplicationRepository;
    @Mock
    AppUserRepository appUserRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    private JobFilterRepository jobFilterRepository;
    private AutoCloseable autoCloseable;
    private JobService underTest;
    private AppUser candidate;
    private AppUser creator;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        underTest = new JobService(jobRepository, jobApplicationRepository, appUserRepository, notificationService, jobFilterRepository);

        candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        creator = new AppUser(9L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
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
    void getAllJobsBySearchTermReturnsJobs() {
        String searchTerm = "developer";
        underTest.getAllJobsBySearch(searchTerm);
        verify(jobRepository, times(1)).findJobsBySearch(searchTerm);
    }

    @Test
    void getAllJobsByFilterReturnsJobs() {
        JobType jobType = JobType.FULLTIME;

        underTest.getAllJobsByFilter(jobType, true, true);

        verify(jobRepository, times(1)).findInternalJobsByJobType(jobType);
        verify(jobRepository, times(1)).findExternalJobsByJobType(jobType);
    }

    @Test
    void getJobReturnsJob() {
        Long id = 1L;

        Optional<Job> optionalJob = underTest.getJob(id);

        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void createValidJob() throws Exception {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "https://google.com", true, true, true);
        request.setCreator(creator);

        String returnValue = underTest.createJob(request);

        assertEquals("redirect:/job/null", returnValue);
    }

    @Test
    void createJobWithInvalidExternalLinkURLThrowsException() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true, true);

        assertThrows(Exception.class, () -> underTest.createJob(request));
    }

    @Test
    void nonRecruiterCreateJobThrows() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true, true);
        request.setCreator(candidate);

        Exception exception = assertThrows(IllegalStateException.class, () -> underTest.createJob(request), "This user is not a recruiter.");
    }

    @Test
    void createJobApplicationThrowsWhenRoleIsNotCandidate() {
        AppUser notCandidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL);

        assertThrows(IllegalStateException.class,
                () -> underTest.createJobApplication(mock(JobApplicationRequest.class), notCandidate, 1L, mock(MultipartFile.class), mock(MultipartFile.class)));
    }

    @Test
    void UserAlreadySubmittedApplication() {
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);

        List<Job> jobsSubmitted = new ArrayList<>();
        jobsSubmitted.add(job);

        when(underTest.getAllJobsAlreadySubmittedByUser(candidate)).thenReturn(jobsSubmitted);

        assertThrows(IllegalStateException.class, () -> underTest.checkIfUserAlreadySubmittedApplication(candidate, job.getID()));

    }
    @Test
    @Disabled
    void createJobApplicationCreatesJobApplicationSuccessfully() throws IOException {
        when(jobRepository.getReferenceById(any(Long.class))).thenReturn(mock(Job.class));

        MultipartFile file = mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[]{});

        String encodedResume = "test";

        when(Base64.getEncoder()).thenReturn(Base64.getEncoder());
        when(Base64.getEncoder().encodeToString(any(byte[].class))).thenReturn(encodedResume);
        when(anyString().isEmpty()).thenReturn(false);

        underTest.createJobApplication(mock(JobApplicationRequest.class), candidate, 1L, file, file);

        verify(jobRepository).save(any(Job.class));
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    @Test
    void deleteJob() {
        String returnedPage = underTest.deleteJob(1L);
        assertEquals("redirect:/jobs", returnedPage);
        verify(jobRepository, never()).findById(1L);
    }

    @Test
    void editJob() {
        Long id = 1L;

        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true, true);
        request.setCreator(creator);

        String returnValue = underTest.editJob(Mockito.mock(Optional.class), request);

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void createJobApplicationWithEmptyResumeThatIsMandatory() {
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate,job.getID(), null, null));
    }

    @Test
    void createJobApplicationWithEmptyCoverLetterThatIsMandatory() {
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate, job.getID(), mock(MultipartFile.class), null));
    }

    @Test
    void saveJobFilterThrowsErrorWhenRoleIsNotCandidateOrNotRecruiter() {
        AppUser companyUser = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL);
        assertThrows(IllegalStateException.class, () -> underTest.saveJobFilter(companyUser, JobType.FULLTIME, true, true));
    }

    @Test
    void saveJobFilterSavesWhenJobFilterExists() {
        JobFilter jobFilter = new JobFilter(candidate, JobType.FULLTIME, true, true);

        when(jobFilterRepository.findJobFilterByAppUser(candidate)).thenReturn(Optional.of(jobFilter));

        underTest.saveJobFilter(candidate, JobType.FULLTIME, true, true);
        verify(jobFilterRepository).save(any(JobFilter.class));
    }

    @Test
    void saveJobFilterSavesWhenJobFilterDoesNotExists() {
        JobFilter jobFilter = null;

        when(jobFilterRepository.findJobFilterByAppUser(candidate)).thenReturn(Optional.ofNullable(jobFilter));

        underTest.saveJobFilter(candidate, JobType.FULLTIME, true, true);

        verify(jobFilterRepository).save(any(JobFilter.class));
    }

    @Test
    void isValidURLSuccess() throws Exception {
        underTest.isValidURL("https://google.com");
    }

    @Test
    void isValidURLThrowsException() {
        assertThrows(Exception.class, () -> underTest.isValidURL(""));
    }

}