package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.CoverLetter;
import com.soen.synapsis.appuser.profile.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A controller class to work with jobs.
 */
@Controller
public class JobController {

    private final JobService jobService;
    private final AuthService authService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
        this.authService = new AuthService();
    }

    public JobController(JobService jobService, AuthService authService) {
        this.jobService = jobService;
        this.authService = authService;
    }

    /**
     * Retrieve and display all job postings.
     *
     * @param jobType          the type of job (fulltime, parttime, contract, etc.).
     * @param showInternalJobs the filtered show internal jobs preference (yes/no).
     * @param showExternalJobs the filtered show external jobs preference (yes/no).
     * @param isFilteringJobs  boolean value to see if the filter has been called.
     * @param searchTerm       jobs to search for.
     * @param model            an object carrying data attributes passed to the view.
     * @return the view containing all jobs.
     */
    @GetMapping("/jobs")
    public String viewJobPosting(@RequestParam(required = false) JobType jobType, @RequestParam(required = false) boolean showInternalJobs, @RequestParam(required = false) boolean showExternalJobs, @RequestParam(required = false) boolean isFilteringJobs, @RequestParam(required = false) String searchTerm, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<Job> jobs;
        JobFilter jobFilter;

        if (isFilteringJobs && searchTerm != null) {
            jobs = jobService.getAllJobsByFilterAndSearchTerm(jobType, showInternalJobs, showExternalJobs, searchTerm);

            jobFilter = jobService.saveJobFilter(authService.getAuthenticatedUser(), jobType, showInternalJobs, showExternalJobs, searchTerm);

            model.addAttribute("jobTypeFilter", jobFilter.getJobType());
            model.addAttribute("showInternalJobsFilter", jobFilter.isShowInternalJobs());
            model.addAttribute("showExternalJobsFilter", jobFilter.isShowExternalJobs());
        }
        else if (isFilteringJobs) {
            jobs = jobService.getAllJobsByFilter(jobType, showInternalJobs, showExternalJobs);

            jobFilter = jobService.saveJobFilter(authService.getAuthenticatedUser(), jobType, showInternalJobs, showExternalJobs, null);

            model.addAttribute("jobTypeFilter", jobFilter.getJobType());
            model.addAttribute("showInternalJobsFilter", jobFilter.isShowInternalJobs());
            model.addAttribute("showExternalJobsFilter", jobFilter.isShowExternalJobs());
        } else if (searchTerm != null) {
            jobs = jobService.getAllJobsBySearch(searchTerm.toLowerCase());

            jobService.saveJobFilter(authService.getAuthenticatedUser(), JobType.ANY, true, true, searchTerm);
        } else {
            jobs = jobService.getAllJobs();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobsSubmitted", jobService.getAllJobsAlreadySubmittedByUser(authService.getAuthenticatedUser()));
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("savedJobs", authService.getAuthenticatedUser().getSavedJobs());
        model.addAttribute("jobTypes", JobType.values());
        model.addAttribute("role", authService.getAuthenticatedUser().getRole());

        return "pages/jobs";
    }

    /**
     * Retrieve all the information for a specific job.
     *
     * @param jobId the id of the specific job.
     * @param model an object carrying data attributes passed to the view.
     * @return the jobs page.
     */
    @GetMapping("/job/{jobId}")
    public String getJob(@PathVariable Long jobId, Model model) {
        Optional<Job> optionalJob = jobService.getJob(jobId);


        if (optionalJob.isEmpty() || !authService.isUserAuthenticated())
            return "redirect:/";

        Job job = optionalJob.get();

        model.addAttribute("authorization", authService.getAuthenticatedUser().getId() == optionalJob.get().getCreator().getId());
        model.addAttribute("role", authService.getAuthenticatedUser().getRole());
        model.addAttribute("creator", job.getCreator().getName());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("address", job.getAddress());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("type", job.getType());
        model.addAttribute("description", job.getDescription());
        model.addAttribute("num_available", job.getNumAvailable());
        model.addAttribute("num_applicants", job.getNumApplicants());
        model.addAttribute("jobId", job.getID());
        model.addAttribute("is_external", job.getIsExternal());
        model.addAttribute("external_link", job.getExternalLink());
        model.addAttribute("need_resume", job.getNeedResume());
        model.addAttribute("need_cover", job.getNeedCover());
        model.addAttribute("need_portfolio", job.getNeedPortfolio());
        model.addAttribute("savedJobs", authService.getAuthenticatedUser().getSavedJobs());

        AppUser candidate = authService.getAuthenticatedUser();
        List<Job> jobsSubmitted = jobService.getAllJobsAlreadySubmittedByUser(candidate);
        model.addAttribute("isJobSubmitted", jobsSubmitted.contains(job));

        if (job.getIsExternal()) {
            return "pages/jobApplicationExternal";
        } else {
            return "pages/job";
        }
    }

    /**
     * Get a job posting.
     *
     * @param model an object carrying data attributes passed to the view.
     * @return a page of the created job.
     */
    @GetMapping(value = "/createjob")
    public String createJob(Model model) {
        if (!authService.isUserAuthenticated() || authService.getAuthenticatedUser().getRole() != Role.RECRUITER)
            return "redirect:/";

        model.addAttribute("jobRequest", new JobRequest());
        return "pages/createJob";
    }

    /**
     * Create a job posting.
     *
     * @param request       the job application request.
     * @param bindingResult an object that contains errors that may have occurred.
     * @param model         an object carrying data attributes passed to the view.
     * @return the created job page.
     */
    @PostMapping("/createjob")
    public String createJob(JobRequest request, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            AppUser creator = authService.getAuthenticatedUser();
            request.setCreator(creator);
            String response = jobService.createJob(request);

            return response;

        } catch (Exception e) {
            model.addAttribute("error", "There was an error creating a new job. " + e.getMessage());
            model.addAttribute("request", request);
            return createJob(model);
        }
    }

    /**
     * Retrieve the job application form for a job posting.
     *
     * @param jobId the id of the job posting.
     * @param model an object carrying data attributes passed to the view.
     * @return the job application page.
     */
    @GetMapping("/jobapplication/{jobId}")
    public String getJobApplication(@PathVariable Long jobId, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<Job> retrievedJob = jobService.getJob(jobId);

        if (retrievedJob.isEmpty()) {
            return "redirect:/jobs";
        }

        AppUser applicant = authService.getAuthenticatedUser();

        try {
            jobService.checkIfUserAlreadySubmittedApplication(applicant, jobId);
        } catch (Exception e) {
            return "redirect:/jobs";
        }

        Job job = retrievedJob.get();

        model.addAttribute("jobId", job.getID());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("need_resume", job.getNeedResume());
        model.addAttribute("need_cover", job.getNeedCover());

        Resume resume = jobService.getResumeByAppUser(applicant);
        CoverLetter coverLetter = jobService.getCoverLetterByAppUser(applicant);

        if (resume != null) {
            model.addAttribute("default_resume", resume.getFileName());
        }

        if (coverLetter != null) {
            model.addAttribute("default_cover_letter", coverLetter.getFileName());
        }

        if (job.getIsExternal()) {
            model.addAttribute("authorization", authService.getAuthenticatedUser().getId() == retrievedJob.get().getCreator().getId());
            model.addAttribute("role", authService.getAuthenticatedUser().getRole());
            model.addAttribute("creator", job.getCreator().getName());
            model.addAttribute("address", job.getAddress());
            model.addAttribute("type", job.getType());
            model.addAttribute("description", job.getDescription());
            model.addAttribute("num_available", job.getNumAvailable());
            model.addAttribute("num_applicants", job.getNumApplicants());
            model.addAttribute("is_external", job.getIsExternal());
            model.addAttribute("external_link", job.getExternalLink());
            model.addAttribute("need_portfolio", job.getNeedPortfolio());

            return "pages/jobapplicationexternal";
        } else {
            model.addAttribute("email", applicant.getEmail());

            return "pages/jobapplicationform";
        }
    }

    /**
     * Submit a job application form.
     *
     * @param request       the applicant's request data.
     * @param bindingResult an object that contains errors that may have occurred.
     * @param model         an object carrying data attributes passed to the view.
     * @param jobId         the job id.
     * @return the application success page.
     */
    @PostMapping("/jobapplication")
    public String createJobApplication(JobApplicationRequest request, @RequestParam("jobId") Long jobId, @RequestParam("resume") MultipartFile resume, @RequestParam("coverLetter") MultipartFile coverLetter, BindingResult bindingResult, Model model) {
        try {
            if (!authService.isUserAuthenticated()) {
                return "redirect:/";
            }

            if (bindingResult.hasErrors()) {
                throw new Exception("There was a problem processing your application. Try again later.");
            }

            AppUser applicant = authService.getAuthenticatedUser();
            request.setApplicant(applicant);

            jobService.createJobApplication(request, applicant, jobId, resume, coverLetter);

            return "redirect:/applicationsuccess";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return getJobApplication(jobId, model);
        }
    }

    /**
     * Display the job application success page.
     *
     * @return the application success page.
     */
    @GetMapping("/applicationsuccess")
    public String returnJobApplicationSuccess() {
        return "pages/applicationsuccess";
    }

    /**
     * Delete a specific job.
     *
     * @param jobId the job id.
     * @return the jobs page.
     */
    @PostMapping("/deletejob")
    public String deleteJob(@RequestParam("jobId") Long jobId) {
        Optional<Job> optionalJob = jobService.getJob(jobId);

        if (optionalJob.isEmpty() || authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
            return "redirect:/";

        return jobService.deleteJob(jobId);

    }

    /**
     * Display the edit job page.
     *
     * @param jobId the job id.
     * @param model an object carrying data attributes passed to the view.
     * @return the edit job page.
     */
    @GetMapping("/editjob")
    public String editJob(@RequestParam("jobId") Long jobId, Model model) {
        if (jobId == null)
            return "redirect:/";
        Optional<Job> optionalJob = jobService.getJob(jobId);
        if (optionalJob.isEmpty())
            return "redirect:/";
        if (authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
            return "redirect:/job/" + jobId;

        Job job = optionalJob.get();
        model.addAttribute("jobRequest", new JobRequest());
        model.addAttribute("jobId", jobId);
        model.addAttribute("creator", job.getCreator().getName());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("address", job.getAddress());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("type", job.getType());
        model.addAttribute("description", job.getDescription());
        model.addAttribute("num_available", job.getNumAvailable());
        model.addAttribute("num_applicants", job.getNumApplicants());
        model.addAttribute("jobId", job.getID());
        model.addAttribute("is_external", job.getIsExternal());
        model.addAttribute("external_link", job.getExternalLink());
        model.addAttribute("need_resume", job.getNeedResume());
        model.addAttribute("need_cover", job.getNeedCover());
        model.addAttribute("need_portfolio", job.getNeedPortfolio());

        return "pages/editjob";
    }

    /**
     * Edit a specific job.
     *
     * @param jobId         the job id.
     * @param request       the job request.
     * @param bindingResult an object that contains errors that may have occurred.
     * @param model         an object carrying data attributes passed to the view.
     * @return the edit job page.
     */
    @PostMapping("/editjob")
    public String editJob(@RequestParam("jobId") Long jobId, JobRequest request, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            Optional<Job> optionalJob = jobService.getJob(jobId);
            if (optionalJob.isEmpty())
                return "redirect:/";
            if (authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
                return "redirect:/job/" + jobId;

            AppUser creator = authService.getAuthenticatedUser();
            request.setCreator(creator);
            String response = jobService.editJob(optionalJob, request);

            return response;

        } catch (Exception e) {
            model.addAttribute("error", "There was an error editing the job. " + e.getMessage());
            return editJob(jobId, model);
        }
    }

    /**
     * Allows users to view all saved jobs.
     * @param model An object carrying data attributes passed to the view.
     * @return View containing saved jobs. If the requester is not authenticated, redirects to home page.
     */
    @GetMapping("/savedjobs")
    public String getSavedJobs(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        AppUser appUser = authService.getAuthenticatedUser();
        Set<Long> savedJobs = appUser.getSavedJobs();


        List<Optional<Job>> jobs = new ArrayList<Optional<Job>>();
        for (Long jid : savedJobs) {
            jobs.add(jobService.getJob(jid));
        }

        List<Job> jobsSubmitted = jobService.getAllJobsAlreadySubmittedByUser(appUser);

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobsSubmitted", jobsSubmitted);

        return "pages/savedjobs";
    }
}
