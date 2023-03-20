package com.soen.synapsis.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for Settings-related functionality
 */
@Service
public class SettingsService {
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    /**
     * @param request Request object containing settings data
     * @param appUser AppUser for which the settings should be updated
     */
    public void updateSettings(UpdateSettingsRequest request, AppUser appUser) {
        Settings settings = settingsRepository.findByAppUser(appUser);

        settings.setJobEmailNotificationsOn(request.isJobEmailNotificationsOn());
        settings.setMessageEmailNotificationsOn(request.isMessageEmailNotificationsOn());
        settings.setConnectionEmailNotificationsOn(request.isConnectionEmailNotificationsOn());

        settingsRepository.save(settings);
        appUser.setSettings(settings);
    }
}
