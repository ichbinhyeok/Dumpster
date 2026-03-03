package com.dumpster.calculator.config;

import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseUrlGuard {

    private static final Set<String> ALLOWED_CANONICAL_HOSTS = Set.of(
            "debrisdecision.com",
            "www.debrisdecision.com"
    );
    private static final Set<String> ALLOWED_LOCAL_HOSTS = Set.of(
            "localhost",
            "127.0.0.1"
    );

    private final String baseUrl;
    private final boolean enforceDomain;

    public BaseUrlGuard(
            @Value("${app.base-url}") String baseUrl,
            @Value("${app.base-url.enforce-domain:true}") boolean enforceDomain
    ) {
        this.baseUrl = baseUrl;
        this.enforceDomain = enforceDomain;
    }

    @PostConstruct
    public void validateConfiguredBaseUrl() {
        validate(baseUrl, enforceDomain);
    }

    public static void validate(String candidateBaseUrl, boolean enforceDomain) {
        URI uri;
        try {
            uri = URI.create(candidateBaseUrl);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("app.base-url is not a valid absolute URL: " + candidateBaseUrl, ex);
        }

        String scheme = lower(uri.getScheme());
        String host = lower(uri.getHost());

        if ((!"https".equals(scheme) && !"http".equals(scheme)) || host == null || host.isBlank()) {
            throw new IllegalStateException("app.base-url must be an absolute http(s) URL: " + candidateBaseUrl);
        }

        if (!enforceDomain) {
            return;
        }

        if (ALLOWED_CANONICAL_HOSTS.contains(host) || ALLOWED_LOCAL_HOSTS.contains(host)) {
            return;
        }

        throw new IllegalStateException(
                "app.base-url host is not allowed: " + host
                        + " (allowed: debrisdecision.com, www.debrisdecision.com, localhost, 127.0.0.1)"
        );
    }

    private static String lower(String value) {
        return value == null ? null : value.toLowerCase(Locale.US);
    }
}
