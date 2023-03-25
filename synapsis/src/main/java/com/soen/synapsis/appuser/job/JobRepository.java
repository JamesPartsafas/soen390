package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>{

    /**
     * Retrieve all job applications submitted by a user.
     *
     * @param userID the id of the user.
     * @return a list of all job applications submitted.
     */
    @Query(value = "SELECT j FROM Job j JOIN JobApplication ja ON j.id = ja.job.id WHERE ja.applicant.id = :userID")
    List<Job> findAllJobsAlreadySubmittedByUserID(@Param("userID") Long userID);

    /**
     * Retrieve all jobs given a search query.
     *
     * @param search the search query.
     * @return a list of jobs that meet the search criteria.
     */
    @Query(value = "SELECT j FROM Job j WHERE LOWER(j.company) LIKE %:search% OR LOWER(j.position) LIKE %:search%")
    List<Job> findJobsBySearch(@Param("search") String search);

    /**
     * Retrieve all internal jobs with the specified job type.
     *
     * @param jobType the type of job (fulltime, parttime, contract, etc.).
     * @return a list of internal jobs that meet the job type preference.
     */
    @Query(value = "SELECT j FROM Job j WHERE j.type = :jobType AND j.isExternal = false")
    List<Job> findInternalJobsByJobType(@Param("jobType") JobType jobType);

    /**
     * Retrieve all external jobs with the specified job type.
     *
     * @param jobType the type of job (fulltime, parttime, contract, etc.).
     * @return a list of external jobs that meet the job type preference.
     */
    @Query(value = "SELECT j FROM Job j WHERE j.type = :jobType AND j.isExternal = true")
    List<Job> findExternalJobsByJobType(@Param("jobType") JobType jobType);
}
