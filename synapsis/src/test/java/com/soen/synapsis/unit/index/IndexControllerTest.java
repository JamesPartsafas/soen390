package com.soen.synapsis.unit.index;

import com.soen.synapsis.appuser.AppUserController;
import com.soen.synapsis.appuser.AppUserService;
import com.soen.synapsis.index.IndexController;
import com.soen.synapsis.index.IndexService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class IndexControllerTest {

    @Mock
    private IndexService indexService;
    @Mock
    private Model model;
    private AutoCloseable autoCloseable;
    private IndexController underTest;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new IndexController(indexService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getHomePage() {
        underTest.getHomePage(model);

        verify(indexService).getHomePage();
    }

    @Test
    void getAdminPage() {
        underTest.getAdminPage(model);
        verify(indexService).getAdminPage();
    }

    @Test
    void getPasswordResetPage() {
        underTest.getPasswordResetPage(model);
        verify(indexService).getPasswordResetPage();
    }

    @Test
    void getAccessDeniedPage() {
        underTest.getAccessDeniedPage(model);
        verify(indexService).getAccessDeniedPage();
    }

}