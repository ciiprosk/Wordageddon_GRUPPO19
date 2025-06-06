package it.unisa.diem.model.gestione.analisi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

//ciao


public class Documento {
    private String nome;
    private String contenuto;

    public Documento(String nome, String contenuto) {
        this.nome = nome;
        this.contenuto = contenuto;
    }

    public String getNome() {

        return nome;
    }

    public String getContenuto() {

        return contenuto;
    }

    public static List<Documento> caricaDocumenti(Difficolta livelloDifficolta, Lingua lingua, int quanti) throws IOException {
        String cartella;

        String difficolta = livelloDifficolta.toString().toLowerCase();

        String l = lingua.toString().toUpperCase();

        cartella = "data/" + l + "/" + difficolta;

        List<Documento> tutti = new ArrayList<>();
        Path dir = Paths.get(cartella);

        DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt");
        for (Path file : stream) {
            byte[] bytes = Files.readAllBytes(file);
            String contenuto = new String(bytes, StandardCharsets.UTF_8);
            tutti.add(new Documento(file.getFileName().toString(), contenuto));
        }

        if (tutti.size() < quanti) {
            throw new IllegalStateException("Non ci sono abbastanza documenti per il livello di difficoltà: " + livelloDifficolta);
        }

        // Mischia e seleziona i primi N
        Collections.shuffle(tutti);
        return tutti.subList(0, quanti);
    }


}

