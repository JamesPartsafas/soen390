package com.soen.synapsis.unit.appuser.profile.companyprofiletest;


import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileRequest;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileService;
import com.soen.synapsis.utilities.SecurityUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class UpdateCompanyProfileServiceTest {
    private UpdateCompanyProfileService underTest;
    private  UpdateCompanyProfileRequest request;

    @Mock
    private AppUserRepository appUserRepository;
    private AppUserService appUserService;
    @Mock
    private CompanyProfileRepository companyProfileRepository;

    private UpdateCompanyProfileService updateCompanyProfileService;
    private AppUser appUser;
    private Long id;
    private String website;
    private String industry;
    private String companySize;
    private String location;
    private String speciality;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UpdateCompanyProfileService(companyProfileRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void updateCompanyUser() {
        String website = "www.google.com";
        String industry = "Construction";
        String companySize = "1800";
        String location = "CANADA";
        String speciality = "Material";
        UpdateCompanyProfileRequest request = new UpdateCompanyProfileRequest(website, industry, companySize, location, speciality);
        underTest.updateProfile(request);
    }
    @Test
    void updateCompanyProfileFail() {
        String returnedPage = underTest.updateProfile( new UpdateCompanyProfileRequest());


        assertEquals("redirect:/", returnedPage);
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
