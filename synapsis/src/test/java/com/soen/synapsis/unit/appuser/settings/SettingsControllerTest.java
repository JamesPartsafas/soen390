package com.soen.synapsis.unit.appuser.settings;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AuthService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.settings.Settings;
import com.soen.synapsis.appuser.settings.SettingsController;
import com.soen.synapsis.appuser.settings.SettingsService;
import com.soen.synapsis.appuser.settings.UpdateSettingsRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SettingsControllerTest {
    private SettingsController underTest;

    @Mock
    private AuthService authService;
    @Mock
    private SettingsService settingsService;
    @Mock
    private Model model;
    @Mock
    private AppUser appUser;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new SettingsController(settingsService, authService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getUserSettingsSuccessful() {
        Settings settings = new Settings(1L, appUser, true, true, true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        when(appUser.getSettings()).thenReturn(settings);

        String path = underTest.getUserSettings(model);

        verify(model).addAttribute("settings", settings);
        verify(model).addAttribute(ArgumentMatchers.anyString(), ArgumentMatchers.any(UpdateSettingsRequest.class));

        assertEquals(path, "pages/settings");
    }

    @Test
    void getUserSettingsFailsUnauthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);

        String path = underTest.getUserSettings(model);

        assertEquals(path, "redirect:/");
    }

    @Test
    void getUserSettingsFailsUserIsAdmin() {
        when(appUser.getRole()).thenReturn(Role.ADMIN);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        String path = underTest.getUserSettings(model);

        assertEquals(path, "redirect:/");
    }

    @Test
    void updateUserSettingsSuccessful() {
        Settings settings = new Settings(1L, appUser, true, true, true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        when(appUser.getSettings()).thenReturn(settings);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
        String path = underTest.updateUserSettings(updateSettingsRequest, bindingResult, model);

        verify(settingsService).updateSettings(updateSettingsRequest, appUser);
        assertEquals("pages/settings", path);
    }

    @Test
    void updateUserSettingsFailsUnauthenticated() {
        when(authService.isUserAuthenticated()).thenReturn(false);

        String path = underTest.updateUserSettings(new UpdateSettingsRequest(), mock(BindingResult.class), model);

        assertEquals(path, "redirect:/");
    }

    @Test
    void updateUserSettingsFailsUserIsAdmin() {
        when(appUser.getRole()).thenReturn(Role.ADMIN);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);

        String path = underTest.updateUserSettings(new UpdateSettingsRequest(), mock(BindingResult.class), model);

        assertEquals(path, "redirect:/");
    }

    @Test
    void updateUserSettingsFailsBindingError() {
        Settings settings = new Settings(1L, appUser, true, true, true);
        when(authService.isUserAuthenticated()).thenReturn(true);
        when(authService.getAuthenticatedUser()).thenReturn(appUser);
        when(appUser.getSettings()).thenReturn(settings);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest();
        Exception thrown = assertThrows(Exception.class, () -> {
            underTest.updateUserSettings(updateSettingsRequest, bindingResult, model);
        });

        assertEquals("Problem with form submission", thrown.getMessage());
    }
}
