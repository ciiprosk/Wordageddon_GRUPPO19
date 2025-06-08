package it.unisa.diem.test;

import it.unisa.diem.model.gestione.analisi.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentoAnalisiTest {
    public static void main(String[] args) {


        try {
            Documento dr=Documento.leggiDocumento("data/ITA/facile/crypto.bin");
            //dr.getParole().forEach(System.out::println);
            System.out.println(dr.getTesto());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




}
