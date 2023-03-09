package com.soen.synapsis.appuser.profile;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, Long> {
    /**
     * Retrieve the profile picture of an app user.
     * @param appUser the object representing an app user.
     * @return the ProfilePicture of an app user.
     */
    ProfilePicture findByAppUser(AppUser appUser);
}
