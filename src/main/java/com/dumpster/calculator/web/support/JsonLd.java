package com.dumpster.calculator.web.support;

import gg.jte.Content;
import gg.jte.TemplateOutput;

public final class JsonLd {
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

        StringBuilder out = new StringBuilder(value.length() + 16);
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c <= 0x1F) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }
}
