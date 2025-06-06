package it.unisa.diem.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Image icon = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/icon.png"));

        primaryStage.getIcons().add(icon);

        Parent root = FXMLLoader.load(getClass().getResource("HomeMenuView.fxml")); // o LoginView.fxml
        primaryStage.setTitle("La mia App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
