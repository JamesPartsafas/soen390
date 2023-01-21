package com.soen.synapsis.unit.appuser;

import com.soen.synapsis.appuser.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    private AppUserService underTest;
    @Mock
    private AppUserRepository appUserRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AppUserService(appUserRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAppUserReturnsUser() {
        Long id = 1L;

        Optional<AppUser> optionalAppUser = underTest.getAppUser(id);

        verify(appUserRepository, times(1)).findById(id);
    }
}
