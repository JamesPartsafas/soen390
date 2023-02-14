package com.soen.synapsis.unit.appuser.profile.userprofiletest;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileController;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileRequest;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileService;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileController;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileRequest;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateAppUserProfileControllerTest {

    @Mock
    private UpdateAppUserProfileService updateAppUserProfileService;

    private AutoCloseable autoCloseable;
    private UpdateAppUserProfileController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UpdateAppUserProfileController(updateAppUserProfileService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewUpdateAppUserPage() {
        String returnedPage = underTest.updateAppUserProfile(Mockito.mock(Model.class));
        assertEquals("redirect:/", returnedPage);
    }


    @Test
    void UpdateAppUserInfo() {
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest("Computer Science", "Problem-solving", "developer", "5144433344", "food delivery", "Data Structures", "ConUhack project", "2022 best employee", "English");
        underTest.updateAppUserProfile(request, mock(BindingResult.class), mock(Model.class));
        updateAppUserProfileService.updateProfile(request);
    }

    @Test
    void updateWithErrors() {
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest("Computer Science", "Problem-solving", "developer", "5144433344", "food delivery", "Data Structures", "ConUhack project", "2022 best employee", "English");
        Model model = mock(Model.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.updateAppUserProfile(request, bindingResult, model);
        updateAppUserProfileService.updateProfile(request);
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}