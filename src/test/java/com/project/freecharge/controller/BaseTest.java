package com.project.freecharge.controller;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.project.freecharge.utilities.Configuration;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the common class which will be shared among all the test cases. It contains generic methods which applies at test level.
 */

public class BaseTest {
    public static Logger logger = getLogData(BaseTest.class.getName());
    public String reportPath = new File("").getAbsoluteFile().toString().trim()+"/TestReport/";
    public static ExtentReports reports;
    public static ExtentTest test;
    public static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();


    public WebDriver getDriver(){
        return  webDriver.get();
    }

    public static ThreadLocal<WebDriver> setUpBrowser(String browser, String testName){
        if(!(browser == null)){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatted = sdf.format(new Date()).toString();
            logger.info("\n"
                    + "\n Test Case Name	:"
                    + testName
                    + "\n Start Time		:"
                    + formatted
                    + "\n----------------------------------------------------------------------------");

            logger.info("Browser is- "+browser);

            if("Chrome".equalsIgnoreCase(browser)){
                try{
                    ChromeOptions co = new ChromeOptions();
                    co.addArguments("--start-maximized");
                    System.setProperty("webdriver.chrome.driver", Configuration.CHROME_DRIVER);
                        webDriver.set(new ChromeDriver(co));

                } catch(Exception e){
                    logger.error("Set correct path in the property");
                    e.printStackTrace();
                }
            }

        } else {
            logger.error("Please make sure that you enter the correct browser");
        }

        return webDriver;

    }

    public  void terminateTest(ExtentTest test, ExtentReports extent, WebDriver driver){
            driver.close();
            driver.quit();
            logger.info("Test Closed");
            extent.endTest(test);
            extent.flush();
            extent.close();
    }

    public ExtentTest generateTest(ExtentReports report, String testName, String desc){
        ExtentTest test = report.startTest(testName, desc);
        return test;
    }

    public void reportFail(ExtentTest test, String methodName, String details){
       test.log(LogStatus.FAIL, methodName, details);
    }

    public void reportPass(ExtentTest test, String methodName, String details){
        test.log(LogStatus.PASS, methodName, details);
    }

    public void tearDown(ExtentTest test, ExtentReports report){
        report.endTest(test);
        report.flush();
        report.close();
    }

    public static JSONObject getJsonObject(String jsonfile){
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try{
            FileReader fileReader = new FileReader(getJsonFile(jsonfile));
            json = (JSONObject) parser.parse(fileReader);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public String readObjectFromRestBody(String body, String key){
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        String value="";
        try{
            String random = RandomStringUtils.randomAlphabetic(5);
            String path = System.getProperty("user.dir")+"/src/test/resources/JsonBody/jsonbody_"+random+".json";
            File file = new File(path);
            file.createNewFile();
            FileWriter fw = new FileWriter(path);
            fw.write(body);
            fw.close();
            FileReader fileReader = new FileReader(path);
            JSONArray jsonarray = (JSONArray)parser.parse(fileReader);
            if(jsonarray!=null && jsonarray.size()>0) {
                for (Object obj : jsonarray) {
                    JSONObject jo = (JSONObject) obj;
                    value = String.valueOf(jo.get(key));
                    break;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String getJsonFile(String fileName){
        return System.getProperty("user.dir")+"/src/test/resources/PostJsons/"+fileName;
    }

    public static Logger getLogData(String className){
        DOMConfigurator.configure("log4j.xml");
        return Logger.getLogger(className);
    }

}
