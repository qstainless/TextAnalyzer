package gce.textanalyzer.tests;

import gce.textanalyzer.controller.DatabaseController;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TextAnalyzerControllerTest {

    static final String validUrl = "http://shakespeare.mit.edu/macbeth/full.html";

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
    public void testGetData() throws IOException {
        String targetHtmlContent = Jsoup.connect(validUrl).get().text();
        BufferedReader bufferedHtmlContent = new BufferedReader(new StringReader(targetHtmlContent));
        try {
            DatabaseController.storeWordsIntoDatabase(bufferedHtmlContent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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