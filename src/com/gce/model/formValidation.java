package com.gce.model;

import javafx.scene.control.Label;

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
