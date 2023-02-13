package com.soen.synapsis.unit.appuser.profile.companyprofiletest;


import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileRequest;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UpdateCompanyProfileServiceTest {
    private UpdateCompanyProfileService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    private AppUserService appUserService;
    private CompanyProfileRepository companyProfileRepository;

    private UpdateCompanyProfileService updateCompanyProfileService;

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
}
