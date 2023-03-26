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

    /**
     * Retrieve all the internal job filters that matches the new job's type, position, and company.
     *
     * @param type the job type (full-time, part-time, internship, etc.) of the new job.
     * @param position the title of the new job.
     * @param company the company of the new job.
     * @return a list of job filters that meet the job's criteria.
     */
    @Query(value = "SELECT jf FROM JobFilter jf  WHERE (jf.jobType = 'ANY' or jf.jobType = :type) and jf.showInternalJobs = true and (:position LIKE CONCAT('%', LOWER(jf.searchTerm), '%') or :company LIKE CONCAT('%', LOWER(jf.searchTerm), '%'))")
    List<JobFilter> findAllInternalJobFiltersMatchingJobTypeAndSearchTerm(@Param("type") JobType type, @Param("position") String position, @Param("company") String company);

    /**
     * Retrieve all the external job filters that matches the new job's type, position, and company.
     *
     * @param type the type (full-time, part-time, internship, etc.) of the new job.
     * @param position the title of the new job.
     * @param company the company of the job.
     * @return a list of job filters that meet the job's criteria.
     */
    @Query(value = "SELECT jf FROM JobFilter jf  WHERE (jf.jobType = 'ANY' or jf.jobType = :type) and jf.showExternalJobs = true and (:position LIKE CONCAT('%', LOWER(jf.searchTerm), '%') or :company LIKE CONCAT('%', LOWER(jf.searchTerm), '%'))")
    List<JobFilter> findAllExternalJobFiltersMatchingJobTypeAndSearchTerm(@Param("type") JobType type, @Param("position") String position, @Param("company") String company);
}
