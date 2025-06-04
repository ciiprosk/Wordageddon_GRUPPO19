package it.unisa.diem.model.gestione.analisi;

public class Documento {
//la classe deve prendere un file in input e trasformarlo in file binario
    private final String lingua;    //tipo enum???????
    private final String nome;
    private final String estensione;
    private final String percorsoFile;
    private final String difficolta;    //tipo enum????????

    public Documento(String lingua, String nome, String estensione, String percorsoFile, String difficolta) {
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

    public String getDifficolta() {
        return difficolta;
    }



}
