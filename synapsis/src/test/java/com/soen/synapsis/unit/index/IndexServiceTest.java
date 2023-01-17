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
}