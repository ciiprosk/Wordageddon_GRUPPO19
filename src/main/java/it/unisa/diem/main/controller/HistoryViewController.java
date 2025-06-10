package it.unisa.diem.main.controller;

import java.time.LocalDateTime;
import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.Main;
import it.unisa.diem.model.gestione.sessione.StoricoSessione;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.SceneLoader;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
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


        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime dataFine = cellData.getValue().getDataFine();
            String formattedDate = dataFine.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            return new SimpleStringProperty(formattedDate);
        });

        scoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPunteggio()).asObject()
        );
    }

    private void loadStoricoSessioni() {
        StoricoSessioneDAOPostgres dao = new StoricoSessioneDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");

        List<StoricoSessione> storicoSessioni = null;
        try {
            storicoSessioni = dao.selectByUser(utente.getUsername());
        } catch (SQLException | DBException e) {
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "DATABASE ERROR", "Impossibile far visualizzare lo storico", "Controllare la connessione al database e riprovare.");
        }
        ObservableList<StoricoSessione> observableSessioni = FXCollections.observableArrayList(storicoSessioni);
        for (StoricoSessione sessione : observableSessioni) {
            System.out.println(sessione.toString());
        }
        tableView.setItems(observableSessioni);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LeaderboardView.fxml"));
            Parent root = loader.load();
            LeaderboardViewController controller = loader.getController();
            controller.setUtente(utente);
            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
        System.out.println(utente.getUsername());
        loadStoricoSessioni();
    }
}
