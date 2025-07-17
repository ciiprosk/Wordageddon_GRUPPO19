package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ValidazioneDocumentoService extends Service<Boolean> {

    private File file;
    private Difficolta difficolta;
    private StopwordManager stopword;

    public void setup(File file, Difficolta difficolta, StopwordManager stopword) {
        this.file = file;
        this.difficolta = difficolta;
        this.stopword = stopword;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                if (file == null || difficolta == null || stopword == null) {
                    throw new IllegalArgumentException("Parametri di validazione mancanti.");
                }

                // === Lettura contenuto ===
                String contenuto = Files.readString(file.toPath());

                // === Conteggio parole totali ===
                String[] paroleRaw = contenuto.trim().split("\\s+");
                int numParole = paroleRaw.length;

                // === Limiti minimi e massimi in base alla difficoltà ===
                int minParole, maxParole;
                switch (difficolta) {
                    case FACILE -> {
                        minParole = 180;
                        maxParole = 200;
                    }
                    case INTERMEDIO -> {
                        minParole = 200;
                        maxParole = 225;
                    }
                    case DIFFICILE -> {
                        minParole = 225;
                        maxParole = 250;
                    }
                    default -> throw new IllegalStateException("Difficoltà non riconosciuta.");
                }

                if (numParole < minParole) {
                    throw new IllegalStateException("Il documento ha solo " + numParole + " parole.\nMinimo richiesto: " + minParole);
                }

                if (numParole > maxParole) {
                    throw new IllegalStateException("Il documento ha " + numParole + " parole.\nMassimo consentito: " + maxParole);
                }

                // === Rimozione stopword e raccolta parole significative ===
                Set<String> stopwords = new HashSet<>(stopword.getParole());

                List<String> paroleSignificative = new ArrayList<>();
                for (String parola : paroleRaw) {
                    String pulita = parola.replaceAll("[^a-zA-ZàèìòùÀÈÌÒÙ]", "").toLowerCase();
                    if (!pulita.isBlank() && !stopwords.contains(pulita)) {
                        paroleSignificative.add(pulita);
                    }
                }

                Set<String> paroleDistinte = new HashSet<>(paroleSignificative);

                // === Vincoli interni per generazione domande ===
                if (paroleSignificative.size() < 50) {
                    throw new IllegalStateException("Il documento contiene solo " + paroleSignificative.size() +
                            " parole significative (non-stopword). Ne servono almeno 50.");
                }

                if (paroleDistinte.size() < 20) {
                    throw new IllegalStateException("Il documento ha solo " + paroleDistinte.size() +
                            " parole significative distinte. Ne servono almeno 20.");
                }

                // === Verifica per domanda di assenza ===
                List<String> dizionario = List.of(
                        "gatto", "cane", "sole", "luna", "tavolo", "computer", "libro", "penna",
                        "scuola", "auto", "telefono", "acqua", "vento", "cuore", "pane"
                );

                boolean copreTuttoIlDizionario = dizionario.stream().allMatch(paroleDistinte::contains);
                if (copreTuttoIlDizionario) {
                    throw new IllegalStateException("Il documento contiene tutte le parole comuni del dizionario.\nNon è possibile generare la domanda di assenza.");
                }

                return true;
            }
        };
    }
}
