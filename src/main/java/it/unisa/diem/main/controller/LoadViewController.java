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
            case "facile":
                textLabel.setText("Hai scelto la via più facile.");
                break;
            case "medio":
                textLabel.setText("Puoi fare di più.");
                break;
            case "difficile":
                textLabel.setText("Benvenuto all'inferno.");
                break;
            default:
                textLabel.setText("Testo non disponibile.");
        }
    }
}
