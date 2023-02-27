package com.soen.synapsis.unit.appuser.profile.userprofiletest;

import com.soen.synapsis.appuser.*;
import com.soen.synapsis.appuser.profile.appuserprofile.AppUserProfile;
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
    private AppUserProfileRepository appUserProfileRepository;

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
        String description = "description";
        String education = "engineering";
        String skill = "self learner";
        String work = "developer";
        String course = "C++";
        String phone = "5144442323";
        String volunteering = "Math Tutoring";
        String project = "amaznotwebsite";
        String award = "beststudentaward";
        String language = "French";
        UpdateAppUserProfileRequest request = new UpdateAppUserProfileRequest(description, education, skill, work, course, phone, volunteering, project, award, language);

        var appUser = new AppUser(1L, "joe", "12345678", "joe@mail.com", Role.CANDIDATE);

        when(appUserProfileRepository.findByAppUser(appUser)).thenReturn(new AppUserProfile());

        underTest.updateProfile(request, appUser);
    }

    @Test
    void updateAppUserProfileFail() {
        var appUser = new AppUser(1L, "joe", "12345678", "joe@mail.com", Role.CANDIDATE);

        when(appUserProfileRepository.findByAppUser(appUser)).thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> underTest.updateProfile(new UpdateAppUserProfileRequest(), appUser));
    }
    @Test
    void testToString() {
        assertThat(underTest.toString()).isNotNull();
    }
}
