package it.unisa.diem.main.controller;

import it.unisa.diem.dao.interfacce.DocumentoDAO;
import it.unisa.diem.dao.postgres.DocumentoDAOPostgres;
import it.unisa.diem.main.service.AnalisiService;
import it.unisa.diem.main.service.DeleteDocumentService;
import it.unisa.diem.main.service.LoadTitlesService;
import it.unisa.diem.model.gestione.analisi.*;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordENG;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.AlertUtils;
import it.unisa.diem.utility.PropertiesLoader;
import it.unisa.diem.utility.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminScreenViewController {

    private static final Logger LOGGER = Logger.getLogger(AdminScreenViewController.class.getName());

    // === PANES ===
    @FXML private AnchorPane adminPane;
    @FXML private AnchorPane listPane;

    // === ADMIN SCREEN FIELDS ===
    @FXML private Button importButton;
    @FXML private Button backButton, backButtonList;
    @FXML private ListView<String> stopwordsListView;
    @FXML private TextField inputField;
    @FXML private CheckBox checkArticles, checkPrepositions, checkToBe, checkToHave, checkCon, checkPronouns;
    @FXML private Button confirmButton;
    @FXML private TextField titleField;
    @FXML private CheckBox checkIt, checkEng, checkEasy, checkNormal, checkHard;
    @FXML private Label alertLabel, importedFileLabel;

    private StopwordManager stopword;
    private ObservableList<String> observableList;
    private String titolo = null;
    private Lingua lingua;
    private Difficolta difficolta;
    private File fileImportato;

    // === LIST TEXTS FIELDS ===
    @FXML private ListView<String> ListView;
    private ObservableList<String> titoliList;

    /*
    private final DocumentoDAOPostgres dao = new DocumentoDAOPostgres(
            PropertiesLoader.getProperty("database.url"),
            PropertiesLoader.getProperty("database.user"),
            PropertiesLoader.getProperty("database.password")
    );
    */
    private final DocumentoDAO dao = new DocumentoDAOPostgres();

    // === INITIALIZE ===
    @FXML
    public void initialize() {
        // Setup Admin Screen
        hideAlert();
        importedFileLabel.setVisible(false);

        Image back = new Image(getClass().getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        // Setup freccia back anche su listPane
        ImageView backViewList = new ImageView(back);
        backViewList.setFitWidth(30);
        backViewList.setFitHeight(30);
        backButtonList.setGraphic(backViewList);

        confirmButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldText, newText) -> validateConfirmButton());

        // Load titles immediately (first load)
        loadTitlesAsync();
    }

    // === STOPWORDS ===
    private void setupStopwordList(StopwordManager stopwordManager) {
        this.stopword = stopwordManager;
        hideAlert();

        observableList = FXCollections.observableArrayList(stopword.getParole());
        stopwordsListView.setItems(observableList);

        stopwordsListView.setEditable(true);
        stopwordsListView.setCellFactory(TextFieldListCell.forListView());
        stopwordsListView.setOnEditCommit(event -> {
            int index = event.getIndex();
            String newValue = event.getNewValue().trim();

            String validationMsg = validateStopword(newValue);
            if (validationMsg != null) {
                showAlert(validationMsg);
                return;
            }

            hideAlert();
            stopwordsListView.getItems().set(index, newValue);
        });
    }

    private String validateStopword(String word) {
        if (word.isEmpty()) {
            return "Non si possono inserire parole vuote.";
        }
        if (word.contains(" ")) {
            return "Le parole non possono contenere spazi.";
        }
        return null;
    }

    private void showAlert(String msg) {
        alertLabel.setText(msg);
        alertLabel.setVisible(true);
        alertLabel.setManaged(true);
    }

    private void hideAlert() {
        alertLabel.setVisible(false);
        alertLabel.setManaged(false);
    }

    // === LOAD STOPWORDS ===
    public void loadStopwordsITA() { setupStopwordList(new StopwordITA()); }
    public void loadStopwordsENG() { setupStopwordList(new StopwordENG()); }

    @FXML
    private void handleDifficultySelection(ActionEvent event) {
        CheckBox selected = (CheckBox) event.getSource();
        if (selected.isSelected()) {
            checkEasy.setSelected(selected == checkEasy);
            checkNormal.setSelected(selected == checkNormal);
            checkHard.setSelected(selected == checkHard);

            difficolta = selected == checkEasy ? Difficolta.FACILE :
                    selected == checkNormal ? Difficolta.INTERMEDIO :
                            Difficolta.DIFFICILE;
        }
        validateConfirmButton();
    }

    @FXML
    private void handleLanguageSelection(ActionEvent event) {
        CheckBox selected = (CheckBox) event.getSource();
        if (selected.isSelected()) {
            checkIt.setSelected(selected == checkIt);
            checkEng.setSelected(selected == checkEng);

            clearStopwordList();
            if (selected == checkIt) {
                loadStopwordsITA();
                lingua = Lingua.ITA;
            } else {
                loadStopwordsENG();
                lingua = Lingua.ENG;
            }
        }
        validateConfirmButton();
    }

    // === IMPORT / ADD / REMOVE ===
    @FXML
    public void handleImport(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file TXT da importare");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo", "*.txt"));

        Window stage = importButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileImportato = selectedFile;
            importedFileLabel.setText("Selected: " + selectedFile.getName());
            importedFileLabel.setVisible(true);
            validateConfirmButton();
        }
    }

    @FXML
    private void handleAdd() {
        hideAlert();
        String text = inputField.getText().trim();

        String validationMsg = validateStopword(text);
        if (validationMsg != null) {
            showAlert(validationMsg);
            return;
        }

        if (!stopwordsListView.getItems().contains(text)) {
            stopwordsListView.getItems().add(text);
            inputField.clear();
        } else {
            showAlert("La parola è già presente.");
        }
    }

    @FXML
    public void handleRemove(ActionEvent actionEvent) {
        hideAlert();
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            stopwordsListView.getItems().remove(text);
            inputField.clear();
        }
    }

    public void clearStopwordList() { stopwordsListView.getItems().clear(); }

    // === CONFIRM ===
    public void handleConfirm(ActionEvent actionEvent) {
        titolo = titleField.getText().trim();

        stopword.clear();

        // Prima carica le predefinite
        stopword.caricaStopword(
                checkArticles.isSelected(),
                checkPrepositions.isSelected(),
                checkPronouns.isSelected(),
                checkToHave.isSelected(),
                checkToBe.isSelected(),
                checkCon.isSelected()
        );

        // Poi aggiunge quelle dell'utente
        for (String s : stopwordsListView.getItems()) {
            stopword.aggiungi(s);
        }

        Documento documento = new Documento(titolo, lingua, difficolta);
        AnalisiService analisiService = new AnalisiService(documento, stopword, fileImportato);

        analisiService.setOnSucceeded(event -> showListPane());
        analisiService.setOnFailed(event -> {
            Throwable e = analisiService.getException();
            LOGGER.log(Level.SEVERE, "Errore Analisi", e);
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore Analisi", null, "Si è verificato un errore durante l'analisi o l'inserimento nel database.\n" + e.getMessage());
        });

        analisiService.start();
    }


    private void validateConfirmButton() {
        boolean isTitleFilled = !titleField.getText().trim().isEmpty();
        boolean isFileImported = fileImportato != null;
        boolean isLanguageSelected = checkIt.isSelected() || checkEng.isSelected();
        boolean isDifficultySelected = checkEasy.isSelected() || checkNormal.isSelected() || checkHard.isSelected();

        confirmButton.setDisable(!(isTitleFilled && isFileImported && isLanguageSelected && isDifficultySelected));
    }

    // === LIST TEXTS LOGIC ===
    private void loadTitlesAsync() {
        LoadTitlesService service = new LoadTitlesService();

        service.setOnSucceeded(event -> {
            List<String> ListDao = service.getValue();
            titoliList = FXCollections.observableArrayList(ListDao);
            ListView.setItems(titoliList);
        });

        service.setOnFailed(event -> {
            Throwable e = service.getException();
            e.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore", null,
                    "Errore durante il caricamento dei titoli:\n" + (e != null ? e.getMessage() : "Errore sconosciuto"));
        });

        service.start();
    }


    @FXML
    public void deleteDocument(ActionEvent actionEvent) {
        String selectedTitle = ListView.getSelectionModel().getSelectedItem();

        if (selectedTitle == null) {
            AlertUtils.mostraAlert(Alert.AlertType.WARNING, "Attenzione", null, "Seleziona un documento da eliminare.");
            return;
        }

        DeleteDocumentService deleteService = new DeleteDocumentService(dao);
        deleteService.setSelectedTitle(selectedTitle);

        deleteService.setOnSucceeded(event -> {
            titoliList.remove(selectedTitle);
            AlertUtils.mostraAlert(Alert.AlertType.INFORMATION, "Successo", null, "Documento eliminato correttamente.");
        });

        deleteService.setOnFailed(event -> {
            Throwable e = deleteService.getException();
            e.printStackTrace();
            AlertUtils.mostraAlert(Alert.AlertType.ERROR, "Errore eliminazione", null,
                    "Errore durante l'eliminazione del documento:\n" + (e != null ? e.getMessage() : "Errore sconosciuto"));
        });

        deleteService.start();
    }

    // === NAVIGATION ===
    @FXML
    private void showListPane() {
        adminPane.setVisible(false);
        listPane.setVisible(true);
        loadTitlesAsync(); // Ricarica ogni volta che si apre la lista
    }

    @FXML
    private void showAdminPane() {
        listPane.setVisible(false);
        adminPane.setVisible(true);
    }

    @FXML
    public void goToMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error loading HomeMenuView", ex);
        }
    }

    private Utente getUtente() {
        return SessionManager.getInstance().getUtenteLoggato();
    }
}
