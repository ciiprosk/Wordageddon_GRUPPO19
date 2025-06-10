package it.unisa.diem.main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadTextViewController {


    @FXML Label textTitleLabel;
    @FXML Label textBodyLabel;

    private String difficolta;
    private String lingua;

    @FXML private Button continueButton;

    public void setDifficolta(String difficolta) {
        this.difficolta = difficolta;
        System.out.println("Difficoltà ricevuta: " + difficolta);
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
        System.out.println("Lingua ricevuta: " + lingua);
    }

    @FXML
    private void initialize() {
        getTextFileNames();
    }





    private List<String> getTextFileNames() {
        String path = "data/"+lingua+"/"+difficolta+"/";



        List<String> files = new ArrayList<>();


        return files;
    }


    private void goToQuestionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/QuestionView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) continueButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Domande");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleContinue(ActionEvent actionEvent) {
        goToQuestionView();
    }

}
