package it.unisa.diem.main.controller;

import it.unisa.diem.dao.postgres.AnalisiDAOPostgres;
import it.unisa.diem.exceptions.DBException;
import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.DomandaFactory;
import it.unisa.diem.model.gestione.utenti.Utente;
import it.unisa.diem.utility.TipoDomanda;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Documento primoDocumento;
    private Documento secondoDocumento;
    private AnalisiDAOPostgres dao = new AnalisiDAOPostgres("jdbc:postgresql://database-1.czikiq82wrwk.eu-west-2.rds.amazonaws.com:5432/Wordageddon", "postgres", "Farinotta01_");

    public void caricaDomande() {
        Difficolta difficolta = primoDocumento.getDifficolta();
        List<Analisi> listaAnalisi= new ArrayList<>();

        try {
            Optional<Analisi> analisi1 = dao.selectAnalisiByTitle(primoDocumento.getTitolo());
            if (analisi1.isPresent()) {
                listaAnalisi.add(analisi1.get());
            } else {
                throw new RuntimeException("Analisi non trovata per titolo: " + primoDocumento.getTitolo());
            }
            System.out.println("ciao difficolta: " + difficolta);
            /*if (difficolta != Difficolta.FACILE) {
                Optional<Analisi> analisi2 = dao.selectAnalisiByTitle(secondoDocumento.getTitolo());
                listaAnalisi.add(analisi2.get());
            } else {
                throw new RuntimeException("Analisi non trovata per titolo: " + secondoDocumento.getTitolo());
            }*/

        } catch (DBException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nel recupero dati dal database", e);
        }

        try {
            listaAnalisi.add(dao.selectAnalisiByTitle(primoDocumento.getTitolo()).get());
        } catch (DBException e) {
            throw new RuntimeException(e);
        }
        /*try {
            listaAnalisi.add(dao.selectAnalisiByTitle(secondoDocumento.getTitolo()).get());
        } catch (DBException e) {
            throw new RuntimeException(e);
        }*/

        DomandaFactory domandaFactory = new DomandaFactory(listaAnalisi);
        System.out.println(domandaFactory.toString());

        List<Domanda> domande = domandaFactory.generaDomande(difficolta);

        List<String> opzioni0 = new ArrayList<>();

        opzioni0.addAll(domande.get(0).getOpzioni());

        domande.add(new Domanda(domande.get(0).getTestoDomanda(), domande.get(0).getTipo(), opzioni0, domande.get(0).getRispostaCorretta()));

        List<String> opzioni1 = new ArrayList<>();

        opzioni1.addAll(domande.get(1).getOpzioni());
        domande.add(new Domanda(domande.get(1).getTestoDomanda(), domande.get(1).getTipo(), opzioni1, domande.get(1).getRispostaCorretta()));
        if(difficolta != Difficolta.FACILE) {
            List<String> opzioni2 = new ArrayList<>();
            opzioni2.addAll(domande.get(2).getOpzioni());
            domande.add(new Domanda(domande.get(2).getTestoDomanda(), domande.get(2).getTipo(), opzioni2, domande.get(2).getRispostaCorretta()));
            List<String> opzioni3 = new ArrayList<>();
            opzioni3.addAll(domande.get(3).getOpzioni());
            domande.add(new Domanda(domande.get(3).getTestoDomanda(), domande.get(3).getTipo(), opzioni3, domande.get(3).getRispostaCorretta()));
        } else {
            List<String> opzioni2 = new ArrayList<>();
            opzioni2.addAll(domande.get(2).getOpzioni());
            domande.add(new Domanda(domande.get(2).getTestoDomanda(), domande.get(2).getTipo(), opzioni0, domande.get(2).getRispostaCorretta()));
        }


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

    public void setDocumento(Documento documento) {
        this.primoDocumento = documento;
        caricaDomande();
    }

    public void setDocumenti(Documento documentoOriginale, Documento documento) {
        primoDocumento = documentoOriginale;
        secondoDocumento = documento;
    }
}
