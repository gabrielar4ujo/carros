package br.com.ifpb.carros.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailConf {
    private static final String PROPERTIES_FILE = "/mail-env.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try(InputStream input = EmailConf.class.getResourceAsStream(PROPERTIES_FILE)){
            properties.load(input);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Properties getProps(){
        return properties;
    }
}
