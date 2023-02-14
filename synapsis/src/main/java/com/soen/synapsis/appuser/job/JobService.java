package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.util.Optional;

@Component
public class JobService {

    private final JobRepository jobRepository;
    private final AppUserService appUserService;

    @Autowired
    public JobService(JobRepository jobRepository, AppUserService appUserService) {
        this.jobRepository = jobRepository;
        this.appUserService = appUserService;
    }

    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    /*public String createJobPosting (AppUser creator, String position, String company, String address, String description, JobType type, int numAvailable) {
        if (creator.getRole() != Role.RECRUITER) {
            throw new IllegalStateException("This user is not a recruiter.");
        }
        jobRepository.save(new Job(creator, position, company, address, description, type, numAvailable));
        return "pages/jobs";
    }*/

    public String createJob (JobRequest request) {

        if (request.getCreator().getRole() != Role.RECRUITER) {
            throw new IllegalStateException("This user is not a recruiter.");
        }

        AppUser creator = request.getCreator();
        String position = request.getPosition();
        String company = request.getCompany();
        String address = request.getAddress();
        String description = request.getDescription();
        JobType type = request.getType();
        int numAvailable = request.getNumAvailable();

        Job job = new Job(creator, position, company, address, description, type, numAvailable);
        jobRepository.save(job);

        return "pages/job/";
    }
}
