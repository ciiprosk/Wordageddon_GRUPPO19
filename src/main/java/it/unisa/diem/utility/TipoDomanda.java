package it.unisa.diem.utility;

/**
 * Enumerazione che rappresenta le diverse tipologie di domande
 * generabili durante una sessione di gioco o quiz.
 */
public enum TipoDomanda {

    /**
     * Domanda basata sulla frequenza di una parola in un documento.
     */
    FREQUENZA,

    /**
     * Domanda che richiede quale tra le opzioni è la parola più frequente in tutti i testi
     */
    CONFRONTO,

    /**
     * Domanda che richiede di associare una parola al documento corretto.
     */
    ASSOCIAZIONE,

    /**
     * Domanda incentrata sull'assenza di una parola nei testi letti.
     */
    ASSENZA
}
