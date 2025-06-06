package it.unisa.diem.main.controller;

import it.unisa.diem.main.Main;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.swing.*;

//Da implementare una lista dei documenti che tiene traccia dei tioli dei documenti

public class AdminScreenView {

    @FXML private Button importButton;
    @FXML private Button backButton;
    @FXML private ListView<String> stopwordsListView = new ListView<>();
    @FXML private TextField inputField;
    @FXML private CheckBox checkArticles;
    @FXML private CheckBox checkPrepositions;
    @FXML private CheckBox checkToBe;
    @FXML private CheckBox checkToHave;
    @FXML private CheckBox checkCon;
    @FXML private CheckBox checkPronouns;
    @FXML private Button confirmButton;
    @FXML private TextField textTitle;
    @FXML private CheckBox checkIt;
    @FXML private CheckBox checkEng;
    @FXML private CheckBox checkEasy;
    @FXML private CheckBox checkNormal;
    @FXML private CheckBox checkHard;


    @FXML
    public void initialize() {
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        stopwordsListView.getItems().addAll("Apple", "Banana", "Cherry");
        stopwordsListView.setEditable(true);

        stopwordsListView.setCellFactory(TextFieldListCell.forListView());

        stopwordsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            stopwordsListView.getItems().set(index, newValue);
        });

    }



    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }


    @FXML
    private void handleAdd() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            stopwordsListView.getItems().add(text);
            inputField.clear();
        }
    }

    public void handleRemove(ActionEvent actionEvent) {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            stopwordsListView.getItems().remove(text);  // Rimuove solo se esiste un elemento con quel testo
            inputField.clear();
        }
    }
}