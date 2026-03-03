package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dumpster.calculator.config.BaseUrlGuard;
import org.junit.jupiter.api.Test;

class BaseUrlGuardTests {

    @Test
    void allowsCanonicalProductionDomain() {
        assertThatCode(() -> BaseUrlGuard.validate("https://debrisdecision.com", true))
                .doesNotThrowAnyException();
    }

    @Test
    void allowsCanonicalWwwDomain() {
        assertThatCode(() -> BaseUrlGuard.validate("https://www.debrisdecision.com", true))
                .doesNotThrowAnyException();
    }

    @Test
    void allowsLocalhostForLocalRuns() {
        assertThatCode(() -> BaseUrlGuard.validate("http://127.0.0.1:4173", true))
                .doesNotThrowAnyException();
        assertThatCode(() -> BaseUrlGuard.validate("http://localhost:8080", true))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsUnexpectedHostWhenEnforced() {
        assertThatThrownBy(() -> BaseUrlGuard.validate("https://example.com", true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("host is not allowed");
    }

    @Test
    void allowsAnyHostWhenDomainEnforcementDisabled() {
        assertThatCode(() -> BaseUrlGuard.validate("https://preview.example.net", false))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsRelativeOrMalformedUrl() {
        assertThatThrownBy(() -> BaseUrlGuard.validate("/dumpster/size-weight-calculator", true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("absolute http(s) URL");
    }
}
