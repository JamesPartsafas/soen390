package com.soen.synapsis.appuser.registration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * This class keeps track of the number of authentication failures
 * and is used to block brute force authentication attacks.
 */
@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 30;

    private LoadingCache<String, Integer> attemptsCache;

    @Autowired
    private HttpServletRequest request;

    public LoginAttemptService(HttpServletRequest request) {
        super();
        this.request = request;
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    /**
     * This class increments cache key that maps to the IP address from which the failed
     * authentication request originated.
     *
     * @param ip Remote IP address from which the failed authentication request was received.
     */
    public void loginFailed(final String ip) {
        int attempts;
        try {
            attempts = attemptsCache.get(ip);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ip, attempts);
    }

    /**
     * Checks if client IP is blocked.
     *
     * @return True if the number of requests from the client IP surpasses MAX_ATTEMPT.
     */
    public boolean isBlocked() {
        try {
            return attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
        } catch (final ExecutionException e) {
            return false;
        }
    }

    /**
     * Gets the client IP address.
     *
     * @return The IP of the client who sent the request.
     */
    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    public LoadingCache<String, Integer> getAttemptsCache() {
        return attemptsCache;
    }

    public void setAttemptsCache(LoadingCache<String, Integer> attemptsCache) {
        this.attemptsCache = attemptsCache;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
