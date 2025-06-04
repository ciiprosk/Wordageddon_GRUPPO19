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
        facileButton.setOnAction(e -> {
            selectedDifficulty = "Easy";
            highlightSelectedButton(facileButton);
        });

        medioButton.setOnAction(e -> {
            selectedDifficulty = "Normal";
            highlightSelectedButton(medioButton);
        });

        difficileButton.setOnAction(e -> {
            selectedDifficulty = "Hard";
            highlightSelectedButton(difficileButton);
        });
    }

    //funzione per aggiungere css a pulsante cliccato
    private void highlightSelectedButton(Button selectedButton) {
        facileButton.getStyleClass().remove("selected-button");
        medioButton.getStyleClass().remove("selected-button");
        difficileButton.getStyleClass().remove("selected-button");

        if (!selectedButton.getStyleClass().contains("selected-button")) {
            selectedButton.getStyleClass().add("selected-button");
        }
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
        System.out.println("hai clickato it");
        //css on click
        engButton.getStyleClass().remove("selected-button");
        if (!itButton.getStyleClass().contains("selected-button")) {
            itButton.getStyleClass().add("selected-button");
        }
    }

    @FXML
    private void handleEngButton() {
        System.out.println("hai clickato eng");
        //css on click
        itButton.getStyleClass().remove("selected-button");
        if (!engButton.getStyleClass().contains("selected-button")) {
            engButton.getStyleClass().add("selected-button");
        }
    }
}
