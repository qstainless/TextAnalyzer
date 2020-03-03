package com.gce.controller;

import com.gce.model.Word;
import com.gce.model.formValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
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

import static com.gce.TextAnalyzer.*;

public class TextAnalyzerUIController implements Initializable {

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
     * Analyze the URL provided by the user
     *
     * @param url The user submitted URL
     */
    @FXML
    private void analyzeUrl(String url) {
        boolean validUrl = validateUrl(url);

        if (validUrl) {
            targetUrl = urlTextField.getText();

            try {
                // Fetch the URL content
                BufferedReader urlContent = fetchUrlContent();

                // Count the word frequencies
                HashMap<String, Integer> wordFrequencies = countWordFrequencies(urlContent);

                // Sort the words by frequency
                ArrayList<HashMap.Entry<String, Integer>> sortedWordList = sortWordsByFrequency(wordFrequencies);

                // Display the word frequencies
                wordTableView.setItems(getWords(sortedWordList));
            } catch (IOException e) {
                formValidation.textFieldNotEmpty(null, messageLabel, "An error occured. An invalid URL, perhaps?");
            }
        }
    }

    /**
     * Simple validation to make sure that the URL field is not empty.
     * Does not check for valid URLs
     *
     * @param url The user submitted URL
     * @return boolean True if the URL field is not empty, False otherwise
     */
    private boolean validateUrl(String url) {
        return formValidation.textFieldNotEmpty(url, messageLabel, "The URL cannot be empty.");
    }

    /**
     * Action to perform when the Analyze! button is clicked
     */
    @FXML
    public void handleAnalyzeButtonAction(ActionEvent actionEvent) {
        analyzeUrl(urlTextField.getText());
    }

    /**
     * Displays the word frequencies table on the GUI
     *
     * @param sortedWordList The sortedWordList to display
     */
    public ObservableList<Word> getWords(ArrayList<HashMap.Entry<String, Integer>> sortedWordList) {
        int rank = 0;

        wordTableView.setEditable(false);

        ObservableList<Word> words = FXCollections.observableArrayList();

        for (HashMap.Entry<String, Integer> temp : sortedWordList) {
            rank++;

            words.add(new Word(rank, temp.getKey(), temp.getValue()));
        }

        messageLabel.setText("After parsing, " + rank + " words were found.");

        return words;
    }
}
