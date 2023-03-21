package com.soen.synapsis.appuser.profile.appuserprofile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserProfileRepository extends JpaRepository<AppUserProfile, Long> {
    /**
     * Retrieve the profile of an app user.
     * @param appUser the object representing an app user.
     * @return the AppUserProfile of an app user.
     */
    AppUserProfile findByAppUser(AppUser appUser);

}