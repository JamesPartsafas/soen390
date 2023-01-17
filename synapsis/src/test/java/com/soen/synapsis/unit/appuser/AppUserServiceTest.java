package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUser;
import com.soen.synapsis.appuser.AppUserService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AppUserServiceTest {

    private AppUserService underTest;

    public AppUserServiceTest() {
        underTest = new AppUserService();
    }

    @Test
    void getAppUserReturnsUser() {
        AppUser appUser = underTest.getAppUser();

        assertThat(appUser).isNotNull();
    }
}
