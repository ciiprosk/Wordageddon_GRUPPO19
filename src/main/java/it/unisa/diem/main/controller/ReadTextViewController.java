package it.unisa.diem.main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ReadTextViewController {

    private String difficolta;
    private String lingua;

    @FXML
    private Button continueButton;

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
        continueButton.setOnAction(e -> goToQuestionView());
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
