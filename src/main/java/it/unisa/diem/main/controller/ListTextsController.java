package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.utility.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListTextsController {

    @FXML private ListView<String> ListView;
    @FXML private Button MenuButton;
    private ObservableList<String> titoliList;
    private String newTitle;

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
        SceneLoader.load("HomeMenuView.fxml", MenuButton);
    }
}
