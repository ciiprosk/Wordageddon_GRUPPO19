package it.unisa.diem.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
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

    public static void mostraConfermaUscitaGioco(Consumer<ButtonType> azioneConfermata) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm exit");
        confirmationAlert.setHeaderText("Quit the game?");
        confirmationAlert.setContentText("If you quit now, your game will be deleted. Do you really want to exit?");

        ButtonType siButton = new ButtonType("Yes, quit");
        ButtonType noButton = new ButtonType("No, continue", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(siButton, noButton);

        DialogPane dialogPane = confirmationAlert.getDialogPane();
        dialogPane.getStylesheets().add(AlertUtils.class.getResource("/it/unisa/diem/main/style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == siButton) {
                azioneConfermata.accept(siButton);
            }
        });
    }


}