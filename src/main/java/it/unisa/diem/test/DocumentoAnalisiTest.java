package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentoAnalisiTest {
    public static void main(String[] args) {

/*
        try{
            DocumentoRosa dr=new DocumentoRosa("crypto", Lingua.ITA, Difficolta.FACILE, null);
            dr.convertiTxtToBin(new File("data/ITA/facile/storiella.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try{
        System.out.println(DocumentoRosa.leggiDocumento("data/ITA/facile/crypto.bin"));
    }catch(IOException | ClassNotFoundException e)
        {e.printStackTrace();}

 */

        try {
            DocumentoRosa dr=DocumentoRosa.leggiDocumento("data/ITA/facile/crypto.bin");
            //dr.getParole().forEach(System.out::println);
            System.out.println(dr.getTesto());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




}
