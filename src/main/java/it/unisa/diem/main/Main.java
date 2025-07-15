package it.unisa.diem.main;

import it.unisa.diem.main.controller.GameSessionController;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Verifico la connessione al db -> in caso di errori mando alert
        String url = PropertiesLoader.getProperty("database.url");
        String user = PropertiesLoader.getProperty("database.user");
        String pass = PropertiesLoader.getProperty("database.password");

        Image icon = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/icon.png"));
        primaryStage.getIcons().add(icon);

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginSignUpView.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("Wordageddon");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

            System.out.println("Database connesso con successo!");

            // 🔷 Gestisco la chiusura della finestra per eliminare la sessione attiva se esiste
            primaryStage.setOnCloseRequest(event -> {
                GameSessionController controller = GameSessionController.getInstance();
                if (controller != null) {
                    controller.deleteGameSessionFromDB(); // Assicurati che sia public se richiamato da qui
                }
            });

        } catch (SQLException e) {
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Connessione al database fallita!", null, "Assicurati di essere connesso a internet!");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        PropertiesLoader.init();
        launch(args);
    }
}
