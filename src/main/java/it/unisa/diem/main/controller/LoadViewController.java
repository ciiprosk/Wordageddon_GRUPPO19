package it.unisa.diem.main.controller;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.concurrent.Task;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LoadViewController {

    @FXML private Label difficultyLabel;
    @FXML private Label textLabel;
    @FXML private ProgressBar progressBar;

    private Difficolta difficolta;
    private Lingua lingua;
    private Utente utente;

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;


        switch (difficolta.toString().toLowerCase()) {
            case "facile": textLabel.setText("This is going to be easy :)"); difficultyLabel.setText("EASY"); break;
            case "intermedio": textLabel.setText("Starting to sweat..."); difficultyLabel.setText("NORMAL"); break;
            case "difficile": textLabel.setText("THIS is gonna be a challenge.");difficultyLabel.setText("HARD"); break;
            default: textLabel.setText("no text");
        }

        // Creiamo un task per simulare il caricamento
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int steps = 100;
                for (int i = 1; i <= steps; i++) {
                    Thread.sleep(20); // Simula lavoro
                    updateProgress(i, steps);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                goToTextView(); // Quando il task termina, cambia scena
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        // Avvia il task su un nuovo thread
        new Thread(task).start();
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }


    private void goToTextView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/ReadTextView.fxml"));
            Parent root = loader.load();

            // Passa la difficoltà al controller di ReadTextView
            ReadTextViewController controller = loader.getController();
            controller.setDifficolta(difficolta);
            controller.setLingua(lingua);
            controller.setUtente(utente);

            Stage stage = (Stage) difficultyLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leggi il Testo");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUtente(Utente utenteToPass) {
        this.utente = utenteToPass;
    }
}
