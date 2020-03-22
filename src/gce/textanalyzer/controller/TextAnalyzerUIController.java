package gce.textanalyzer.controller;

import gce.textanalyzer.model.Word;
import gce.textanalyzer.model.formValidation;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * This is the main controller for the GUI of the TextAnalyzer application.
 */
public class TextAnalyzerUIController implements Initializable {

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
     *
     */
    @FXML
    public void handleAnalyzeButtonAction() {
        analyzeUrl(urlTextField.getText());
    }

    /**
     * Takes the URL provided in the {@code targetUrl} textfield and processes it
     * for analysis, as follows:
     * <ol>
     *     <li>First, it attempts to fetch the content from the URL by
     *     calling the {@link TextAnalyzerUIController#fetchUrlContent}
     *     method. If the URL is empty or invalid (not found/malformed
     *     URL), the program will display an error message.</li>
     *     <li>Next, the program will call the
     *     {@link TextAnalyzerUIController#countWordFrequencies} method
     *     to count the frequency of words after stripping away all HTML
     *     tags and some punctuation. The unique words and their
     *     frequencies will be added to a HashMap.</li>
     *     <li>The program will then create an ArrayList from the HashMap
     *     to sort the words by frequency in descending order by calling
     *     {@link TextAnalyzerUIController#sortWordsByFrequency}.</li>
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

        if (validateUrl(url)) {
            // The target URL to parse
            String targetUrl = urlTextField.getText();

            try {
                // Fetch the URL content
                BufferedReader targetHtmlContent = fetchUrlContent(targetUrl);

                // Create a HashMap with the extracted words and their frequencies
                HashMap<String, Integer> wordFrequencies = countWordFrequencies(targetHtmlContent);

                // Create an ArrayList with the extracted words sorted by frequency in descending order
                ArrayList<HashMap.Entry<String, Integer>> sortedWordList = sortWordsByFrequency(wordFrequencies);

                // Populate the wordTableView in the GUI with the results
                displaySortedWords(sortedWordList);
            } catch (IOException e) {
                formValidation.textFieldNotEmpty(null, messageLabel, "An error occurred. An invalid URL, perhaps?");
            }
        }
    }

    /**
     * Parses through the {@code sortedWordList} and populates the
     * {@code TableView} with the words sorted by frequency in descending
     * order. Displays the total number of words and number of unique words
     * found in the source URL.
     *
     * @param sortedWordList The sortedWordList to display
     */
    public void displaySortedWords(ArrayList<HashMap.Entry<String, Integer>> sortedWordList) {
        int rank = 0;

        NumberFormat wordCountFormat = NumberFormat.getInstance();

        ObservableList<Word> words = FXCollections.observableArrayList();

        for (HashMap.Entry<String, Integer> temp : sortedWordList) {
            words.add(new Word(++rank, temp.getKey(), temp.getValue()));
        }

        messageLabel.setText("After parsing, " + wordCountFormat.format(rank)
                + " unique words were found, out of a total of "
                + wordCountFormat.format(totalNumberOfWords) + " words.");

        wordTableView.setItems(words);
    }

    /**
     * Calls the {@link formValidation} class methods to check whether
     * or not the URL fields is empty.
     *
     * @param url The URL submitted by the user
     * @return True if the URL field is not empty
     */
    public boolean validateUrl(String url) {
        return formValidation.textFieldNotEmpty(url, messageLabel, "The URL cannot be empty.");
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
     * Creates a {@code HashMap} to store the words extracted from the URL and their
     * frequencies.
     *
     * @param urlContent The buffered URL content
     * @return The HashMap with words and their frequencies as keys and value, respectively
     * @throws IOException the IO Exception
     */
    public static HashMap<String, Integer> countWordFrequencies(BufferedReader urlContent) throws IOException {
        // Temporary string to store each line of the buffered inputUrl
        String inputLine;

        // HashMap stores words as keys and frequency as values
        HashMap<String, Integer> wordCount = new HashMap<>();

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

        urlContent.close();

        return wordCount;
    }

    /**
     * Creates an {@code ArrayList} that will contain the sorted {@code wordCount HashMap}
     * keys by values in descending order. Uses {@code Comparator} to sort the {@code ArrayList}
     *
     * @param wordCount The HashMap with words and their frequencies
     * @return The sortedWordList ArrayList
     */
    public static ArrayList<HashMap.Entry<String, Integer>> sortWordsByFrequency(HashMap<String, Integer> wordCount) {
        // create and populate an ArrayList with the words in the wordCount HashMap and their frequencies
        ArrayList<HashMap.Entry<String, Integer>> sortedWordList = new ArrayList<>(wordCount.entrySet());

        // Sort the ArrayList
        sortedWordList.sort((freq1, freq2) -> freq2.getValue().compareTo(freq1.getValue()));

        return sortedWordList;
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
}
