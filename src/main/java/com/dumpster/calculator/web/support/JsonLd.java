package com.dumpster.calculator.web.support;

import gg.jte.Content;
import gg.jte.TemplateOutput;
import tools.jackson.core.io.JsonStringEncoder;

public final class JsonLd {
    private static final JsonStringEncoder ENCODER = JsonStringEncoder.getInstance();

    private JsonLd() {
    }

    public static Content content(String value) {
        String escaped = escape(value);
        return new Content() {
            @Override
            public void writeTo(TemplateOutput output) {
                output.writeUnsafeContent(escaped);
            }

            @Override
            public boolean isEmptyContent() {
                return escaped.isEmpty();
            }
        };
    }

    public static String escape(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        StringBuilder escaped = new StringBuilder(value.length() + 16);
        ENCODER.quoteAsString(value, escaped);
        return escaped.toString()
                .replace("\u2028", "\\u2028")
                .replace("\u2029", "\\u2029");
    }
}
