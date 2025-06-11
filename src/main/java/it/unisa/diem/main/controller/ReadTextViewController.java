package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.utenti.Utente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ReadTextViewController {


    @FXML Label textTitleLabel;
    @FXML Label textBodyLabel;

    private Difficolta difficolta;
    private Lingua lingua;
    private String path;
    private Documento documento;
    private Documento documentoOriginale = null;

    private List<String> words = new ArrayList<>();

    @FXML private Button continueButton;
    private Utente utente;

    public void setDifficolta(Difficolta difficolta) {
        this.difficolta = difficolta;
        System.out.println("Difficoltà ricevuta: " + difficolta);
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
        System.out.println("Lingua ricevuta: " + lingua);
        getTextFileNames();
    }

    @FXML
    private void initialize() {
    }





    private void getTextFileNames() {
        path = "data/"+lingua+"/"+difficolta+"/";
        List<String> files = new ArrayList<>();

        DocumentoDAOPostgres dao = new DocumentoDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");
        try {
            files = dao.selectTitlesByLangAndDif(lingua, difficolta);
        } catch (DBException e) {
            throw new RuntimeException(e);
            //alert
        }


        for(String file : files) {
            System.out.println(file.toString());
        }

        selectFile(files);
    }

    private void selectFile(List<String> titoli) {
        String titolo = "";
        if (titoli != null && !titoli.isEmpty()) {
            Random random = new Random();
            titolo = titoli.get(random.nextInt(titoli.size()));
            System.out.println("Titolo selezionato: " + titolo);
            textTitleLabel.setText(titolo);
        } else {
            System.out.println("La lista dei titoli è vuota o nulla.");
            //alert
        }
        String newPath = path + titolo + ".bin";
        try {
            documento = Documento.leggiDocumento(newPath);
            words = documento.getTesto();
        } catch (IOException e) {
            throw new RuntimeException(e);
            //alert

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
            //alert
        }

        setBodyText();
    }

    private void setBodyText() {
        textBodyLabel.setWrapText(true);
        textBodyLabel.setText(words.stream().collect(Collectors.joining("\n")));
        System.out.println(words.stream().collect(Collectors.joining("\n")));
    }

    private void goToQuestionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/QuestionView.fxml"));
            Parent root = loader.load();
            QuestionViewController controller = loader.getController();
            controller.setUtente(utente);

            if (documentoOriginale != null) {
                controller.setDocumenti(documentoOriginale, documento);  // Passo entrambi i documenti
            } else {
                controller.setDocumento(documento); // Solo uno se difficoltà FACILE
            }

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Domande");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleContinue(ActionEvent actionEvent) {
        if(difficolta != Difficolta.FACILE){
            goToSecondText();
        }else{
            goToQuestionView();
        }
    }

    private void goToSecondText() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/ReadTextView.fxml"));
            Parent root = loader.load();
            ReadTextViewController controller = loader.getController();
            controller.setUtente(utente);
            controller.setLingua(lingua);
            controller.setDifficolta(difficolta);
            controller.setDocumentoOriginale(this.documento);

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Secondo Documento");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setDocumentoOriginale(Documento documento) {
        documentoOriginale = documento;
    }

    public void setUtente(Utente utenteToPass) {
        this.utente = utenteToPass;
    }


}
