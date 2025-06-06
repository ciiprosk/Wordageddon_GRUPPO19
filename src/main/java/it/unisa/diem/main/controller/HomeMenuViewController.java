package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class HomeMenuViewController {
    @FXML private Button newGameButton;
    @FXML private Button leaderboardButton;
    @FXML private Button historyButton;
    @FXML private Button logOutButton;
    @FXML private Button adminButton;

    private boolean isAdmin = false; //da modificare

    @FXML
    public void initialize() {
        isAdmin = true;            // Temporaneo, finché non c'è il database
        showAdminButton();
    }

    public void goToNewGame(ActionEvent actionEvent) {
        SceneLoader.load("DifficultySelectionView.fxml", newGameButton);
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        SceneLoader.load("LeaderboardView.fxml", leaderboardButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }

    public void goToLogIn(ActionEvent actionEvent) {
        SceneLoader.load("LoginView.fxml", logOutButton);
    }

    public void goToLoadDocument(ActionEvent actionEvent) {
        SceneLoader.load("AdminScreenView.fxml", adminButton);
    }

    private void showAdminButton() {
        adminButton.setVisible(isAdmin);
        adminButton.setManaged(isAdmin);
        adminButton.setDisable(!isAdmin);
    }
}
