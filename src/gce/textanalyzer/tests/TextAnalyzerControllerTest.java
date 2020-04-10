package gce.textanalyzer.tests;

import gce.textanalyzer.controller.DatabaseController;
import gce.textanalyzer.controller.TextAnalyzerController;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TextAnalyzerControllerTest {

    static final String malformedUrl = "a malformed url";
    static final String invalidUrl = "http://shakespeare.mit.edu/macbeth/fulls.html";
    static final String validUrl = "http://shakespeare.mit.edu/macbeth/full.html";

    /**
     * Test to determine if the supplied URL is malformed (the URL
     * cannot be parsed)
     */
    @Test
    @Order(1)
    @DisplayName("A malformed URL was given. Should throw a MalformedURLException.")
    void testAMalformedUrl() {
        assertThrows(MalformedURLException.class, () -> TextAnalyzerController.fetchUrlContent(malformedUrl)
        );
    }

    /**
     * Test to determine if the supplied URL is valid (exists) or not (Not Fount)
     */
    @Test
    @Order(2)
    @DisplayName("An invalid URL was given. Should throw a FileNotFoundException.")
    void testAnInvalidUrl() {
        assertThrows(FileNotFoundException.class, () -> TextAnalyzerController.fetchUrlContent(invalidUrl)
        );
    }

    /**
     * Test to determine that the fetched URL is not null (the URL exists).
     *
     * @throws IOException The IOException
     */
    @Test
    @Order(3)
    @DisplayName("A valid URL was given. Should not throw an IOException.")
    void testAValidUrl() throws IOException {
        assertNotNull(TextAnalyzerController.fetchUrlContent(validUrl));
    }

    /**
     * Tests the database connection
     */
    @Test
    @Order(4)
    @DisplayName("A database connection is successfully created.")
    void testDatabaseConnection() {
        Connection connection = DatabaseController.dbConnect("word_occurrences");
        assertNotNull(connection);
    }

    /**
     * Creates the word_occurrences schema
     */
    @Test
    @Order(5)
    @DisplayName("The 'word_occurrences' schema and the 'word' table are created if they don't already exist.")
    void testSchemaAndTableCreation() {
        DatabaseController.createSchema();
    }

    @Test
    @Order(6)
    @DisplayName("Populates the database with words from http://shakespeare.mit.edu/macbeth/full.html")
    public void testGetData() throws IOException, SQLException {
        BufferedReader targetHtmlContent = TextAnalyzerController.fetchUrlContent("http://shakespeare.mit.edu/macbeth/full.html");
        DatabaseController.storeWordsIntoDatabase(targetHtmlContent);
    }

    @Test
    @Order(7)
    @DisplayName("Fetches all words from the database.")
    void testGetAllWords() throws SQLException {
        ResultSet allWords = DatabaseController.getAllWords();
        while (allWords.next()) {
            System.out.println(allWords.getString("wordContent") + ": " + allWords.getInt("wordFrequency"));
        }

        allWords.close();
    }

    @Test
    @Order(8)
    @DisplayName("Verify that the target URL has 3394 unique words.")
    void testGetUniqueWordCount() throws SQLException {
        int uniqueWords = DatabaseController.getUniqueWordCount();
        assertEquals(3394, uniqueWords);
    }

    @Test
    @Order(9)
    @DisplayName("Verify that the target URL has 18122 total words.")
    void testGetAllWordCount() throws SQLException {
        int allWords = DatabaseController.getAllWordCount();
        assertEquals(18122, allWords);
    }

}