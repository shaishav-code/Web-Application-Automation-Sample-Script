package com.demo.qa.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.internal.Utils;

import com.demo.pages.LoginPage;
import com.demo.utlility.ConfigReader;
import com.demo.utlility.TestUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
	public int DRIVER_WAIT = 80;
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static int note_data = 0;
	public static String suiteName = "";
	public static String testName = "";
	public String currentTest;
	protected static String screenshot_folder_path = null; // Path to screenshot folder
	ConfigReader con = new ConfigReader();
	TestUtil testutil = new TestUtil();
	public static WebDriver driver;
	public String baseURl = con.getUrl();
	public String userName = con.getUsername();
	public String passWord = con.getPassword();
	public String superAdminUserName= con.getSuperAdminUsername();
	public String superAdminPassword= con.getSuperAdminPassword();
	public static Logger logger;
	public static  String userDir = System.getProperty("user.dir");
	public static String browserName = "";
	public static String browserVersion = "";
	public  static String testUrl ;
	public static HashMap<String,String> globalMap=new HashMap<String,String>();
	public static ArrayList<String> arrayList=new ArrayList<String>();
	
	public LoginPage loginPage;
	@Parameters("browser")
	@BeforeClass
	public void setup(Method method,String browser,ITestContext testContext) {
		String SCREENSHOT_FOLDER_NAME = "screenshots";		
		screenshot_folder_path = new File(SCREENSHOT_FOLDER_NAME).getAbsolutePath();
		suiteName = testContext.getSuite().getName();
        currentTest = testContext.getCurrentXmlTest().getName(); // get Name of current test.	
		
		//currentTest = methodName.getRealClass().getSimpleName() + "." + methodName.getMethodName();
		log("current test- " +currentTest);
		log("Current Xml Suite is:---->" + suiteName);
		logger = Logger.getLogger("Base Class");
		// configure log4j properties file
		PropertyConfigurator.configure("Log4j.properties");
		if (browser.equalsIgnoreCase("chrome"))
		{
			//setup the chromedriver using WebDriverManager
	        WebDriverManager.chromedriver().setup();
	        //Create driver object for Chrome
	        driver = new ChromeDriver();
			logger.info("Chrome Browser Opened");
			driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
			logger.info("PageLoad wait given");
			driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
			logger.info("Implicit wait given");
			driver.manage().timeouts().setScriptTimeout(TestUtil.SET_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
			logger.info("Java Script wait given");
			driver.manage().window().maximize();
			logger.info("Maximize Window Size");
		}

		else if (browser.equalsIgnoreCase("firefox")) {
			//setup the firefoxdriver using WebDriverManager
			WebDriverManager.firefoxdriver().setup();
			//Create driver object for firefox
			driver = new FirefoxDriver();
			logger.info("FireFox Browser Opened");
			driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
			logger.info("PageLoad wait given");
			driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);
			logger.info("Implicit wait given");
			driver.manage().timeouts().setScriptTimeout(TestUtil.SET_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
			logger.info("Java Script wait given");
			driver.manage().window().maximize();
			logger.info("Maximize Window Size");
		}

		driver.get(con.getUrl());
		logger.info("Url opened successfully");
		loginPage = new LoginPage(driver);
	}

	@AfterClass

	public void close() {

		 driver.close();
		 driver.quit();

	}
	
	@BeforeSuite(alwaysRun = true)
	 protected void fetchSuiteConfiguration(ITestContext testContext) {
		

		// Pass the URL to be tested
		testUrl = testContext.getCurrentXmlTest().getParameter("selenium.url");
		// testUrl = TestData.getURL();
		System.out.println("======" + testUrl + "=========");
	}
	
	@AfterSuite
	public void testResults()
	{
		System.out.println("The passed tests - " + passed_count);
		System.out.println("The failed tests - " + failed_count);
		System.out.println("The skipped tests - " + skipped_count);
		
	}
	
	private void getShortException(IResultMap tests) {

		for (ITestResult result : tests.getAllResults()) {

			Throwable exception = result.getThrowable();
			List<String> msgs = Reporter.getOutput(result);
			boolean hasReporterOutput = msgs.size() > 0;
			boolean hasThrowable = exception != null;
			if (hasThrowable) {
				boolean wantsMinimalOutput = result.getStatus() == ITestResult.SUCCESS;
				if (hasReporterOutput) {
					log("<b><i>" + (wantsMinimalOutput ? "Expected Exception" : "Failure Reason:") + "</b></i>");
				}

				// Getting first line of the stack trace
				String str = Utils.stackTrace(exception, true)[0];
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(str);
				String firstLine = scanner.nextLine();
				log("<Strong><font color=#ff0000>" + firstLine + "</font></strong>");
			}
		}
	}
	
	public static int passed_count = 0;
    public static int failed_count = 0;
	public static int skipped_count = 0;
	
@AfterMethod(alwaysRun = true)
public void tearDown(ITestResult testResult) {
	
	
	ITestContext ex = testResult.getTestContext();

	if(testResult.getStatus() == ITestResult.SUCCESS)
    {
		passed_count++;
       
    }

    else if(testResult.getStatus() == ITestResult.FAILURE)
    {
    	failed_count++;  

    }

     else if(testResult.getStatus() == ITestResult.SKIP )
     {	 
    	 skipped_count++;

     }
	try {
		testName = testResult.getName();
		if (!testResult.isSuccess()) {

			//Common.saveScreenshot(driver, currentTest);
			// Print test result to Jenkins Console
			System.out.println();
			System.out.println("TEST FAILED - " + testName);
			System.out.println();
			System.out.println("ERROR MESSAGE: " + testResult.getThrowable());
			System.out.println("\n");
			Reporter.setCurrentTestResult(testResult);			
			Reporter.log("<br></br><Strong><font color=#ff0000>Fail                  </font></strong><img src='"
					+ userDir + "\\report-imgs\\fail.png' alt='fail' height='15' width='15'/>");
			getShortException(ex.getFailedTests());
			String screenshotName = currentTest + getCurrentTimeStampString();			
			makeScreenshot(driver, screenshotName);			
		} else {
		    //Print test result to Jenkins console
			System.out.println("TEST PASSED - " + testName + "\n");
		}
		System.out.println("--------------- Test status : " + testResult.getStatus() + " ---------------");
	     //driver.manage().deleteAllCookies();
		 //driver.close();
		 //driver.quit();

	} catch (Exception throwable) {
		System.out.println("message from tear down" + throwable);
	} finally {

		if (browserName.contains("internet explorer")) {
			killIEServer();
			TestUtil.pause(5);
		}
	}

}

public static void jsClick(WebDriver driver, WebElement element) {
	TestUtil.pause(1);
	highlightElement(driver, element);
	System.out.println("============Apply NOw:::::::::Test::::::"+element.getText());
	((JavascriptExecutor) driver).executeScript(
			"return arguments[0].click();", element);
	// this.waitForAjax("0");
}

public static void clickableElement(WebElement webelement, WebDriver driver) 
{	
	(new WebDriverWait(driver, 60)).until(ExpectedConditions.elementToBeClickable(webelement));
	
}
	


	public void add_screenShot(String testname) throws IOException {

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File target = new File(System.getProperty("user.dir") + "/ScreenShots/" + testname + ".PNG");
		FileUtils.copyFile(source, target);

	}

	public static String randomeNum() {
		String generatedString2 = RandomStringUtils.randomNumeric(2);
		return (generatedString2);
	}
	
	
	public static void scrollToElement(WebDriver driver, WebElement element)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}
	
	public static void scrollToTop(WebDriver driver) {
		
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");
		}
	
	public static void waitForPageLoaded(WebDriver driver) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 70);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}
	
	public static boolean waitforElementClickable(WebDriver driver,WebElement element,  int timeoutInSeconds)
	{
		try{
		WebDriverWait wait= new WebDriverWait(driver, timeoutInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		highlightElement(driver, element);
		return true;
		}
		 catch (Exception e) {
			
			System.out.println(e);
			return false;
		}
	}
	
	public static void highlightElement(WebDriver driver, WebElement element) {
		

		// draw a border around the found element

		((JavascriptExecutor) driver).executeScript("arguments[0].style.border = '3px solid yellow'", element);

		TestUtil.pause(2);

		((JavascriptExecutor) driver).executeScript("arguments[0].style.border = '0px'", element);
	}
	
	public static void mouseHover(WebDriver driver, WebElement element) {
		Actions builder = new Actions(driver);
		builder.moveToElement(element).build().perform();

	}
	
	public static void waitForElement(WebDriver driver,WebElement element) {
		  (new WebDriverWait(driver, 35)).until(ExpectedConditions
		    .visibilityOf(element));
		 }
	
public static void log(String msg) {
		
		System.out.println("======" + msg + "======");
		Reporter.log("<br>" + msg);
	}

	public void failedLog(String msg)
	{
		System.out.println("<strong>======" + msg + "======</strong>");
		Reporter.log("<font color=#FF0000>--Failed- </font>" + "<strong>======" + msg + "======</strong>");
	}
	
	public void testCaselog(String msg) {
		System.out.println("<strong>======" + msg + "======</strong>");
		Reporter.log("<br></br>" + "<strong>======" + msg + "======</strong>");
	}

	public void testInfoLog(String msg1, String msg2) {

		log("<strong>" + msg1 + " : </strong><font color=#9400D3>" + msg2 + "</font>&nbsp; <img src='" + userDir
				+ "\\report-imgs\\info.png' alt='info' height='15' width='15'/>");
	}

	public void testStepsLog(String msg) {
	
		System.err.println(msg);
		log(msg + "<br>");
	}

	public void testVerifyLog(String msg) {

		log("<font color=#000080>" + msg + "</font>");

	}

	public void testValidationLog(String msg) {

		log("Validation Message : <Strong><font color=#ff0000>" + msg + "</strong></font>");

	}

	public void testConfirmationLog(String msg) {

		log("Confirmation Message : <Strong><font color=#008000>" + msg + "</strong></font>");

	}

	public void testWarningLog(String msg) {

		log("Warning Message : <Strong><font color=#FF2070>" + msg + "</strong></font>&nbsp; <img src='" + userDir
				+ "\\report-imgs\\warning.png' alt='info' height='15' width='15'/>");

	}
	
	public static String getCurrentTimeStampString() {
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("ddMMYYYY_HH-mm-ss");
		TimeZone timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(new SimpleTimeZone(timeZone.getOffset(date.getTime()), "GMT"));
		sd.setCalendar(cal);
		return sd.format(date);
	}

	public static void makeScreenshot(WebDriver driver, String screenshotName) {

		WebDriver augmentedDriver = new Augmenter().augment(driver);

		/* Take a screenshot */
		File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		String nameWithExtention = screenshotName + ".png";

		/* Copy screenshot to specific folder */
		try {
			/*
			 * String reportFolder = "target" + File.separator +
			 * "failsafe-reports" + File.separator + "firefox" + File.separator;
			 */
			String reportFolder = "test-output" + File.separator;
			String screenshotsFolder = "screenshots";
			File screenshotFolder = new File(reportFolder + screenshotsFolder);
			if (!screenshotFolder.getAbsoluteFile().exists()) {
				screenshotFolder.mkdir();
			}
			FileUtils.copyFile(screenshot,
					new File(screenshotFolder + File.separator + nameWithExtention).getAbsoluteFile());
		} catch (IOException e) {
			log("Failed to capture screenshot: " + e.getMessage());
		}
		log("<br><b>Screenshot- </b>" + getScreenshotLink(nameWithExtention, nameWithExtention)); // add
																		// screenshot
																		// link
																		// to
																		// the
																		// report
	}
	
	public static String getScreenshotLink(String screenshot_name, String link_text) {

		return "<a href='../test-output/screenshots/" + screenshot_name + "' target='_new'>" + link_text + "</a>";
	}
	/**
	 * This method is killing the IE instance once test is completed. After killing
	 * IE instance it's also clear and kill the IE Server driver.
	 * 
	 */
	public void killIEServer() {
		try {

			TestUtil.pause(5);

			String[] cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";
			cmd[2] = "taskkill /F /IM iexplore.exe";

			Process process = Runtime.getRuntime().exec(cmd);

			Process process1 = Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");

			System.err.println(process + "" + process1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void PresenceOfElement( By locator,WebDriver driver) 
	{	
		(new WebDriverWait(driver, 60)).until(ExpectedConditions.presenceOfElementLocated(locator));
	
	}
	
	public static void clickOn(WebDriver driver, WebElement element) {
		highlightElement(driver, element);
		TestUtil.pause(1);
		element.click();
	}
	

	
	
	public void loginValid()
	{
		TestUtil.pause(3);
		driver.findElement(By.name("email")).sendKeys(userName);
		TestUtil.pause(3);
		driver.findElement(By.cssSelector("div#login-form>form>div>md-icon")).click();
		driver.findElement(By.name("password")).sendKeys(passWord);
		driver.findElement(By.xpath("//*[text()='LOG IN']")).click();
		TestUtil.pause(3);
		boolean status =  afetrloginPopUP();
		if(status)
		{
			TestUtil.pause(3);
			driver.findElement(By.xpath("//button[text()='SKIP']")).click();
		}
		
		else
		{
			System.out.println("Pop Not Displyed");
		}
			
	}
	
	public void superAdminloginValid()
	{
		driver.get(con.getUrl());
		TestUtil.pause(3);
		driver.findElement(By.name("email")).sendKeys(superAdminUserName);
		TestUtil.pause(3);
		driver.findElement(By.cssSelector("div#login-form>form>div>md-icon")).click();
		driver.findElement(By.name("password")).sendKeys(superAdminPassword);
		driver.findElement(By.xpath("//*[text()='LOG IN']")).click();
		TestUtil.pause(3);
		boolean status =  afetrloginPopUP();
		if(status)
		{
			TestUtil.pause(3);
			driver.findElement(By.xpath("//button[text()='SKIP']")).click();
		}
		
		else
		{
			System.out.println("Pop Not Displyed");
		}
			
	}
	
	public boolean afetrloginPopUP() {

		return driver.findElement(By.xpath("//*[text()[normalize-space()='Tour of GreenRFP']]")).isDisplayed();
	}
	
	public boolean changeUser()
	
	{
		System.out.println("Going to close userr");
		return driver.findElement(By.className("icon-window-close")).isDisplayed();
	}
	
	public void switchUser()
	{
		System.out.println("Start the execution");
		TestUtil.pause(3);
		boolean s = driver.findElement(By.xpath("//*[@class='header-close']//span[1]")).isDisplayed();
		System.out.println(s);
		TestUtil.pause(3);
		if(s)
		{
			TestUtil.pause(3);
			System.out.println("Going to close user");		
			jsClick(driver, driver.findElement(By.xpath("//*[@class='header-close']//span[1]")));
			jsClick(driver, driver.findElement(By.xpath("//button[text()='Yes']")));
		}
		
		else
		{
			System.out.println("No need to swtich to user");
		}
	}
	
	
	
	/**
	 * Get random numeric of given lenth.
	 * 
	 * @param length
	 *            desired length.
	 * @return
	 */
	// will return number between 1 and length (both inclusive)
	public static int randomNumericValueGenerate(int length) {

		Random randomGenerator = new Random();

		int randomInt = randomGenerator.nextInt(length);
		return (randomInt + 1);
	}
	
	public static int[] randomNumericValuesGenerate(int length, int count) {

		Random randomGenerator = new Random();

		int[] randomInts = randomGenerator.ints(1, (length + 1)).distinct().limit(count).toArray();
		return randomInts;
	}

	public static String randomStringGenerate() {

		int length = randomNumericValueGenerate(10);
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder( length );
		   for( int i = 0; i < length; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
	}
	
	public static String randomStringGenerate(int len) {

		int length = randomNumericValueGenerate(len);
		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();
		StringBuilder sb = new StringBuilder( length );
		   for( int i = 0; i < length; i++ ) 
		      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		   return sb.toString();
	}
	public static void type(WebDriver driver, WebElement element, String data) {
		element.clear();
		element.sendKeys(data);
	}
	public static void scrollToElementTillTrue(WebDriver driver, WebElement element)
	{
		TestUtil.pause(2);
		try {
		JavascriptExecutor executor=(JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);",element);
		TestUtil.pause(8);
		}catch(Exception e) {
			System.out.println("e");
		}
	}
	
	public static void enterDataIn(WebDriver driver, WebElement element, String search_phrase) {
		element.clear();
		element.sendKeys(search_phrase);
	}
	
	public static boolean invisibilityOfElementLocated(WebDriver driver, By locator, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			System.out.println(e);
			return false;
		}
	}
	
	public static boolean visibilityOfElementLocated(WebDriver driver, By locator, int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			System.out.println(e);
			return false;
		}
	}
	
	public void deleteCookies()
	{
		TestUtil.pause(3);
		driver.manage().deleteAllCookies();
		TestUtil.pause(5);
	}
	
	public static void SwitchtoTab(WebDriver driver,int tabNumber)
	 {
	  ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
	  driver.switchTo().window(tabs.get(tabNumber));
	 }
	
	/*
	////// For New 

	public String getProperties(String path, String key) {
		Properties propwrite = new Properties();
        String fileName = System.getProperty("user.dir") + "/"+path;

        try {
            InputStream input = new FileInputStream(fileName);
            propwrite.load(input);
            String value = propwrite.getProperty(key);
            input.close();
            return value.trim(); //  Return value after removing leading and trailing blank spaces.

        } catch (IOException e) {
            e.printStackTrace();
            return "Sorry, unable to find " + fileName;
        }
    }*/
	@FindBy(className = "icon-window-close") 
	WebElement remote_close_btn;
	public void closingSession()
	{
		if(isRemoteCloseIcon())
		{
			TestUtil.pause(3);
		}
		
		else
		{
			System.out.println("Close icon not present");
		}
	}
	
	public boolean isRemoteCloseIcon() {
		try {
			remote_close_btn.isDisplayed();
			System.out.println("Remore Close icon Displayed");
			return true;
		} catch (Exception e) {
			// Ignore
			
			return false;
		}
	}

}