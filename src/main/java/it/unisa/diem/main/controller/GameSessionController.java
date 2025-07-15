package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.DomandaDAOPostgres;
import it.unisa.diem.dao.postgres.SessioneDAOPostgres;
import it.unisa.diem.dao.postgres.SessioneDocumentoDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.main.Main;
import it.unisa.diem.main.service.*;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDateTime;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
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
    @FXML private Label questionTimerLabel;


    // === ResultPane controls ===
    @FXML private Label scoreLabel;
    @FXML private Button backToMenuButton;
    @FXML private VBox reviewBox;
    @FXML private AnchorPane loadingOverlay;
    @FXML private ProgressIndicator loadingSpinner;
    @FXML private Label loadingMessageLabel;

    // === Altri pulsanti ===
    @FXML private Button backButtonReading;
    @FXML private Button backButtonQuestion;

    @FXML private AnchorPane loadingPane;
    @FXML private Label loadingInstructionLabel;
    @FXML private Label difficultyMessageLabel;
    @FXML private ProgressBar loadingProgressBar;

    @FXML private Button instructionsToggleButton;
    @FXML private TextArea instructionsTextArea;



    // === ATTRIBUTI ===
    private GameSession gameSession;
    private Timeline readingTimer;
    private int readingTimeSeconds;
    private int currentDocumentIndex;
    private int currentReadingIndex = 0;
    private boolean isGameStarted = false;



    // === SERVICES ===
    private LoadAnalysesService loadAnalysesService;
    private GenerateQuestionsService generateQuestionsService;

    private String url;
    private String user;
    private String pass;

    private SessioneDAOPostgres sessioneDAO;
    private SessioneDocumentoDAOPostgres sessioneDocumentoDAO;
    private DomandaDAOPostgres domandaDAO;

    private Timeline questionTimer;
    private int questionTimeRemaining;
    private boolean sessioneCompletata = false;




    //singleton ottengo l’istanza attiva se la schermata di gioco è stata caricata.
    private static GameSessionController instance;

    public GameSessionController() {
        instance = this;
    }

    public static GameSessionController getInstance() {
        return instance;
    }


    // === METODO INITIALIZE ===
    @FXML
    public void initialize() {

        Image back = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/yellowbackarrow.png"));
        ImageView backView = new ImageView(back);
        backView.setFitWidth(30);
        backView.setFitHeight(30);
        backButton.setGraphic(backView);

        Image home = new Image(Main.class.getClassLoader().getResourceAsStream("immagini/home.png"));

        ImageView homeView1 = new ImageView(home);
        homeView1.setFitWidth(30);
        homeView1.setFitHeight(30);
        backButtonQuestion.setGraphic(homeView1);

        ImageView homeView2 = new ImageView(home);
        homeView2.setFitWidth(30);
        homeView2.setFitHeight(30);
        backButtonReading.setGraphic(homeView2);

        backButtonReading.setOnAction(this::handleBackToHomeWithConfirmation);
        backButtonQuestion.setOnAction(this::handleBackToHomeWithConfirmation);





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
        instructionsTextArea.setText(
                "Here are the instructions:\n" +
                        "1. Select language and difficulty.\n" +
                        "2. Read the texts carefully.\n" +
                        "3. Answer each question within the time limit.\n" +
                        "4. Review your results at the end."
        );
        linguaComboBox.getItems().addAll(Lingua.values());
        difficoltaComboBox.getItems().addAll(Difficolta.values());

        startGameButton.setOnAction(e -> startNewGame());
        instructionsToggleButton.setOnAction(e -> toggleInstructions());
    }

    private void toggleInstructions() {
        boolean currentlyVisible = instructionsTextArea.isVisible();
        instructionsTextArea.setVisible(!currentlyVisible);

        if (currentlyVisible) {
            instructionsToggleButton.setText("Show Instructions");
        } else {
            instructionsToggleButton.setText("Hide Instructions");
        }
    }


    private void startNewGame() {
        currentReadingIndex = 0;

        Lingua lingua = linguaComboBox.getValue();
        Difficolta difficolta = difficoltaComboBox.getValue();
        Utente utente = SessionManager.getInstance().getUtenteLoggato();

        if (lingua != null && difficolta != null && utente != null) {
            boolean singleText = difficolta == Difficolta.FACILE;
            showLoadingScreen(difficolta, singleText);


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
                hideLoadingOverlay(); // 🔷 Nascondi in caso di errore
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

            // 🔢 Contatore per tenere traccia dei completamenti
            final int totalAnalisi = analyses.size();
            final int[] completati = {0}; // usiamo array per poterlo modificare dentro lambda

            if (totalAnalisi == 0) {
                generateQuestions(analyses, difficolta);
                return;
            }

            for (Analisi analisi : analyses) {
                SessioneDocumento sd = new SessioneDocumento(gameSession.getSessioneId(), analisi.getTitolo());
                InsertSessioneDocumentoService insertService = new InsertSessioneDocumentoService(sessioneDocumentoDAO, sd, gameSession, analisi);

                insertService.setOnSucceeded(ev -> {
                    completati[0]++;
                    if (completati[0] == totalAnalisi) {
                        generateQuestions(analyses, difficolta); // ✅ quando tutte completate
                    }
                });

                insertService.setOnFailed(ev -> {
                    hideLoadingOverlay();
                    Throwable ex = insertService.getException();
                    showAlert("Errore nel salvataggio dei documenti: " + ex.getMessage());
                    // ❌ Interrompe la catena se c’è un errore
                });

                insertService.start();
            }
        });
        loadAnalysesService.setOnFailed(event -> {
            hideLoadingOverlay(); // 🔷 Nascondi in caso di errore
            Throwable ex = loadAnalysesService.getException();
            ex.printStackTrace();
            showAlert("Errore durante il caricamento dei testi: " + ex.getMessage());
        });

        loadAnalysesService.start();
    }

    private void showLoadingScreen(Difficolta difficolta, boolean singleText) {
        selectionPane.setVisible(false);
        readingPane.setVisible(false);
        questionPane.setVisible(false);
        resultPane.setVisible(false);
        loadingOverlay.setVisible(false);

        loadingPane.setVisible(true);

        String instruction = singleText ?
                "Read the proposed text carefully and answer the questions.\nIt will not be possible to reconsult the text!" :
                "Read the proposed texts carefully and answer the questions.\nIt will not be possible to reconsult the texts!";
        loadingInstructionLabel.setText(instruction);

        String diffMsg = switch (difficolta) {
            case FACILE -> "It will be easy.";
            case INTERMEDIO -> "It will be a balanced challenge.";
            case DIFFICILE -> "Good luck. It's not going to be easy.";
        };
        difficultyMessageLabel.setText(diffMsg);

        loadingProgressBar.setProgress(0);

        // Simulazione caricamento visuale (non logica!)
        Timeline progressTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {
                    double progress = loadingProgressBar.getProgress();
                    loadingProgressBar.setProgress(progress + 0.01);
                })
        );
        progressTimeline.setCycleCount(100);
        progressTimeline.setOnFinished(e -> {
            loadingPane.setVisible(false); // ✅ Non avvia la lettura qui
        });
        progressTimeline.play();
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
                hideLoadingOverlay(); // 🔷 Nascondi in caso di errore
                showAlert("Errore nel recupero della sessione: " + e.getMessage());
                return;
            }

            InsertQuestionsService insertQuestionsService = new InsertQuestionsService(domandaDAO, domande);

            insertQuestionsService.setOnSucceeded(ev -> {
                hideLoadingOverlay(); // 🔷 Nascondi quando finito
                System.out.println("✅ Domande inserite correttamente nel DB per sessione ID = " + gameSession.getSessioneId());
                gameSession.setCurrentQuestionIndex(0);
                gameSession.setScore(0);
                showReadingPane();
            });

            insertQuestionsService.setOnFailed(ev -> {
                hideLoadingOverlay(); // 🔷 Nascondi in caso di errore
                Throwable ex = insertQuestionsService.getException();
                ex.printStackTrace();
                showAlert("Errore nel salvataggio delle domande: " + ex.getMessage());
            });

            insertQuestionsService.start();
        });

        generateQuestionsService.setOnFailed(event -> {
            hideLoadingOverlay(); // 🔷 Nascondi in caso di errore
            Throwable ex = generateQuestionsService.getException();
            ex.printStackTrace();
            showAlert("Errore durante la generazione delle domande: " + ex.getMessage());
        });

        generateQuestionsService.start();
    }

    @FXML
    private void handleBackToHomeWithConfirmation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm exit");
        alert.setHeaderText("Quit the game?");
        alert.setContentText("If you quit now, your game will be deleted. Do you really want to go back to home?");

        ButtonType siButton = new ButtonType("Yes, quit");
        ButtonType noButton = new ButtonType("No, continue", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(siButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == siButton) {
                showLoadingOverlayWithMessage("Tornando alla home...");
                deleteGameSessionFromDB();

                Timeline delay = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
                        Parent root = loader.load();

                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Menu");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        hideLoadingOverlay();
                    }
                }));
                delay.play();

            }
        });
    }



    // === SHOW READING PANE ===
    private void showReadingPane() {
        isGameStarted = true;

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

    public void deleteGameSessionFromDB() {
        if (gameSession != null && !sessioneCompletata) {
            try {
                domandaDAO.deleteBySessioneId(gameSession.getSessioneId());
                sessioneDocumentoDAO.deleteBySessioneId(gameSession.getSessioneId());
                sessioneDAO.delete(gameSession.getSessioneId());
                System.out.println("✅ Sessione eliminata con successo");
            } catch (DBException e) {
                System.err.println("Errore durante la cancellazione della sessione: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ Sessione completata, nessuna eliminazione necessaria");
        }
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

            startQuestionTimer(); // 🔷 Avvia timer per questa domanda
        } else {
            showLoadingOverlayWithMessage("Sto analizzando le risposte...");

            // 🔷 Attendi 1 secondo prima di mostrare i risultati
            Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                hideLoadingOverlay();
                showResultPane();
            }));
            delayTimeline.setCycleCount(1);
            delayTimeline.play();
        }
    }

    private void startQuestionTimer() {
        questionTimeRemaining = 15; // tempo limite per domanda
        updateQuestionTimerLabel();

        if (questionTimer != null) {
            questionTimer.stop();
        }

        questionTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            questionTimeRemaining--;
            updateQuestionTimerLabel();

            if (questionTimeRemaining <= 0) {
                questionTimer.stop();
                handleAnswerTimeout(); // 🔷 Scadenza tempo = nessuna risposta
            }
        }));

        questionTimer.setCycleCount(Timeline.INDEFINITE);
        questionTimer.play();
    }

    private void handleAnswerTimeout() {
        Domanda domanda = gameSession.getCurrentQuestion();
        handleAnswer("NESSUNA RISPOSTA", domanda);
    }


    private void updateQuestionTimerLabel() {
        questionTimerLabel.setText(String.valueOf(questionTimeRemaining));
    }




    private void setAnswerHandlers(Domanda domanda) {
        answerButton1.setOnAction(e -> handleAnswer(answerButton1.getText(), domanda));
        answerButton2.setOnAction(e -> handleAnswer(answerButton2.getText(), domanda));
        answerButton3.setOnAction(e -> handleAnswer(answerButton3.getText(), domanda));
        answerButton4.setOnAction(e -> handleAnswer(answerButton4.getText(), domanda));
    }

    private void handleAnswer(String rispostaUtente, Domanda domanda) {
        if (questionTimer != null) {
            questionTimer.stop(); // 🔷 Stoppa timer alla risposta
        }

        domanda.setRispostaUtente(rispostaUtente);

        if (domanda.verificaRisposta(rispostaUtente)) {
            gameSession.incrementScore();
        }
        gameSession.incrementQuestionIndex();
        loadNextQuestion();
    }


    private void showResultPane() {
        hideLoadingOverlay(); // 🔷 Nascondi overlay prima di mostrare i risultati

        questionPane.setVisible(false);
        resultPane.setVisible(true);
        sessioneCompletata = true;
        scoreLabel.setText("Punteggio: " + gameSession.getScore() + "/" + gameSession.getDomande().size());

        reviewBox.getChildren().clear();
        for (Domanda d : gameSession.getDomande()) {
            Label questionText = new Label("Domanda: " + d.getTestoDomanda());
            String rispostaUtente = d.getRispostaUtente();
            String rispostaCorretta = d.getRispostaCorretta();

            Label rispostaLabel;
            if (d.verificaRisposta(rispostaUtente)) {
                rispostaLabel = new Label("Risposta data: " + rispostaUtente + " ✔️");
                reviewBox.getChildren().addAll(questionText, rispostaLabel);
            } else {
                rispostaLabel = new Label("Risposta data: " + rispostaUtente + " ❌");
                Label correttaLabel = new Label("Risposta corretta: " + rispostaCorretta);
                reviewBox.getChildren().addAll(questionText, rispostaLabel, correttaLabel);
            }
        }

        try {
            Sessione sessione = sessioneDAO.selectById(gameSession.getSessioneId()).orElseThrow();
            sessione.setCompletato(true);
            sessione.setPunteggio(gameSession.getScore());
            sessioneDAO.update(sessione);
            System.out.println("✅ Sessione completata e punteggio salvato: " + gameSession.getScore());
        } catch (DBException e) {
            showAlert("Errore nel salvataggio del punteggio: " + e.getMessage());
        }


    }



    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attenzione");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showLoadingOverlayWithMessage(String message) {
        loadingMessageLabel.setText(message);
        loadingOverlay.setVisible(true);
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
        deleteGameSessionFromDB(); // 🔷 Cancella sessione se partita iniziata

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/diem/main/HomeMenuView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void hideLoadingOverlay() {
        loadingOverlay.setVisible(false);
    }


}