package it.unisa.diem.main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.unisa.diem.utility.SceneLoader;

public class SignUpViewController {

    @FXML private TextField signUpEmailField;
    @FXML private TextField signUpUsernameField;
    @FXML private PasswordField signUpPasswordField;
    @FXML private PasswordField signUpConfirmField;
    @FXML private Label passwordMismatchLabel, passwordTooShortLabel;
    @FXML private Button signUpButton;

    @FXML
    public void initialize() {
        signUpEmailField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        signUpConfirmField.textProperty().addListener((obs, oldVal, newVal) -> validateSignUpForm());
        validateSignUpForm();
    }

    @FXML
    private void handleSignUp() {
        System.out.println("Registrazione premuta");
    }

    private void validateSignUpForm() {
        String email = signUpEmailField.getText();
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();
        String confirmPassword = signUpConfirmField.getText();

        boolean fieldsFilled = !email.isEmpty() && !username.isEmpty()
                && !password.isEmpty() && !confirmPassword.isEmpty();

        boolean emailValid = email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
        boolean passwordsMatch = password.equals(confirmPassword);
        boolean passwordLongEnough = password.length() >= 8;

        boolean showMismatch = !passwordsMatch && !confirmPassword.isEmpty();
        passwordMismatchLabel.setVisible(showMismatch);
        passwordMismatchLabel.setManaged(showMismatch);

        boolean showTooShort = !passwordLongEnough && !password.isEmpty();
        passwordTooShortLabel.setVisible(showTooShort);
        passwordTooShortLabel.setManaged(showTooShort);

        boolean isValid = fieldsFilled && emailValid && passwordsMatch && passwordLongEnough;
        signUpButton.setDisable(!isValid);
    }

    @FXML
    private void goToLogin() {
        SceneLoader.load("LoginView.fxml", signUpButton);
    }
}
