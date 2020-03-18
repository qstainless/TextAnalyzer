package com.gce.tests;

import com.gce.model.formValidation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class formValidationTest {
    static String nullUrl = null;
    static String notEmptyUrl = "http://somewhere.com/file_to_read.html";

    /**
     * Checks if the URL text field is empty
     */
    @Test
    @DisplayName("An empty URL was given.")
    void testEmptyUrl() {
        boolean actualResult = formValidation.textFieldNotEmpty(nullUrl);

        assertFalse(actualResult);
    }

    /**
     * Checks if the URL text field is not empty. The formValidation
     * class only checks if the URL input field is empty or not,
     * regardless of whether or not the URL is invalid
     */
    @Test
    @DisplayName("The URL field is not empty (not null).")
    void testNotEmptyUrl() {
        boolean actualResult = formValidation.textFieldNotEmpty(notEmptyUrl);

        assertTrue(actualResult);
    }
}