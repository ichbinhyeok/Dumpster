package com.dumpster.calculator.infra.tracking;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TrackingRequestGuardFilter extends OncePerRequestFilter {

    private static final String TRACKING_PATH = "/api/events";
    private static final long REFILL_WINDOW_MILLIS = 60_000L;

    private final int rateLimitPerMinute;
    private final int maxBodyBytes;
    private final Clock clock;
    private final Map<String, TokenBucket> tokenBuckets = new ConcurrentHashMap<>();

    public TrackingRequestGuardFilter(
            @Value("${app.tracking.rate-limit-per-minute:60}") int rateLimitPerMinute,
            @Value("${app.tracking.max-body-bytes:8192}") int maxBodyBytes,
            Clock clock
    ) {
        this.rateLimitPerMinute = Math.max(1, rateLimitPerMinute);
        this.maxBodyBytes = Math.max(1, maxBodyBytes);
        this.clock = clock;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !TRACKING_PATH.equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        CachedBodyRequest wrappedRequest = new CachedBodyRequest(request);
        if (wrappedRequest.bodyLength() > maxBodyBytes) {
            response.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "request body is too large");
            return;
        }

        String clientIp = resolveClientIp(wrappedRequest);
        long nowMillis = Instant.now(clock).toEpochMilli();
        TokenBucket bucket = tokenBuckets.computeIfAbsent(clientIp, ip -> new TokenBucket(rateLimitPerMinute, nowMillis));
        if (!bucket.tryConsume(nowMillis)) {
            response.sendError(429, "too many requests");
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
    }

    private static String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            String[] values = forwardedFor.split(",");
            if (values.length > 0 && !values[0].isBlank()) {
                return values[0].trim();
            }
        }
        String remoteAddr = request.getRemoteAddr();
        return remoteAddr == null || remoteAddr.isBlank() ? "unknown" : remoteAddr;
    }

    private static final class TokenBucket {
        private final int capacity;
        private double tokens;
        private long lastRefillMillis;

        private TokenBucket(int capacity, long nowMillis) {
            this.capacity = capacity;
            this.tokens = capacity;
            this.lastRefillMillis = nowMillis;
        }

        private synchronized boolean tryConsume(long nowMillis) {
            refill(nowMillis);
            if (tokens < 1.0d) {
                return false;
            }
            tokens -= 1.0d;
            return true;
        }

        private void refill(long nowMillis) {
            long elapsed = nowMillis - lastRefillMillis;
            if (elapsed <= 0L) {
                return;
            }
            double refillTokens = (elapsed / (double) REFILL_WINDOW_MILLIS) * capacity;
            tokens = Math.min(capacity, tokens + refillTokens);
            lastRefillMillis = nowMillis;
        }
    }

    private static final class CachedBodyRequest extends HttpServletRequestWrapper {
        private final byte[] body;

        private CachedBodyRequest(HttpServletRequest request) throws IOException {
            super(request);
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
        }

        private int bodyLength() {
            return body.length;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return byteStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // no-op for synchronous reads
                }
            };
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
