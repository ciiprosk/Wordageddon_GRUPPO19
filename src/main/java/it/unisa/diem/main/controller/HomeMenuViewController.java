package it.unisa.diem.main.controller;

import it.unisa.diem.model.gestione.utenti.Ruolo;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SessionManager;
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

    private boolean isAdmin;

    @FXML
    public void initialize() {
        showAdminButton();
    }

    public void goToNewGame(ActionEvent actionEvent) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/GameSessionView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) newGameButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("NEW GAME");

            stage.setOnCloseRequest(null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void goToLeaderboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LeaderboardView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboard");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HistoryView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("History");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToLogIn(ActionEvent actionEvent) {
        SessionManager.getInstance().logout(); // Logout utente
        SceneLoader.load("LoginSignUpView.fxml", logOutButton);
    }

    public void goToLoadDocument(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/AdminScreenView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) adminButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAdminButton() {
        Utente utenteLoggato = SessionManager.getInstance().getUtenteLoggato();
        if (utenteLoggato != null && utenteLoggato.getRuolo() == Ruolo.ADMIN) {
            isAdmin = true;
        }
        adminButton.setVisible(isAdmin);
        adminButton.setManaged(isAdmin);
        adminButton.setDisable(!isAdmin);
    }
}