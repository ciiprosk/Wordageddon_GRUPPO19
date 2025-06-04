package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class AdminScreenView {

    @FXML private Button importButton;
    @FXML private Button backButton;
    @FXML private TextArea stopwordsArea;
    @FXML private CheckBox checkArticles;
    @FXML private CheckBox checkPrepositions;
    @FXML private CheckBox checkToBe;
    @FXML private CheckBox checkToHave;
    @FXML private CheckBox checkCon;
    @FXML private CheckBox checkPronouns;
    @FXML private Button confirmButton;

    @FXML
    public void initialize() {
        stopwordsArea.textProperty().addListener((obs, oldVal, newVal) -> containsSpecialCharacters());
    }

    public void containsSpecialCharacters() {
        String text = stopwordsArea.getText();

        boolean areaIsFilled = !text.isEmpty();

        // Check if text contains ONLY letters and spaces (valid text)
        boolean areaIsValid = text.matches("[a-zA-Z\\s]*");

        // Enable button only if text is filled AND valid (no special characters)
        boolean areaIsRegular = areaIsFilled && areaIsValid;

        confirmButton.setDisable(!areaIsRegular);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }


}