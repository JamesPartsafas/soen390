package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.Resume;
import com.soen.synapsis.appuser.profile.ResumeRepository;
import com.soen.synapsis.websockets.notification.NotificationDTO;
import com.soen.synapsis.websockets.notification.NotificationService;
import com.soen.synapsis.websockets.notification.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * A service class to work with jobs.
 */
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final AppUserRepository appUserRepository;
    private final NotificationService notificationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobFilterRepository jobFilterRepository;
    private final ResumeRepository resumeRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, AppUserRepository appUserRepository, NotificationService notificationService, JobFilterRepository jobFilterRepository, ResumeRepository resumeRepository) {
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.appUserRepository = appUserRepository;
        this.notificationService = notificationService;
        this.jobFilterRepository = jobFilterRepository;
        this.resumeRepository = resumeRepository;
    }

    /**
     * Retrieve all the jobs.
     *
     * @return a list of jobs.
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Retrieve all the jobs submitted by a user.
     *
     * @param user the appuser.
     * @return a list of jobs.
     */
    public List<Job> getAllJobsAlreadySubmittedByUser(AppUser user) {
        return jobRepository.findAllJobsAlreadySubmittedByUserID(user.getId());
    }

    /**
     * Retrieve a job given an id.
     *
     * @param id the job id.
     * @return the job.
     */
    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    /**
     * Create a new job posting.
     *
     * @param request the job application request.
     * @return the job page.
     */
    public String createJob(JobRequest request) throws Exception {

        if (request.getCreator().getRole() != Role.RECRUITER) {
            throw new IllegalStateException("This user is not a recruiter.");
        }

        if (request.getIsExternal()) {
            isValidURL(request.getExternalLink());
        }

        AppUser creator = request.getCreator();
        String position = request.getPosition();
        String company = request.getCompany();
        String address = request.getAddress();
        String description = request.getDescription();
        JobType type = request.getType();
        int numAvailable = request.getNumAvailable();
        boolean isExternal = request.getIsExternal();
        String externalLink = request.getExternalLink();
        boolean needResume = request.getNeedResume();
        boolean needCover = request.getNeedCover();
        boolean needPortfolio = request.getNeedPortfolio();


        Job job = new Job(creator, position, company, address, description, type, numAvailable, isExternal, externalLink, needResume, needCover, needPortfolio);
        jobRepository.save(job);

        return "redirect:/job/" + job.getID();
    }

    /**
     * Create a new job application.
     *
     * @param request the job application request.
     * @param applicant the user submitting the application.
     * @param jobId the job id.
     */
    public void createJobApplication(JobApplicationRequest request, AppUser applicant, Long jobId, MultipartFile resume, MultipartFile coverLetter) throws IOException {

        checkIfUserAlreadySubmittedApplication(applicant, jobId);

        if (applicant.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("You are not a candidate. Only candidates can submit application forms.");
        }

        Job job = jobRepository.getReferenceById(jobId);

        JobApplication jobApplication = new JobApplication(
                job,
                applicant,
                new Timestamp(System.currentTimeMillis()),
                JobApplicationStatus.SUBMITTED,
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone(),
                request.getAddress(),
                request.getCity(),
                request.getCountry(),
                request.isLegallyAllowedToWork(),
                request.getLinks()
        );

        Resume defaultResume = resumeRepository.findByAppUser(applicant);
        String encodedResume = Base64.getEncoder().encodeToString(resume.getBytes());

        if(job.getNeedResume() && encodedResume.isEmpty() && defaultResume == null) {
            throw new IllegalStateException("It is mandatory to upload your resume.");
        }
        if(encodedResume.isEmpty() && defaultResume != null) {
            jobApplication.setResume(defaultResume.getDefaultResume());
        }
        if(!encodedResume.isEmpty() && defaultResume == null) {
            jobApplication.setResume(encodedResume);
        }
        if(!encodedResume.isEmpty() && defaultResume != null) {
            jobApplication.setResume(encodedResume);
        }

        if(job.getNeedCover()) {
                String encodedCoverLetter = Base64.getEncoder().encodeToString(coverLetter.getBytes());
                if (encodedCoverLetter.isEmpty()) {
                    throw new IllegalStateException("It is mandatory to upload your cover letter.");
                }
                jobApplication.setCoverLetter(encodedCoverLetter);
            }

        job.setNumApplicants(job.getNumApplicants() + 1);
        jobRepository.save(job);

        jobApplicationRepository.save(jobApplication);
    }

    /**
     * Check if user already submitted the job application form.
     *
     * @param applicant the user submitting the application.
     * @param jobId the job id.
     */
    public void checkIfUserAlreadySubmittedApplication(AppUser applicant, Long jobId) {
        List<Job> jobsSubmitted = getAllJobsAlreadySubmittedByUser(applicant);
        for (Job job : jobsSubmitted) {
            if (job.getID() == jobId) {
                throw new IllegalStateException("You already submitted an application form for this job.");
            }
        }
    }

    /**
     * Delete a job.
     *
     * @param id the job id.
     * @return the jobs page.
     */
    public String deleteJob(Long id) {
        jobRepository.deleteById(id);
        return "redirect:/jobs";
    }

    /**
     * Edit a job posting.
     *
     * @param optionalJob the job to be edited.
     * @param request the job request.
     * @return the job page.
     */
    public String editJob(Optional<Job> optionalJob, JobRequest request) {

        Job job = optionalJob.get();

        if (job == null)
            return "redirect:/";

        job.setPosition(request.getPosition());
        job.setCompany(request.getCompany());
        job.setAddress(request.getAddress());
        job.setDescription(request.getDescription());
        job.setNumAvailable(request.getNumAvailable());
        job.setIsExternal(request.getIsExternal());
        job.setExternalLink(request.getExternalLink());
        job.setType(request.getType());
        job.setNeedResume(request.getNeedResume());
        job.setNeedCover(request.getNeedCover());
        job.setNeedPortfolio(request.getNeedPortfolio());

        jobRepository.save(job);

        sendJobNotifications(job.getID());

        return "redirect:/job/" + job.getID();
    }

    //This should be changed to however we are implementing suggested jobs
    private void sendJobNotifications(Long jobId) {
        for (AppUser appUser : appUserRepository.findAll()) {
            NotificationDTO notificationDTO = new NotificationDTO(
                    0L,
                    appUser.getId(),
                    NotificationType.JOB,
                    "You have a new job suggestion!",
                    "/job/" + jobId,
                    false
            );
            notificationService.saveNotification(notificationDTO, appUser);
        }
    }

    /**
     * Retrieve all jobs given the search term.
     *
     * @param searchTerm the job to search for.
     * @return a list of jobs that meet the search criteria.
     */
    public List<Job> getAllJobsBySearch(String searchTerm) {
        return jobRepository.findJobsBySearch(searchTerm);
    }

    /**
     * Retrieve all jobs given the filter preferences.
     *
     * @param jobType the type of job (fulltime, parttime, contract, etc).
     * @param showInternalJobs true if we want internal jobs to be retrieved; otherwise false.
     * @param showExternalJobs true if we want external jobs to be retrieved; otherwise false.
     * @return a list of jobs that meet the filtered preferences.
     */
    public List<Job> getAllJobsByFilter(JobType jobType, boolean showInternalJobs, boolean showExternalJobs) {
        List<Job> jobs = new ArrayList<>();

        if(showInternalJobs) {
            jobs.addAll(jobRepository.findInternalJobsByJobType(jobType));
        }

        if(showExternalJobs) {
            jobs.addAll(jobRepository.findExternalJobsByJobType(jobType));
        }

        return jobs;
    }

    /**
     * Save a job filter preference for a user.
     *
     * @param appUser the user whose filter preferences is saved.
     * @param jobType the type of job preference (fulltime, parttime, contract, etc).
     * @param showInternalJobs true if we want internal jobs to be retrieved; otherwise false.
     * @param showExternalJobs true if we want external jobs to be retrieved; otherwise false.
     * @return the newly created job filter.
     */
    public JobFilter saveJobFilter(AppUser appUser, JobType jobType, boolean showInternalJobs, boolean showExternalJobs) {
        if (appUser.getRole() != Role.CANDIDATE && appUser.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("Permission denied. Only Candidates and Recruiters can save job filters.");
        }

        Optional<JobFilter> optionalJobFilter = jobFilterRepository.findJobFilterByAppUser(appUser);
        JobFilter jobFilter;

        if (optionalJobFilter.isPresent()) {
            jobFilter = optionalJobFilter.get();
            jobFilter.setJobType(jobType);
            jobFilter.setShowInternalJobs(showInternalJobs);
            jobFilter.setShowExternalJobs(showExternalJobs);
        } else {
            jobFilter = new JobFilter(appUser, jobType, showInternalJobs, showExternalJobs);
        }

        jobFilterRepository.save(jobFilter);

        return jobFilter;
    }

    /**
     * Validate that the given external link URL is good
     *
     * @param url The URL for the external link.
     * @throws MalformedURLException MalformedURLException is thrown when a malformed syntax is found in the input String url.
     */
    public void isValidURL(String url) throws Exception {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new Exception("Invalid URL for external link.");
        }
    }

    /**
     * Gets the resume of a given app user.
     *
     * @param appUser an object representing the app user
     * @return the resume of the app user.
     */
    public Resume getResumeByAppUser(AppUser appUser) {
       return resumeRepository.findByAppUser(appUser);
    }

}
