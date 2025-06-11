package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.AnalisiDAOPostgres;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.Main;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordENG;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class AdminScreenViewController {

    @FXML private Button importButton;
    @FXML private Button backButton;
    @FXML private ListView<String> stopwordsListView;
    @FXML private TextField inputField;
    @FXML private CheckBox checkArticles;
    @FXML private CheckBox checkPrepositions;
    @FXML private CheckBox checkToBe;
    @FXML private CheckBox checkToHave;
    @FXML private CheckBox checkCon;
    @FXML private CheckBox checkPronouns;
    @FXML private Button confirmButton;
    @FXML private TextField titleField;
    @FXML private CheckBox checkIt;
    @FXML private CheckBox checkEng;
    @FXML private CheckBox checkEasy;
    @FXML private CheckBox checkNormal;
    @FXML private CheckBox checkHard;
    @FXML private Label alertLabel;

    Utente utente;

    private StopwordManager stopword;
    private ObservableList<String> observableList;
    String titolo = null;
    Lingua lingua;
    Difficolta difficolta;
    private File fileImportato;


    @FXML
    public void initialize() {
        /*esempio temporaneo*/
        StopwordITA stopwordITA = new StopwordITA();
        stopwordITA.aggiungi("esempio");
        /*temporaneo*/

        /*disabilita il label di errore*/
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);

        /*imposta le parole della lista di stopword come */
        observableList = FXCollections.observableArrayList(stopwordITA.getParole());
        stopwordsListView.setItems(observableList);

        /*rende la lista editabile*/
        stopwordsListView.setEditable(true);
        stopwordsListView.setCellFactory(TextFieldListCell.forListView());
        stopwordsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue().trim();

            if (newValue.isEmpty()) {
                alertLabel.setText("Non si possono inserire parole vuote.");
                alertLabel.setVisible(true);
                alertLabel.setManaged(true);
                return;
            }

            if (newValue.contains(" ")) {
                alertLabel.setText("Le parole non possono contenere spazi.");
                alertLabel.setVisible(true);
                alertLabel.setManaged(true);
                return;
            }

            alertLabel.setVisible(false);
            alertLabel.setManaged(false);
            stopwordsListView.getItems().set(index, newValue);
        });

        /*imposta l'immagine del tasto indietro*/
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        //disabilita del pulsante di conferma
        confirmButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldText, newText) -> validateConfirmButton());

    }

    //riempimento lista a seconda della lingua
    public void listaStopwordIta() {

        stopword = new StopwordITA();
        stopword.aggiungi("esempio");


        /*disabilita il messaggio di errore*/
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);

        /*imposta le parole della lista di stopword come */
        observableList = FXCollections.observableArrayList(stopword.getParole());
        stopwordsListView.setItems(observableList);

        /*rende la lista editabile*/
        stopwordsListView.setEditable(true);
        stopwordsListView.setCellFactory(TextFieldListCell.forListView());
        stopwordsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            stopwordsListView.getItems().set(index, newValue);
        });
    }

    public void listaStopwordEng() {
        /*esempio temporaneo*/
        stopword = new StopwordENG();
        stopword.aggiungi("example");
        /*temporaneo*/

        /*disabilita il messaggio di errore*/
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);

        /*imposta le parole della lista di stopword come */
        observableList = FXCollections.observableArrayList(stopword.getParole());
        stopwordsListView.setItems(observableList);

        /*rende la lista editabile*/
        stopwordsListView.setEditable(true);
        stopwordsListView.setCellFactory(TextFieldListCell.forListView());
        stopwordsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue();
            stopwordsListView.getItems().set(index, newValue);
        });
    }

    //checklist lingue e difficolta
    @FXML
    private void handleDifficultySelection(ActionEvent event) {
        CheckBox selected = (CheckBox) event.getSource();

        if (selected.isSelected()) {
            if (selected == checkEasy) {
                checkNormal.setSelected(false);
                checkHard.setSelected(false);

                difficolta = Difficolta.FACILE;
            } else if (selected == checkNormal) {
                checkEasy.setSelected(false);
                checkHard.setSelected(false);

                difficolta = Difficolta.INTERMEDIO;
            } else if (selected == checkHard) {
                checkEasy.setSelected(false);
                checkNormal.setSelected(false);

                difficolta = Difficolta.DIFFICILE;
            }
        }

        validateConfirmButton();
    }

    @FXML
    private void handleLanguageSelection(ActionEvent event) {
        CheckBox selected = (CheckBox) event.getSource();
        if (selected.isSelected()) {
            if (selected == checkIt) {
                checkEng.setSelected(false);
                emptyList();
                listaStopwordIta();

                lingua = Lingua.ITA;
            } else if (selected == checkEng) {
                checkIt.setSelected(false);
                emptyList();
                listaStopwordEng();

                lingua = Lingua.ENG;
            }
        }
        validateConfirmButton();
    }

    //Pulsante indietro
    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void goToListTexts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/ListTextsView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) importButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Titles");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    //ListView
    @FXML
    private void handleAdd() {
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);
        String text = inputField.getText().trim();

        if (text.contains(" ")) {
            alertLabel.setText("Le parole non possono contenere spazi.");
            alertLabel.setVisible(true);
            alertLabel.setManaged(true);
            return;
        }

        if (!text.isEmpty() && !stopwordsListView.getItems().contains(text)) {
            stopwordsListView.getItems().add(text);
            inputField.clear();
        } else if (stopwordsListView.getItems().contains(text)) {
            alertLabel.setText("La parola è già presente.");
            alertLabel.setVisible(true);
            alertLabel.setManaged(true);
            System.out.println("La parola " + text + " è già presente");
        }
    }

    public void handleRemove(ActionEvent actionEvent) {
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            stopwordsListView.getItems().remove(text);
            inputField.clear();
        }
    }

    public void emptyList() {
        stopwordsListView.getItems().clear();
    }


    //import del file tramite filechooser
    public void handleImport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file TXT da importare");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("File di testo", "*.txt")
        );

        Window stage = importButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileImportato = selectedFile;
            validateConfirmButton();
        }
    }



    public void handleConfirm(ActionEvent actionEvent) {
        titolo = titleField.getText().trim();


        stopword.getParole().clear(); //senza questo potrebbe riapparire una parola modificata manualmente dall'utente (credo)
        for (String parola : stopwordsListView.getItems()) {
            stopword.aggiungi(parola);
        }


        stopword.caricaStopword(checkArticles.isSelected(), checkPrepositions.isSelected(), checkPronouns.isSelected(), checkToHave.isSelected(), checkToBe.isSelected(), checkCon.isSelected());
        System.out.println(stopword.getParole());
        Documento documento = new Documento(titolo, lingua, difficolta);

        try {
            documento.convertiTxtToBin(fileImportato);
        }catch (IOException e) {
            e.printStackTrace();
        }

        DocumentoDAOPostgres daoDoc = new DocumentoDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");
        AnalisiDAOPostgres daoAn = new AnalisiDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");
        try {
            Analisi analisi = new Analisi(documento, stopword);
            analisi.caricaAnalisi();
            daoDoc.insert(documento);
            daoAn.insert(analisi);
        } catch (DBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        goToListTexts();
    }

    private void validateConfirmButton() {
        boolean isTitleFilled = !titleField.getText().trim().isEmpty();
        boolean isFileImported = fileImportato != null;
        boolean isLanguageSelected = checkIt.isSelected() || checkEng.isSelected();
        boolean isDifficultySelected = checkEasy.isSelected() || checkNormal.isSelected() || checkHard.isSelected();

        confirmButton.setDisable(!(isTitleFilled && isFileImported && isLanguageSelected && isDifficultySelected));
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
        System.out.println(utente.getUsername());
    }

}