package it.unisa.diem.main.controller;

import it.unisa.diem.main.Main;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DifficultySelectionViewController {
    @FXML private Button facileButton;
    @FXML private Button medioButton;
    @FXML private Button difficileButton;
    @FXML private Button backButton;
    @FXML private Button itButton;
    @FXML private Button engButton;
    @FXML private Button playButton;

    private Difficolta selectedDifficolta = null;
    private Lingua selectedLanguage = null;
    private Utente utente = null;

    @FXML
    public void initialize() {
        Image back= new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image it = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/it.png"));
        ImageView itView = new ImageView(it);
        itView.setFitWidth(20);
        itView.setFitHeight(20);
        itButton.setGraphic(itView);

        Image eng = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/eng.png"));
        ImageView engView = new ImageView(eng);
        engView.setFitWidth(20);
        engView.setFitHeight(20);
        engButton.setGraphic(engView);


            facileButton.setOnAction(e -> {
                selectedDifficolta = Difficolta.FACILE;
                highlightSelectedButton(facileButton);
            });

            medioButton.setOnAction(e -> {
                selectedDifficolta = Difficolta.INTERMEDIO;
                highlightSelectedButton(medioButton);
            });

            difficileButton.setOnAction(e -> {
                selectedDifficolta = Difficolta.DIFFICILE;
                highlightSelectedButton(difficileButton);
            });

    }

    //funzione per aggiungere css a pulsante cliccato
    private void highlightSelectedButton(Button selectedButton) {
        facileButton.getStyleClass().remove("selected-button");
        medioButton.getStyleClass().remove("selected-button");
        difficileButton.getStyleClass().remove("selected-button");

        if (!selectedButton.getStyleClass().contains("selected-button")) {
            selectedButton.getStyleClass().add("selected-button");
        }
    }


    private void goToLoadView(Difficolta difficolta, Lingua lingua, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LoadView.fxml"));
            Parent root = loader.load();
            LoadViewController controller = loader.getController();
            controller.setDifficolta(difficolta);
            controller.setLingua(lingua);
            controller.setUtente(utente);


            Stage stage = (Stage) sourceButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        selectedDifficolta = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            //controller.setUtente(utente);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handlePlayButton() {
        if (selectedDifficolta != null && selectedLanguage != null) {
            goToLoadView(selectedDifficolta, selectedLanguage, playButton);
        } else {
            if (selectedDifficolta == null && selectedLanguage == null) {
                System.out.println("Seleziona una difficoltà e una lingua prima di continuare.");
            } else if (selectedDifficolta == null) {
                System.out.println("Seleziona una difficoltà prima di continuare.");
            } else {
                System.out.println("Seleziona una lingua prima di continuare.");
            }
        }
    }


    @FXML
    private void handleItButton() {
        selectedLanguage = Lingua.ITA;
        System.out.println("hai clickato it");

        engButton.getStyleClass().remove("selected-button");

        if (!itButton.getStyleClass().contains("selected-button")) {
            itButton.getStyleClass().add("selected-button");
        }
    }

    @FXML
    private void handleEngButton() {
        selectedLanguage = Lingua.ENG;
        System.out.println("hai clickato eng");
        itButton.getStyleClass().remove("selected-button");

        if (!engButton.getStyleClass().contains("selected-button")) {
            engButton.getStyleClass().add("selected-button");
        }
    }


    public void setUtente(Utente utenteToPass) {
        this.utente = utenteToPass;
    }
}
