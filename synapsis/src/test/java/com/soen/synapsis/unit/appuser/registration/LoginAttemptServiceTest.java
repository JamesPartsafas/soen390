package com.soen.synapsis.unit.appuser.registration;

import com.google.common.cache.LoadingCache;
import com.soen.synapsis.appuser.registration.LoginAttemptService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class LoginAttemptServiceTest {
    private LoginAttemptService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new LoginAttemptService(request);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void loginFailedSuccessfulCaching1stAttempt() {
        underTest.loginFailed("1");
        LoadingCache<String, Integer> cache = underTest.getAttemptsCache();
        Integer result;
        try {
            result = cache.get("1");
        } catch (ExecutionException e) {
            result = -1;
        }

        assertEquals(result, 1);
    }

    @Test
    void loginFailedSuccessful2ndAttempt() {
        LoadingCache<String, Integer> cache = underTest.getAttemptsCache();
        cache.put("1", 1);
        underTest.loginFailed("1");

        Integer result;
        try {
            result = cache.get("1");
        } catch (ExecutionException e) {
            result = -1;
        }

        assertEquals(result, 2);
    }

    @Test
    void isBlockedTrueWhenAttemptsGreaterThanMaxAttempts() {
        LoadingCache<String, Integer> cache = underTest.getAttemptsCache();
        cache.put("1", 31);
        when(request.getHeader("X-Forwarded-For")).thenReturn("1,");
        boolean result = underTest.isBlocked();

        assertTrue(result);
    }

    @Test
    void isBlockedFalseWhenAttemptsGreaterThanMaxAttempts() {
        LoadingCache<String, Integer> cache = underTest.getAttemptsCache();
        cache.put("1", 1);
        when(request.getHeader("X-Forwarded-For")).thenReturn("1,");
        boolean result = underTest.isBlocked();

        assertFalse(result);
    }
}
