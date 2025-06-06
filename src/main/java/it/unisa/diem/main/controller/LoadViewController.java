package it.unisa.diem.main.controller;

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

    private String difficolta;

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
        difficultyLabel.setText(difficolta);

        switch (difficolta.toLowerCase()) {
            case "easy": textLabel.setText("This is going to be easy :)"); break;
            case "normal": textLabel.setText("Starting to sweat..."); break;
            case "hard": textLabel.setText("THIS is gonna be a challenge."); break;
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
                goToQuestionView(); // Quando il task termina, cambia scena
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        // Avvia il task su un nuovo thread
        new Thread(task).start();
    }


    private void goToQuestionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/QuestionView.fxml"));
            Parent root = loader.load();

            // Passa la difficoltà al controller di QuestionView
            QuestionViewController controller = loader.getController();
            controller.setDifficulty(difficolta);

            Stage stage = (Stage) difficultyLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Domande");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
