package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserDetails;
import com.soen.synapsis.appuser.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class JobController {

    private final JobService jobService;
    private final AppUserService appUserService;

    @Autowired
    public JobController(JobService jobService, AppUserService appUserService) {
        this.jobService = jobService;
        this.appUserService = appUserService;
    }

    @GetMapping("/job/{jid}")
    public String getJob(@PathVariable Long jid, Model model) {
        Optional<Job> optionalJob = jobService.getJob(jid);

        if (optionalJob.isEmpty())
            return "redirect:/";

        Job job = optionalJob.get();

        model.addAttribute("creator", job.getCreator().getName());
        model.addAttribute("company", job.getCompany());
        model.addAttribute("address", job.getAddress());
        model.addAttribute("position", job.getPosition());
        model.addAttribute("type", job.getType());
        model.addAttribute("description", job.getDescription());
        model.addAttribute("num_available", job.getNumAvailable());
        model.addAttribute("num_applicants", job.getNumApplicants());

        return "pages/job";
    }

    @GetMapping(value = "/createjob")
    public String createJob(Model model) {
        model.addAttribute("jobRequest", new JobRequest());
        return "pages/createjob";
    }

    @PostMapping("/createjob")
    public String createJob(JobRequest request, BindingResult bindingResult, HttpServletRequest servlet, Model model, @AuthenticationPrincipal AppUserDetails creatorDetails) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception();
            }

            AppUser creator = appUserService.getAppUser(creatorDetails.getUsername());
            request.setCreator(creator);
            String response = jobService.createJob(request);

            return response;

        }
        catch (Exception e) {
            model.addAttribute("error", "There was an error creating a new job. " + e.getMessage());
            return "pages/createjob";
        }
    }

}
