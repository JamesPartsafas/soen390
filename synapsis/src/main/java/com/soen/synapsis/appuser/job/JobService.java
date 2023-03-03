package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobService(JobRepository jobRepository, JobApplicationRepository jobApplicationRepository) {
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getAllJobsAlreadySubmittedByUser(AppUser user) {
        return jobRepository.findAllJobApplicationsSubmittedByUserID(user.getId());
    }

    public Optional<Job> getJob(Long id) {
        return jobRepository.findById(id);
    }

    public String createJob(JobRequest request) {

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

    public void createJobApplication(JobApplication request, AppUser applicant, Long jobID) {

        checkIfUserAlreadySubmittedApplication(applicant, jobID);

        if (applicant.getRole() != Role.CANDIDATE) {
            throw new IllegalStateException("You are not a candidate. Only candidates can submit application forms.");
        }

        Job job = jobRepository.getReferenceById(jobID);

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
                request.getResume(),
                request.getCoverLetter(),
                request.isLegallyAllowedToWork(),
                request.getLinks()
        );

        jobApplicationRepository.save(jobApplication);
    }

    public void checkIfUserAlreadySubmittedApplication(AppUser applicant, Long jobID) {
        List<Job> jobsSubmitted = getAllJobsAlreadySubmittedByUser(applicant);
        for (Job job : jobsSubmitted) {
            if (job.getID() == jobID) {
                throw new IllegalStateException("You already submitted an application form for this job.");
            }
        }
    }

}
