package com.dumpster.calculator;

import static org.assertj.core.api.Assertions.assertThat;

import com.dumpster.calculator.infra.persistence.MarketTierZipRuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MarketTierZipRuleRepositoryTests {

    @Autowired
    private MarketTierZipRuleRepository repository;

    @Test
    void resolvesKnownUrbanValueAndRegionalZipRanges() {
        assertThat(repository.resolveByZip("94105"))
                .isPresent()
                .get()
                .extracting(rule -> rule.marketTier())
                .isEqualTo("urban");

        assertThat(repository.resolveByZip("58012"))
                .isPresent()
                .get()
                .extracting(rule -> rule.marketTier())
                .isEqualTo("value");

        assertThat(repository.resolveByZip("07005"))
                .isPresent()
                .get()
                .extracting(rule -> rule.marketTier())
                .isEqualTo("coastal");

        assertThat(repository.resolveByZip("83702"))
                .isPresent()
                .get()
                .extracting(rule -> rule.marketTier())
                .isEqualTo("mountain");

        assertThat(repository.resolveByZip("63101"))
                .isPresent()
                .get()
                .extracting(rule -> rule.marketTier())
                .isEqualTo("heartland");
    }

    @Test
    void returnsEmptyForUnmappedZip() {
        assertThat(repository.resolveByZip("12345")).isEmpty();
    }
}
