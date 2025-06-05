package it.unisa.diem.model.gestione.analisi;

import java.util.*;
import java.util.regex.*;


public class Analisi {

    private final String nome;
    private final String percorsoFile;

    public Analisi(String nome, String percorsoFile) {

        this.nome = nome;
        this.percorsoFile = percorsoFile;

    }

    public String getNome() {
        return nome;
    }

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public static Map<String, Map<String, Integer>> calcolaFrequenze(List<Documento> documenti, List<String> stopword) {
        Map<String, Map<String, Integer>> risultato = new HashMap<>();
        Pattern pattern = Pattern.compile("\\b[a-zA-ZàèéìòùÀÈÉÌÒÙ]{3,}\\b"); // solo parole di almeno 3 lettere

        for (Documento doc : documenti) {
            Map<String, Integer> frequenze = new HashMap<>();
            Matcher matcher = pattern.matcher(doc.getContenuto().toLowerCase());

            while (matcher.find()) {
                String parola = matcher.group();

                // Escludi se è una stopword
                if (stopword.contains(parola)) continue;

                // Conta la parola
                if (!frequenze.containsKey(parola)) {
                    frequenze.put(parola, 1);
                } else {
                    frequenze.put(parola, frequenze.get(parola) + 1);
                }
            }

            risultato.put(doc.getNome(), frequenze);
        }

        return risultato;
    }
}
