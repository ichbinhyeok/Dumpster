package com.dumpster.calculator.web.support;

public final class AnalyticsConfig {
    private AnalyticsConfig() {
    }

    public static boolean ga4Enabled() {
        String env = System.getenv("APP_ANALYTICS_GA4_ENABLED");
        if (env != null && !env.isBlank()) {
            return Boolean.parseBoolean(env.trim());
        }

        String prop = System.getProperty("app.analytics.ga4-enabled");
        if (prop != null && !prop.isBlank()) {
            return Boolean.parseBoolean(prop.trim());
        }

        return true;
    }
}
