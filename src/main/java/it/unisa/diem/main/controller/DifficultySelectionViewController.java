package it.unisa.diem.main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DifficultySelectionViewController {
    @FXML
    private Button facileButton;

    @FXML
    private Button medioButton;

    @FXML
    private Button difficileButton;

    @FXML
    public void initialize() {
        facileButton.setOnAction(e -> goToLoadView("Facile", facileButton));
        medioButton.setOnAction(e -> goToLoadView("Medio", medioButton));
        difficileButton.setOnAction(e -> goToLoadView("Difficile", difficileButton));
    }

    private void goToLoadView(String difficulty, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LoadView.fxml"));
            Parent root = loader.load();

            // Ottieni il controller e imposta la difficoltà
            LoadViewController controller = loader.getController();
            controller.setDifficulty(difficulty);

            // Ottieni lo stage corrente dal bottone sorgente
            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

