package com.gce.model;

import javafx.scene.control.Label;

/**
 * Validation class to check if the user entered a value in the URL text field.
 * The class does not check for a valid URL. If the URL is invalid, the program
 * will return an error (TextAnalyzer class).
 */
public class formValidation {

    public static boolean textFieldNotEmpty (String input) {
        boolean out = false;

        if (input != null && !input.isEmpty()) {
            out = true;
        }

        return out;
    }

    public static boolean textFieldNotEmpty (String input, Label messageLabel, String validationText) {
        boolean out = true;
        String message = null;

        if (!textFieldNotEmpty(input)) {
            out = false;
            message = validationText;
        }

        messageLabel.setText(message);

        return out;
    }
}
