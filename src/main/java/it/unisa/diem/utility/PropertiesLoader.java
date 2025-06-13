package it.unisa.diem.utility;

import java.io.IOException;
import java.io.InputStream;
//import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {
    //deve leggere da un file properties--> per il db
    private static Properties prop ;
    public static void init(){
        if(prop == null){
            prop = new Properties();
        }
        //URL g=PropertiesLoader.class.getClassLoader().getResource("database.properties");
        try(InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream("config/config.properties")){
            if(is == null){
                System.out.println("Config file not found");
            }
            /*
            Reads a property list (key and element pairs) from the input byte stream.
             The input stream is in a simple line-oriented format as specified in load(Reader)
              and is assumed to use the ISO 8859-1 character encoding;
             */
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
