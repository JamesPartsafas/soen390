package com.soen.synapsis.appuser.profile.appuserprofile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserProfileRepository extends JpaRepository<AppUserProfile, Long> {
    AppUserProfile findByAppUser(AppUser appUser);

}