package it.unisa.diem.model.gestione.sessione;

import java.util.*;
import java.util.stream.Collectors;
import it.unisa.diem.utility.TipoDomanda;

public class DomandaFactory {

    private List<String> stopwords;

    public DomandaFactory(List<String> stopwords) {
        this.stopwords = stopwords;
    }

    public List<Domanda> generaDomande(Map<String, Map<String, Integer>> frequenzeTesti) {
        List<Domanda> domande = new ArrayList<>();

        List<TipoDomanda> tipi = new ArrayList<>();
        tipi.add(TipoDomanda.FREQUENZA);
        tipi.add(TipoDomanda.CONFRONTO);
        tipi.add(TipoDomanda.ASSOCIAZIONE);
        tipi.add(TipoDomanda.ASSENZA);

        Collections.shuffle(tipi); // Mischia l’ordine

        for (TipoDomanda tipo : tipi) {
            switch (tipo) {
                case FREQUENZA:
                    domande.add(generaDomandaFrequenza(frequenzeTesti));
                    break;
                case CONFRONTO:
                    domande.add(generaDomandaConfronto(frequenzeTesti));
                    break;
                case ASSOCIAZIONE:
                    domande.add(generaDomandaAssociazione(frequenzeTesti));
                    break;
                case ASSENZA:
                    domande.add(generaDomandaAssenza(frequenzeTesti));
                    break;
            }
        }

        return domande;
    }


    private Domanda generaDomandaFrequenza(Map<String, Map<String, Integer>> frequenzeTesti) {
        String documento = selezionaDocumentoCasuale(frequenzeTesti);
        Map<String, Integer> freq = frequenzeTesti.get(documento);

        String parola = selezionaParolaCasuale(freq);
        int frequenzaCorretta = freq.get(parola);

        List<String> opzioni = generaOpzioniNumeriche(frequenzaCorretta);
        String testo = String.format("Quante volte compare la parola \"%s\" nel documento \"%s\"?", parola, documento);

        return new Domanda(testo, TipoDomanda.FREQUENZA, opzioni, String.valueOf(frequenzaCorretta));
    }

    private Domanda generaDomandaConfronto(Map<String, Map<String, Integer>> frequenzeTesti) {
        Set<String> paroleTotali = new HashSet<>();
        frequenzeTesti.values().forEach(mappa -> paroleTotali.addAll(mappa.keySet()));

        List<String> paroleValide = paroleTotali.stream()
                .filter(p -> !stopwords.contains(p.toLowerCase()))
                .collect(Collectors.toList());

        Collections.shuffle(paroleValide);
        List<String> scelte = paroleValide.subList(0, 4);

        String parolaCorretta = scelte.stream().max(
                Comparator.comparingInt(p -> sommaFrequenze(p, frequenzeTesti))
        ).orElse(scelte.get(0));

        String testo = "Quale tra le seguenti parole è la più frequente in tutti i documenti?";

        return new Domanda(testo, TipoDomanda.CONFRONTO, scelte, parolaCorretta);
    }

    private Domanda generaDomandaAssociazione(Map<String, Map<String, Integer>> frequenzeTesti) {
        String documento = selezionaDocumentoCasuale(frequenzeTesti);
        Map<String, Integer> freq = frequenzeTesti.get(documento);

        List<String> parole = freq.entrySet().stream()
                .filter(e -> !stopwords.contains(e.getKey().toLowerCase()))
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String parolaCorretta = parole.get(0);
        List<String> opzioni = generaDistrattori(parolaCorretta, parole);

        String testo = String.format("Quale parola compare più spesso nel documento \"%s\"?", documento);

        return new Domanda(testo, TipoDomanda.ASSOCIAZIONE, opzioni, parolaCorretta);
    }

    private Domanda generaDomandaAssenza(Map<String, Map<String, Integer>> frequenzeTesti) {
        // Trova tutte le parole presenti in tutti i documenti
        Set<String> presenti = new HashSet<>();
        for (Map<String, Integer> mappa : frequenzeTesti.values()) {
            for (String parola : mappa.keySet()) {
                if (!stopwords.contains(parola.toLowerCase())) {
                    presenti.add(parola);
                }
            }
        }

        // Lista di parole comuni da cui prendere la parola  //aggiungeremo poi altro, eventualmente un dizionario per testo
        List<String> dizionario = Arrays.asList("aquila", "giocattolo", "filosofia", "montagna", "computer", "zebra", "filamento", "arcobaleno");

        // Trova una parola assente
        String parolaCorretta = null;
        for (String p : dizionario) {
            if (!presenti.contains(p)) {
                parolaCorretta = p;
                break;
            }
        }

        if (parolaCorretta == null) {
            parolaCorretta = "fantasma"; // "Se non sono riuscito a trovare nessuna parola assente nei documenti tra quelle del dizionario, allora usa una parola predefinita ('fantasma') come risposta corretta."
        }

        // Seleziona 3 parole presenti come distrattori
        List<String> parolePresenti = new ArrayList<>(presenti);
        Collections.shuffle(parolePresenti);
        List<String> distrattori = parolePresenti.subList(0, Math.min(3, parolePresenti.size()));

        // Costruisci e mescola le opzioni
        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaCorretta);
        opzioni.addAll(distrattori);
        Collections.shuffle(opzioni);

        String testo = "Quale di queste parole non appare mai in nessun documento?";
        return new Domanda(testo, TipoDomanda.ASSENZA, opzioni, parolaCorretta);
    }


    // --- METODI DI SUPPORTO ---

    private String selezionaDocumentoCasuale(Map<String, Map<String, Integer>> mappa) {
        List<String> documenti = new ArrayList<>(mappa.keySet());
        return documenti.get(new Random().nextInt(documenti.size()));
    }

    private String selezionaParolaCasuale(Map<String, Integer> freq) {
        List<String> parole = freq.keySet().stream()
                .filter(p -> !stopwords.contains(p.toLowerCase()))
                .collect(Collectors.toList());
        Collections.shuffle(parole);
        return parole.get(0);
    }

    private List<String> generaOpzioniNumeriche(int corretta) {
        Set<Integer> opzioni = new HashSet<>();
        opzioni.add(corretta);
        Random rand = new Random();
        while (opzioni.size() < 4) {
            int val = corretta + rand.nextInt(5) - 2;
            if (val >= 0) opzioni.add(val);
        }
        return opzioni.stream().map(String::valueOf).collect(Collectors.toList());
    }

    private List<String> generaDistrattori(String corretta, List<String> parole) {
        List<String> distrattori = new ArrayList<>(parole);
        distrattori.remove(corretta);
        Collections.shuffle(distrattori);
        List<String> opzioni = new ArrayList<>();
        opzioni.add(corretta);
        opzioni.addAll(distrattori.subList(0, 3));
        Collections.shuffle(opzioni);
        return opzioni;
    }

    private int sommaFrequenze(String parola, Map<String, Map<String, Integer>> mappe) {
        return mappe.values().stream()
                .mapToInt(mappa -> mappa.getOrDefault(parola, 0))
                .sum();
    }

    private List<String> generaParoleAssenti(Set<String> presenti) {
        // In pratica: da un dizionario di parole comuni
        List<String> dizionario = Arrays.asList("aquila", "giocattolo", "filosofia", "montagna", "computer", "zebra");
        return dizionario.stream()
                .filter(p -> !presenti.contains(p))
                .limit(4)
                .collect(Collectors.toList());
    }
}

