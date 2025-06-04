package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.sessione.Domanda;
import it.unisa.diem.model.gestione.sessione.DomandaFactory;

import java.io.IOException;
import java.util.*;

//ciao

public class Main {
    public static void main(String[] args) {
        // Step 1: Definizione delle stopword a mano
        List<String> stopwords = Arrays.asList("il", "lo", "la", "i", "gli", "le", "e", "a", "di", "da", "in", "che", "un", "una");

        try {
            // Step 2: Carica i documenti dalla cartella dei testi facili
            List<Documento> documenti = Documento.caricaDocumenti("medio", 2);

            // Step 3: Calcola le frequenze parola per documento
            Map<String, Map<String, Integer>> frequenze = Analisi.calcolaFrequenze(documenti, stopwords);

            // Step 4: Genera le domande
            DomandaFactory factory = new DomandaFactory(stopwords);
            List<Domanda> domande = factory.generaDomande(frequenze);

            // Step 5: Stampa ogni domanda e opzioni
            for (int i = 0; i < domande.size(); i++) {
                Domanda d = domande.get(i);
                System.out.println("\nDomanda " + (i + 1) + ":");
                System.out.println(d.getTestoDomanda());

                List<String> opzioni = d.getOpzioni();
                for (int j = 0; j < opzioni.size(); j++) {
                    System.out.println((char) ('A' + j) + ") " + opzioni.get(j));
                }

                System.out.println("Risposta corretta: " + d.getRispostaCorretta());
            }

        } catch (IOException e) {
            System.err.println("Errore nella lettura dei file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore generico: " + e.getMessage());
        }
    }
}

