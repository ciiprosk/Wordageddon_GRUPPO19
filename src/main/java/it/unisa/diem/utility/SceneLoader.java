package it.unisa.diem.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe di utilità per il caricamento di scene FXML in un'applicazione JavaFX.
 */
public class SceneLoader {

    /**
     * Carica una nuova scena a partire da un file FXML, sostituendo la scena corrente
     * ottenuta dal controllo che ha attivato l'evento.
     *
     * @param fxml     il nome del file FXML da caricare
     * @param trigger  il controllo che ha attivato il cambio di scena, utilizzato per ottenere lo stage corrente
     */
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
