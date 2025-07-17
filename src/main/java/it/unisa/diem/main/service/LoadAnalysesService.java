package it.unisa.diem.main.service;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.Documento;
import it.unisa.diem.model.gestione.analisi.Lingua;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadAnalysesService extends Service<List<Analisi>> {

    private Lingua lingua;
    private Difficolta difficolta;

    public void setLingua(Lingua lingua) { this.lingua = lingua; }
    public void setDifficolta(Difficolta difficolta) { this.difficolta = difficolta; }

    @Override
    protected Task<List<Analisi>> createTask() {
        return new Task<>() {
            @Override
            protected List<Analisi> call() throws Exception {
                System.out.println("CIAO SONO QUI");
                List<Analisi> analyses = new ArrayList<>();
                String folderPath = "analysis/" + lingua + "/" + difficolta.toString().toLowerCase();
                File folder = new File(folderPath);
                File[] files = folder.listFiles((dir, name) -> name.endsWith("_analysis.bin"));

                if (files == null || files.length == 0) {
                    throw new IOException("No analyses found for selected language and difficulty.");
                }

                Random random = new Random();

                int numToSelect = switch (difficolta) {
                    case FACILE -> 1;
                    case INTERMEDIO -> 2;
                    case DIFFICILE -> 3;
                };

                System.out.println("DEBUG - Files trovati nella cartella " + folderPath + ":");
                for (File f : files) {
                    System.out.println("DEBUG - " + f.getAbsolutePath());
                }
                List<File> fileList = new ArrayList<>(List.of(files));
                for (int i = 0; i < numToSelect && !fileList.isEmpty(); i++) {
                    File selectedFile = fileList.remove(random.nextInt(fileList.size()));
                    System.out.println("DEBUG - Selected file: " + selectedFile.getPath());
                    Documento doc = Documento.leggiDocumento(convertAnalysisToDataPath(selectedFile.getPath()));
                    System.out.println(doc);
                    Analisi analisi = Analisi.leggiAnalisi(doc);
                    analyses.add(analisi);
                }

                return analyses;
            }
        };
    }

    /**
     * Converte un path di analisi in path del relativo file .bin di testo.
     * Esempio: analysis/ITA/facile/prezzemolo_analysis.bin -> data/ITA/facile/prezzemolo.bin
     */
    private String convertAnalysisToDataPath(String analysisPath) {
        System.out.println("DEBUG - convertAnalysisToDataPath input: " + analysisPath);
        File file = new File(analysisPath);
        String fileName = file.getName();
        System.out.println(fileName);
        String titolo = fileName.replace("_analysis.bin", "");

        // Recupero cartelle padre
        File difficoltaDir = file.getParentFile();
        File linguaDir = difficoltaDir.getParentFile();

        String difficolta = difficoltaDir.getName();
        String lingua = linguaDir.getName();

        String dataPath = "data/" + lingua + "/" + difficolta + "/" + titolo + ".bin";
        System.out.println("DEBUG - convertAnalysisToDataPath output: " + dataPath);
        return dataPath;
    }

}
