package com.soen.synapsis.unit.index;

import com.soen.synapsis.index.IndexService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndexServiceTest {

    private IndexService underTest;

    public IndexServiceTest() {
        underTest = new IndexService();
    }

    @Test
    void getHomePage() {
        String expected = "pages/home";

        assertEquals(expected, underTest.getHomePage());
    }

    @Test
    void getAdminPage() {
        String expected = "pages/adminCreationPage";
        assertEquals(expected, underTest.getAdminPage());
    }

    @Test
    void getAccessDeniedPage(){
        String expected = "pages/accessDenied";
        assertEquals(expected, underTest.getAccessDeniedPage());
    }

    @Test
    void getPasswordResetPage(){
        String expected = "pages/passwordResetForm";
        assertEquals(expected, underTest.getPasswordResetPage());
    }
}