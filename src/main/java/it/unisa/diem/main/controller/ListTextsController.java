package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ListTextsController {

    @FXML private ListView<String> ListView;
    @FXML private Button MenuButton;
    private ObservableList<String> titoliList;
    private String newTitle;
    private Utente utente;

    @FXML
    public void initialize() {
        DocumentoDAOPostgres dao = new DocumentoDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");

        try {
            List<String> ListDao = dao.selectAllTitles();
            titoliList = FXCollections.observableArrayList(ListDao);
            ListView.setItems(titoliList);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) MenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
