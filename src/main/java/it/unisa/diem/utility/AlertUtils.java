package it.unisa.diem.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Classe di utilità per la gestione e la visualizzazione di finestre di dialogo Alert in JavaFX.
 * Fornisce metodi per mostrare avvisi con o senza azioni personalizzate.
 */
public class AlertUtils {

    /**
     * Mostra un Alert con i parametri specificati. Applica uno stile personalizzato al dialogo.
     *
     * @param tipo      il tipo di Alert da mostrare
     * @param titolo    il titolo della finestra di dialogo
     * @param header    il testo dell'intestazione
     * @param contenuto il contenuto del messaggio dell'alert
     */
    public static void mostraAlert(Alert.AlertType tipo, String titolo, String header, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtils.class.getResource("/it/unisa/diem/main/style.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }

    /**
     * Mostra un Alert con i parametri specificati ed esegue un'azione in base al pulsante selezionato.
     * Applica uno stile personalizzato al dialogo. Se l'utente chiude la finestra senza selezionare un pulsante,
     * viene passato ButtonType#CLOSE al consumatore.
     *
     * @param tipo      il tipo di Alert da mostrare
     * @param titolo    il titolo della finestra di dialogo
     * @param header    il testo dell'intestazione
     * @param contenuto il contenuto del messaggio dell'alert
     * @param azione    il consumatore da eseguire in base al pulsante scelto dall'utente
     */
    public static void mostraAlertConAzione(Alert.AlertType tipo, String titolo, String header, String contenuto, Consumer<ButtonType> azione) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AlertUtils.class.getResource("/it/unisa/diem/main/style.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("dialog-pane");

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType sceltaEffettiva = result.orElse(ButtonType.CLOSE);
        azione.accept(sceltaEffettiva);
    }
}
