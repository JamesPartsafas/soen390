package com.soen.synapsis.unit.appuser.profile.companyprofiletest;


import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.Role;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfile;
import com.soen.synapsis.appuser.profile.companyprofile.CompanyProfileRepository;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileRequest;
import com.soen.synapsis.appuser.profile.companyprofile.updateprofile.UpdateCompanyProfileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UpdateCompanyProfileServiceTest {
    private UpdateCompanyProfileService underTest;
    @Mock
    private CompanyProfileRepository companyProfileRepository;

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
        String description = "description";
        String website = "www.google.com";
        String industry = "Construction";
        String companySize = "1800";
        String location = "CANADA";
        String speciality = "Material";
        UpdateCompanyProfileRequest request = new UpdateCompanyProfileRequest(description, website, industry, companySize, location, speciality);

        var appUser = new AppUser(1L, "google", "12345678", "google@mail.com", Role.COMPANY);

        when(companyProfileRepository.findByAppUser(appUser)).thenReturn(new CompanyProfile());

        underTest.updateProfile(request, appUser);
    }

    @Test
    void updateCompanyProfileFail() {
        var appUser = new AppUser(1L, "google", "12345678", "google@mail.com", Role.COMPANY);

        when(companyProfileRepository.findByAppUser(appUser)).thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> underTest.updateProfile(new UpdateCompanyProfileRequest(), appUser));
    }

    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
