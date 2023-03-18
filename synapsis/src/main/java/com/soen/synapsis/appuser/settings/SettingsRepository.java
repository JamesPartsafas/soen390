package com.soen.synapsis.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    /**
     * Retrieve the settings of an app user.
     *
     * @param appUser the object representing an app user.
     * @return the Settings of an app user.
     */
    Settings findByAppUser(AppUser appUser);
}
