package it.unisa.diem.main.controller;

import it.unisa.diem.utility.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ListTextsController {

    @FXML private ListView<String> ListView;
    @FXML private Button MenuButton;
    private ObservableList<String> titoliList;
    private String newTitle;

    @FXML
    public void initialize() {
        titoliList = FXCollections.observableArrayList(
                "Titolo 1", "Titolo 2", "Titolo 3"
        );
        ListView.setItems(titoliList);
    }

    public void setNewTitle(String titolo) {
        newTitle = titolo;
        addTitle();
    }

    private void addTitle() {
        System.out.println(newTitle);
        if (ListView != null) {
            titoliList.add(newTitle);
        }
    }

    @FXML
    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", MenuButton);
    }
}
