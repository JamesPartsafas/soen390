package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.CoverLetter;
import com.soen.synapsis.appuser.profile.CoverLetterRepository;
import com.soen.synapsis.appuser.profile.Resume;
import com.soen.synapsis.appuser.profile.ResumeRepository;
import com.soen.synapsis.websockets.notification.NotificationService;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A service class to work with jobs.
 */
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final NotificationService notificationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobFilterRepository jobFilterRepository;
    private final ResumeRepository resumeRepository;
    private final CoverLetterRepository coverLetterRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, NotificationService notificationService, JobFilterRepository jobFilterRepository, ResumeRepository resumeRepository, CoverLetterRepository coverLetterRepository) {
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.notificationService = notificationService;
        this.jobFilterRepository = jobFilterRepository;
        this.resumeRepository = resumeRepository;
        this.coverLetterRepository = coverLetterRepository;
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
     * Retrieve all jobs created by the recruiter.
     *
     * @param recruiter the recruiter to search for
     * @return a list of jobs
     */
    public List<Job> getMyCreatedJobs(AppUser recruiter) {return jobRepository.findJobsByCreatorEquals(recruiter); }

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
        AppUser company = request.getCompany();
        String address = request.getAddress();
        String description = request.getDescription();
        JobType type = request.getType();
        int numAvailable = request.getNumAvailable();
        boolean isExternal = request.getIsExternal();
        String externalLink = request.getExternalLink();
        boolean needResume = request.getNeedResume();
        boolean needCover = request.getNeedCover();


        Job job = new Job(creator, position, company, address, description, type, numAvailable, isExternal, externalLink, needResume, needCover);
        jobRepository.save(job);

        Executor executor = Executors.newSingleThreadExecutor();
        JobNotificationSender notificationSender = new JobNotificationSender(job, jobFilterRepository, notificationService);
        executor.execute(notificationSender);

        return "redirect:/job/" + job.getID();
    }

    /**
     * Create a new job application.
     *
     * @param request   the job application request.
     * @param applicant the user submitting the application.
     * @param jobId     the job id.
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

        CoverLetter defaultCoverLetter = coverLetterRepository.findByAppUser(applicant);
        String encodedCoverLetter = Base64.getEncoder().encodeToString(coverLetter.getBytes());

        if(job.getNeedResume() && encodedResume.isEmpty() && defaultResume == null) {
            throw new IllegalStateException("It is mandatory to upload your resume.");
        }
        else if(encodedResume.isEmpty() && defaultResume != null) {
            jobApplication.setResume(defaultResume.getDefaultResume());
        }
        else if(!encodedResume.isEmpty() && defaultResume == null) {
            jobApplication.setResume(encodedResume);
        }
        else if(!encodedResume.isEmpty() && defaultResume != null) {
            jobApplication.setResume(encodedResume);
        }

        if(job.getNeedCover() && encodedCoverLetter.isEmpty() && defaultCoverLetter == null) {
            throw new IllegalStateException("It is mandatory to upload your cover letter.");
        }
        else if(encodedCoverLetter.isEmpty() && defaultCoverLetter != null) {
            jobApplication.setCoverLetter(defaultCoverLetter.getDefaultCoverLetter());
        }
        else if(!encodedCoverLetter.isEmpty() && defaultCoverLetter == null) {
            jobApplication.setCoverLetter(encodedCoverLetter);
        }
        else if(!encodedCoverLetter.isEmpty() && defaultCoverLetter != null) {
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
     * @param jobId     the job id.
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
     * @param request     the job request.
     * @return the job page.
     */
    public String editJob(Optional<Job> optionalJob, JobRequest request) throws Exception {

        Job job = optionalJob.get();

        if (job == null)
            return "redirect:/";

        if (request.getIsExternal()) {
            isValidURL(request.getExternalLink());
        }

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

        jobRepository.save(job);


        Executor executor = Executors.newSingleThreadExecutor();
        JobNotificationSender notificationSender = new JobNotificationSender(job, jobFilterRepository, notificationService);
        executor.execute(notificationSender);

        return "redirect:/job/" + job.getID();
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
     * @param jobType          the type of job (fulltime, parttime, contract, etc).
     * @param showInternalJobs true if we want internal jobs to be retrieved; otherwise false.
     * @param showExternalJobs true if we want external jobs to be retrieved; otherwise false.
     * @return a list of jobs that meet the filtered preferences.
     */
    public List<Job> getAllJobsByFilter(JobType jobType, boolean showInternalJobs, boolean showExternalJobs) {
        List<Job> jobs = new ArrayList<>();

        if (jobType == JobType.ANY) {
            if (showInternalJobs) {
                jobs.addAll(jobRepository.findInternalJobs());
            }

            if (showExternalJobs) {
                jobs.addAll(jobRepository.findExternalJobs());
            }

        } else {
            if (showInternalJobs) {
                jobs.addAll(jobRepository.findInternalJobsByJobType(jobType));
            }

            if (showExternalJobs) {
                jobs.addAll(jobRepository.findExternalJobsByJobType(jobType));
            }
        }

        return jobs;
    }

    /**
     * Retrieve all jobs given the filter preferences and the search term.
     *
     * @param jobType          the type of job (fulltime, parttime, contract, etc).
     * @param showInternalJobs true if we want internal jobs to be retrieved; otherwise false.
     * @param showExternalJobs true if we want external jobs to be retrieved; otherwise false.
     * @param searchTerm       the search key for the jobs being looked up.
     * @return
     */
    public List<Job> getAllJobsByFilterAndSearchTerm(JobType jobType, boolean showInternalJobs, boolean showExternalJobs, String searchTerm) {
        List<Job> jobs = new ArrayList<>();

        if (jobType == JobType.ANY) {
            if (showInternalJobs) {
                jobs.addAll(jobRepository.findInternalJobsBySearchTerm(searchTerm));
            }

            if (showExternalJobs) {
                jobs.addAll(jobRepository.findExternalJobsBySearchTerm(searchTerm));
            }

        } else {
            if (showInternalJobs) {
                jobs.addAll(jobRepository.findInternalJobsByJobTypeAndSearchTerm(jobType, searchTerm));
            }

            if (showExternalJobs) {
                jobs.addAll(jobRepository.findExternalJobsByJobTypeAndSearchTerm(jobType, searchTerm));
            }
        }

        return jobs;
    }

    /**
     * Save a job filter preference for a user.
     *
     * @param appUser          the user whose filter preferences is saved.
     * @param jobType          the type of job preference (fulltime, parttime, contract, etc).
     * @param showInternalJobs true if we want internal jobs to be retrieved; otherwise false.
     * @param showExternalJobs true if we want external jobs to be retrieved; otherwise false.
     * @param searchTerm       the search key for the jobs being looked up.
     * @return the newly created job filter.
     */
    public JobFilter saveJobFilter(AppUser appUser, JobType jobType, boolean showInternalJobs, boolean showExternalJobs, String searchTerm) {
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
            jobFilter.setSearchTerm(searchTerm);
        } else {
            jobFilter = new JobFilter(appUser, jobType, showInternalJobs, showExternalJobs, searchTerm);
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

    /**
     * Gets the cover letter of a given app user.
     *
     * @param appUser an object representing the app user
     * @return the cover letter of the app user.
     */
    public CoverLetter getCoverLetterByAppUser(AppUser appUser) {
        return coverLetterRepository.findByAppUser(appUser);
    }
}
