package com.apexdiagnostics.common;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class InputSanitizer {
    private static final Pattern HTML = Pattern.compile("(?is)<[^>]*>");

    public String clean(String value) {
        return value == null ? null : HTML.matcher(value).replaceAll("").replace("\u0000", "").trim();
    }

    public String requireClean(String value, String field) {
        String clean = clean(value);
        if (clean == null || clean.isBlank()) throw new IllegalArgumentException(field + " is required");
        return clean;
    }
}
