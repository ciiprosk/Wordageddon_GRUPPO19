package it.unisa.diem.main.controller;

import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class HomeMenuViewController {
    @FXML private Button newGameButton;
    @FXML private Button leaderboardButton;
    @FXML private Button historyButton;
    @FXML private Button logOutButton;
    @FXML private Button adminButton;

    private Utente utenteToPass;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LeaderboardView.fxml"));
            Parent root = loader.load();
            LeaderboardViewController controller = loader.getController();
            controller.setUtente(utenteToPass);
            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public void setUtente(Utente utente) {
        utenteToPass = utente;
    }
}
