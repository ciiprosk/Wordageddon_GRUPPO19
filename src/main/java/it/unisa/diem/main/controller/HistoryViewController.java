package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.StoricoSessioneDAOPostgres;
import it.unisa.diem.main.Main;
import it.unisa.diem.main.service.HistoryService;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.VoceStorico;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
import it.unisa.diem.utility.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryViewController {
    @FXML private Button leaderboardButton;
    @FXML private Button backButton;

    @FXML private TableView<VoceStorico> tableView;
    @FXML private TableColumn<VoceStorico, String> dateColumn;
    @FXML private TableColumn<VoceStorico, Integer> scoreColumn;
    @FXML private TableColumn<VoceStorico, String> langColummn;
    @FXML private ComboBox<String> difficoltaComboBox;
    @FXML private StackPane loadingOverlay;
    @FXML private ProgressIndicator loadingSpinner;
    @FXML private Label loadingMessageLabel;


    private Map<Difficolta, List<VoceStorico>> storicoPerDifficolta = new HashMap<>();

    @FXML
    public void initialize() {
        // 🔵 Imposta immagini bottoni
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

        // 🔵 Configura colonne
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime dataFine = cellData.getValue().getDataFine();
            String formattedDate = dataFine != null
                    ? dataFine.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : ":/";
            return new SimpleStringProperty(formattedDate);
        });

        scoreColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getPunteggio()).asObject()
        );

        langColummn.setCellValueFactory(cellData -> {
            Lingua lingua = cellData.getValue().getLingua();
            return new SimpleStringProperty(lingua != null ? lingua.toString() : ":/");
        });

        // 🔵 Configura ComboBox difficoltà
        difficoltaComboBox.getItems().addAll("EASY", "NORMAL", "HARD");
        difficoltaComboBox.setValue("EASY"); // default selezione

        // 🔵 Listener per filtrare la tabella in base alla selezione
        difficoltaComboBox.setOnAction(event -> {
            String selezione = difficoltaComboBox.getValue();
            updateTableForSelection(selezione);
        });

        // 🔵 Carica tutti i dati all'avvio
        loadAllHistory();
    }

    private void loadAllHistory() {
        Utente utente = SessionManager.getInstance().getUtenteLoggato();
        if (utente == null) {
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore sessione", null, "Utente non loggato.");
            return;
        }

        showLoadingOverlayWithMessage("Caricamento storico in corso..."); // 🔵 Mostra overlay

        HistoryService service = new HistoryService();

        service.setOnSucceeded(event -> {
            hideLoadingOverlay(); // 🔵 Nascondi overlay
            Map<Difficolta, List<VoceStorico>> risultati = service.getValueMap();
            storicoPerDifficolta.putAll(risultati);
            updateTableForSelection(difficoltaComboBox.getValue());
        });

        service.setOnFailed(event -> {
            hideLoadingOverlay(); // 🔵 Nascondi overlay
            Throwable e = service.getException();
            e.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore caricamento", null,
                    "Errore durante il caricamento dello storico:\n" + (e != null ? e.getMessage() : "Errore sconosciuto"));
        });

        service.setParameters(utente.getUsername());
        service.start();
    }


    private void updateTableForSelection(String difficolta) {
        Difficolta difficoltaDB = switch (difficolta) {
            case "EASY" -> Difficolta.FACILE;
            case "NORMAL" -> Difficolta.INTERMEDIO;
            case "HARD" -> Difficolta.DIFFICILE;
            default -> throw new IllegalArgumentException("Difficoltà non valida: " + difficolta);
        };

        List<VoceStorico> storico = storicoPerDifficolta.getOrDefault(difficoltaDB, List.of());
        tableView.getItems().setAll(storico);
    }

    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore", null, "Impossibile caricare la schermata Home.");
        }
    }

    public void goToLeaderboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/LeaderboardView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboard");
        } catch (Exception ex) {
            ex.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore", null, "Impossibile caricare la schermata Leaderboard.");
        }
    }

    private void showLoadingOverlayWithMessage(String message) {
        loadingMessageLabel.setText(message);
        loadingOverlay.setVisible(true);
    }

    private void hideLoadingOverlay() {
        loadingOverlay.setVisible(false);
    }

}
