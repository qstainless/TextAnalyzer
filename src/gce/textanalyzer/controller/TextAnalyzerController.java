package gce.textanalyzer.controller;

import gce.textanalyzer.model.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;

/**
 * This is the main controller for the GUI of the TextAnalyzer application.
 */
public class TextAnalyzerController implements Initializable {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField urlTextField;

    @FXML
    private TableView<Word> wordTableView;

    @FXML
    private TableColumn<Word, Integer> wordRank;

    @FXML
    private TableColumn<Word, String> wordContent;

    @FXML
    private TableColumn<Word, Integer> wordFrequency;

    /**
     * Called by the {@code FXMLLoader} to initialize the controller after its root
     * element has been completely processed. Defines the properties of the
     * column names in the {@link TableView} where the programs results
     * will be displayed.
     *
     * @param location  The location used to resolve relative paths for the
     *                  root object, or <tt>null</tt> if the location is
     *                  not known.
     * @param resources The resources used to localize the root object, or
     *                  <tt>null</tt> if the root object was not localized.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        wordRank.setCellValueFactory(new PropertyValueFactory<>("wordRank"));
        wordContent.setCellValueFactory(new PropertyValueFactory<>("wordContent"));
        wordFrequency.setCellValueFactory(new PropertyValueFactory<>("wordFrequency"));
    }

    /**
     * Action to perform when the Analyze! button is clicked
     */
    @FXML
    public void handleAnalyzeButtonAction() {
        analyzeUrl(urlTextField.getText());
    }

    /**
     * Action to perform when the Quit menu item or Quit button is clicked
     */
    @FXML
    public void handleQuitButtonAction() {
        System.exit(0);
    }

    /**
     * Takes the URL provided in the {@code targetUrl} textfield and processes it
     * for analysis, as follows:
     * <ol>
     *     <li>First, it attempts to fetch the content from the URL by
     *     calling the {@link TextAnalyzerController#fetchUrlContent}
     *     method. If the URL is empty or invalid (not found/malformed
     *     URL), the program will display an error message.</li>
     *     <li>Next, the program will call the
     *     {@link DatabaseController#storeWordsIntoDatabase} method to store the
     *     unique words and their frequencies in the database, after
     *     stripping away all HTML tags and some punctuation. </li>
     *     <li>The program will then read the words from the database,
     *     sorted by frequency in descending order by calling
     *     {@link DatabaseController#getAllWords()}.</li>
     *     <li>Finally, the program will populate the {@code wordTableView}
     *     in the GUI with the results.</li>
     * </ol>
     *
     * @param url The URL submitted by the user
     */
    @FXML
    public void analyzeUrl(String url) {
        wordTableView.getItems().clear();
        wordTableView.setEditable(false);

        DatabaseController.createSchema();

        if (validateUrl(url)) {
            // The target URL to parse
            String targetUrl = urlTextField.getText();

            try {
                // Fetch the URL content
                BufferedReader targetHtmlContent = fetchUrlContent(targetUrl);

                // Stores the HashMap key/value pairs in the database
                DatabaseController.storeWordsIntoDatabase(targetHtmlContent);

                // Populate the wordTableView in the GUI with the results
                displaySortedWords();
            } catch (IOException | SQLException e) {
                textFieldNotEmpty(null, messageLabel, "An error occurred. An invalid URL, perhaps? " +
                        "See console for additional details.");
                System.out.println(e);
            }
        }
    }

    /**
     * Fetches the database for the unique word count, total word count
     * and then fetches all words and their frequencies to populate the
     * {@code TableView} with the words sorted by frequency in descending
     * order. Displays the total number of words and number of unique words
     * found in the source URL.
     */
    public void displaySortedWords() throws SQLException {
        int rank = 0;
        int uniqueWords = DatabaseController.getUniqueWordCount();
        int totalNumberOfWords = DatabaseController.getAllWordCount();

        NumberFormat wordCountFormat = NumberFormat.getInstance();

        ObservableList<Word> words = FXCollections.observableArrayList();

        ResultSet wordPairs = DatabaseController.getAllWords();

        while (wordPairs.next()) {
            words.add(new Word(++rank, wordPairs.getString("wordContent"), wordPairs.getInt("wordFrequency")));
        }

        wordPairs.close();

        messageLabel.setText("After parsing, " + wordCountFormat.format(uniqueWords)
                + " unique words were found, out of a total of "
                + wordCountFormat.format(totalNumberOfWords) + " words.");

        wordTableView.setItems(words);
    }

    /**
     * Checks whether or not the URL field is empty or valid.
     *
     * @param url The URL submitted by the user
     * @return True if the URL field is not empty
     */
    public boolean validateUrl(String url) {
        return textFieldNotEmpty(url, messageLabel, "The URL must be valid and cannot be empty.");
    }

    /**
     * Attempts to fetch the URL provided by the user in the GUI.
     *
     * @param targetUrl the target url
     * @return The buffered URL content
     * @throws IOException the IO Exception
     */
    public static BufferedReader fetchUrlContent(String targetUrl) throws IOException {
        return new BufferedReader(new InputStreamReader(new URL(targetUrl).openStream()));
    }

    /**
     * Converts each {@code inputLine} of the {@code inputFile} from HTML to
     * plain text by stripping select characters and strings using regular
     * expressions.
     *
     * @param inputLine The string to convert from html to plain text.
     * @return A plain text version of the {@code inputLine}
     */
    public static String htmlToText(String inputLine) {
        return inputLine
                .toLowerCase()
                .replaceAll(">'", ">")
                .replaceAll("<.*?>", "")
                .replaceAll("<.*", "")  // hack to strip unclosed html tags
                .replaceAll(".*?>", "") // hack to strip unopened html tags
                .replaceAll(" '", " ")
                .replaceAll("[!.,]'", "")
                .replaceAll("[\\[|.?!,;:{}()\\]]", "")
                .replaceAll("--", " ")
                .trim();
    }

    /**
     * {@code out} defaults to false if the input parameter is <tt>null</tt>
     * or empty.
     *
     * @param targetUrl The value of the URL textfield.
     * @return True if the URL textfield is not empty
     *
     * @see TextAnalyzerController#textFieldNotEmpty(String, Label, String)
     */
    public static boolean textFieldNotEmpty (String targetUrl) {
        boolean out = false;

        if (targetUrl != null && !targetUrl.isEmpty() && isValidURL(targetUrl)) {
            out = true;
        }

        return out;
    }

    /**
     * @param targetUrl The value of the URL textfield.
     * @param messageLabel Placeholder in the GUI for error messages
     * @param validationText Feedback to user on errors.
     * @return True if the URL textfield is not empty
     */
    public static boolean textFieldNotEmpty (String targetUrl, Label messageLabel, String validationText) {
        boolean out = true;
        String message = null;

        if (!textFieldNotEmpty(targetUrl)) {
            out = false;
            message = validationText;
        }

        messageLabel.setText(message);

        return out;
    }

    /**
     * Checks if the targetUrl is a valid URL
     *
     * @param targetUrl The target URL entered by the user
     * @return True if the URL is valir
     */
    public static boolean isValidURL(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
