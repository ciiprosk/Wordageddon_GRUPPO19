package it.unisa.diem.main.controller;

//import it.unisa.diem.dao.postgres.ClassificaDAOPostgres;
import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.Main;
//import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.PropertiesLoader;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class LeaderboardViewController {
    @FXML private Button historyButton;
    @FXML private Button backButton;
    @FXML private ComboBox<String> difficoltaComboBox;
    @FXML private TableView<VoceClassifica> leaderboardTable;
    @FXML private TableColumn<VoceClassifica, String> usernameCol;
    @FXML private TableColumn<VoceClassifica, Number> mediaCol;
    @FXML private TableColumn<VoceClassifica, Number> sumCol;

    private Utente utenteToPass;

 private final StoricoSessioneDAOPostgres StoricoSessioneDAO =
            new StoricoSessioneDAOPostgres(PropertiesLoader.getProperty("database.url"), PropertiesLoader.getProperty("database.user"), PropertiesLoader.getProperty("database.password"));



    @FXML
    public void initialize() {
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image history= new Image(Main.class.getClassLoader().getResourceAsStream("immagini/history.png"));
        ImageView historyView = new ImageView(history);
        historyView.setFitWidth(30);
        historyView.setFitHeight(30);
        historyButton.setGraphic(historyView);

        usernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        sumCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSommaPunteggio()));
        mediaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getMediaPunteggio()));

        // ComboBox difficoltà
        difficoltaComboBox.getItems().addAll("EASY", "NORMAL", "HARD");
        difficoltaComboBox.setOnAction(event -> {
            String selezione = difficoltaComboBox.getValue();
            loadTable(selezione);
        });
    }

    private void loadTable(String difficolta) {
        System.out.println("Carico classifica per difficoltà: " + difficolta);

        Difficolta difficoltaDB = switch (difficolta) {
            case "EASY" -> Difficolta.FACILE;
            case "NORMAL" -> Difficolta.INTERMEDIO;
            case "HARD" -> Difficolta.DIFFICILE;
            default -> throw new IllegalArgumentException("Difficoltà non valida: " + difficolta);
        };

        List<VoceClassifica> top10 = null;
        try {
            top10 = StoricoSessioneDAO.selectByTopRanking(difficoltaDB);
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Numero risultati: " + top10.size());
        for (VoceClassifica voce : top10) {
            System.out.println(voce.getUsername() + " - " + voce.getSommaPunteggio() + " - " + voce.getMediaPunteggio());
        }

        leaderboardTable.getItems().setAll(top10);

    }

    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            HomeMenuViewController controller = loader.getController();
            controller.setUtente(utenteToPass);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HistoryView.fxml"));
            Parent root = loader.load();
            HistoryViewController controller = loader.getController();
            controller.setUtente(utenteToPass);
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Caricamento");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUtente(Utente utente) {
        utenteToPass = utente;
    }
}
