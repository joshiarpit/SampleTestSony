package com.testautomation.acceptancesuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;


public class AcceptanceTest {


    public static String LoanNumber = null;
    //public static ExtentTest logger ;
    public static WebDriver driver;
    public static String TestSuiteFlag = null;
    public static String testExecutionName = null;
    public static boolean testExecutionStatus = false;


    @Test(priority = 1)
    public void Add_Property(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        testExecutionName = Testname;
        rt.test = RunTestNG.report.startTest(Testname, method.getName());
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        rt.test.log(LogStatus.INFO, "Add Property Test Cases Started", "Add Property in progress.");
        LoanNumber = generateRandomNumber(10);

        try {
            WebElement element = driver.findElement(By.xpath("//*[contains(@href,'index.cfm?event=property.addProperty') and @title='Add']"));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception ee) {
            driver.findElement(By.xpath("//*[contains(@href,'index.cfm?event=property.addProperty') and @title='Add']")).click();
        }
        try {
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.findElement(By.id("loan_number")).sendKeys(LoanNumber);
            driver.findElement(By.id("address")).sendKeys("6060 Center Drive");
            driver.findElement(By.id("city")).sendKeys("SEATTLE");
            Select select1 = new Select(driver.findElement(By.cssSelector("select[name='state']")));
            select1.selectByVisibleText("Washington");
            driver.findElement(By.id("Postal_Code")).sendKeys("98101");
            driver.findElement(By.id("fc_sale_date")).sendKeys("03/12/2022");
            driver.findElement(By.id("current_UPB")).sendKeys("4000");
            driver.findElement(By.name("btnSearch")).click();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

            boolean msg = driver.getPageSource().contains("Property Insert Successful");
            if (msg) {
               /* File Scrfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(Scrfile, new File(reportPath + "\\" + Testname + ".png"));*/
                String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
                rt.test.log(LogStatus.PASS, "Property Added Successfully", image);
                RunTestNG.report.flush();
            } else {
               /* File Scrfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(Scrfile, new File(reportPath + "\\" + Testname + ".png"));*/
                String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                rt.test.log(LogStatus.FAIL, "Cannot Add property", image1);
                rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                TestSuiteFlag = "FAIL";
                RunTestNG.report.endTest(rt.test);
                RunTestNG.report.flush();
                assertTrue(false, "Add Property Operation Failed.");
            }
            //driver.findElement(By.linkText("Click here")).click();
            driver.findElement(By.xpath("(//a[text()='Click here'])[2]")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Got an Exception while adding a property" +
                    ExceptionUtils.getMessage(e) + "]", image);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            assertTrue(false, "Cannot Add property[" + e.getMessage() + "]");
        }
        TestSuiteFlag = "PASS";
        String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
        rt.test.log(LogStatus.PASS, "Property Added successfully.", image);
        RunTestNG.report.endTest(rt.test);
    }

    @Test(priority = 2, dependsOnMethods = {"Add_Property"})

