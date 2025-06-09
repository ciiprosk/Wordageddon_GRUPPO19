package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import it.unisa.diem.utility.SceneLoader;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.verificaPassword;

public class LoginViewController {

    @FXML
    private TextField loginUsernameField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label incorrectLabel;

    private String url;
    private String user;
    private String pass;

    private Utente utenteToPass;

    @FXML
    public void initialize() {

        PropertiesLoader.init();

        this.url = PropertiesLoader.getProperty("database.url");
        this.user = PropertiesLoader.getProperty("database.user");
        this.pass = PropertiesLoader.getProperty("database.password");

        try {
            Connection co = DriverManager.getConnection(url, user, pass);
            System.out.println("Connessione al database riuscita!");
        } catch (SQLException e) {
            System.out.println("Connessione al database fallita!"); //probabilmente da sostituire con un alert e
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Connessione al database fallita!", null, "Assicurati di essere connesso a internet!");
            Platform.exit();
        }

        loginUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        loginPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        validateLoginForm();
    }

    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();

        incorrectLabel.setVisible(false); // Nasconde errore ogni volta che si prova il login

        UtenteDAOPostgres utentePostgres = new UtenteDAOPostgres(url, user, pass);

        Optional<Utente> optionalUser = null;
        try {
            optionalUser = utentePostgres.selectByUsername(username);
        } catch (SQLException | DBException e) {
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "ERRORE DATABASE", null, "Impossibile contattare il database!");
        }

        if (optionalUser.isEmpty()) {
            incorrectLabel.setVisible(true);
            return;
        }

        Utente utente = optionalUser.get();

        boolean passwordCorretta = verificaPassword(password, utente.getHashedPassword(), utente.getSalt());

        if (!passwordCorretta) {
            incorrectLabel.setVisible(true);
            return;
        }

        utenteToPass = utente;

        // Login avvenuto con successo
        goToHomeMenu();
    }


    private void validateLoginForm() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();
        boolean isValid = !username.isEmpty() &&
                password.length() >= 8 &&
                password.matches(".*[^a-zA-Z0-9 ].*") &&
                !password.contains(" ");
        loginButton.setDisable(!isValid);
        incorrectLabel.setVisible(false);
    }

    @FXML
    private void goToRegister() {
        SceneLoader.load("SignUpView.fxml", loginButton);
    }

    @FXML
    private void goToHomeMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utenteToPass);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


