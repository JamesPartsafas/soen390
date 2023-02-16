package com.soen.synapsis.unit.appuser.profile.companyprofiletest;


import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
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

class
UpdateCompanyProfileControllerTest {
    @Mock
    private UpdateCompanyProfileService updateCompanyProfileService;
    private AutoCloseable autoCloseable;
    private UpdateCompanyProfileController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UpdateCompanyProfileController(updateCompanyProfileService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void viewUpdateCompanyPage() {
        String returnedPage = underTest.updateAppUserProfile(Mockito.mock(Model.class));

        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void UpdateCompanyInfo() {
        UpdateCompanyProfileRequest request = new UpdateCompanyProfileRequest("www.google.come", "technology", "10000", "USA", "Social");

        underTest.updateAppUserProfile(request, mock(BindingResult.class), mock(Model.class));

        updateCompanyProfileService.updateProfile(request, new AppUser(1L, "google",
                "12345678", "google@mail.com", Role.COMPANY));
    }

    @Test
    void updateWithErrors() {
        UpdateCompanyProfileRequest request = new UpdateCompanyProfileRequest("www.google.come", "technology", "10000", "USA", "Social");
        Model model = mock(Model.class);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        underTest.updateAppUserProfile(request, bindingResult, model);

        updateCompanyProfileService.updateProfile(request, new AppUser(1L, "google",
                "12345678", "google@mail.com", Role.COMPANY));
    }


    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}