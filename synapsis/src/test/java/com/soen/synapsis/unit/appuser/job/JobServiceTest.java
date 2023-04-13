package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import com.soen.synapsis.appuser.profile.CoverLetterRepository;
import com.soen.synapsis.appuser.profile.ResumeRepository;
import com.soen.synapsis.websockets.notification.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.time.Instant;
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
    NotificationService notificationService;
    @Mock
    private JobFilterRepository jobFilterRepository;
    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private CoverLetterRepository coverLetterRepository;
    private AutoCloseable autoCloseable;
    private JobService underTest;
    private AppUser candidate;
    private AppUser creator;
    private AppUser company;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        underTest = new JobService(jobRepository, jobApplicationRepository, notificationService, jobFilterRepository, resumeRepository, coverLetterRepository);

        candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        creator = new AppUser(9L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        company = new AppUser(1L, "joecompany", "1234", "joecompanyunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL);
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
    void getMyCreatedJobsReturnsAllMyJobs() {
        underTest.getMyCreatedJobs(creator);
        verify(jobRepository, times(1)).findJobsByCreatorEquals(creator);
    }

    @Test
    void getAllJobsBySearchTermReturnsJobs() {
        String searchTerm = "developer";
        underTest.getAllJobsBySearch(searchTerm);
        verify(jobRepository, times(1)).findJobsBySearch(searchTerm);
    }

    @Test
    void getAllJobsByFilterWithOneJobTypeReturnsJobs() {
        JobType jobType = JobType.FULLTIME;

        underTest.getAllJobsByFilter(jobType, true, true);

        verify(jobRepository, times(1)).findInternalJobsByJobType(jobType);
        verify(jobRepository, times(1)).findExternalJobsByJobType(jobType);
    }

    @Test
    void getAllJobsByFilterWithAllJobTypesReturnsJobs() {
        JobType jobType = JobType.ANY;

        underTest.getAllJobsByFilter(jobType, true, true);

        verify(jobRepository, times(1)).findInternalJobs();
        verify(jobRepository, times(1)).findExternalJobs();
    }

    @Test
    void getAllJobsByFilterAndSearchTermWithOneJobTypeReturnsJobs() {
        String searchTerm = "developer";
        JobType jobType = JobType.FULLTIME;

        underTest.getAllJobsByFilterAndSearchTerm(jobType, true, true, searchTerm);

        verify(jobRepository, times(1)).findInternalJobsByJobTypeAndSearchTerm(jobType, searchTerm);
        verify(jobRepository, times(1)).findExternalJobsByJobTypeAndSearchTerm(jobType, searchTerm);
    }

    @Test
    void getAllJobsByFilterAndSearchTermWithAllJobTypesReturnsJobs() {
        String searchTerm = "developer";
        JobType jobType = JobType.ANY;

        underTest.getAllJobsByFilterAndSearchTerm(jobType, true, true, searchTerm);

        verify(jobRepository, times(1)).findInternalJobsBySearchTerm(searchTerm);
        verify(jobRepository, times(1)).findExternalJobsBySearchTerm(searchTerm);
    }

    @Test
    void getJobReturnsJob() {
        Long id = 1L;

        Optional<Job> optionalJob = underTest.getJob(id);

        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void createValidJob() throws Exception {
        JobRequest request = new JobRequest("Software Engineer", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "https://google.com", true, true);
        request.setCreator(creator);

        String returnValue = underTest.createJob(request);

        assertEquals("redirect:/job/null", returnValue);
    }

    @Test
    void createJobWithInvalidExternalLinkURLThrowsException() {
        JobRequest request = new JobRequest("Software Engineer", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);

        assertThrows(Exception.class, () -> underTest.createJob(request));
    }

    @Test
    void nonRecruiterCreateJobThrows() {
        JobRequest request = new JobRequest("Software Engineer", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
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
        Job job = new Job(creator, "Software Engineer", company, "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);

        List<Job> jobsSubmitted = new ArrayList<>();
        jobsSubmitted.add(job);

        when(underTest.getAllJobsAlreadySubmittedByUser(candidate)).thenReturn(jobsSubmitted);

        assertThrows(IllegalStateException.class, () -> underTest.checkIfUserAlreadySubmittedApplication(candidate, job.getID()));

    }

    @Test
    void createJobApplicationCreatesJobApplicationSuccessfully() throws IOException {
        Long jobId = 1L;
        Job job = new Job(jobId, creator, "position",
                company, "address", "description", JobType.FULLTIME, 5,
                5, false, null, true, true);
        JobApplicationRequest request = new JobApplicationRequest(job, candidate, Timestamp.from(Instant.now()),
                JobApplicationStatus.SUBMITTED, candidate.getEmail(), "Joe", "User", "123-456-1234",
                "1234 street", "Montreal", "Canada", true, null);

        when(jobRepository.findAllJobsAlreadySubmittedByUserID(candidate.getId())).thenReturn(new ArrayList<>());
        when(jobRepository.getReferenceById(jobId)).thenReturn(job);

        byte[] byteArray = "1234".getBytes();
        MultipartFile resume = mock(MultipartFile.class);
        when(resume.getBytes()).thenReturn(byteArray);
        MultipartFile coverLetter = mock(MultipartFile.class);
        when(coverLetter.getBytes()).thenReturn(byteArray);

        underTest.createJobApplication(request, candidate, jobId, resume, coverLetter);

        verify(jobRepository, times(1)).save(any(Job.class));
        verify(jobApplicationRepository, times(1)).save(any(JobApplication.class));
    }

    @Test
    void deleteJob() {
        String returnedPage = underTest.deleteJob(1L);
        assertEquals("redirect:/jobs", returnedPage);
        verify(jobRepository, never()).findById(1L);
    }

    @Test
    void editJob() throws Exception {
        Long id = 1L;

        JobRequest request = new JobRequest("Software Engineer", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true);
        request.setCreator(creator);

        String returnValue = underTest.editJob(Mockito.mock(Optional.class), request);

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void createJobApplicationWithEmptyResumeThatIsMandatory() {
        Job job = new Job(creator, "Software Engineer", company, "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate, job.getID(), null, null));
    }

    @Test
    void createJobApplicationWithEmptyCoverLetterThatIsMandatory() {
        Job job = new Job(creator, "Software Engineer", company, "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate, job.getID(), mock(MultipartFile.class), null));
    }

    @Test
    void saveJobFilterThrowsErrorWhenRoleIsNotCandidateOrNotRecruiter() {
        AppUser companyUser = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL);
        assertThrows(IllegalStateException.class, () -> underTest.saveJobFilter(companyUser, JobType.FULLTIME, true, true, ""));
    }

    @Test
    void saveJobFilterSavesWhenJobFilterExists() {
        JobFilter jobFilter = new JobFilter(candidate, JobType.FULLTIME, true, true, "");

        when(jobFilterRepository.findJobFilterByAppUser(candidate)).thenReturn(Optional.of(jobFilter));

        underTest.saveJobFilter(candidate, JobType.FULLTIME, true, true, "");
        verify(jobFilterRepository).save(any(JobFilter.class));
    }

    @Test
    void saveJobFilterSavesWhenJobFilterDoesNotExists() {
        JobFilter jobFilter = null;

        when(jobFilterRepository.findJobFilterByAppUser(candidate)).thenReturn(Optional.ofNullable(jobFilter));

        underTest.saveJobFilter(candidate, JobType.FULLTIME, true, true, "");

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

    @Test
    void getResumeByAppUser() {
        AppUser appUser= new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        assertEquals(null, underTest.getResumeByAppUser(appUser));
    }

    @Test
    void getCoverLetterByAppUser() {
        AppUser appUser= new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        assertEquals(null, underTest.getCoverLetterByAppUser(appUser));
    }

    @Test
    void getSuggestedJobsWithNoFilterReturnsEmptyList() {
        AppUser appUser = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        when(jobFilterRepository.findJobFilterByAppUser(appUser)).thenReturn(Optional.empty());

        List<Job> jobs = underTest.getSuggestedJobs(appUser);

        assertEquals(0, jobs.size());
    }

    @Test
    void getSuggestedJobsWithFilterReturnsJobs() {
        AppUser appUser = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);

        JobType jobType = JobType.FULLTIME;
        when(jobFilterRepository.findJobFilterByAppUser(appUser))
                .thenReturn(Optional.of(new JobFilter(appUser, jobType, true, false, null)));

        underTest.getSuggestedJobs(appUser);

        verify(jobRepository).findInternalJobsByJobType(jobType);
    }
}