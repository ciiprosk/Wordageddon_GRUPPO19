package it.unisa.diem.model.gestione.analisi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

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

    public static List<Documento> caricaDocumenti(String livelloDifficolta, int quanti) throws IOException {
        String cartella;
        livelloDifficolta = livelloDifficolta.toLowerCase();

        if (livelloDifficolta.equals("facile")) {
            cartella = "testi/testiFacili";
        } else if (livelloDifficolta.equals("medio")) {
            cartella = "testi/testiMedi";
        } else if (livelloDifficolta.equals("difficile")) {
            cartella = "testi/testiDifficili";
        } else {
            throw new IllegalArgumentException("Livello difficoltà non valido");
        }

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

