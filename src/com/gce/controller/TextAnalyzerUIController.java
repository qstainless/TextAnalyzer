package com.gce.controller;

import com.gce.TextAnalyzer;
import com.gce.model.formValidation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TextAnalyzerUIController implements Initializable {

    @FXML
    private Label messageLabel;

    @FXML
    private TextField urlTextField;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    /**
     * Simple form validation. A URL must be entered into the input field
     * Does not check for valid URLs.
     */
    private void validateUrl(String url) {
        boolean validUrl = formValidation.textFieldNotEmpty(url, messageLabel, "The URL cannot be empty.");

        if (validUrl) {
            TextAnalyzer.analyzeUrl(urlTextField.getText());
        }
    }

    @FXML
    public void handleAnalyzeButtonAction(ActionEvent actionEvent) {
        validateUrl(urlTextField.getText());
    }
}
