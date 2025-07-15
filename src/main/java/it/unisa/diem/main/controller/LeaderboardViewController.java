package it.unisa.diem.main.controller;

import it.unisa.diem.main.Main;
import it.unisa.diem.main.service.LoadLeaderboardService;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.classifica.VoceClassifica;
import it.unisa.diem.utility.AlertUtils;
import javafx.beans.property.SimpleDoubleProperty;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardViewController {

    @FXML private Button historyButton;
    @FXML private Button backButton;
    @FXML private ComboBox<String> difficoltaComboBox;
    @FXML private TableView<VoceClassifica> leaderboardTable;
    @FXML private TableColumn<VoceClassifica, String> usernameCol;
    @FXML private TableColumn<VoceClassifica, Number> mediaCol;
    @FXML private TableColumn<VoceClassifica, Number> sumCol;

    @FXML private StackPane loadingOverlay;
    @FXML private ProgressIndicator loadingSpinner;
    @FXML private Label loadingMessageLabel;

    private final LoadLeaderboardService leaderboardService = new LoadLeaderboardService();
    private final Map<Difficolta, List<VoceClassifica>> leaderboardCache = new HashMap<>();

    @FXML
    public void initialize() {
        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image history = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/history.png"));
        ImageView historyView = new ImageView(history);
        historyView.setFitWidth(30);
        historyView.setFitHeight(30);
        historyButton.setGraphic(historyView);

        usernameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        sumCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSommaPunteggio()));
        mediaCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMediaPunteggio()));

        difficoltaComboBox.getItems().addAll("EASY", "NORMAL", "HARD");
        difficoltaComboBox.setValue("EASY"); // 🔵 Default

        difficoltaComboBox.setOnAction(event -> updateTableForSelection(difficoltaComboBox.getValue()));

        loadAllLeaderboards();
    }

    private void loadAllLeaderboards() {
        showLoadingOverlay("Caricamento classifica...");

        leaderboardService.setOnSucceeded(event -> {
            hideLoadingOverlay();
            Map<Difficolta, List<VoceClassifica>> risultati = leaderboardService.getValueMap();
            leaderboardCache.putAll(risultati);
            updateTableForSelection(difficoltaComboBox.getValue());
        });

        leaderboardService.setOnFailed(event -> {
            hideLoadingOverlay();
            Throwable e = leaderboardService.getException();
            e.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore", null,
                    "Errore durante il caricamento della leaderboard:\n" + (e != null ? e.getMessage() : "Errore sconosciuto"));
        });

        leaderboardService.start();
    }


    private void updateTableForSelection(String difficolta) {
        Difficolta diff = switch (difficolta) {
            case "EASY" -> Difficolta.FACILE;
            case "NORMAL" -> Difficolta.INTERMEDIO;
            case "HARD" -> Difficolta.DIFFICILE;
            default -> throw new IllegalArgumentException("Difficoltà non valida: " + difficolta);
        };

        List<VoceClassifica> lista = leaderboardCache.getOrDefault(diff, List.of());
        leaderboardTable.getItems().setAll(lista);
    }

    private void showLoadingOverlay(String message) {
        loadingMessageLabel.setText(message);
        loadingOverlay.setVisible(true);
    }

    private void hideLoadingOverlay() {
        loadingOverlay.setVisible(false);
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
        }
    }

    public void goToHistory(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HistoryView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("History");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
