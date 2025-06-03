package it.unisa.diem.main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import it.unisa.diem.utility.SceneLoader;

public class LoginViewController {

    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;
    @FXML private Button loginButton;

    @FXML
    public void initialize() {
        loginUsernameField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        loginPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateLoginForm());
        validateLoginForm();
    }

    @FXML
    private void handleLogin() {
        System.out.println("Login premuto");
    }

    private void validateLoginForm() {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();
        boolean isValid = !username.isEmpty() && password.length() >= 8;
        loginButton.setDisable(!isValid);
    }

    @FXML
    private void goToRegister() {
        SceneLoader.load("SignUpView.fxml", loginButton);
    }
}
