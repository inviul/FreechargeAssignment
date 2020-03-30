package com.project.freecharge.utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static final String CONFIG_FILE = System.getProperty("user.dir")+"/src/main/resources/config.properties";
    public static final String CHROME_DRIVER = System.getProperty("user.dir")+"/src/test/resources/Container/chromedriver.exe";
    private static Properties properties;
    public static String ACCESS_TOKEN = getVal("accesstoken");
    public static String BASE_URL = getVal("baseurl");
    public static String JSON_MEDIA = getVal("jsonmediatype");
    public static String JSON_MEDIA_TYPE = getVal("jsonmediatype1");
    public static String DEFAULT_MEDIA_TYPE = getVal("defaultmediatype");

    private static String getVal(String key){
        properties = new Properties();
        FileInputStream fis = null;
        try{
             fis= new FileInputStream(CONFIG_FILE);
            properties.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }


}
