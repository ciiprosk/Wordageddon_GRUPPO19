package it.unisa.diem.main.controller;

import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.TipoDomanda;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class QuestionViewController {

    @FXML private Label questionAskedNumberLabel;
    @FXML private Label totQuestionsNumberLabel;
    @FXML private Label questionLabel;
    @FXML private Button answerButton1;
    @FXML private Button answerButton2;
    @FXML private Button answerButton3;
    @FXML private Button answerButton4;

    private int currentQuestionIndex = 0;
    private int score = 0;

    private final List<Domanda> domande = new ArrayList<>();
    private Utente utente;

    public void initialize() {
        List<String> opzioni1 = new ArrayList<>();
        opzioni1.add("Roma");
        opzioni1.add("Milano");
        opzioni1.add("Napoli");
        opzioni1.add("Torino");
        domande.add(new Domanda("Qual è la capitale d'Italia?", TipoDomanda.ASSOCIAZIONE, opzioni1, "Roma"));

        List<String> opzioni2 = new ArrayList<>();
        opzioni2.add("4");
        opzioni2.add("3");
        opzioni2.add("5");
        opzioni2.add("6");
        domande.add(new Domanda("Quanto fa 2 + 2?", TipoDomanda.ASSOCIAZIONE, opzioni2, "4"));

        List<String> opzioni3 = new ArrayList<>();
        opzioni3.add("Ferro");
        opzioni3.add("Rame");
        opzioni3.add("Oro");
        opzioni3.add("Argento");
        domande.add(new Domanda("Quale metallo ha simbolo chimico Fe?", TipoDomanda.ASSOCIAZIONE, opzioni3, "Ferro"));

        List<String> opzioni4 = new ArrayList<>();
        opzioni4.add("Marte");
        opzioni4.add("Venere");
        opzioni4.add("Giove");
        opzioni4.add("Saturno");
        domande.add(new Domanda("Quale pianeta è noto come 'pianeta rosso'?", TipoDomanda.ASSOCIAZIONE, opzioni4, "Marte"));

        totQuestionsNumberLabel.setText(String.valueOf(domande.size()));
        showQuestion();
    }

    private void showQuestion() {
        if (currentQuestionIndex >= domande.size()) {
            System.out.println("Punteggio finale: " + score + "/" + domande.size());
            disableButtons();
            return;
        }

        Domanda d = domande.get(currentQuestionIndex);
        questionAskedNumberLabel.setText(String.valueOf(currentQuestionIndex + 1));
        questionLabel.setText(d.getTestoDomanda());

        List<String> opzioni = d.getOpzioni();
        answerButton1.setText(opzioni.get(0));
        answerButton2.setText(opzioni.get(1));
        answerButton3.setText(opzioni.get(2));
        answerButton4.setText(opzioni.get(3));

        setAnswerHandlers(d);
    }

    private void setAnswerHandlers(Domanda domanda) {
        answerButton1.setOnAction(e -> handleAnswer(answerButton1.getText(), domanda));
        answerButton2.setOnAction(e -> handleAnswer(answerButton2.getText(), domanda));
        answerButton3.setOnAction(e -> handleAnswer(answerButton3.getText(), domanda));
        answerButton4.setOnAction(e -> handleAnswer(answerButton4.getText(), domanda));
    }

    private void handleAnswer(String rispostaUtente, Domanda domanda) {
        if (domanda.verificaRisposta(rispostaUtente)) {
            score++;
        }
        currentQuestionIndex++;
        showQuestion();
    }

    private void disableButtons() {
        answerButton1.setDisable(true);
        answerButton2.setDisable(true);
        answerButton3.setDisable(true);
        answerButton4.setDisable(true);
    }


    public void setUtente(Utente utenteToPass) {
        this.utente = utenteToPass;
    }
}
