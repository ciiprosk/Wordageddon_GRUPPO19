package it.unisa.diem.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    //deve leggere da un file properties--> per il db
    private static Properties prop ;
    public static void init(){
        if(prop == null){
            prop = new Properties();
        }
        try(InputStream is = PropertiesLoader.class.getClassLoader().getResourceAsStream("config/config.properties")){
            if(is == null){
                System.out.println("Config file not found");
            }
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
