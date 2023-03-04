package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/jobs")
    public String viewJobPosting(Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        List<Job> jobs = jobService.getAllJobs();

        AppUser candidate = authService.getAuthenticatedUser();
        List<Job> jobsSubmitted = jobService.getAllJobsAlreadySubmittedByUser(candidate);

        model.addAttribute("jobs", jobs);
        model.addAttribute("jobsSubmitted", jobsSubmitted);

        return "pages/jobs";
    }

    @GetMapping("/job/{jid}")
    public String getJob(@PathVariable Long jid, Model model) {
        Optional<Job> optionalJob = jobService.getJob(jid);


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
        model.addAttribute("jid", job.getID());
        model.addAttribute("is_external", job.getIsExternal());
        model.addAttribute("external_link", job.getExternalLink());
        model.addAttribute("need_resume", job.getNeedResume());
        model.addAttribute("need_cover", job.getNeedCover());
        model.addAttribute("need_portfolio", job.getNeedPortfolio());

        return "pages/job";
    }

    @GetMapping(value = "/createjob")
    public String createJob(Model model) {
        if (!authService.isUserAuthenticated() || authService.getAuthenticatedUser().getRole() != Role.RECRUITER)
            return "redirect:/";

        model.addAttribute("jobRequest", new JobRequest());
        return "pages/createjob";
    }

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

        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error creating a new job. " + e.getMessage());
            return createJob(model);
        }
    }


    @GetMapping("/jobapplication/{jobID}")
    public String getJobApplication(@PathVariable Long jobID, Model model) {
        if (!authService.isUserAuthenticated()) {
            return "redirect:/";
        }

        Optional<Job> retrievedJob = jobService.getJob(jobID);

        if (retrievedJob.isEmpty()) {
            return "redirect:/jobs";
        }

        AppUser applicant = authService.getAuthenticatedUser();

        try {
            jobService.checkIfUserAlreadySubmittedApplication(applicant, jobID);
        } catch(Exception e) {
            return "redirect:/jobs";
        }

        Job job = retrievedJob.get();

        model.addAttribute("email", applicant.getEmail());
        model.addAttribute("jobid", job.getID());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("position", job.getPosition());

        return "pages/jobapplicationform";
    }

    @PostMapping("/jobapplication")
    public String createJobApplication(JobApplication request, BindingResult bindingResult, Model model, @RequestParam("jobid") Long jobID) {
        try {
            if (!authService.isUserAuthenticated()) {
                return "redirect:/";
            }

            if (bindingResult.hasErrors()) {
                throw new Exception("There was a problem processing your application. Try again later.");
            }

            AppUser applicant = authService.getAuthenticatedUser();
            request.setApplicant(applicant);

            jobService.createJobApplication(request, applicant, jobID);

            return "redirect:/applicationsuccess";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return getJobApplication(jobID, model);
        }
    }

    @GetMapping("/applicationsuccess")
    public String returnJobApplicationSuccess() {
        return "pages/applicationsuccess";
    }

    @PostMapping("/deletejob")
    public String deleteJob(@RequestParam("jid") Long jid) {
        Optional<Job> optionalJob = jobService.getJob(jid);

        if (optionalJob.isEmpty() || authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
            return "redirect:/";

        return jobService.deleteJob(jid);

    }

    @GetMapping("/editjob")
    public String editJob(@RequestParam("jid") Long jid, Model model) {
        if (jid == null)
            return "redirect:/";
        Optional<Job> optionalJob = jobService.getJob(jid);
        if (optionalJob.isEmpty())
            return "redirect:/";
        if (authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
            return "redirect:/job/" + jid;

        Job job = optionalJob.get();
        model.addAttribute("jobRequest", new JobRequest());
        model.addAttribute("jid", jid);
        model.addAttribute("creator", job.getCreator().getName());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("address", job.getAddress());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("type", job.getType());
        model.addAttribute("description", job.getDescription());
        model.addAttribute("num_available", job.getNumAvailable());
        model.addAttribute("num_applicants", job.getNumApplicants());
        model.addAttribute("jid", job.getID());
        model.addAttribute("is_external", job.getIsExternal());
        model.addAttribute("external_link", job.getExternalLink());
        model.addAttribute("need_resume", job.getNeedResume());
        model.addAttribute("need_cover", job.getNeedCover());
        model.addAttribute("need_portfolio", job.getNeedPortfolio());

        return "pages/editjob";
    }

    @PostMapping("/editjob")
    public String editJob(@RequestParam("jid") Long jid, JobRequest request, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            Optional<Job> optionalJob = jobService.getJob(jid);
            if (optionalJob.isEmpty())
                return "redirect:/";
            if (authService.getAuthenticatedUser().getId() != optionalJob.get().getCreator().getId())
                return "redirect:/job/" + jid;

            AppUser creator = authService.getAuthenticatedUser();
            request.setCreator(creator);
            String response = jobService.editJob(optionalJob, request);

            return response;

        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error editing the job. " + e.getMessage());
            return editJob(jid, model);
        }
    }

}
