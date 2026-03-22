package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.web.support.JsonLd;
import org.junit.jupiter.api.Test;

class JsonLdTests {

    @Test
    void escapeHandlesBackslashesQuotesAndUnicodeSeparators() {
        String escaped = JsonLd.escape("quote \" slash \\ line\u2028paragraph\u2029");

        assertThat(escaped).isEqualTo("quote \\\" slash \\\\ line\\u2028paragraph\\u2029");
    }
}
