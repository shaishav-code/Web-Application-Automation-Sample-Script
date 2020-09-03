package com.demo.login.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.demo.helper.browserhelper.BrowserHelper;
import com.demo.pages.LoginPage;
import com.demo.qa.base.BaseClass;
import com.demo.utlility.TestUtil;
import com.demo.utlility.XLUtils;

public class LoginPageTest extends BaseClass{
	public String exceptedTitle = "Green RFP";
	LoginPage loginPage;
	TestUtil testutil = new TestUtil();
	XLUtils readData = new XLUtils();
	int numOfFailure = 0;
	int stepCount = 1;
	String dataFilePath =  System.getProperty("user.dir") + "/src/main/java/com/demo/testdata/login.xlsx";
	String diasableActualColor = "#000000";
	String enableActualColor = "#ffffff";
	String exceptedUrl = "https://uat.thegreenrfp.com/myaccount/dashboard";
	
	String invalidUsername =  XLUtils.getCellValue(dataFilePath,"Sheet1",1,0);
	 String correcrPassword = XLUtils.getCellValue(dataFilePath,"Sheet1",1,1);
	 String validUsername = XLUtils.getCellValue(dataFilePath,"Sheet1",2,0);
	 String incorrectPassword = XLUtils.getCellValue(dataFilePath,"Sheet1",2,1);
	 
	 
	@Test(priority=1)
	public void verifyTitle() throws IOException
	{
		 System.out.println("Invalid Username is: "+invalidUsername);
		 System.out.println("Correct password: "+correcrPassword);
		 System.out.println("Valid Username is: "+validUsername);
		 System.out.println("InCorrect password: "+incorrectPassword);
		 
		loginPage = new LoginPage(driver);
		waitForPageLoaded(driver);
		TestUtil.pause(5);
		String title=loginPage.varifyPageTitle();
		if(title.equals(exceptedTitle))
		{
			Assert.assertTrue(true);
		logger.info("Login test passed");
	}
	else
	{
		add_screenShot("verifyTitle");
		Assert.assertTrue(false);
		logger.info("Login test failed");
	}
		
	}
	@Test(priority=2)
	public void loginWithoutEnteringPassword() throws IOException
	{
		testStepsLog("Step " + (stepCount++) + " : Perform login without entering password and Verify Log In Button is Disabled");
		loginPage.enterEmail(userName);
		logger.info("Entred Valid Username");
		loginPage.clickOnArrow();
		logger.info("Click On Arrow");
		loginPage.verifyLoginButtonDisabled();
		if(loginPage.verifyLoginButtonDisabled())
		{
			Assert.assertTrue(true);
			logger.info("Verify Log in Button Disabled test passed");
		}
		else {
		add_screenShot("verifyLoginButtonDisabled");
		Assert.assertTrue(false);
		logger.info("Verify Log in Button Disabled test Failed");
		}
	}
	
	@Test(priority=3)
	public void verifyDisableLoginButtonColor()
	{
		testStepsLog("Step " + (stepCount++) + " : Verify button of color of Log in button when it in diseable state");
		TestUtil.pause(2);
		String actualColor = loginPage.verifyColorOfLogInButton();
		logger.info("Capture Login button Color");
		if(actualColor.equals(diasableActualColor))
		{
			Assert.assertTrue(true);
			logger.info("Log in Button is in diseable state and color is also matched");
		}
		else
		{
			Assert.assertTrue(false);
			logger.info("Log in Button is in diseable state and color is not matched");
		}
	}
	
	@Test(priority=4)
	public void verifyenbleLoginButtonColor()
	{
		testStepsLog("Step " + (stepCount++) + " : Verify button of color of Log in button when it in enable state");
		TestUtil.pause(2);
		loginPage.enterPassword(correcrPassword);
		
		TestUtil.pause(2);
		String actualColor = loginPage.verifyColorOfLogInButton();
		logger.info("Capture Login button Color");
		System.out.println(actualColor);
		if(actualColor.equals(enableActualColor))
		{
			Assert.assertTrue(true);
			logger.info("Log in Button is in enable state and color is also matched");
		}
		else
		{
			Assert.assertTrue(false);
			logger.info("Log in Button is in enable state and color is not matched");
		}
	}
	//Invalid id invalid password
	@Test(priority=5)
		public void loginByEnteringInvalidUserId()
		{
			testStepsLog("Step " + (stepCount++) + " : Perform login with invalid ID and Verify error message.");			
			loginPage.enterEmail(invalidUsername);
			logger.info("Entered invalid user id");
			loginPage.enterPassword(correcrPassword);
			logger.info("Entered Valid Password");
			loginPage.clickOnLogin();
			logger.info("Click on Login");
			boolean errorMessage= loginPage.verifyErrorMessage("No User Found.");
			Assert.assertTrue(errorMessage,"Error message not displayed");			
		}
	//valid id invalid password
	@Test(priority=6)
	public void loginByEnteringInvalidPassword()
	{	
				testStepsLog("Step " + (stepCount++) + " : Perform login with invalid password and Verify error message.");			
				loginPage.enterEmail(validUsername);
				logger.info("Entered valid user id");
				loginPage.enterPassword(incorrectPassword);
				logger.info("Entered invalid Password");
				loginPage.clickOnLogin();	
				logger.info("Click on Login");
				TestUtil.pause(2);
				boolean errorMessage= loginPage.verifyErrorMessage("Credentials do not match.");
				Assert.assertTrue(errorMessage,"Credentials do not match.");
	}
	//Login by entering blank password
	@Test(priority=7,alwaysRun=true)
	public void loginByWithoutEnteringPassword()
	{	
				testStepsLog("Step " + (stepCount++) + " : Perform login by entering blank password and Verify error message.");			
				loginPage.enterEmail(validUsername);
				logger.info("Entered Valid User name");
				loginPage.enterPassword(" ");
				logger.info("Entered Blank Password");
				//loginPage.clickOnLogin();	
				TestUtil.pause(3);
				boolean errorMessage= loginPage.verifyErrorMessage("Password field is required");
				Assert.assertTrue(errorMessage,"Error message not displayed");
	}

	@Test(priority=8,alwaysRun=true)
	public void validLogin() throws IOException
	{
		testStepsLog("Step " + (stepCount++) + " : Perform login using valid Input.");
		TestUtil.pause(2);
		BrowserHelper browerHelper = new BrowserHelper(driver);
		browerHelper.refresh();
		loginPage.enterEmail(userName);
		logger.info("Entred Valid Username");
		loginPage.clickOnArrow();
		logger.info("Click On Arrow");
		loginPage.enterPassword(passWord);
		logger.info("Enter valid password");
		TestUtil.pause(2);
		loginPage.clickOnLogin();
		logger.info("Click on Login");
		TestUtil.pause(5);
		if(driver.getCurrentUrl().equals(exceptedUrl))
		{
			Assert.assertTrue(true);
			logger.info("Login successfull");
		}
		else
		{
			add_screenShot("loginfalied");
			Assert.assertTrue(false);
			logger.info("Login Not successfully");
		}
	}

}