    public void Assign_Roles(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        rt.test = RunTestNG.report.startTest(Testname, method.getName());
        try {
            driver.findElement(By.xpath("//a[contains(text(),'Assign Roles')]")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            TestSuiteFlag = "FAIL";
            //System.out.println("Failed");
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Task Doesn't Exist [" + e.getMessage() + "]", image);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            RunTestNG.report.flush();
            assertTrue(false, "Task Doesn't  Exist in the page");
            return;
        }
        WebElement table = driver.findElement(By.xpath("//div[2][contains(@class,'roundedContent')]"));
        List<WebElement> rows = table.findElements(By.xpath(".//*[@class='formInput']"));
        System.out.println(rows.size());
        for (WebElement eachPage : rows) {
            Select select1 = new Select(eachPage);
            select1.selectByIndex(1);
        }
        driver.findElement(By.xpath("//*[@name='btnSubmit']")).click();
        Thread.sleep(3000);
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.switchTo().defaultContent();
        PageErrorCheck(Testname, RunTestNG.test);
        Thread.sleep(3000);
        String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
        rt.test.log(LogStatus.PASS, "Assigned Roles to the Property Successfully", image);
        //report.endTest(rt.test);
        RunTestNG.report.flush();

        if (TestSuiteFlag.equalsIgnoreCase("FAIL")) {
            String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Test case Failed.", image1);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            assertTrue(false);
            return;
        }
        TestSuiteFlag = "PASS";
        String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
        rt.test.log(LogStatus.PASS, "Assign Roles completed successfully.", image1);
        RunTestNG.report.endTest(rt.test);
        RunTestNG.report.flush();
    }

    @Test(priority = 3, dependsOnMethods = {"Assign_Roles"})
    public void Assign_listing_Agent(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        rt.test = RunTestNG.report.startTest(Testname, method.getName());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            driver.findElement(By.linkText("Assign Listing Agent")).click();
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            driver.findElement(By.xpath(".//*[@name='filter_agent_name_email' and @placeholder='Name/Email']")).sendKeys(RunTestNG.SYSPARAM.getProperty("Agent_Username"));
            driver.findElement(By.xpath("//*[@name='filter_city_state_zip' and @placeholder='City, State, Zip']")).clear();
            driver.findElement(By.name("btnSearch")).click();
            Thread.sleep(5000);
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

            boolean b = driver.getPageSource().contains("Sorry, no results found. Please try your search again");
            if (b) {
                TestSuiteFlag = "FAIL";
                String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                rt.test.log(LogStatus.FAIL, "No Agent Found with the name specified in config.properties", image);
                rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                RunTestNG.report.endTest(rt.test);
                assertTrue(false, "No Agent Found");
            }

            driver.findElement(By.xpath("//*/span[text()='Select']")).click();
            Thread.sleep(5000);
            driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
            driver.findElement(By.xpath(".//*[@class='selectAgent']")).click();
            Thread.sleep(5000);
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//*[@id='viewtasks2']")).click();
            Thread.sleep((2000));
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            String[] ErrorCheck1 = {"Error", "Exception", "exception", "Transformer not Found"};
            for (String element : ErrorCheck1) {
                boolean b2 = driver.getPageSource().contains(element);
                if (b2) {
                    TestSuiteFlag = "FAIL";
                    String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                    rt.test.log(LogStatus.FAIL, "Cannot Assign Agent to the property", image);
                    rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                    RunTestNG.report.endTest(rt.test);
                }
            }
            driver.findElement(By.xpath("//*[@id='viewtasks2']")).click();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            String[] ErrorCheck = {"Error", "Exception", "exception", "Transformer not Found"};
            for (String element : ErrorCheck) {
                boolean b2 = driver.getPageSource().contains(element);
                if (b2) {
                    TestSuiteFlag = "FAIL";
                    String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                    rt.test.log(LogStatus.FAIL, "Cannot Assign Agent to the property", image);
                    rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                    RunTestNG.report.endTest(rt.test);
                    RunTestNG.report.flush();
                    assertTrue(false);
                }
            }
            Thread.sleep(3000);
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
            rt.test.log(LogStatus.PASS, "Agent Assigned Successfully", image);
            RunTestNG.report.endTest(rt.test);
            RunTestNG.report.flush();
        } catch (Exception e) {
            TestSuiteFlag = "FAIL";
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Agent Assigned couldn't complete, got error[" + e.getMessage() + "]", image);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            RunTestNG.report.flush();
            assertTrue(false, "Agent Assigned couldn't complete");
        }

    }

    @Test(priority = 4, dependsOnMethods = {"Assign_listing_Agent"})
    public void Order_Title(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        rt.test = RunTestNG.report.startTest(Testname, method.getName());
        try {

            driver.findElement(By.linkText("Order Title")).click();
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            Select select2 = new Select(driver.findElement(By.cssSelector("select[name='product_id']")));
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            select2.selectByVisibleText("End To End Title");
            Thread.sleep(2000);
            Select select3 = new Select(driver.findElement(By.cssSelector("select[name='sel_vendor']")));
            select3.selectByVisibleText(RunTestNG.SYSPARAM.getProperty("Vendor_First_Last"));
            Thread.sleep(5000);
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//input[@name='BTNSUBMIT']")).click();
            driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
            PageErrorCheck(Testname, RunTestNG.test);
            Thread.sleep(3000);
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
            rt.test.log(LogStatus.PASS, "Order title step completed successfully.", image);
            RunTestNG.report.endTest(rt.test);

        } catch (Exception ee) {
            TestSuiteFlag = "FAIL";
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Order title task failed with an error [" + ee.getMessage() + "]", image);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            assertTrue(false, "Order title task failed with an error.");
        }
    }

