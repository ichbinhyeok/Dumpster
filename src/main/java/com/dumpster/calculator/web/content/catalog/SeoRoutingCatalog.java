package com.dumpster.calculator.web.content.catalog;

import java.util.Map;
import java.util.function.Predicate;

public final class SeoRoutingCatalog {

    private static final Map<String, String> MATERIAL_SLUG_TO_ID = Map.of(
            "concrete", "concrete",
            "shingles", "asphalt_shingles",
            "drywall", "drywall",
            "dirt", "dirt_soil",
            "brick-block", "brick"
    );
    private static final Map<String, String> MATERIAL_ID_TO_CANONICAL_PATH = Map.of(
            "concrete", "/dumpster/weight/concrete",
            "asphalt_shingles", "/dumpster/weight/shingles",
            "drywall", "/dumpster/weight/drywall",
            "dirt_soil", "/dumpster/weight/dirt",
            "brick", "/dumpster/weight/brick-block"
    );
    private static final Map<String, String> PROJECT_SLUG_TO_ID = Map.of(
            "bathroom-remodel", "bathroom_remodel",
            "roof-tear-off", "roof_tearoff",
            "deck-removal", "deck_demolition",
            "garage-cleanout", "garage_cleanout",
            "kitchen-remodel", "kitchen_remodel",
            "estate-cleanout", "estate_cleanout",
            "yard-cleanup", "yard_cleanup",
            "dirt-grading", "dirt_grading",
            "concrete-removal", "concrete_removal",
            "light-commercial-fitout", "light_commercial_fitout"
    );
    private static final Map<String, String> PROJECT_ID_TO_CANONICAL_PATH = Map.of(
            "bathroom_remodel", "/dumpster/size/bathroom-remodel",
            "roof_tearoff", "/dumpster/size/roof-tear-off",
            "deck_demolition", "/dumpster/size/deck-removal",
            "garage_cleanout", "/dumpster/size/garage-cleanout",
            "kitchen_remodel", "/dumpster/size/kitchen-remodel",
            "estate_cleanout", "/dumpster/size/estate-cleanout",
            "yard_cleanup", "/dumpster/size/yard-cleanup",
            "dirt_grading", "/dumpster/size/dirt-grading",
            "concrete_removal", "/dumpster/size/concrete-removal",
            "light_commercial_fitout", "/dumpster/size/light-commercial-fitout"
    );
    private static final Map<String, String> SPECIAL_PAGE_ALIASES = Map.of(
            "how-many-tons-can-a-10-yard-dumpster-hold", "10-yard-dumpster-weight-limit-overage",
            "dumpster-vs-junk-removal", "dumpster-vs-junk-removal-which-is-cheaper",
            "roofing-squares-to-dumpster-size", "roof-shingles-dumpster-size-calculator",
            "drywall-sheets-to-dumpster-size", "drywall-disposal-dumpster-rules"
    );

    private SeoRoutingCatalog() {
    }

    public static String resolveMaterialId(String materialPathToken, Predicate<String> materialExists) {
        String direct = MATERIAL_SLUG_TO_ID.get(materialPathToken);
        if (direct != null) {
            return direct;
        }
        String normalized = materialPathToken.replace('-', '_');
        if (materialExists.test(normalized)) {
            return normalized;
        }
        return materialPathToken;
    }

    public static String resolveProjectId(String projectPathToken, Predicate<String> projectExists) {
        String direct = PROJECT_SLUG_TO_ID.get(projectPathToken);
        if (direct != null) {
            return direct;
        }
        String normalized = projectPathToken.replace('-', '_');
        if (projectExists.test(normalized)) {
            return normalized;
        }
        return projectPathToken;
    }

    public static String materialCanonicalPath(String materialId) {
        return MATERIAL_ID_TO_CANONICAL_PATH.getOrDefault(materialId, "/dumpster/weight/" + materialId);
    }

    public static String projectCanonicalPath(String projectId) {
        return PROJECT_ID_TO_CANONICAL_PATH.getOrDefault(projectId, "/dumpster/size/" + projectId);
    }

    public static String resolveSpecialSlug(String slug) {
        return SPECIAL_PAGE_ALIASES.getOrDefault(slug, slug);
    }

    public static String materialPublicSlug(String materialId) {
        String canonicalPath = materialCanonicalPath(materialId);
        String prefix = "/dumpster/weight/";
        if (canonicalPath.startsWith(prefix)) {
            String token = canonicalPath.substring(prefix.length());
            return token.contains("_") ? token.replace('_', '-') : token;
        }
        return materialId.replace('_', '-');
    }

    public static String projectPublicSlug(String projectId) {
        String canonicalPath = projectCanonicalPath(projectId);
        String prefix = "/dumpster/size/";
        if (canonicalPath.startsWith(prefix)) {
            String token = canonicalPath.substring(prefix.length());
            return token.contains("_") ? token.replace('_', '-') : token;
        }
        return projectId.replace('_', '-');
    }
}

