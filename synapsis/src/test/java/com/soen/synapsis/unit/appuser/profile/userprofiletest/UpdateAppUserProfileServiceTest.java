package com.soen.synapsis.unit.appuser.profile.userprofiletest;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfileRepository;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileRequest;
import com.soen.synapsis.appuser.profile.appuserprofile.updateprofile.UpdateAppUserProfileService;
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

public class UpdateAppUserProfileServiceTest {

    private UpdateAppUserProfileService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    private AppUserService appUserService;
    private AppUserProfileRepository appUserProfileRepository;

    private UpdateAppUserProfileService updateAppUserProfileService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UpdateAppUserProfileService(appUserProfileRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void updateAppUser() {
        String education = "engineering";
        String skill = "self learner";
        String work = "developer";
        String course = "C++";
        String phone = "5144442323";
        String volunteering = "Math Tutoring";
        String project = "amaznotwebsite";
        String award = "beststudentaward";
        String language = "French";
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest(education, skill, work, course, phone, volunteering, project, award, language);

        underTest.updateProfile(request);
    }
}
