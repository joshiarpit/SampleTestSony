package com.testautomation.acceptancesuite;



import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.TestNG;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class RunTestNG {
    public static boolean overallResult = true;
    public static ExtentReports report;
    public static Properties SYSPARAM = null;
    public static String reportPath = "";
    public static ExtentTest test;
    public static Map<String, String> listTest = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {


        TestNG runner = new TestNG();
        List<String> suitefiles = new ArrayList();
        suitefiles.add("testng.xml");
        runner.setTestSuites(suitefiles);
        SYSPARAM = new Properties();
        FileInputStream ist = new FileInputStream(System.getProperty("user.dir") + "\\Resources\\config.properties");
        SYSPARAM.load(ist);
        DateFormat df = new SimpleDateFormat("MMddyyyy");

        // Get the date today using Calendar object.
        java.util.Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);
        reportPath = SYSPARAM.getProperty("Report_Path");
        report = ExtentManager.getReporter(reportPath + "\\PRO_REO_" + reportDate + ".html");
        test = report.startTest("Acceptance Suite", "Objective: Verify whether a loan order is completed successfully by Agent and Vendor");
        runner.run();
        report.endTest(test);
        if (listTest.containsValue("false") ||
                overallResult == false) {
            test.log(LogStatus.FAIL, "Acceptance Suite failed due to Error mentioned at the Test level.", "");
            System.exit(1);
        } else {
            System.exit(0);
        }

        /*if(!AcceptanceTest.TestSuiteFlag.equalsIgnoreCase("PASS") &&
                overallResult == false) {

        } else {
            test.log(LogStatus.PASS, "Acceptance Suite Passed.","All Test Passed.");
            System.exit(0);
        }*/

    }

}