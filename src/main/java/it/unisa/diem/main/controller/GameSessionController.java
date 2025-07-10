package it.unisa.diem.main.controller;

import it.unisa.diem.main.service.LoadAnalysesService;
import it.unisa.diem.main.service.GenerateQuestionsService;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.SessionManager;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameSessionController {

    @FXML private StackPane rootStackPane;

    // === SUB-PANES ===
    @FXML private AnchorPane selectionPane;
    @FXML private AnchorPane readingPane;
    @FXML private AnchorPane questionPane;
    @FXML private AnchorPane resultPane;

    // === SelectionPane controls ===
    @FXML private ComboBox<Lingua> linguaComboBox;
    @FXML private ComboBox<Difficolta> difficoltaComboBox;
    @FXML private Button startGameButton, backButton;

    // === ReadingPane controls ===
    @FXML private Label textTitleLabel;
    @FXML private TextArea textBodyArea;
    @FXML private Label timerLabel;
    @FXML private Button continueButton;

    // === QuestionPane controls ===
    @FXML private Label questionLabel;
    @FXML private Button answerButton1;
    @FXML private Button answerButton2;
    @FXML private Button answerButton3;
    @FXML private Button answerButton4;

    // === ResultPane controls ===
    @FXML private Label scoreLabel;
    @FXML private Button backToMenuButton;

    // === ATTRIBUTI ===
    private GameSession gameSession;
    private Timeline readingTimer;
    private int readingTimeSeconds;
    private int currentDocumentIndex;
    private int currentReadingIndex = 0;


    // === SERVICES ===
    private LoadAnalysesService loadAnalysesService;
    private GenerateQuestionsService generateQuestionsService;

    // === METODO INITIALIZE ===
    @FXML
    public void initialize() {
        setupSelectionPane();
    }

    // === SETUP SELECTION PANE ===
    private void setupSelectionPane() {
        linguaComboBox.getItems().addAll(Lingua.values());
        difficoltaComboBox.getItems().addAll(Difficolta.values());

        startGameButton.setOnAction(e -> startNewGame());
    }

    private void startNewGame() {
        currentDocumentIndex = 0;
        currentReadingIndex = 0;


        Lingua lingua = linguaComboBox.getValue();
        Difficolta difficolta = difficoltaComboBox.getValue();
        Utente utente = SessionManager.getInstance().getUtenteLoggato(); // Usa SessionManager per l'utente loggato

        if (lingua != null && difficolta != null && utente != null) {
            gameSession = new GameSession(utente, lingua, difficolta);
            loadDocumentsAndAnalyses(lingua, difficolta);
        } else {
            showAlert("Seleziona sia lingua che difficoltà (e assicurati di essere loggato).");
        }
    }

    // === LOAD DOCUMENTS AND ANALYSES ===
    private void loadDocumentsAndAnalyses(Lingua lingua, Difficolta difficolta) {
        loadAnalysesService = new LoadAnalysesService();
        loadAnalysesService.setLingua(lingua);
        loadAnalysesService.setDifficolta(difficolta);

        loadAnalysesService.setOnSucceeded((WorkerStateEvent event) -> {
            List<Analisi> analyses = loadAnalysesService.getValue();
            gameSession.setAnalyses(analyses); // Salva nella sessione

            generateQuestions(analyses, difficolta);
        });

        loadAnalysesService.setOnFailed(event -> {
            Throwable ex = loadAnalysesService.getException();
            ex.printStackTrace();
            showAlert("Errore durante il caricamento dei testi: " + ex.getMessage());
        });

        loadAnalysesService.start();
    }

    // === GENERATE QUESTIONS ===
    private void generateQuestions(List<Analisi> analyses, Difficolta difficolta) {
        generateQuestionsService = new GenerateQuestionsService();
        generateQuestionsService.setAnalyses(analyses);
        generateQuestionsService.setDifficolta(difficolta);

        generateQuestionsService.setOnSucceeded(event -> {
            List<Domanda> domande = generateQuestionsService.getValue();
            gameSession.setDomande(domande);
            gameSession.setCurrentQuestionIndex(0);
            gameSession.setScore(0);

            showReadingPane();
        });

        generateQuestionsService.setOnFailed(event -> {
            Throwable ex = generateQuestionsService.getException();
            ex.printStackTrace();
            showAlert("Errore durante la generazione delle domande: " + ex.getMessage());
        });

        generateQuestionsService.start();
    }

    // === SHOW READING PANE ===
    private void showReadingPane() {
        selectionPane.setVisible(false);
        readingPane.setVisible(true);

        // Ottieni l'analisi corrispondente al documento corrente
        Analisi currentAnalysis = gameSession.getAnalyses().get(currentReadingIndex);
        textTitleLabel.setText(currentAnalysis.getTitolo());
        textBodyArea.setText(String.join("\n", currentAnalysis.getDocumento().getTesto()));

        setupReadingTimer();

        continueButton.setOnAction(e -> {
            stopReadingTimer();

            // Se NON siamo all'ultimo documento, passa al prossimo documento da leggere
            if (currentReadingIndex < gameSession.getAnalyses().size() - 1) {
                currentReadingIndex++;
                showReadingPane(); // carica la lettura del documento successivo
            } else {
                // Altrimenti passa alle domande
                showQuestionPane();
            }
        });
    }


    private void handleReadingComplete() {
        currentDocumentIndex++;
        if (currentDocumentIndex < gameSession.getAnalyses().size()) {
            showReadingPane(); // Carica il prossimo documento
        } else {
            showQuestionPane(); // Passa alle domande
        }
    }




    private void showQuestionPane() {
        readingPane.setVisible(false);
        questionPane.setVisible(true);
        loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (gameSession.hasNextQuestion()) {
            Domanda domanda = gameSession.getCurrentQuestion();
            questionLabel.setText(domanda.getTestoDomanda());
            List<String> opzioni = domanda.getOpzioni();
            answerButton1.setText(opzioni.get(0));
            answerButton2.setText(opzioni.get(1));
            answerButton3.setText(opzioni.get(2));
            answerButton4.setText(opzioni.get(3));

            setAnswerHandlers(domanda);
        } else {
            showResultPane();
        }
    }

    private void setAnswerHandlers(Domanda domanda) {
        answerButton1.setOnAction(e -> handleAnswer(answerButton1.getText(), domanda));
        answerButton2.setOnAction(e -> handleAnswer(answerButton2.getText(), domanda));
        answerButton3.setOnAction(e -> handleAnswer(answerButton3.getText(), domanda));
        answerButton4.setOnAction(e -> handleAnswer(answerButton4.getText(), domanda));
    }

    private void handleAnswer(String rispostaUtente, Domanda domanda) {
        if (domanda.verificaRisposta(rispostaUtente)) {
            gameSession.incrementScore();
        }
        gameSession.incrementQuestionIndex();
        loadNextQuestion();
    }

    private void showResultPane() {
        questionPane.setVisible(false);
        resultPane.setVisible(true);
        scoreLabel.setText("Punteggio: " + gameSession.getScore() + "/" + gameSession.getDomande().size());
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void setupReadingTimer() {
        Difficolta diff = gameSession.getDifficolta();
        readingTimeSeconds = switch (diff) {
            case FACILE -> 90; // 1 minuto e mezzo
            case INTERMEDIO -> 60; // 1 minuto
            case DIFFICILE -> 90; // 1 minuto e mezzo
        };

        updateTimerLabel();

        readingTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            readingTimeSeconds--;
            updateTimerLabel();

            if (readingTimeSeconds <= 0) {
                readingTimer.stop();
                handleReadingComplete();
            }
        }));

        readingTimer.setCycleCount(Timeline.INDEFINITE);
        readingTimer.play();
    }


    private void updateTimerLabel() {
        int minutes = readingTimeSeconds / 60;
        int seconds = readingTimeSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void stopReadingTimer() {
        if (readingTimer != null) {
            readingTimer.stop();
        }
    }


    @FXML
    private void handleBackToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();

            // Recupera lo stage dal bottone che ha generato l'evento
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
