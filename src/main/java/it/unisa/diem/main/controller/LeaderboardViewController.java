package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class LeaderboardViewController {
    @FXML private Button historyButton;
    @FXML private Button backButton;
    @FXML
    private ComboBox<String> difficoltaComboBox;

    @FXML
    public void initialize() {
        difficoltaComboBox.getItems().addAll("EASY", "HARD", "NORMAL");


        // Aggiungi il listener per gestire la selezione
        difficoltaComboBox.setOnAction(event -> {
            String selezione = difficoltaComboBox.getValue();
            switch (selezione) {
                case "EASY":
                    loadTable("EASY");
                    break;
                case "NORMAL":
                    loadTable("NORMAL");
                    break;
                case "HARD":
                    loadTable("HARD");
                    break;
            }
        });
    }

    private void loadTable(String difficolta) {
        difficoltaComboBox.setValue(difficolta);
        System.out.println("la tabella selezionata è: " + difficolta);
    }


    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }
}
