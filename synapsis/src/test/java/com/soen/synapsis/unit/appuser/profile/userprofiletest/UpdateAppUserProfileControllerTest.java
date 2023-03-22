package com.soen.synapsis.unit.appuser.profile.userprofiletest;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileController;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileRequest;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdateAppUserProfileControllerTest {

    @Mock
    private UpdateAppUserProfileService updateAppUserProfileService;
    @Mock
    AppUserService appUserService;

    private AutoCloseable autoCloseable;
    private UpdateAppUserProfileController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UpdateAppUserProfileController(updateAppUserProfileService, appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewUpdateAppUserPage() {
        String returnedPage = underTest.updateAppUserProfile(Mockito.mock(Model.class));
        assertEquals("pages/updateuserpage", returnedPage);
    }


    @Test
    void UpdateAppUserInfo() {
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest("description", "Computer Science", "Problem-solving", "developer", "5144433344", "food delivery", "Data Structures", "ConUhack project", "2022 best employee", "English");
        underTest.updateAppUserProfile(request, mock(MultipartFile.class), mock(BindingResult.class), mock(Model.class));
        updateAppUserProfileService.updateProfile(request, new AppUser(1L, "joe",
                "12345678", "joe@mail.com", Role.CANDIDATE));
    }

    @Test
    void updateWithErrors() {
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest("description", "Computer Science", "Problem-solving", "developer", "5144433344", "food delivery", "Data Structures", "ConUhack project", "2022 best employee", "English");
        Model model = mock(Model.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.updateAppUserProfile(request, mock(MultipartFile.class), bindingResult, model);
        updateAppUserProfileService.updateProfile(request, new AppUser(1L, "joe",
                "12345678", "joe@mail.com", Role.CANDIDATE));
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}