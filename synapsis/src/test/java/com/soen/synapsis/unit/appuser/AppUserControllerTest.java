package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.AppUserController;
import com.soen.synapsis.appuser.AppUserDetailsService;
import com.soen.synapsis.appuser.AppUserRepository;
import com.soen.synapsis.appuser.AppUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class AppUserControllerTest {

    @Mock
    private AppUserService appUserService;
    private AutoCloseable autoCloseable;
    private AppUserController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserController(appUserService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAppUser() {
        underTest.getAppUser();

        verify(appUserService).getAppUser();
    }

    @Test
    void getUserData() {
        String expected = "This is the user page";

        assertEquals(expected, underTest.getUserData());
    }

    @Test
    void getAdminData() {
        String expected = "This is the admin page";

        assertEquals(expected, underTest.getAdminData());
    }
}