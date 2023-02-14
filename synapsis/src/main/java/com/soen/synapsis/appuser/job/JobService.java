package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Authenticator;
import java.util.Optional;

@Component
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

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

        return "redirect:/job/" + job.getID();
    }
}
