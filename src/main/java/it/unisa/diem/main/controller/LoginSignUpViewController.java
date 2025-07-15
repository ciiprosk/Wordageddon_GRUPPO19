package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.UtenteDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.service.LoginService;
import it.unisa.diem.main.service.SignUpService;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SessionManager;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Optional;

import static it.unisa.diem.model.gestione.utenti.SicurezzaPassword.verificaPassword;

public class LoginSignUpViewController {

    @FXML private VBox loginPane;
    @FXML private VBox signUpPane;

    // Login fields
    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Label incorrectLabel;
    @FXML private Button loginButton;

    // SignUp fields
    @FXML private TextField signUpEmailField;
    @FXML private TextField signUpUsernameField;
    @FXML private PasswordField signUpPasswordField;
    @FXML private PasswordField signUpConfirmField;
    @FXML private Label passwordMismatchLabel, passwordValidationLabel, mailInUseLabel, usernameInUseLabel;
    @FXML private Button signUpButton;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressIndicator loadingSpinner;
    @FXML private Label loadingMessageLabel;



    private String url;
    private String user;
    private String pass;

    @FXML
    public void initialize() {
        this.url = PropertiesLoader.getProperty("database.url");
        this.user = PropertiesLoader.getProperty("database.user");
        this.pass = PropertiesLoader.getProperty("database.password");

        // Login validation
        loginUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        loginPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        validateLoginForm();

        // SignUp validation
        signUpEmailField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpConfirmField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        validateSignUpForm();

        //evito di avere un campo con focus di default. rischiavo di ostruire la leggibilità
        loginPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        newWin.focusedProperty().addListener((obsFocus, wasFocused, isFocused) -> {
                            if (isFocused) {
                                // Richiedi focus al pannello root, non ai campi
                                loginPane.requestFocus();
                            }
                        });
                    }
                });
            }
        });
    }

    // 🔵 Switch methods
    @FXML
    private void showSignUpPane() {
        loginPane.setVisible(false);
        signUpPane.setVisible(true);
        signUpPane.requestFocus();
    }

    @FXML
    private void showLoginPane() {
        signUpPane.setVisible(false);
        loginPane.setVisible(true);
        loginPane.requestFocus();
    }

    // 🔵 Login logic
    @FXML
    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();

        incorrectLabel.setVisible(false);
        loginButton.setDisable(true);
        showLoadingOverlayWithMessage("Accesso in corso..."); // 🔵 Mostra caricamento con messaggio

        UtenteDAOPostgres utentePostgres = new UtenteDAOPostgres(url, user, pass);
        LoginService service = new LoginService(username, password, utentePostgres);

        service.setOnSucceeded(event -> {
            hideLoadingOverlay();
            loginButton.setDisable(false);
            Utente utente = service.getValue();

            if (utente == null) {
                incorrectLabel.setVisible(true);
                return;
            }

            SessionManager.getInstance().login(utente);
            goToHomeMenu();
        });

        service.setOnFailed(event -> {
            hideLoadingOverlay();
            loginButton.setDisable(false);
            incorrectLabel.setVisible(true);
        });

        service.start();
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

    // 🔵 SignUp logic
    @FXML
    private void handleSignUp() {
        String email = signUpEmailField.getText().trim();
        String username = signUpUsernameField.getText().trim();
        String password = signUpPasswordField.getText();

        UtenteDAOPostgres utentePostgres = new UtenteDAOPostgres(url, user, pass);
        SignUpService service = new SignUpService(email, username, password, utentePostgres);

        signUpButton.setDisable(true);
        showLoadingOverlayWithMessage("Registrazione in corso..."); // 🔵 Mostra caricamento con messaggio

        service.setOnSucceeded(event -> {
            hideLoadingOverlay();
            signUpButton.setDisable(false);

            if (service.isEmailInUse()) {
                mailInUseLabel.setVisible(true);
                mailInUseLabel.setManaged(true);
                return;
            }

            if (service.isUsernameInUse()) {
                usernameInUseLabel.setVisible(true);
                usernameInUseLabel.setManaged(true);
                return;
            }

            if (service.getValue()) {
                clearSignUpFields();
                showLoginPane();
            } else {
                AlertUtils.mostraAlert(Alert.AlertType.ERROR, "ERRORE DATABASE", null, "Impossibile contattare il database!");
            }
        });

        service.setOnFailed(event -> {
            hideLoadingOverlay();
            signUpButton.setDisable(false);
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "ERRORE DATABASE", null, "Impossibile completare la registrazione!");
        });

        service.start();
    }






    private void validateSignUpForm() {
        String email = signUpEmailField.getText();
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPassword = signUpConfirmField.getText();

        usernameInUseLabel.setVisible(false);
        usernameInUseLabel.setManaged(false);
        mailInUseLabel.setVisible(false);
        mailInUseLabel.setManaged(false);

        boolean fieldsFilled = !email.isEmpty() && !username.isEmpty()
                && !password.isEmpty() && !confirmPassword.isEmpty();

        boolean emailValid = isEmailValida(email);
        boolean passwordsMatch = password.equals(confirmPassword);

        boolean passwordLongEnough = password.length() >= 8;
        boolean passwordHasSpecialChar = password.matches(".*[^a-zA-Z0-9 ].*");
        boolean passwordHasSpace = password.contains(" ");

        boolean passwordValid = passwordLongEnough && passwordHasSpecialChar && !passwordHasSpace;

        passwordValidationLabel.setVisible(!passwordValid && !password.isEmpty());
        passwordValidationLabel.setManaged(!passwordValid && !password.isEmpty());

        passwordMismatchLabel.setVisible(!passwordsMatch && !confirmPassword.isEmpty());
        passwordMismatchLabel.setManaged(!passwordsMatch && !confirmPassword.isEmpty());

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
                "gmail.com", "outlook.com", "hotmail.com", "yahoo.com",
                "icloud.com", "aol.com", "zoho.com", "protonmail.com",
                "gmx.com", "mail.com", "studenti.unisa.it", "unisa.it"
        };

        for (String d : dominioValido) {
            if (dominio.equals(d)) {
                return true;
            }
        }
        return false;
    }

    // 🔵 Go to Home
    private void goToHomeMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void mostraAlert(Alert.AlertType tipo, String titolo, String header, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    private void clearSignUpFields() {
        signUpEmailField.clear();
        signUpUsernameField.clear();
        signUpPasswordField.clear();
        signUpConfirmField.clear();
    }

    private void showLoadingOverlay() {
        loadingMessageLabel.setText("");
        loadingOverlay.setVisible(true);
    }

    private void showLoadingOverlayWithMessage(String message) {
        loadingMessageLabel.setText(message);
        loadingOverlay.setVisible(true);
    }

    private void hideLoadingOverlay() {
        loadingOverlay.setVisible(false);
    }



}
