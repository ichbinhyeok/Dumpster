package com.dumpster.calculator.web.support;

public final class BuildInfo {
    private static final String UNKNOWN = "dev-local";

    private BuildInfo() {
    }

    public static String fullRef() {
        String env = System.getenv("APP_BUILD_SHA");
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        String prop = System.getProperty("app.build.sha");
        if (prop != null && !prop.isBlank()) {
            return prop.trim();
        }
        return UNKNOWN;
    }

    public static String shortRef() {
        String full = fullRef();
        if (full.length() <= 10) {
            return full;
        }
        return full.substring(0, 10);
    }
}
