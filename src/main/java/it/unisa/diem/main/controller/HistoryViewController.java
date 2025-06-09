package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.main.Main;
import it.unisa.diem.model.gestione.sessione.StoricoSessione;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SceneLoader;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.SQLOutput;
import java.util.List;

public class HistoryViewController {
    @FXML private Button leaderboardButton;
    @FXML private Button backButton;

    @FXML private TableView<StoricoSessione> tableView;
    @FXML private TableColumn<StoricoSessione, String> dateColumn;
    @FXML private TableColumn<StoricoSessione, Integer> scoreColumn;

    private Utente utente;

    @FXML
    public void initialize() {
        // Imposta le immagini dei bottoni
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image leader = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/trophy.png"));
        ImageView leaderView = new ImageView(leader);
        leaderView.setFitWidth(30);
        leaderView.setFitHeight(30);
        leaderboardButton.setGraphic(leaderView);

        // Inizializza colonne della tabella per StoricoSessione
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataFine().toString())
        );

        scoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSessione().getPunteggio()).asObject()
        );
    }

    private void loadStoricoSessioni() {
        StoricoSessioneDAOPostgres dao = new StoricoSessioneDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");

        List<StoricoSessione> storicoSessioni = dao.selectByUser(utente.getUsername());
        ObservableList<StoricoSessione> observableSessioni = FXCollections.observableArrayList(storicoSessioni);

        tableView.setItems(observableSessioni);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        SceneLoader.load("LeaderboardView.fxml", leaderboardButton);
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
        System.out.println(utente.getUsername());
        loadStoricoSessioni();
    }
}
