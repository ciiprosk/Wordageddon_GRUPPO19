package it.unisa.diem.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneLoader {
    public static void load(String fxml, Control trigger) {
        try {
            Parent root = FXMLLoader.load(SceneLoader.class.getResource("/it/unisa/diem/main/" + fxml));
            Stage stage = (Stage) trigger.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
