package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DomandaDAOPostgres;
import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.dao.postgres.SessioneDocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.service.InsertQuestionsService;
import it.unisa.diem.main.service.InsertSessionService;
import it.unisa.diem.main.service.LoadAnalysesService;
import it.unisa.diem.main.service.GenerateQuestionsService;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.GameSession;
import it.unisa.diem.model.gestione.sessione.Sessione;
import it.unisa.diem.model.gestione.sessione.SessioneDocumento;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.PropertiesLoader;
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

import java.time.LocalDateTime;
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

    private String url;
    private String user;
    private String pass;

    private SessioneDAOPostgres sessioneDAO;
    private SessioneDocumentoDAOPostgres sessioneDocumentoDAO;
    private DomandaDAOPostgres domandaDAO;


    // === METODO INITIALIZE ===
    @FXML
    public void initialize() {
        this.url = PropertiesLoader.getProperty("database.url");
        this.user = PropertiesLoader.getProperty("database.user");
        this.pass = PropertiesLoader.getProperty("database.password");

        sessioneDAO = new SessioneDAOPostgres(url, user, pass);
        sessioneDocumentoDAO = new SessioneDocumentoDAOPostgres(url, user, pass);
        domandaDAO = new DomandaDAOPostgres(url, user, pass);

        setupSelectionPane();
    }


    // === SETUP SELECTION PANE ===
    private void setupSelectionPane() {
        linguaComboBox.getItems().addAll(Lingua.values());
        difficoltaComboBox.getItems().addAll(Difficolta.values());

        startGameButton.setOnAction(e -> startNewGame());
    }

    private void startNewGame() {
        currentReadingIndex = 0;

        Lingua lingua = linguaComboBox.getValue();
        Difficolta difficolta = difficoltaComboBox.getValue();
        Utente utente = SessionManager.getInstance().getUtenteLoggato();

        if (lingua != null && difficolta != null && utente != null) {
            gameSession = new GameSession(utente, lingua, difficolta);
            Sessione sessione = new Sessione(utente, LocalDateTime.now());

            InsertSessionService insertSessionService = new InsertSessionService(sessioneDAO, sessione);

            insertSessionService.setOnSucceeded(event -> {
                Sessione insertedSessione = insertSessionService.getValue();
                System.out.println("✅ Sessione inserita con ID = " + insertedSessione.getId());
                gameSession.setSessioneId(insertedSessione.getId());
                loadDocumentsAndAnalyses(lingua, difficolta);
            });

            insertSessionService.setOnFailed(event -> {
                Throwable ex = insertSessionService.getException();
                ex.printStackTrace();
                showAlert("Errore nella creazione della sessione: " + ex.getMessage());
            });

            insertSessionService.start();

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
            gameSession.setAnalyses(analyses);

            for (Analisi analisi : analyses) {
                try {
                    sessioneDocumentoDAO.insert(new SessioneDocumento(gameSession.getSessioneId(), analisi.getTitolo()));
                } catch (DBException e) {
                    showAlert("Errore nel salvataggio dei documenti: " + e.getMessage());
                    return;
                }
            }

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

            // 🔷 Imposta numero domanda e sessione PRIMA di inserire nel DB
            int numero = 1;
            try {
                Sessione sessioneCorrente = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow();
                for (Domanda d : domande) {
                    d.setNumeroDomanda(numero++);
                    d.setSessione(sessioneCorrente);
                }
            } catch (DBException e) {
                showAlert("Errore nel recupero della sessione: " + e.getMessage());
                return;
            }

            // 🔷 Usa InsertQuestionsService
            InsertQuestionsService insertQuestionsService = new InsertQuestionsService(domandaDAO, domande);

            insertQuestionsService.setOnSucceeded(ev -> {
                System.out.println("✅ Domande inserite correttamente nel DB per sessione ID = " + gameSession.getSessioneId());
                gameSession.setCurrentQuestionIndex(0);
                gameSession.setScore(0);
                showReadingPane();
            });

            insertQuestionsService.setOnFailed(ev -> {
                Throwable ex = insertQuestionsService.getException();
                ex.printStackTrace();
                showAlert("Errore nel salvataggio delle domande: " + ex.getMessage());
            });

            insertQuestionsService.start();
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
        try {
            Sessione sessione = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow();
            sessione.setCompletato(true);
            sessione.setPunteggio(gameSession.getScore());
            sessioneDAO.update(sessione);
        } catch (DBException e) {
            showAlert("Errore nel salvataggio del punteggio: " + e.getMessage());
        }
        System.out.println("✅ Sessione completata e punteggio salvato: " + gameSession.getScore());

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
