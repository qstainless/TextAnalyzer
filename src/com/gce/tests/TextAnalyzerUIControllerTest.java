package com.gce.tests;

import com.gce.controller.TextAnalyzerUIController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TextAnalyzerUIControllerTest {

    static String malformedUrl = "fvsdfjkbweiqug";
    static String invalidUrl = "http://shakespeare.mit.edu/macbeth/fulls.html";
    static String validUrl = "http://shakespeare.mit.edu/macbeth/full.html";

    /**
     * Checks if the URL input by the user is malformed (the URL
     * cannot be parsed)
     */
    @Test
    @DisplayName("A malformed URL was given. Should throw a MalformedURLException.")
    void testAMalformedUrl() {
        assertThrows(MalformedURLException.class, () -> {
                    TextAnalyzerUIController.fetchUrlContent(malformedUrl);
                }
        );
    }

    /**
     * Checks if the URL is valid (exists) or not (Not Fount)
     */
    @Test
    @DisplayName("An invalid URL was given. Should throw a FileNotFoundException.")
    void testAnInvalidUrl() {
        assertThrows(FileNotFoundException.class, () -> {
                    TextAnalyzerUIController.fetchUrlContent(invalidUrl);
                }
        );
    }

    /**
     * Checks if the fetched URL is not null (the URL exists).
     *
     * @throws IOException The IOException
     */
    @Test
    @DisplayName("A valid URL was given. Should not throw an IOException.")
    void testAValidUrl() throws IOException {
        assertNotNull(TextAnalyzerUIController.fetchUrlContent(validUrl));
    }

    /**
     * Tests the countWordFrequencies method
     *
     * @throws IOException The IOException
     */
    @Test
    @DisplayName("The resulting HashMap should contain the correct keys and values.")
    void testCountWordFrequencies() throws IOException {
        String sampleContent = "<p>This is a sample HTML line. It is what it is.</p>";

        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("this", 1);
        expected.put("is", 3);
        expected.put("a", 1);
        expected.put("sample", 1);
        expected.put("html", 1);
        expected.put("line", 1);
        expected.put("it", 2);
        expected.put("what", 1);

        // The countWordFrequencies method parses the BufferedReader
        // So we must first convert the sampleContent to BufferedReader
        Reader inputString = new StringReader(sampleContent);
        BufferedReader reader = new BufferedReader(inputString);

        assertEquals(expected, TextAnalyzerUIController.countWordFrequencies(reader));
    }

    /**
     * Tests for sorting of the key/value pairs in the HashMap
     */
    @Test
    @DisplayName("The ArrayList should contain the properly sorted list of words.")
    void testSortWordsByFrequency() throws IOException {

        // Create the expected values from the sample content:
        // "<p>This is a sample HTML line. It is what it is.</p>"
        ArrayList<Integer> arraylist = new ArrayList<>();
        arraylist.add(3);
        arraylist.add(2);
        arraylist.add(1);
        arraylist.add(1);
        arraylist.add(1);
        arraylist.add(1);
        arraylist.add(1);
        arraylist.add(1);

        HashMap<String, Integer> testWords = new HashMap<>();
        testWords.put("is", 3);
        testWords.put("it", 2);
        testWords.put("a", 1);
        testWords.put("what", 1);
        testWords.put("line", 1);
        testWords.put("this", 1);
        testWords.put("html", 1);
        testWords.put("sample", 1);

        ArrayList<HashMap.Entry<String, Integer>> sortedList = TextAnalyzerUIController.sortWordsByFrequency(testWords);

        int count = 0;

        for (HashMap.Entry<String, Integer> words : sortedList) {
            assertEquals(arraylist.get(count), words.getValue());
            count++;
        }
    }
}