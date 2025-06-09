package it.unisa.diem.main.controller;

//import it.unisa.diem.dao.postgres.ClassificaDAOPostgres;
import it.unisa.diem.main.Main;
//import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.PropertiesLoader;
import it.unisa.diem.utility.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class LeaderboardViewController {
    @FXML private Button historyButton;
    @FXML private Button backButton;
    @FXML private ComboBox<String> difficoltaComboBox;
 //   @FXML private TableView<VoceClassifica> leaderboardTable;
 //   @FXML private TableColumn<VoceClassifica, String> usernameCol;
 //   @FXML private TableColumn<VoceClassifica, Number> mediaCol;

    private Utente utenteToPass;
/*
 private final ClassificaDAOPostgres classificaDAO =
            new ClassificaDAOPostgres(PropertiesLoader.getProperty("database.url"), PropertiesLoader.getProperty("database.user"), PropertiesLoader.getProperty("database.password"));

 */

    @FXML
    public void initialize() {
        PropertiesLoader.init();
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

 //       usernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
 //       mediaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getMediaPunteggio()));

        // ComboBox difficoltà
        difficoltaComboBox.getItems().addAll("EASY", "NORMAL", "HARD");
        difficoltaComboBox.setOnAction(event -> {
            String selezione = difficoltaComboBox.getValue();
            loadTable(selezione);
        });
    }

    private void loadTable(String difficolta) {
        System.out.println("Carico classifica per difficoltà: " + difficolta);

        String difficoltaDB = switch (difficolta) {
            case "EASY" -> "FACILE";
            case "NORMAL" -> "INTERMEDIO";
            case "HARD" -> "DIFFICILE";
            default -> throw new IllegalArgumentException("Difficoltà non valida: " + difficolta);
        };
/*
        List<VoceClassifica> top10 = classificaDAO.getTop10ByDifficolta(difficoltaDB);
        System.out.println("Numero risultati: " + top10.size());
        for (VoceClassifica voce : top10) {
            System.out.println(voce.getUsername() + " - " + voce.getMediaPunteggio());
        }

        leaderboardTable.getItems().setAll(top10);

 */
    }




    public void goToMainMenu(ActionEvent actionEvent) {
        SceneLoader.load("HomeMenuView.fxml", backButton);
    }

    public void goToHistory(ActionEvent actionEvent) {
        SceneLoader.load("HistoryView.fxml", historyButton);
    }

    public void setUtente(Utente utente) {
        utenteToPass = utente;
    }
}
