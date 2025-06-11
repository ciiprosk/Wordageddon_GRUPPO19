package it.unisa.diem.main.controller;

import java.time.LocalDateTime;

import it.unisa.diem.dao.postgres.SessioneDocumentoDAOPostgres;
import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.Main;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.StoricoSessione;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
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
    @FXML private TableColumn<StoricoSessione, String> modeColummn;
    @FXML private TableColumn<StoricoSessione, String> langColummn;


    private Utente utente;
    private String url;
    private String username;
    private String password;

    @FXML
    public void initialize() {
        //sono rosa inizializzo i campi per le query
        url = PropertiesLoader.getProperty("database.url");
        username = PropertiesLoader.getProperty("database.user");
        password = PropertiesLoader.getProperty("database.password");

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
            if(dataFine != null) {
                return new SimpleStringProperty(formattedDate);
            }else{
                return new SimpleStringProperty(":/");
            }
        });

        scoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPunteggio()).asObject()
        );

        langColummn.setCellValueFactory(cellData -> {
            Lingua lingua = cellData.getValue().getLingua();
            if(lingua != null) {
                return new SimpleStringProperty(lingua.toString());
            } else {
                return new SimpleStringProperty(":/");
            }
        });

        modeColummn.setCellValueFactory(cellData -> {
            Difficolta difficolta = cellData.getValue().getDifficolta();
            if(difficolta != null) {
                return new SimpleStringProperty(difficolta.toString());
            } else {
                return new SimpleStringProperty(":/");
            }
        });
    }

    private void loadStoricoSessioni() {
        StoricoSessioneDAOPostgres dao = new StoricoSessioneDAOPostgres(url, username, password);
        SessioneDocumentoDAOPostgres docDao = new SessioneDocumentoDAOPostgres(url, username, password);

        List<StoricoSessione> storicoSessioni = null;
        try {
            storicoSessioni = dao.selectByUser(utente.getUsername());

            for (StoricoSessione sessione : storicoSessioni) {
                List<Documento> documenti = docDao.selectDocumentsBySession(sessione.getId());

                if (!documenti.isEmpty()) {
                    Documento documento = documenti.get(0);
                    sessione.setLingua(documento.getLingua());
                    sessione.setDifficolta(documento.getDifficolta());
                } else {
                    sessione.setLingua(null);
                    sessione.setDifficolta(null);
                }
            }

        } catch (SQLException | DBException e) {
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "DATABASE ERROR", "Impossibile far visualizzare lo storico", "Controllare la connessione al database e riprovare.");
        }

        ObservableList<StoricoSessione> observableSessioni = FXCollections.observableArrayList(storicoSessioni);
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
