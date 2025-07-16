package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

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

                String contenuto = Files.readString(file.toPath());
                int numParole = contenuto.trim().split("\\s+").length;
                int numCaratteri = contenuto.length();

                int maxParole = difficolta == Difficolta.DIFFICILE ? 250 : 200;
                int maxCaratteri = difficolta == Difficolta.DIFFICILE ? 1250 : 1000;

                if (numParole > maxParole) {
                    throw new IllegalStateException("Il documento contiene troppe parole: " + numParole +
                            ". Limite: " + maxParole);
                }

                if (numCaratteri > maxCaratteri) {
                    throw new IllegalStateException("Il documento contiene troppi caratteri: " + numCaratteri +
                            ". Limite: " + maxCaratteri);
                }

                // Applica stopword
                Set<String> stopwords = new HashSet<>(stopword.getParole());

                String[] paroleRaw = contenuto.toLowerCase().split("\\s+");

                List<String> paroleSignificative = new ArrayList<>();
                for (String parola : paroleRaw) {
                    String pulita = parola.replaceAll("[^a-zA-ZàèìòùÀÈÌÒÙ]", "");
                    if (!pulita.isBlank() && !stopwords.contains(pulita)) {
                        paroleSignificative.add(pulita);
                    }
                }

                Set<String> paroleDistinte = new HashSet<>(paroleSignificative);

                if (paroleSignificative.size() < 50) {
                    throw new IllegalStateException("Il documento contiene solo " + paroleSignificative.size() +
                            " parole significative. Ne servono almeno 50.");
                }

                if (paroleDistinte.size() < 20) {
                    throw new IllegalStateException("Il documento ha solo " + paroleDistinte.size() +
                            " parole diverse. Ne servono almeno 20.");
                }

                List<String> dizionario = List.of(
                        "gatto", "cane", "sole", "luna", "tavolo", "computer", "libro", "penna",
                        "scuola", "auto", "telefono", "acqua", "vento", "cuore", "pane"
                );

                boolean copreTuttoIlDizionario = dizionario.stream().allMatch(paroleDistinte::contains);
                if (copreTuttoIlDizionario) {
                    throw new IllegalStateException("Il documento contiene tutte le parole comuni. Impossibile generare la domanda di assenza.");
                }

                return true;
            }
        };
    }
}
