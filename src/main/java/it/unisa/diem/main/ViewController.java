package it.unisa.diem.main;

import javafx.fxml.FXML;
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
        loginPane.setVisible(false);
        registerPane.setVisible(true);
    }

    @FXML
    public void showLogin() {
        registerPane.setVisible(false);
        loginPane.setVisible(true);
    }

}
