package com.gce.controller;

import com.gce.model.formValidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static com.gce.TextAnalyzer.*;

public class TextAnalyzerUIController implements Initializable {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField urlTextField;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    /*
      Simple form validation. A URL must be entered into the input field
      Does not check for valid URLs.
     */
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
                displayWordRankings(sortedWordList);
            } catch (IOException e) {
                formValidation.textFieldNotEmpty(null, messageLabel, "An error occured. Unable to analyze content from URL: \"" + targetUrl + "\"");
            }
        }
    }

    private boolean validateUrl(String url) {
        return formValidation.textFieldNotEmpty(url, messageLabel, "The URL cannot be empty.");
    }

    @FXML
    public void handleAnalyzeButtonAction(ActionEvent actionEvent) {
        analyzeUrl(urlTextField.getText());
    }
}