    @Test(priority = 5, dependsOnMethods = {"Order_Title"})
    public void Open_Completed_Task(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        rt.test = RunTestNG.report.startTest(Testname, method.getName());

        try {
            driver.findElement(By.xpath("//*[@id='viewtasks2']")).click();
            driver.findElement(By.linkText("Order Title")).click();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            PageErrorCheck(Testname, RunTestNG.test);
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
            rt.test.log(LogStatus.PASS, "Opening an already completed task", image);
            RunTestNG.report.endTest(rt.test);
            RunTestNG.report.flush();
        } catch (Exception e) {
            TestSuiteFlag = "FAIL";
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Open an already completed task 'Order title' failed", image);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            assertTrue(false);
        }
    }

    @Test(priority = 6, dependsOnMethods = {"Open_Completed_Task"})
    public void Agent_Vendor_Test(Method method) throws Exception {
        RunTestNG rt = new RunTestNG();
        String Testname = method.getName().replaceAll("_", " ");
        rt.test = RunTestNG.report.startTest(Testname, method.getName());

        try {
            driver.manage().timeouts().implicitlyWait(300, TimeUnit.MILLISECONDS);
            driver.findElement(By.xpath("//a[contains(text(),'Edit')]")).click();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            String LoanNumber = driver.findElement(By.id("loan_number")).getAttribute("value");
            try {
                if (!Agentlogin(rt.test)) {
                    String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                    rt.test.log(LogStatus.FAIL, "Agent Login Page Failed..", image);
                    rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                    assertTrue(false, "Agent Login Page Failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                String image = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                rt.test.log(LogStatus.FAIL, "Agent Login Page", image);
                rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                assertTrue(false);
            }

            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            Select select1 = new Select(driver.findElement(By.name("property_SearchType")));
            select1.selectByValue("LoanNumber");
            driver.findElement(By.name("property_SearchText")).sendKeys(LoanNumber);
            driver.findElement(By.name("btnSearch")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Thread.sleep(10000);
            driver.findElement(By.xpath("(//a[contains(@href,'event=property.viewTasks&property_id')])[2]")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            driver.findElement(By.xpath("//a[text()='Accept / Reject Listing']")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            if (!driver.findElement(By.name("sel_acceptAgreement")).isDisplayed()) {
                int i = 0;
                while (i < 5) {
                    try {
                        driver.findElement(By.xpath("//a[text()='Accept / Reject Listing']")).click();
                        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                        if (driver.findElement(By.name("sel_acceptAgreement")).isDisplayed()) {
                            break;
                        }
                    } catch (Exception ee) {
                        System.out.println("Looping for " + i + " th time to find the Link in Agent portal");
                    }
                    i = +1;
                }
            }

            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Select select = new Select(driver.findElement(By.name("sel_acceptAgreement")));
            select.selectByValue("Accept");
            Thread.sleep(1000);
            driver.findElement(By.name("btnSubmit")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(300, TimeUnit.MILLISECONDS);
            PageErrorCheck(Testname, RunTestNG.test);
            Thread.sleep(3000);
            String image = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
            rt.test.log(LogStatus.PASS, "Accept Reject Listing", image);
            //report.endTest(rt.test);
            rt.test = RunTestNG.report.startTest("Accept Reject Title Order");
            driver.manage().timeouts().implicitlyWait(300, TimeUnit.MILLISECONDS);
            if (!Vendorlogin(rt.test)) {
                String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
                rt.test.log(LogStatus.FAIL, "Vendor Login Page Failed..", image1);
                rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                assertTrue(false, "Vendor Login Page didn't appear in specific time.");
                return;
            }
            ;
            driver.manage().timeouts().implicitlyWait(300, TimeUnit.MILLISECONDS);
            Thread.sleep(10000);
            List<WebElement> ele = driver.findElements(By.xpath(".//input[@id='property_SearchText']"));
            List<WebElement> btn = driver.findElements(By.xpath(".//*[@id='propertySearch_searchButton']"));
            try {
                ele.get(0).sendKeys(LoanNumber);
                btn.get(0).click();
            } catch (Exception ev) {
                ele.get(1).sendKeys(LoanNumber);
                btn.get(1).click();
            }
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Thread.sleep(10000);
            driver.findElement(By.xpath("//a[text()='6060 CENTER DRIVE']")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.xpath("//a[text()='Complete Title']")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            Thread.sleep(10000);
            if (!driver.findElement(By.xpath("//a[text()='Accept/Reject Title Order']")).isDisplayed()) {
                int i = 0;
                while (i < 3) {
                    try {
                        driver.findElement(By.xpath("//a[text()='Accept/Reject Title Order']")).click();
                        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                        if (driver.findElement(By.name("sel_accept_reject")).isDisplayed()) {
                            break;
                        }
                    } catch (Exception ee) {

                    }
                    i = +1;
                }
            }
            Thread.sleep(10000);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            Select select21 = new Select(driver.findElement(By.name("sel_accept_reject")));
            select21.selectByValue("Accept");
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.findElement(By.name("btnSubmit")).click();
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            String Testname1 = "Accept_Reject_Title_Order";
            PageErrorCheck(Testname, RunTestNG.test);
            Thread.sleep(3000);
            String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "PASS"));
            rt.test.log(LogStatus.PASS, "Accept Reject Title Order", image1);
            RunTestNG.report.endTest(rt.test);
        } catch (Exception e) {
            TestSuiteFlag = "FAIL";
            System.out.println(e.getMessage());
            String image1 = rt.test.addScreenCapture(getScreenshots(Testname, "FAIL"));
            rt.test.log(LogStatus.FAIL, "Accept Reject Title Order \n" + ExceptionUtils.getFullStackTrace(e), image1);
            rt.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(rt.test);
            //   Assert.fail("Agent Portal Acceptance of the Order Failed");
            assertTrue(false, "Agent Portal Acceptance of the Order Failed");
        }
    }

    @BeforeMethod
    public void beforeMethod() {

    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        RunTestNG.report.flush();

        TestSuiteFlag = "false";

        try {
            if (result.getStatus() == ITestResult.SUCCESS) {

                //Do something here
                TestSuiteFlag = "PASS";
                System.out.println(result.getName() + "----> passed ");

            } else if (result.getStatus() == ITestResult.FAILURE) {
                //Do something here
                System.out.println(result.getName() + "-------> Failed ");
                RunTestNG.overallResult = false;
            } else if (result.getStatus() == ITestResult.SKIP) {

                System.out.println(result.getName() + "--------> Skiped");
            }

            try {
                RunTestNG.listTest.put(result.getName(), TestSuiteFlag);
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @BeforeTest
    public void beforeTest() throws InterruptedException, IOException {

        TestSuiteFlag = "FAIL";
        System.out.println("Launch, Reports at [" + RunTestNG.reportPath + "] path");
        DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
        dc.setBrowserName("internet explorer");
        dc.setPlatform(Platform.WINDOWS);
        dc.setJavascriptEnabled(true);
        dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

        dc.setCapability("enablePersistentHover", false);
        dc.setCapability("ignoreZoomSetting", true);
        dc.setCapability("ie.ensureCleanSession", true);
//			  dc.setCapability("nativeEvents", false); // Dont need to change

        //System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\config\\IEDriverServer64.exe");
        System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\Resources\\IEDriverServer.exe");
        driver = new InternetExplorerDriver(dc);
        String WS_URL = "";
        String env = RunTestNG.SYSPARAM.getProperty("ENV");


        switch (env.toLowerCase()) {
            case "dev":
            case "devint":
            case "dev_int":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_DEV_INT");
                //agentUrl="https://agents.devint.eqdev/";
                break;


            case "devagl_g":
            case "devaglg":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_DEV_AGL_G");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "devagl":
            case "ea":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_DEV_AGL");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "alpha":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_ALPHA");
                //agentUrl="https://agents.alpha.eqdev/";
                break;

            case "alphax7":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_ALPHAX7");
                //agentUrl="https://alphax7agents.alphax.eqdev";
                break;

            case "beta":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_BETA");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax5":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_BETAX5");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax6":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_BETAX6");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax7":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_BETAX7");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "stage":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_STAGE");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "rem":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_REM");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "production":
            case "prod":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_PRODUCTION");
                //agentUrl = "https://agents.equator.com/";
                break;


            case "perf":
            case "performance":
                WS_URL = RunTestNG.SYSPARAM.getProperty("EQ_PRO_PERF");
                //agentUrl = "https://agents.equator.com/";
                break;

            default:
                System.out.println("Invalid Environment");
        }
        try {
            driver.get(WS_URL);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            try {
                if (driver.findElement(By.name("auth_username")).isDisplayed()) {
                    System.out.println("'DEMOREO PRO' Home page displayed successfully.");
                    String image1 = RunTestNG.test.addScreenCapture(getScreenshots("WS Home", "PASS"));
                    RunTestNG.test.log(LogStatus.PASS, "WS Home Page loaded Succesfully.", image1);
                    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                System.out.println("'DEMOREO PRO' Home page didn't loaded successfully.");
                String image1 = RunTestNG.test.addScreenCapture(getScreenshots("WS HOME PAGE", "FAIL"));
                RunTestNG.test.log(LogStatus.FAIL, "WS Home Page was not loaded Succesfully.", image1);
                RunTestNG.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                RunTestNG.report.endTest(RunTestNG.test);
                assertTrue(false);
                return;
            }
            driver.findElement(By.xpath(".//*[(@id='enter_username' or @id ='auth_username' ) and @type='text']")).clear();
            driver.findElement(By.xpath(".//*[(@id='enter_username' or @id ='auth_username' ) and @type='text']")).sendKeys(RunTestNG.SYSPARAM.getProperty("WS_Username"));
            driver.findElement(By.xpath(".//*[(@id='enter_password' or @id='auth_password') and @type ='password']")).clear();
            driver.findElement(By.xpath(".//*[(@id='enter_password' or @id='auth_password') and @type ='password']")).sendKeys(RunTestNG.SYSPARAM.getProperty("WS_Password"));
            driver.findElement(By.id("btnLogin")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            PageErrorCheck("Login", RunTestNG.test);
            Select select;
            select = new Select(driver.findElement(By.cssSelector("select[name='change_lender_id']")));
            select.selectByVisibleText("DEMOREO PRO");
            Thread.sleep(5000);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            TestSuiteFlag = "PASS";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            TestSuiteFlag = "FAIL";
            System.out.println("Login Failed, please check the workstation userlogin or No Privileges to access the lender 'DEMOREO PRO'.");
            //e.printStackTrace();
            String image1 = RunTestNG.test.addScreenCapture(getScreenshots("WS HOME", "FAIL"));
            RunTestNG.test.log(LogStatus.FAIL, "Login Failed, please check the workstation userlogin credentials", image1);
            RunTestNG.test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(RunTestNG.test);
        }
    }

    @AfterTest(alwaysRun = true)
    public void Aftertest() {

        driver.quit();
        RunTestNG.report.flush();
        String status = RunTestNG.test.getRunStatus().toString();
        RunTestNG.report.addSystemInfo("Loan Number ", LoanNumber);
        RunTestNG.report.addSystemInfo("Work Station Username ", RunTestNG.SYSPARAM.getProperty("WS_Username"));
        RunTestNG.report.addSystemInfo("Agent Station Username ", RunTestNG.SYSPARAM.getProperty("Agent_Username"));
        RunTestNG.report.addSystemInfo("Vendor Station Username ", RunTestNG.SYSPARAM.getProperty("Vendor_Username"));

    }

    public void PageErrorCheck(String testname, ExtentTest testlocal) {
        String[] ErrorCheck = {"unexpected error", "server error", "500 - internal server error.",
                "service unavailable",
                "404 - file or directory not found",
                "error has occurred",
                "caught exception in jsondataset"};
        for (String element : ErrorCheck) {
            String url = driver.getCurrentUrl();
            boolean b = driver.getPageSource().toLowerCase().contains(element);
            if (b) {
                String image1 = testlocal.addScreenCapture(getScreenshots("ERROR FOUND", "FAIL"));
                testlocal.log(LogStatus.FAIL, "WS Home Page was not loaded Succesfully.", image1);
                testlocal.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            }
        }
    }

    public String generateRandomNumber(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public boolean Agentlogin(ExtentTest test) throws InterruptedException, IOException {
        String agentUrl = "";
        boolean agentlogin = false;
        String env = RunTestNG.SYSPARAM.getProperty("ENV");
        String reportPath = System.getProperty("user.dir") + RunTestNG.SYSPARAM.getProperty("Report_Path");
        switch (env.toLowerCase()) {

            case "dev":
            case "devint":
            case "dev_int":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_DEV_INT");
                //agentUrl="https://agents.devint.eqdev/";
                break;


            case "devagl_g":
            case "devaglg":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_DEV_AGL_G");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "devagl":
            case "ea":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_DEV_AGL");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "alpha":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_ALPHA");
                //agentUrl="https://agents.alpha.eqdev/";
                break;

            case "alphax7":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_ALPHAX7");
                //agentUrl="https://alphax7agents.alphax.eqdev";
                break;

            case "beta":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_BETA");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax5":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_BETAX5");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax6":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_BETAX6");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax7":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_BETAX7");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "stage":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_STAGE");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "rem":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_REM");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "production":
            case "prod":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_PRODUCTION");
                //agentUrl = "https://agents.equator.com/";
                break;


            case "perf":
            case "performance":
                agentUrl = RunTestNG.SYSPARAM.getProperty("AGENT_PERF");
                //agentUrl = "https://agents.equator.com/";
                break;

            default:
                System.out.println("Invalid Environment Parameter configured");
        }
        try {
            driver.navigate().to(agentUrl);
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            if (!driver.findElement(By.id("enter_username")).isDisplayed()) {
                String image1 = test.addScreenCapture(getScreenshots("Agent Portal", "FAIL"));
                test.log(LogStatus.FAIL, "Agent Portal is Down, Not able to navigate to login page.", image1);
                test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                RunTestNG.report.endTest(test);
                return agentlogin;
            }
            String a_Username = RunTestNG.SYSPARAM.getProperty("Agent_Username");
            String a_Password = RunTestNG.SYSPARAM.getProperty("Agent_Password");
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.findElement(By.id("enter_username")).clear();
            driver.findElement(By.id("enter_username")).sendKeys(a_Username);
            driver.findElement(By.id("enter_password")).clear();
            driver.findElement(By.id("enter_password")).sendKeys(a_Password);
            driver.findElement(By.id("btnLogin")).click();
            String image_1 = test.addScreenCapture(getScreenshots("AGENT LOGIN", "PASS"));
            test.log(LogStatus.PASS, "Agent Login added with [" + a_Username + "], Password [" + a_Password + "]", image_1);
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            PageErrorCheck("Agent Login", test);
            driver.findElement(By.xpath("//*[@id='menubar']//a[text()='Properties']")).click();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Agent Login Failed, Inv");
            // TODO Auto-generated catch block
            e.printStackTrace();
            TestSuiteFlag = "FAIL";
            String image1 = test.addScreenCapture(getScreenshots("AGENT LOGIN", "FAIL"));
            test.log(LogStatus.FAIL, "Login Failed, please check the agent userlogin credentials", image1);
            test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(test);
            return agentlogin;
        }
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.findElement(By.xpath("//*[@id='tabs_propertyTabs']//a[text()='Search Properties']")).click();
        Thread.sleep(5000);
        return true;
    }

    public boolean Vendorlogin(ExtentTest test) throws InterruptedException, IOException {
        String vendorUrl = "";
        String env = RunTestNG.SYSPARAM.getProperty("ENV");
        String reportPath = System.getProperty("user.dir") + RunTestNG.SYSPARAM.getProperty("Report_Path");
        boolean vendorFlag = false;
        switch (env.toLowerCase()) {
            case "dev":
            case "devint":
            case "dev_int":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_DEV_INT");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "devagl_g":
            case "devaglg":
            case "devagileg":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_DEV_AGL_G");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "devagl":
            case "devagile":
            case "ea":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_DEV_AGL");
                //agentUrl="https://agents.devint.eqdev/";
                break;

            case "alpha":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_ALPHA");
                //agentUrl="https://agents.alpha.eqdev/";
                break;

            case "alphax7":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_ALPHAX7");
                //agentUrl="https://alphax7agents.alphax.eqdev";
                break;

            case "beta":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_BETA");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax5":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_BETAX5");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax6":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_BETAX6");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "betax7":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_BETAX7");
                //agentUrl="https://betaagents.equator.com";
                break;

            case "stage":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_STAGE");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "rem":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_REM");
                //agentUrl="https://stageagents.equator.com/";
                break;

            case "production":
            case "prod":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_PRODUCTION");
                //agentUrl = "https://agents.equator.com/";
                break;


            case "perf":
            case "performance":
                vendorUrl = RunTestNG.SYSPARAM.getProperty("VENDOR_PERF");
                //agentUrl = "https://agents.equator.com/";
                break;

            default:
                System.out.println("Invalid Environment which is configured for Vendor, vendor portal link missing.");
        }
        driver.navigate().to(vendorUrl);
        String v_username = RunTestNG.SYSPARAM.getProperty("Vendor_Username");
        String v_password = RunTestNG.SYSPARAM.getProperty("Vendor_Password");
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            if (!driver.findElement(By.id("enter_username")).isDisplayed()) {
                String image1 = test.addScreenCapture(getScreenshots("VENDOR PORTAL", "FAIL"));
                test.log(LogStatus.FAIL, "Vendor Page didn't loaded.", image1);
                test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
                RunTestNG.report.endTest(test);
                Assert.fail("Vendor portal didn't appeared as expected.");
                return false;
            }
        } catch (Exception ee) {

        }
        driver.findElement(By.id("enter_username")).sendKeys(v_username);
        driver.findElement(By.id("enter_password")).sendKeys(v_password);
        driver.findElement(By.id("btnLogin")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        String image_1 = test.addScreenCapture(getScreenshots("VENDOR LOGIN", "FAIL"));
        test.log(LogStatus.PASS, "Vendor Login added with [" + v_username + "], Password [" + v_password + "]", image_1);
        try {
            driver.findElement(By.xpath("//*[@id='menubar']//a[text()='Properties']")).click();
            driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Vendor Login Failed..!");
            e.printStackTrace();
            TestSuiteFlag = "FAIL";
            String image1 = test.addScreenCapture(getScreenshots("VENDOR LOGIN", "FAIL"));
            test.log(LogStatus.FAIL, "Login Failed, please check the vendor login[" + e.getMessage() + "]", image1);
            test.log(LogStatus.INFO, "Got the Current URL", driver.getCurrentUrl());
            RunTestNG.report.endTest(test);
        }
        driver.findElement(By.xpath("//*[@id='tabs_propertyTabs']//a[text()='Search Properties']")).click();
        Thread.sleep(5000);
        return true;
    }

    private String getScreenshots(String testname, String status) {
        String filePath = null;

        File Scrfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            filePath = RunTestNG.reportPath + "\\" + testname + "_" + status + ".png";

            FileUtils.copyFile(Scrfile, new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

}

class ExtentManager {
    private static ExtentReports extent;

    public synchronized static ExtentReports getReporter(String filePath) {
        if (extent == null) {
            String env = RunTestNG.SYSPARAM.getProperty("ENV");
            extent = new ExtentReports(filePath, true);
            try {
                extent
                        .addSystemInfo("Host Name", InetAddress.getLocalHost().getHostName())
                        .addSystemInfo("Environment", env)
                        .addSystemInfo("Operating System", System.getProperty("os.name"))
                        .addSystemInfo("Run Machine -  IP Address", InetAddress.getLocalHost().getHostAddress())
                        .addSystemInfo("Lender Name ", "DEMO PRO REO")
                ;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return extent;
    }
}




