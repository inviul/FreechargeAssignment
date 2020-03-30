package com.project.freecharge.controller;

import com.project.freecharge.utilities.Configuration;
import com.project.freecharge.utilities.RESTUtility;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * This class is extented by all the API tests as it initially set ups the base url and after execution of all the tests within
 * the suite it rests the base url.
 */

public class APITestBed extends BaseTest {

    @BeforeSuite
    public void apiTestBedsetUp(){
        RESTUtility.setBaseURI(Configuration.BASE_URL);
    }

    @AfterSuite
    public void flushTestBed(){
        RESTUtility.resetBaseURI(Configuration.BASE_URL);
    }


}
