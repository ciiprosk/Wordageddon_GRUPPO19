package it.unisa.diem.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class AlertUtils {

    public static void mostraAlert(Alert.AlertType tipo, String titolo, String header, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);

        // Aggiunta CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtils.class.getResource("/it/unisa/diem/main/style.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }
}