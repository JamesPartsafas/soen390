package com.soen.synapsis.unit.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.settings.Settings;
import com.soen.synapsis.appuser.settings.SettingsRepository;
import com.soen.synapsis.appuser.settings.SettingsService;
import com.soen.synapsis.appuser.settings.UpdateSettingsRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SettingsServiceTest {
    private SettingsService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private SettingsRepository settingsRepository;

    @BeforeEach
    public void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new SettingsService(settingsRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void updateSettingsSuccessful() {
        AppUser appUser = mock(AppUser.class);
        Settings settings = new Settings(1L, appUser, true, true, true);
        when(settingsRepository.findByAppUser(appUser)).thenReturn(settings);

        underTest.updateSettings(new UpdateSettingsRequest(), appUser);

        verify(settingsRepository).save(settings);
    }
}
