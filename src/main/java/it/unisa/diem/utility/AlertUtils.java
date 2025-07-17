package it.unisa.diem.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;
import java.util.function.Consumer;

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

    public static void mostraAlertConAzione(Alert.AlertType tipo, String titolo, String header, String contenuto, Consumer<ButtonType> azione) {
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

        Optional<ButtonType> result = alert.showAndWait();

        // Gestione anche della chiusura con la X o ESC
        ButtonType sceltaEffettiva = result.orElse(ButtonType.CLOSE);
        azione.accept(sceltaEffettiva);
    }


}