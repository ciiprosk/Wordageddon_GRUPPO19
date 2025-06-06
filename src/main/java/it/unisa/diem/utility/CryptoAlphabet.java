package it.unisa.diem.utility;

public class CryptoAlphabet {
    public static final String alphabet="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.!?";
    private static final String alfabeto_cifrato  = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm9876543210£,.!?";

    //non ha senso mettere il costruttore
    public static String cripta(String line){
        StringBuffer str=new StringBuffer();
        for(char carattere: line.toCharArray()){
            //devo convertire il singolo carattere nel carattere a cui corrisponde nel cifrato
            //per farlo devo trovare la posizione in cui si trova nella mia stringa alphabet
            /*the index of the first occurrence of the character in the character
             sequence represented by this object, or -1 if the character does not occur.
             */

            int posAlphabet=alphabet.indexOf(carattere);

            if(posAlphabet!=-1){
                char criptato=alfabeto_cifrato.charAt(posAlphabet);//cerco nell'alfabeto criptato dove si trova la lettera corrispondente
                str.append(criptato);
            }else str.append(carattere); //in questo caso vuol dire che non ha trovato corrispondenze e lo mettiamo così com'è

        }
        return str.toString();
    }

    public static String decripta(String line){//fa la stessa cosa ma al contrario
        StringBuffer str=new StringBuffer();
        for(char carattere : line.toCharArray()){
            int posCifrato=alfabeto_cifrato.indexOf(carattere);
            if(posCifrato!=-1){
                char decriptato=alphabet.charAt(posCifrato);
                str.append(decriptato);
            }else str.append(carattere);
        }
        return str.toString();
    }
}
