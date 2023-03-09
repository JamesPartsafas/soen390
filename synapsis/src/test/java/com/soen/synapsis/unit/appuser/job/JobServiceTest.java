package com.soen.synapsis.unit.appuser.job;

import com.fasterxml.jackson.databind.ser.Serializers;
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
import org.mockito.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private AutoCloseable autoCloseable;
    private JobService underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobService(jobRepository, jobApplicationRepository, appUserRepository, notificationService);
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
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true, true);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);

        String returnValue = underTest.createJob(request);

        assertEquals("redirect:/job/null", returnValue);
    }

    @Test
    void nonRecruiterCreateJobThrows() {
        JobRequest request = new JobRequest("Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 1, true, "", true, true, true);
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        request.setCreator(creator);

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
        AppUser candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        AppUser creator = new AppUser(9L, "joe", "1234", "joerecruiter@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);

        List<Job> jobsSubmitted = new ArrayList<>();
        jobsSubmitted.add(job);

        when(underTest.getAllJobsAlreadySubmittedByUser(candidate)).thenReturn(jobsSubmitted);

        assertThrows(IllegalStateException.class, () -> underTest.checkIfUserAlreadySubmittedApplication(candidate, job.getID()));

    }
    @Test
    @Disabled
    void createJobApplicationCreatesJobApplicationSuccessfully() throws IOException {
        AppUser candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
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
        AppUser creator = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        request.setCreator(creator);

        String returnValue = underTest.editJob(Mockito.mock(Optional.class), request);

        assertEquals("redirect:/", returnValue);
    }

    @Test
    void createJobApplicationWithEmptyResumeThatIsMandatory() {
        AppUser candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        AppUser creator = new AppUser(9L, "joe", "1234", "joerecruiter@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate,job.getID(), null, null));
    }

    @Test
    void createJobApplicationWithEmptyCoverLetterThatIsMandatory() {
        AppUser candidate = new AppUser(10L, "joe", "1234", "joeunittest@mail.com", Role.CANDIDATE, AuthProvider.LOCAL);
        AppUser creator = new AppUser(9L, "joe", "1234", "joerecruiter@mail.com", Role.RECRUITER, AuthProvider.LOCAL);
        Job job = new Job(creator, "Software Engineer", "Synapsis", "1 Synapsis Street, Montreal, QC, Canada", "Sample Description", JobType.FULLTIME, 5, true, "", true, true, true);
        assertThrows(NullPointerException.class, () -> underTest.createJobApplication(mock(JobApplicationRequest.class), candidate, job.getID(), mock(MultipartFile.class), null));
    }

}