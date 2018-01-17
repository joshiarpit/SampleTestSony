package com.testautomation.acceptancesuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

/**
 * Created by sanjay.jain on 4/28/2016.
 */

public class Reporter {


    private static ExtentReports reporter;
    private static ExtentTest logger;

    public static ExtentReports getReport() {
        return reporter;
    }

    public static ExtentTest getLogger() {
        return logger;
    }

    public static void setLogger(ExtentTest logger) {
        Reporter.logger = logger;
    }

    public static ExtentTest generatereport(String reportfileName, boolean replaceExisting, String testcase, String description) {
        reporter = new ExtentReports(reportfileName, replaceExisting);
        ExtentTest logger = reporter.startTest(testcase, description);
        return logger;
    }


}

