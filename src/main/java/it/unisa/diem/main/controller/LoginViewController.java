package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.unisa.diem.utility.SceneLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.verificaPassword;

public class LoginViewController {

    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Button loginButton;
    @FXML private Label incorrectLabel;


    @FXML
    public void initialize() {

        PropertiesLoader.init();
        String url=PropertiesLoader.getProperty("database.url");
        String user=PropertiesLoader.getProperty("database.user");
        String password=PropertiesLoader.getProperty("database.password");

        try{
            Connection co= DriverManager.getConnection(url, user, password);
            System.out.println("Connessione al database riuscita!");
        } catch (SQLException e) {
            System.out.println("Connessione al database fallita!"); //probabilmente da sostituire con un alert e
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

        UtenteDAOPostgres utentePostgres = new UtenteDAOPostgres(
                PropertiesLoader.getProperty("database.url"),
                PropertiesLoader.getProperty("database.user"),
                PropertiesLoader.getProperty("database.password")
        );

        Optional<Utente> optionalUser = utentePostgres.selectByUsername(username);

        if (!optionalUser.isPresent()) {
            incorrectLabel.setVisible(true);
            return;
        }

        Utente utente = optionalUser.get();

        boolean passwordCorretta = verificaPassword(password, utente.getHashedPassword(), utente.getSalt());

        if (!passwordCorretta) {
            incorrectLabel.setVisible(true);
            return;
        }

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
        SceneLoader.load("HomeMenuView.fxml", loginButton);
    }

}
