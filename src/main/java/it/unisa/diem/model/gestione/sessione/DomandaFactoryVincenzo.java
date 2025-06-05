package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.AnalisiRosa;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.utility.TipoDomanda;

import java.util.*;

public class DomandaFactoryVincenzo {
    List<Domanda> listaDomande;
    List<AnalisiRosa> listaAnalisi; //lista di documenti scelti da controller

    // Lista fittizia di parole "dizionario" da cui attingere
    List<String> dizionario = Arrays.asList(
            "gatto", "cane", "sole", "luna", "tavolo", "computer", "libro", "penna",
            "scuola", "auto", "telefono", "acqua", "vento", "cuore", "pane"
    );

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
            if (alternativa >= 1) {
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
                "How many times does the word \"%s\" appear in the document \"%s\"?",
                parolaScelta, titoloDocumento
        );

        // Crea la domanda passando la risposta corretta come String
        return new Domanda(testoDomanda, TipoDomanda.FREQUENZA, opzioni, String.valueOf(frequenzaCorretta));
    }


    private Domanda generaDomandaConfronto() {
        Random random = new Random();
        Map<String, Integer> frequenzeGlobali = new HashMap<>();

        // sommo le frequenze di tutte le analisi
        for (AnalisiRosa analisi : listaAnalisi) {
            Map<String, Integer> frequenze = analisi.getFrequenzeTesti();
            for (Map.Entry<String, Integer> entry : frequenze.entrySet()) {
                String parola = entry.getKey();
                int count = entry.getValue();
                if (parola != null && !parola.trim().isEmpty()) {
                    frequenzeGlobali.put(parola, frequenzeGlobali.getOrDefault(parola, 0) + count);
                }
            }
        }

        // Se ci sono meno di 4 parole, non possiamo creare la domanda
        if (frequenzeGlobali.size() < 4) {
            throw new IllegalStateException("Non ci sono abbastanza parole per creare la domanda di confronto.");
        }

        // Seleziono 4 parole diverse casuali dalla mappa
        List<String> tutteLeParole = new ArrayList<>(frequenzeGlobali.keySet());
        Collections.shuffle(tutteLeParole);
        List<String> paroleSelezionate = tutteLeParole.subList(0, 4);

        String rispostaCorretta = paroleSelezionate.get(0);
        int maxFrequenza = frequenzeGlobali.get(rispostaCorretta);

        for (String parola : paroleSelezionate) {
            int freq = frequenzeGlobali.get(parola);
            if (freq > maxFrequenza) {
                rispostaCorretta = parola;
                maxFrequenza = freq;
            }
        }

        List<String> opzioni = new ArrayList<>(paroleSelezionate);
        Collections.shuffle(opzioni);

        String testoDomanda = "Which of the following words appears most frequently across all documents?";

        return new Domanda(testoDomanda, TipoDomanda.CONFRONTO, opzioni, rispostaCorretta);
    }

    private Domanda generaDomandaAssociazione() {
        Random random = new Random();
        AnalisiRosa analisi = listaAnalisi.get(random.nextInt(listaAnalisi.size()));
        Map<String, Integer> frequenze = analisi.getFrequenzeTesti();

        if (frequenze.size() < 4) {
            throw new IllegalStateException("Il documento non contiene abbastanza parole per creare la domanda.");
        }

        // Trovo la parola con frequenza massima
        String parolaMax = null;
        int maxFreq = -1;
        for (Map.Entry<String, Integer> entry : frequenze.entrySet()) {
            if (entry.getValue() > maxFreq) {
                parolaMax = entry.getKey();
                maxFreq = entry.getValue();
            }
        }

        // distrattori: parole diverse da quella corretta
        List<String> distrattori = new ArrayList<>(frequenze.keySet());
        distrattori.remove(parolaMax);
        Collections.shuffle(distrattori);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaMax);
        for (int i = 0; i < 3 && i < distrattori.size(); i++) {
            opzioni.add(distrattori.get(i));
        }

        // Se non abbiamo abbastanza opzioni, la domanda non è valida
        if (opzioni.size() < 4) {
            throw new IllegalStateException("Non ci sono abbastanza parole diverse per generare la domanda.");
        }

        Collections.shuffle(opzioni);

        String titoloDoc = analisi.getDocumento().getTitolo(); // o toString()
        String testoDomanda = String.format(
                "Which word appears most frequently in the document \"%s\"?",
                titoloDoc
        );

        // Crea la domanda
        return new Domanda(testoDomanda, TipoDomanda.ASSOCIAZIONE, opzioni, parolaMax);
    }

    private Domanda generaDomandaAssenza() {
        Random random = new Random();
        Set<String> parolePresenti = new HashSet<>();
        for (AnalisiRosa analisi : listaAnalisi) {
            parolePresenti.addAll(analisi.getFrequenzeTesti().keySet());
        }

        // Trova parole che non compaiono nei documenti (presenti nel dizionario ma non nei testi)
        List<String> paroleAssenti = new ArrayList<>();
        for (String parola : dizionario) {
            if (!parolePresenti.contains(parola)) {
                paroleAssenti.add(parola);
            }
        }

        if (paroleAssenti.isEmpty()) {
            throw new IllegalStateException("Nessuna parola assente trovata tra quelle disponibili.");
        }

        String parolaCorretta = paroleAssenti.get(random.nextInt(paroleAssenti.size()));

        List<String> parolePresentiLista = new ArrayList<>(parolePresenti);
        Collections.shuffle(parolePresentiLista);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaCorretta);
        for (int i = 0; i < 3 && i < parolePresentiLista.size(); i++) {
            opzioni.add(parolePresentiLista.get(i));
        }

        if (opzioni.size() < 4) {
            throw new IllegalStateException("Non ci sono abbastanza parole presenti per generare distrattori.");
        }

        Collections.shuffle(opzioni);

        String testoDomanda = "Which of the following words never appears in any of the documents?";

        return new Domanda(testoDomanda, TipoDomanda.ASSENZA, opzioni, parolaCorretta);
    }
}
