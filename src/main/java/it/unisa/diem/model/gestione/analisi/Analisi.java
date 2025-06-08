package it.unisa.diem.model.gestione.analisi;

import it.unisa.diem.model.gestione.analisi.stopword.StopwordManager;
import it.unisa.diem.utility.CryptoAlphabet;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Analisi {
    private Documento documento;
    private Map<String, Integer> frequenzeTesti;
    private Lingua linguaAnalisi;
    private Difficolta difficoltaAnalisi;
    private String titolo;
    private StopwordManager stopwordAnalisi;
    private List<String> testo;

    public Analisi(Documento documento) throws IOException, ClassNotFoundException {
        //dal documento posso ricavare la difficoltà, lingua, stopword...
        frequenzeTesti=new HashMap<>();
        this.documento= documento;
        recuperaDatiFromDocumentoRosa();
    }
    public Analisi() throws IOException, ClassNotFoundException {
        frequenzeTesti=new HashMap<>();
    }


    private void recuperaDatiFromDocumentoRosa() throws IOException, ClassNotFoundException {
        linguaAnalisi=documento.getLingua();
        difficoltaAnalisi=documento.getDifficolta();
        titolo=documento.getTitolo();
        stopwordAnalisi=documento.getStopword();
        Documento doc= Documento.leggiDocumento(documento.getPath());
        testo=doc.getTesto();
    }

    public Documento getDocumento() {
        return documento;
    }

    public Map<String, Integer> getFrequenzeTesti() {

        return frequenzeTesti;
    }

    private List<String> getWordsDocument() {
        List<String> parole = testo.stream()
            .flatMap(line -> Arrays.stream(line.split(" ")))
            .filter(word -> !word.trim().isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toList());
    
    // Applica il filtro stopword solo se stopwordAnalisi non è null
    if (stopwordAnalisi != null) {
        parole = parole.stream()
                .map(word -> {
                    for(String punt: stopwordAnalisi.getPunteggiatura())
                        word=word.replace(punt, "");
                    return word.trim();
                }).filter(word -> !word.isEmpty())
                .filter(word -> !stopwordAnalisi.getParole().contains(word))
                .collect(Collectors.toList());
    }
    
    return parole;
}


    public Map<String, Integer> getFrequenzeTestiRosa() throws IOException, ClassNotFoundException {
        List<String> parole=getWordsDocument();
        frequenzeTesti = new HashMap<>();

        for(String parola : parole){
            if(frequenzeTesti.containsKey(parola)){
                frequenzeTesti.put(parola, frequenzeTesti.get(parola)+1);
            }else{
                frequenzeTesti.put(parola, 1);
            }
        }
        return frequenzeTesti;
    }

    public Map<String, Integer> getFrequenza(){
        return frequenzeTesti;
    }
    public void caricaAnalisi() throws IOException, ClassNotFoundException {
        //devo scrivere su file frequenze testi così com'è
        File file=new File("analysis/"+ linguaAnalisi+"/"+difficoltaAnalisi+"/"+titolo+".bin");
        try(DataOutputStream dos=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))){
            //devo convertire la mappa in una lista formata da key e value, prima una striga e poi un numero
            frequenzeTesti.entrySet().stream().forEach(linea->{
                try {
                    dos.writeUTF(CryptoAlphabet.cripta(linea.getKey()));
                    dos.writeInt(linea.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public Analisi leggiAnalisi(Documento doc){
        Analisi a = null;
        try(DataInputStream dis=new DataInputStream(new BufferedInputStream(new FileInputStream(doc.getPath())))){
            a=new Analisi();
            getAttributes(doc.getPath(), a);
            while(true){
                try{
                    String parola=CryptoAlphabet.decripta(dis.readUTF());
                    int frequenza=dis.readInt();
                    a.frequenzeTesti.put(parola, frequenza);
                }catch(EOFException e){
                    return a;
                }
            }

        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return a;
    }
    private static void getAttributes(String filename, Analisi a){
        String[] split= filename.split("[/.]");
        a.titolo=split[split.length-2];
        a.difficoltaAnalisi=Difficolta.valueOf(split[split.length-3].toUpperCase());
        a.linguaAnalisi=Lingua.valueOf(split[split.length-4].toUpperCase());
    }

    @Override
    public String toString(){
        return frequenzeTesti.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining("\n"));
    }

}