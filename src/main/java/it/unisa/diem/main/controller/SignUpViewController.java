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

public class SignUpViewController {

    @FXML private TextField signUpEmailField;
    @FXML private TextField signUpUsernameField;
    @FXML private PasswordField signUpPasswordField;
    @FXML private PasswordField signUpConfirmField;
    @FXML private Label passwordMismatchLabel, passwordValidationLabel, mailInUseLabel, usernameInUseLabel;
    @FXML private Button signUpButton;

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


        signUpEmailField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpConfirmField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        validateSignUpForm();
    }

    //IMPORTANTE: BISOGNA EFFETTUARE IL CONTROLLO SU PASSWORD
    @FXML
    private void handleSignUp() {
        String email = signUpEmailField.getText().trim();
        String username = signUpUsernameField.getText().trim();
        String password = signUpPasswordField.getText();

        usernameInUseLabel.setVisible(false);

        UtenteDAOPostgres utentePostgres = new UtenteDAOPostgres(PropertiesLoader.getProperty("database.url"), PropertiesLoader.getProperty("database.user"), PropertiesLoader.getProperty("database.password"));

        Optional<Utente> optionalUser = utentePostgres.selectByUsername(username);

        if (optionalUser.isPresent()) {
            usernameInUseLabel.setVisible(true);
            return;
        }


        utentePostgres.insert(new Utente(username, email, password));

        mostraAlert(Alert.AlertType.INFORMATION, "Successo", null, "Registrazione avvenuta con successo.");

        goToLogin();
    }


    private void validateSignUpForm() {
        String email = signUpEmailField.getText();
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPassword = signUpConfirmField.getText();

        boolean fieldsFilled = !email.isEmpty() && !username.isEmpty()
                && !password.isEmpty() && !confirmPassword.isEmpty();

        boolean emailValid = isEmailValida(email);
        boolean passwordsMatch = password.equals(confirmPassword);

        // Controlli sulla password
        boolean passwordLongEnough = password.length() >= 8;
        boolean passwordHasSpecialChar = password.matches(".*[^a-zA-Z0-9 ].*");
        boolean passwordHasSpace = password.contains(" ");

        boolean passwordValid = passwordLongEnough && passwordHasSpecialChar && !passwordHasSpace;

        boolean showInvalidPassword = !passwordValid && !password.isEmpty();
        passwordValidationLabel.setVisible(showInvalidPassword);
        passwordValidationLabel.setManaged(showInvalidPassword);

        boolean showMismatch = !passwordsMatch && !confirmPassword.isEmpty();
        passwordMismatchLabel.setVisible(showMismatch);
        passwordMismatchLabel.setManaged(showMismatch);

        boolean isValid = fieldsFilled && emailValid && passwordsMatch && passwordValid;
        signUpButton.setDisable(!isValid);
    }

    private boolean isEmailValida(String email) {
        boolean sintassiValida = email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        if (!sintassiValida) return false;

        String[] parti = email.split("@");
        if (parti.length != 2) return false;

        String dominio = parti[1].toLowerCase();
        String[] dominioValido = {
                "gmail.com",
                "outlook.com",
                "hotmail.com",
                "yahoo.com",
                "icloud.com",
                "aol.com",
                "zoho.com",
                "protonmail.com",
                "gmx.com",
                "mail.com",
                "studenti.unisa.it",
                "unisa.it"
        };

        for (String d : dominioValido) {
            if (dominio.equals(d)) {
                return true;
            }
        }

        return false;
    }


    @FXML
    private void goToLogin() {
        SceneLoader.load("LoginView.fxml", signUpButton);
    }

    private static void mostraAlert(Alert.AlertType tipo, String titolo, String header, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}

