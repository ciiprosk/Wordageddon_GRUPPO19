package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.Analisi;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.utility.TipoDomanda;

import java.util.*;

/**
 * Factory per la generazione di domande basate su un insieme di analisi.
 * Supporta la generazione di diversi tipi di domande con vari livelli di difficoltà.
 */
public class DomandaFactory {
    List<Domanda> listaDomande;
    List<Analisi> listaAnalisi;

    /**
     * Costruttore che inizializza la factory con una lista di analisi.
     *
     * @param listaAnalisi la lista di analisi da utilizzare per generare le domande
     */
    public DomandaFactory(List <Analisi> listaAnalisi) {
        this.listaAnalisi = listaAnalisi;
        listaDomande = new ArrayList<>();
    }

    /**
     * Genera una lista di domande in base al livello di difficoltà specificato.
     *
     * @param difficolta il livello di difficoltà delle domande da generare
     * @return una lista di domande generate
     */
    public List<Domanda> generaDomande(Difficolta difficolta) {
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

    /**
     * Genera una domanda sulla frequenza di una parola in un documento.
     *
     * @return una domanda di tipo FREQUENZA
     */
    private Domanda generaDomandaFrequenza() {
        Random random = new Random();
        Analisi analisiScelta = listaAnalisi.get(random.nextInt(listaAnalisi.size()));
        Map<String, Integer> frequenze = analisiScelta.getFrequenzeTesto();

        List<String> parole = new ArrayList<>(frequenze.keySet());
        String parolaScelta = parole.get(random.nextInt(parole.size()));
        int frequenzaCorretta = frequenze.get(parolaScelta);

        Set<Integer> opzioniNumeriche = new HashSet<>();
        opzioniNumeriche.add(frequenzaCorretta);

        while (opzioniNumeriche.size() < 4) {
            int variazione = random.nextInt(5) + 1;
            int alternativa = frequenzaCorretta + (random.nextBoolean() ? variazione : -variazione);
            if (alternativa >= 1) {
                opzioniNumeriche.add(alternativa);
            }
        }

        List<String> opzioni = new ArrayList<>();
        for (Integer val : opzioniNumeriche) {
            opzioni.add(String.valueOf(val));
        }

        Collections.shuffle(opzioni);

        String titoloDocumento = analisiScelta.getDocumento().getTitolo();
        String testoDomanda = String.format(
                "How many times does the word \"%s\" appear in the document \"%s\"?",
                parolaScelta, titoloDocumento
        );

        return new Domanda(testoDomanda, TipoDomanda.FREQUENZA, opzioni, String.valueOf(frequenzaCorretta));
    }

    /**
     * Genera una domanda di confronto tra le frequenze di parole in tutti i documenti.
     *
     * @return una domanda di tipo CONFRONTO
     * @throws IllegalStateException se non ci sono abbastanza parole per generare la domanda
     */
    private Domanda generaDomandaConfronto() {
        Random random = new Random();
        Map<String, Integer> frequenzeGlobali = new HashMap<>();

        for (Analisi analisi : listaAnalisi) {
            Map<String, Integer> frequenze = analisi.getFrequenzeTesto();
            for (Map.Entry<String, Integer> entry : frequenze.entrySet()) {
                String parola = entry.getKey();
                int count = entry.getValue();
                if (parola != null && !parola.trim().isEmpty()) {
                    frequenzeGlobali.put(parola, frequenzeGlobali.getOrDefault(parola, 0) + count);
                }
            }
        }

        if (frequenzeGlobali.size() < 4) {
            throw new IllegalStateException("Non ci sono abbastanza parole per creare la domanda di confronto.");
        }

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

    /**
     * Genera una domanda di associazione tra una parola e la sua frequenza in un documento.
     *
     * @return una domanda di tipo ASSOCIAZIONE
     * @throws IllegalStateException se il documento non contiene abbastanza parole
     */
    private Domanda generaDomandaAssociazione() {
        Random random = new Random();
        Analisi analisi = listaAnalisi.get(random.nextInt(listaAnalisi.size()));
        Map<String, Integer> frequenze = analisi.getFrequenzeTesto();

        if (frequenze.size() < 4) {
            throw new IllegalStateException("Il documento non contiene abbastanza parole per creare la domanda.");
        }

        String parolaMax = null;
        int maxFreq = -1;
        for (Map.Entry<String, Integer> entry : frequenze.entrySet()) {
            if (entry.getValue() > maxFreq) {
                parolaMax = entry.getKey();
                maxFreq = entry.getValue();
            }
        }

        List<String> distrattori = new ArrayList<>(frequenze.keySet());
        distrattori.remove(parolaMax);
        Collections.shuffle(distrattori);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaMax);
        for (int i = 0; i < 3 && i < distrattori.size(); i++) {
            opzioni.add(distrattori.get(i));
        }

        if (opzioni.size() < 4) {
            throw new IllegalStateException("Non ci sono abbastanza parole diverse per generare la domanda.");
        }

        Collections.shuffle(opzioni);

        String titoloDoc = analisi.getDocumento().getTitolo();
        String testoDomanda = String.format(
                "Which word appears most frequently in the document \"%s\"?",
                titoloDoc
        );

        return new Domanda(testoDomanda, TipoDomanda.ASSOCIAZIONE, opzioni, parolaMax);
    }

    /**
     * Genera una domanda sull'assenza di una parola in tutti i documenti.
     *
     * @return una domanda di tipo ASSENZA
     * @throws IllegalStateException se non ci sono parole assenti o abbastanza distrattori
     */
    private Domanda generaDomandaAssenza() {
        Random random = new Random();
        Set<String> parolePresenti = new HashSet<>();
        for (Analisi analisi : listaAnalisi) {
            parolePresenti.addAll(analisi.getFrequenzeTesto().keySet());
        }

        List<String> dizionario = Arrays.asList(
                "gatto", "cane", "sole", "luna", "tavolo", "computer", "libro", "penna",
                "scuola", "auto", "telefono", "acqua", "vento", "cuore", "pane"
        );

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