package gce.textanalyzer.tests;

import gce.textanalyzer.model.formValidation;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class formValidationTest {
    static final String nullUrl = null;
    static final String notEmptyUrl = "http://somewhere.com/file_to_read.html";

    /**
     * Checks if the URL text field is empty
     */
    @Test
    @Order(1)
    @DisplayName("The URL field is empty.")
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
    @Order(2)
    @DisplayName("The URL field is not empty.")
    void testNotEmptyUrl() {
        boolean actualResult = formValidation.textFieldNotEmpty(notEmptyUrl);

        assertTrue(actualResult);
    }
}