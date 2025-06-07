package it.unisa.diem.model.gestione.sessione;

import it.unisa.diem.model.gestione.analisi.AnalisiRosa;
import it.unisa.diem.model.gestione.analisi.Difficolta;
import it.unisa.diem.model.gestione.analisi.DocumentoRosa;
import it.unisa.diem.model.gestione.analisi.Lingua;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordITA;
import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args)throws Exception{

            // Creiamo prima un documento di test e lo convertiamo in binario

            StopwordManager s=new StopwordITA();
            s.caricaStopword(true, true, true, true, true, true);
            s.aggiungi("prova");
            s.aggiungi("volta");
            DocumentoRosa dr = new DocumentoRosa("testo_prova", Lingua.ITA, Difficolta.FACILE, s);
            dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
            AnalisiRosa a =new AnalisiRosa(dr);


            System.out.println(a.getFrequenzeTestiRosa());
            /*
            // Test con il documento appena creato
            String path = "data/ITA/FACILE/testo.bin";

            System.out.println("=== Test Analisi Documento ===\n");
            System.out.println("Analisi del documento: " + path);
            System.out.println("-------------------------");

            // Carica il documento
            DocumentoRosa documento = DocumentoRosa.leggiDocumento(path);

            // Mostra informazioni del documento
            System.out.println("Titolo: " + documento.getTitolo());
            System.out.println("Lingua: " + documento.getLingua());
            System.out.println("Difficoltà: " + documento.getDifficolta());

            // Crea e esegui l'analisi
            AnalisiRosa analisi = new AnalisiRosa(path);
            Map<String, Integer> frequenze = analisi.getFrequenzeTestiRosa();

            // Stampa le frequenze ordinate per numero di occorrenze (decrescente)
            System.out.println("\nFrequenze delle parole:");
            frequenze.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        System.out.printf("%-20s: %d%n", entry.getKey(), entry.getValue());
                    });

            System.out.println("\nContenuto del documento:");
            System.out.println(documento.toString());

        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Errore nella deserializzazione: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore generico: " + e.getMessage());
            e.printStackTrace();
        }
        */


    }

}