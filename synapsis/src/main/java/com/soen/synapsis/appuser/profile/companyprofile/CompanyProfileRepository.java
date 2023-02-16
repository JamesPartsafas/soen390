package com.soen.synapsis.appuser.profile.companyprofile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {
    CompanyProfile findByAppUser(AppUser appUser);
}