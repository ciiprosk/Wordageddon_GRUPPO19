package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DifficultySelectionViewController {
    @FXML private Button facileButton;
    @FXML private Button medioButton;
    @FXML private Button difficileButton;
    @FXML private Button backButton;
    @FXML private Button itButton;
    @FXML private Button engButton;
    @FXML private Button playButton;

    private String selectedDifficulty = null;

    @FXML
    public void initialize() {
        facileButton.setOnAction(e -> selectedDifficulty = "Easy");
        medioButton.setOnAction(e -> selectedDifficulty = "Normal");
        difficileButton.setOnAction(e -> selectedDifficulty = "Hard");
    }

    private void goToLoadView(String difficulty, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LoadView.fxml"));
            Parent root = loader.load();

            LoadViewController controller = loader.getController();
            controller.setDifficulty(difficulty);

            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        selectedDifficulty = null;
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    @FXML
    private void handlePlayButton() {
        if (selectedDifficulty != null) {
            goToLoadView(selectedDifficulty, playButton);
        } else {
            System.out.println("Seleziona una difficoltà prima di continuare.");
        }
    }

    @FXML
    private void handleItButton() {
        System.out.println("You have pressed it");
    }

    @FXML
    private void handleEngButton() {
        System.out.println("You have pressed eng");
    }
}
