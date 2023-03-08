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
    List<Job> findAllJobApplicationsSubmittedByUserID(@Param("userID") Long userID);
}
