package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.AnalisiRosa;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.utility.TipoDomanda;

import java.util.*;

public class DomandaFactoryVincenzo {
    List<Domanda> listaDomande;
    List<AnalisiRosa> listaAnalisi;

    public DomandaFactoryVincenzo(List <AnalisiRosa> listaAnalisi) {
        this.listaAnalisi = listaAnalisi;
        listaDomande = new ArrayList<>();
    }

    List<Domanda> generaDomande(Difficolta difficolta) {
        if (difficolta == Difficolta.FACILE) {
            listaDomande.add(generaDomandaAssociazione());
            listaDomande.add(generaDomandaFrequenza());
            listaDomande.add(generaDomandaAssenza());
            return listaDomande;
        }
        listaDomande.add(generaDomandaAssociazione());
        listaDomande.add(generaDomandaFrequenza());
        listaDomande.add(generaDomandaAssenza());
        listaDomande.add(generaDomandaConfronto());
        return listaDomande;
    }



    private Domanda generaDomandaFrequenza() {
        Random random = new Random();

        // Selezione casuale di un'analisi
        AnalisiRosa analisiScelta = listaAnalisi.get(random.nextInt(listaAnalisi.size()));

        Map<String, Integer> frequenze = analisiScelta.getFrequenzeTesti();

        // Scelgo una parola a caso
        List<String> parole = new ArrayList<>(frequenze.keySet());
        String parolaScelta = parole.get(random.nextInt(parole.size()));
        int frequenzaCorretta = frequenze.get(parolaScelta);

        // Ho scelto set per evitare duplicati
        Set<Integer> opzioniNumeriche = new HashSet<>();
        opzioniNumeriche.add(frequenzaCorretta);

        // Genera 3 alternative sbagliate plausibili
        while (opzioniNumeriche.size() < 4) {
            //uso questa variabile per creare risposte sbagliate ma vicine e plausibili alla risposta corretta
            int variazione = random.nextInt(5) + 1; // variazione da 1 a 5
            int alternativa = frequenzaCorretta + (random.nextBoolean() ? variazione : -variazione);
            if (alternativa >= 0) {
                opzioniNumeriche.add(alternativa);
            }
        }

        // Converto le opzioni in String
        List<String> opzioni = new ArrayList<>();
        for (Integer val : opzioniNumeriche) {
            opzioni.add(String.valueOf(val));
        }

        // Mischia le opzioni
        Collections.shuffle(opzioni);

        // Usa il titolo del documento per costruire la domanda
        String titoloDocumento = analisiScelta.getDocumento().getTitolo(); // oppure toString()
        String testoDomanda = String.format(
                "Quante volte compare la parola \"%s\" nel documento \"%s\"?",
                parolaScelta, titoloDocumento
        );

        // Crea la domanda passando la risposta corretta come String
        return new Domanda(testoDomanda, TipoDomanda.FREQUENZA, opzioni, String.valueOf(frequenzaCorretta));
    }


    private Domanda generaDomandaConfronto() { return null;}
    private Domanda generaDomandaAssociazione() { return null;}
    private Domanda generaDomandaAssenza() { return null;}
}
