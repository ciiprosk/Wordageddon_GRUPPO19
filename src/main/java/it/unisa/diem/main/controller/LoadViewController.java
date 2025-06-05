package it.unisa.diem.main.controller;

import javafx.fxml.*;
import javafx.scene.control.Label;

public class LoadViewController {
    @FXML
    private Label difficultyLabel;

    @FXML
    private Label textLabel;

    public void setDifficulty(String difficulty) {
        difficultyLabel.setText(difficulty);

        // Imposta il testo in base alla difficoltà
        switch (difficulty.toLowerCase()) {
            case "easy":
                textLabel.setText("This is going to be easy :)");
                break;
            case "normal":
                textLabel.setText("Starting to sweat...");
                break;
            case "hard":
                textLabel.setText("THIS is gonna be a challenge.");
                break;
            default:
                textLabel.setText("no text");
        }
    }
}
