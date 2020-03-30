package com.project.freecharge.test;

import com.project.freecharge.controller.BaseTest;
import com.relevantcodes.extentreports.ExtentReports;
import com.project.freecharge.controller.MyListeners;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * This test class is to test the frontend of Bewkoof.com. It inherits BaseTest class where basic methods like setting up browser,
 * report logging, logger, teardown, etc are written.
 * @BeforeTest annotation sets up reporting, browser set up, url opening
 * @AfterTest annotation tears down everything
 * @Test annotation contains the real tests
 * Parameters for browser & url are taken from testng.xml
 * It implements ITestListener as well
 */

@Listeners(MyListeners.class)
public class BewkoofTest extends BaseTest {

    @Parameters({"browser","url"})
    @BeforeTest
    public void setUp(String browser, String url){
        reports = new ExtentReports(reportPath+this.getClass().getSimpleName()+".html",false);
        test= generateTest(reports, this.getClass().getSimpleName(), "This test is to validate the Bewkoof.com Frontend");
        webDriver=setUpBrowser(browser,this.getClass().getSimpleName());
        getDriver().get(url);
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterTest
    public void tearDown(){
        super.terminateTest(test, reports, getDriver());
    }

    @Test
    public void test(){

    }

}
