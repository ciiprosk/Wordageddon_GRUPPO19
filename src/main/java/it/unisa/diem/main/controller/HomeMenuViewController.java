package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class HomeMenuViewController {
    @FXML private Button newGameButton;
    @FXML private Button leaderboardButton;
    @FXML private Button historyButton;

    public void goToNewGame(ActionEvent actionEvent) {
        SceneLoader.load("DifficultySelectionView.fxml", newGameButton);
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        SceneLoader.load("LeaderboardView.fxml", leaderboardButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }
}
