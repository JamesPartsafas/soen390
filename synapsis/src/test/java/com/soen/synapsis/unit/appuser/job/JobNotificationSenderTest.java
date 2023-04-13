package com.soen.synapsis.unit.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthProvider;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.job.*;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class JobNotificationSenderTest {
    private JobNotificationSender underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private Job job;
    @Mock
    private JobFilterRepository jobFilterRepository;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new JobNotificationSender(job, jobFilterRepository, notificationService);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void runSuccessfulExternal() {
        List<JobFilter> jobFilterList = new ArrayList<>();
        jobFilterList.add(new JobFilter(mock(AppUser.class), JobType.FULLTIME, true, true, ""));

        when(job.getIsExternal()).thenReturn(true);
        when(job.getType()).thenReturn(JobType.FULLTIME);
        when(job.getPosition()).thenReturn("");
        when(job.getCompany()).thenReturn(new AppUser(1L, "joecompany", "1234", "joecompanyunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL));
        when(jobFilterRepository.findAllExternalJobFiltersMatchingJobTypeAndSearchTerm(JobType.FULLTIME, job.getPosition(), "joecompany")).thenReturn(jobFilterList);

        underTest.run();

        verify(notificationService).saveNotification(ArgumentMatchers.any(NotificationDTO.class), ArgumentMatchers.any(AppUser.class));
    }

    @Test
    public void runSuccessfulInternal() {
        List<JobFilter> jobFilterList = new ArrayList<>();
        jobFilterList.add(new JobFilter(mock(AppUser.class), JobType.FULLTIME, true, true, ""));
        when(job.getIsExternal()).thenReturn(false);
        when(job.getType()).thenReturn(JobType.FULLTIME);
        when(job.getPosition()).thenReturn("");
        when(job.getCompany()).thenReturn(new AppUser(1L, "joecompany", "1234", "joecompanyunittest@mail.com", Role.COMPANY, AuthProvider.LOCAL));
        when(jobFilterRepository.findAllInternalJobFiltersMatchingJobTypeAndSearchTerm(JobType.FULLTIME, job.getPosition(), job.getCompany().getName())).thenReturn(jobFilterList);

        underTest.run();

        verify(notificationService).saveNotification(ArgumentMatchers.any(NotificationDTO.class), ArgumentMatchers.any(AppUser.class));
    }
}
