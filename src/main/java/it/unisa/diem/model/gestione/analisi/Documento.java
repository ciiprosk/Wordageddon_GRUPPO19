package it.unisa.diem.model.gestione.analisi;

public class Documento {

    private final String lingua;    //tipo enum???????
    private final String nome;
    private final String estensione;
    private final String percorsoFile;
    private final Difficoltà difficolta;    //tipo enum????????

    public Documento(String lingua, String nome, String estensione, String percorsoFile, Difficoltà difficolta) {
        this.lingua = lingua;
        this.nome = nome;
        this.estensione = estensione;
        this.percorsoFile = percorsoFile;
        this.difficolta = difficolta;
    }

    public String getLingua() {
        return lingua;
    }

    public String getNome() {
        return nome;
    }

    public String getEstensione() {
        return estensione;
    }

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public Difficoltà getDifficolta() {
        return difficolta;
    }



}
