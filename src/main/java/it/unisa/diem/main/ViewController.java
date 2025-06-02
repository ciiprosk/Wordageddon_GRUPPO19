package it.unisa.diem.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ViewController {
    @FXML
    private VBox loginPane, registerPane;
    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private TextField registerEmailField, registerUsernameField;
    @FXML private PasswordField registerPasswordField, registerConfirmField;
    @FXML private Button registerButton, loginButton;

    @FXML
    public void initialize() {
        // Quando che un campo cambia, controlliamo la validità
        registerEmailField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerConfirmField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());

        loginUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        loginPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());

        validateRegisterForm();
        validateLoginForm();
    }


    @FXML
    private void handleLogin() {
        System.out.println("Login premuto");
    }

    @FXML
    private void handleRegister() {
        System.out.println("Registrazione premuta");
    }

    @FXML
    public void showRegister() {
        clearLoginFields();
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    public void showLogin() {
        clearRegisterFields();
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

    private void validateRegisterForm() {
        String email = registerEmailField.getText();
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmField.getText();

        boolean isValid = !email.isEmpty()
                && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")
                && !username.isEmpty()
                && !password.isEmpty()
                && password.equals(confirmPassword);

        registerButton.setDisable(!isValid);
    }

    private void validateLoginForm() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        boolean isValid = !username.isEmpty() && !password.isEmpty();
        loginButton.setDisable(!isValid);
    }

    private void clearLoginFields() {
        loginUsernameField.clear();
        loginPasswordField.clear();
    }

    private void clearRegisterFields() {
        registerEmailField.clear();
        registerUsernameField.clear();
        registerPasswordField.clear();
        registerConfirmField.clear();
        validateRegisterForm(); // aggiorna stato bottone se usi la validazione automatica
    }



}
