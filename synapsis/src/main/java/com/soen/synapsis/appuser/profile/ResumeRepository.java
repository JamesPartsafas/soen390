package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    /**
     * Retrieve the default resume of an app user.
     * @param appUser the object representing an app user.
     * @return the DefaultResume of an app user.
     */
    Resume findByAppUser(AppUser appUser);
}
