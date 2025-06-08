package it.unisa.diem.utility;

import javafx.scene.control.Alert;

public class AlertUtils {

    public static void mostraAlert(Alert.AlertType tipo, String titolo, String header, String contenuto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}
