package com.soen.synapsis.appuser.profile.companyprofile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    /**
     * Retrieve the profile of a company user.
     * @param appUser the object representing a company user.
     * @return the CompanyProfile of a company user.
     */
    CompanyProfile findByAppUser(AppUser appUser);
}