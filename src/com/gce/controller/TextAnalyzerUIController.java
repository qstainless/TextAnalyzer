package com.gce.controller;

import com.gce.model.Word;
import com.gce.model.formValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This is the main controller for the GUI of the TextAnalyzer application.
 */
public class TextAnalyzerUIController implements Initializable {

    // The target URL to parse
    private static String targetUrl;

    // The total number of words fetched from the targetUrl
    private static int totalNumberOfWords;

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

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL location, ResourceBundle resources) {
        wordRank.setCellValueFactory(new PropertyValueFactory<Word, Integer>("wordRank"));
        wordContent.setCellValueFactory(new PropertyValueFactory<Word, String>("wordContent"));
        wordFrequency.setCellValueFactory(new PropertyValueFactory<Word, Integer>("wordFrequency"));
    }

    /**
     * Action to perform when the Analyze! button is clicked
     *
     * @param actionEvent the action event
     */
    @FXML
    public void handleAnalyzeButtonAction(ActionEvent actionEvent) {
        analyzeUrl(urlTextField.getText());
    }

    /**
     * Analyze the URL provided by the user
     *
     * @param url The user submitted URL
     */
    @FXML
    public void analyzeUrl(String url) {
        wordTableView.getItems().clear();
        wordTableView.setEditable(false);

        if (validateUrl(url)) {
            targetUrl = urlTextField.getText();

            try {
                // Fetch the URL content
                BufferedReader targetHtmlContent = fetchUrlContent(targetUrl);

                // Count the word frequencies
                HashMap<String, Integer> wordFrequencies = countWordFrequencies(targetHtmlContent);

                // Sort the words by frequency
                ArrayList<HashMap.Entry<String, Integer>> sortedWordList = sortWordsByFrequency(wordFrequencies);

                // Display the word frequencies
                wordTableView.setItems(displayWords(sortedWordList));
            } catch (IOException e) {
                formValidation.textFieldNotEmpty(null, messageLabel, "An error occurred. An invalid URL, perhaps?");
            }
        }
    }

    /**
     * Displays the word frequencies in the GUI
     *
     * @param sortedWordList The sortedWordList to display
     * @return the observable list
     */
    public ObservableList<Word> displayWords(ArrayList<HashMap.Entry<String, Integer>> sortedWordList) {
        int rank = 0;

        NumberFormat wordCountFormat = NumberFormat.getInstance();

        ObservableList<Word> words = FXCollections.observableArrayList();

        for (HashMap.Entry<String, Integer> temp : sortedWordList) {
            words.add(new Word(++rank, temp.getKey(), temp.getValue()));
        }

        messageLabel.setText("After parsing, " + wordCountFormat.format(rank)
                + " unique words were found, out of a total of "
                + wordCountFormat.format(totalNumberOfWords) + " words.");

        return words;
    }

    /**
     * Simple validation to make sure that the URL field is not empty.
     * Does not check for valid URLs
     *
     * @param url The user submitted URL
     * @return boolean True if the URL field is not empty, False otherwise
     */
    public boolean validateUrl(String url) {
        return formValidation.textFieldNotEmpty(url, messageLabel, "The URL cannot be empty.");
    }

    /**
     * Fetch the URL to parse
     *
     * @return The buffered URL content
     * @throws IOException the io exception
     */
    public static BufferedReader fetchUrlContent(String targetUrl) throws IOException {
        return new BufferedReader(new InputStreamReader(new URL(targetUrl).openStream()));
    }

    /**
     * Create a hash map to store the words extracted from the URL and their frequency
     *
     * @param urlContent The buffered URL content
     * @return The wordCount HashMap
     * @throws IOException the io exception
     */
    public static HashMap<String, Integer> countWordFrequencies(BufferedReader urlContent) throws IOException {
        // temp string to store each line of the buffered inputUrl
        String inputLine;

        // HashMap stores words as keys and frequency as values
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();

        // Add words and their frequency to the hash map
        while ((inputLine = urlContent.readLine()) != null) {
            // convert the html formatted line to plain text
            String filteredInputLine = htmlToText(inputLine);

            // extract words from filteredInputLine using StringTokenizer
            StringTokenizer wordsInLine = new StringTokenizer(filteredInputLine);

            // add words and their frequencies to the wordCount HashMap
            while (wordsInLine.hasMoreTokens()) {
                String word = wordsInLine.nextToken();
                totalNumberOfWords++;
                Integer currentWordFrequency = wordCount.get(word);
                int newWordFrequency = currentWordFrequency == null ? 1 : currentWordFrequency + 1;
                wordCount.put(word, newWordFrequency);
            }
        }

        // close the stream and release system resources.
        urlContent.close(); // Shout out to Prof. Jeho Park for drilling this into my head!

        return wordCount;
    }

    /**
     * Method to sort the wordCount HashMap by frequency values
     *
     * @param wordCount The HashMap with words and their frequencies
     * @return The sortedWordList
     */
    public static ArrayList<HashMap.Entry<String, Integer>> sortWordsByFrequency(HashMap<String, Integer> wordCount) {
        // create and populate an ArrayList with the words in the wordCount HashMap and their frequencies
        ArrayList<HashMap.Entry<String, Integer>> sortedWordList = new ArrayList<>(wordCount.entrySet());

        // use Comparator to sort the ArrayList
        sortedWordList.sort((freq1, freq2) -> freq2.getValue().compareTo(freq1.getValue()));

        return sortedWordList;
    }

    /**
     * Converts each line of the inputFile from html to plain text
     *
     * @param inputLine The string to convert from html to plain text
     * @return The plain text inputLine
     */
    public static String htmlToText(String inputLine) {
        return inputLine
                .toLowerCase()                   // convert to lower case
                .replaceAll(">'", ">")           // strip leading apostrophe after html tag
                .replaceAll("<.*?>", "")         // strip html tags
                .replaceAll("<.*", "")           // hack: strip unclosed html tags
                .replaceAll(".*?>", "")          // hack: strip unopened html tags
                .replaceAll(" '", " ")           // strip leading apostrophe after space
                .replaceAll("[!.,]'", "")           // strip apostrophe after punctuation
                .replaceAll("[\\[|.?!,;:{}()\\]]", "") // strip punctuation except apostrophe
                .replaceAll("--", " ")           // strip multiple double dashes found in the text
                .trim();                         // trim any remaining whitespace around each line
    }
}
