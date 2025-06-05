package it.unisa.diem.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProperiesLoader {
    //deve leggere da un file properties
    private static Properties prop ;
    public static void init(){

        try(InputStream is = ProperiesLoader.class.getClassLoader().getResourceAsStream("config/config.properties")){

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
