package com.apexdiagnostics.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {
    private final InputSanitizer sanitizer = new InputSanitizer();

    @Test
    void cleanRemovesMarkupAndNullBytes() {
        assertEquals("patient", sanitizer.clean(" <b>patient</b>\u0000 "));
    }

    @Test
    void cleanPreservesNull() {
        assertNull(sanitizer.clean(null));
    }

    @Test
    void requireCleanRejectsBlankValues() {
        assertThrows(IllegalArgumentException.class, () -> sanitizer.requireClean(" <i></i> ", "name"));
    }

    @Test
    void requireCleanReturnsSanitizedValue() {
        assertEquals("Alice", sanitizer.requireClean("<b>Alice</b>", "name"));
    }
}
