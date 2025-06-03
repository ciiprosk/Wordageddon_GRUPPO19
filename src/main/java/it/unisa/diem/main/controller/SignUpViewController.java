package it.unisa.diem.main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.unisa.diem.utility.SceneLoader;

public class SignUpViewController {

    @FXML private TextField registerEmailField;
    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerConfirmField;
    @FXML private Label passwordMismatchLabel, passwordValidationLabel;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        registerEmailField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        registerConfirmField.textProperty().addListener((obs, oldVal, newVal) -> validateRegisterForm());
        validateRegisterForm();
    }

    @FXML
    private void handleRegister() {
        System.out.println("Registrazione premuta");
    }

    private void validateRegisterForm() {
        String email = registerEmailField.getText();
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = registerConfirmField.getText();

        boolean fieldsFilled = !email.isEmpty() && !username.isEmpty()
                && !password.isEmpty() && !confirmPassword.isEmpty();

        boolean emailValid = email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
        boolean passwordsMatch = password.equals(confirmPassword);
        boolean passwordLongEnough = password.length() >= 8;
        boolean passwordHasSpecialChar = password.matches(".*[!@#\\$%\\^&\\*()_\\-+=\\[\\]{};:'\",.<>?/\\\\|`~].*");
        boolean passwordHasNoSpaces = !password.contains(" ");


        // Etichette rosse visibili solo quando serve
        passwordMismatchLabel.setVisible(!passwordsMatch && !confirmPassword.isEmpty());
        passwordValidationLabel.setVisible(
                !password.isEmpty() && (
                        !passwordLongEnough ||
                                !passwordHasNoSpaces ||
                                !passwordHasSpecialChar
                )
        );

        boolean isValid = fieldsFilled && emailValid && passwordsMatch && passwordLongEnough
                && passwordHasSpecialChar && passwordHasNoSpaces;
        registerButton.setDisable(!isValid);
    }


    @FXML
    private void goToLogin() {
        SceneLoader.load("LoginView.fxml", registerButton);
    }
}
