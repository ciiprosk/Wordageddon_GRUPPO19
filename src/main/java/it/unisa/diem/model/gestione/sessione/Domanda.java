package it.unisa.diem.model.gestione.sessione;

public class Domanda {

    private final Sessione sessione;
    private static int numero;
    private final int valore;   //nel diagramma è chiamato PUNTI
    private final String tipologia;     //tipo enum????????
    private String rispostaInserita;
    private final String testo;

    public Domanda(Sessione sessione, int valore, String tipologia, String testo) {
        this.sessione = sessione;
        this.valore = valore;
        this.tipologia = tipologia;
        this.testo = testo;
    }
}
