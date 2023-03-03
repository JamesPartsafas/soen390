package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.Authenticator;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
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
        Boolean isExternal = request.getIsExternal();
        String externalLink = request.getExternalLink();

        Job job = new Job(creator, position, company, address, description, type, numAvailable, isExternal, externalLink);
        jobRepository.save(job);

        return "redirect:/job/" + job.getID();
    }

    public String deleteJob (Long id) {
        jobRepository.deleteById(id);
        return "redirect:/jobs";
    }
}
