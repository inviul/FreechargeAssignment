package com.project.freecharge.controller;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * MyListeners class implements ITestListener which is used for logging and reporting at different events.
 */

public class MyListeners implements ITestListener {

    public static Logger logger = BaseTest.getLogData(MyListeners.class.getName());

    public void onStart(ITestContext context) {
        logger.info("*** Test Suite " + context.getName() + " started ***");
    }

    public void onFinish(ITestContext context) {
        logger.info(("*** Test Suite " + context.getName() + " ending ***"));
    }

    public void onTestStart(ITestResult result) {
        logger.info(("*** Running test method " + result.getMethod().getMethodName() + "..."));
    }

    public void onTestSuccess(ITestResult result) {
        logger.info("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
        new BaseTest().reportPass(BaseTest.test, result.getMethod().getMethodName(),"Test Passed");
    }

    public void onTestFailure(ITestResult result) {
        logger.error("*** Test execution " + result.getMethod().getMethodName() + " failed...");
        new BaseTest().reportFail(BaseTest.test,result.getMethod().getMethodName(),"Test Failed");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("*** Test failed but within percentage % " + result.getMethod().getMethodName());
    }

}
