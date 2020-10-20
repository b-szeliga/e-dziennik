package com.bszeliga.logic;

import javafx.scene.control.TextField;

public class ValidateTextField {
    private final TextField textField;

    public ValidateTextField(TextField textField) {
        this.textField = textField;
    }

    public void replaceLettersToNumbers(String newValue) {
        // Check if user wrote any letters instead of a number:
        if (!newValue.matches("\\d*")) {
            // change that letter into empty space
            textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
    }
}
