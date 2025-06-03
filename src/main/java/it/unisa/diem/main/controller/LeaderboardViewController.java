package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LeaderboardViewController {
    @FXML private Button historyButton;
    @FXML private Button backButton;

    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }
}
