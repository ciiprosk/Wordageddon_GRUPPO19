package it.unisa.diem.main;

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
        //verifico la connessione al db-> in caso di errori mando alert
        String url=PropertiesLoader.getProperty("database.url");
        String user=PropertiesLoader.getProperty("database.user");
        String pass=PropertiesLoader.getProperty("database.password");

        Image icon = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/icon.png"));

        primaryStage.getIcons().add(icon);
        try(Connection con= DriverManager.getConnection(url, user, pass)){
            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml")); // o LoginView.fxml
            primaryStage.setTitle("Wordageddon");

            primaryStage.setScene(new Scene(root));

            primaryStage.show();
            System.out.println("Database connesso con successo!");
        }catch(SQLException e){
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Connessione al database fallita!", null, "Assicurati di essere connesso a internet!");
            Platform.exit();
        }

    }


    public static void main(String[] args) {
        PropertiesLoader.init();
        launch(args);
    }

}
