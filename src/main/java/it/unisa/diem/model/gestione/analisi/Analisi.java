package it.unisa.diem.model.gestione.analisi;

public class Analisi {

    private final String nome;
    private final String percorsoFile;
    private final Documento documento;

    public Analisi(String nome, String percorsoFile, Documento documento) {
        this.nome = nome;
        this.percorsoFile = percorsoFile;
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public Documento getDocumento() {
        return documento;
    }

}
