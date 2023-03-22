package com.soen.synapsis.appuser.job;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFilterRepository extends JpaRepository<JobFilter, Long> {

    /**
     * Retrieve the job filter set by the app user.
     *
     * @param appUser the user who we are searching for.
     * @return the job filter preference.
     */
    Optional<JobFilter> findJobFilterByAppUser(AppUser appUser);

    @Query(value = "SELECT jf FROM JobFilter jf  WHERE jf.jobType = :type and jf.showInternalJobs = true")
    List<JobFilter> findAllInternalJobFiltersMatchingJob(@Param("type") JobType type);

    @Query(value = "SELECT jf FROM JobFilter jf  WHERE jf.jobType = :type and jf.showExternalJobs = true")
    List<JobFilter> findAllExternalJobFiltersMatchingJob(@Param("type") JobType type);
}
