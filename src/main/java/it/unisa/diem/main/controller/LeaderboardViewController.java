package it.unisa.diem.main.controller;

import it.unisa.diem.main.Main;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LeaderboardViewController {
    @FXML private Button historyButton;
    @FXML private Button backButton;
    @FXML
    private ComboBox<String> difficoltaComboBox;

    @FXML
    public void initialize() {
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image history= new Image(Main.class.getClassLoader().getResourceAsStream("immagini/history.png"));
        ImageView historyView = new ImageView(history);
        historyView.setFitWidth(30);
        historyView.setFitHeight(30);
        historyButton.setGraphic(historyView);

        difficoltaComboBox.getItems().addAll("EASY", "NORMAL", "HARD");


        // Aggiungi il listener per gestire la selezione
        difficoltaComboBox.setOnAction(event -> {
            String selezione = difficoltaComboBox.getValue();
            switch (selezione) {
                case "EASY":
                    loadTable("EASY");
                    break;
                case "NORMAL":
                    loadTable("NORMAL");
                    break;
                case "HARD":
                    loadTable("HARD");
                    break;
            }
        });
    }

    private void loadTable(String difficolta) {
        difficoltaComboBox.setValue(difficolta);
        System.out.println("la tabella selezionata è: " + difficolta);
    }


    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }
}
