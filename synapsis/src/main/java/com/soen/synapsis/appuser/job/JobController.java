package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

        model.addAttribute("jobs", jobs);

        return "pages/jobs";
    }

    @GetMapping("/job/{jid}")
    public String getJob(@PathVariable Long jid, Model model) {
        Optional<Job> optionalJob = jobService.getJob(jid);


        if (optionalJob.isEmpty())
            return "redirect:/";

        Job job = optionalJob.get();

        model.addAttribute("authorization", authService.getAuthenticatedUser().getId() == optionalJob.get().getID());
        model.addAttribute("creator", job.getCreator().getName());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("address", job.getAddress());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("type", job.getType());
        model.addAttribute("description", job.getDescription());
        model.addAttribute("num_available", job.getNumAvailable());
        model.addAttribute("num_applicants", job.getNumApplicants());
        model.addAttribute("role",authService.getAuthenticatedUser().getRole());
        model.addAttribute("jid", job.getID());


        return "pages/job";
    }

    @GetMapping(value = "/createjob")
    public String createJob(Model model) {
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

    @GetMapping("/deletejob/{jid}")
    public String deleteJob(@PathVariable Long jid, Model model) {
        Optional<Job> optionalJob = jobService.getJob(jid);

        if (optionalJob.isEmpty() || authService.getAuthenticatedUser().getId() != optionalJob.get().getID())
            return "redirect:/";

        return jobService.deleteJob(jid);
    }

}
