package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverLetterRepository extends JpaRepository<CoverLetter, Long> {
    /**
     * Retrieve the default cover letter of an app user.
     * @param appUser the object representing an app user.
     * @return the CoverLetter of an app user.
     */
    CoverLetter findByAppUser(AppUser appUser);
}
