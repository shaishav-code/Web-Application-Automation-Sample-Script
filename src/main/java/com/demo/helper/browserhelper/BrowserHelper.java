
package com.demo.helper.browserhelper;

import java.util.LinkedList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class BrowserHelper{

	private WebDriver driver;
	private Logger Log = Logger.getLogger(BrowserHelper.class);

	public BrowserHelper(WebDriver driver) {
		this.driver = driver;
		Log.debug("BrowserHelper : " + this.driver.hashCode());
	}

	public void goBack() {
		driver.navigate().back();
		Log.info("Navigate Back");
	}

	public void goForward() {
		driver.navigate().forward();
		Log.info("Navigate Forward");
	}

	public void refresh() {
		driver.navigate().refresh();
		Log.info("Refresh the url");
	}

	public Set<String> getWindowHandlens() {
		Log.info("");
		return driver.getWindowHandles();
	}

	public void SwitchToWindow(int index) {

		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());

		if (index < 0 || index > windowsId.size()){
			throw new IllegalArgumentException("Invalid Index : " + index);
		}
		driver.switchTo().window(windowsId.get(index));
		Log.info(index);
	}

	public void switchToParentWindow() {
		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());
		driver.switchTo().window(windowsId.get(0));
		Log.info("Switch to Parent window");
	}

	public void switchToParentWithChildClose() {
		LinkedList<String> windowsId = new LinkedList<String>(getWindowHandlens());

		for (int i = 1; i < windowsId.size(); i++) {
			Log.info(windowsId.get(i));
			driver.switchTo().window(windowsId.get(i));
			driver.close();
		}

		switchToParentWindow();
	}
	

	
	public void switchToFrame(String nameOrId) {
		driver.switchTo().frame(nameOrId);
		Log.info(nameOrId);
	}
	
	public void navigateTO(String url)
	{
		driver.navigate().to(url);
		Log.info("Navigate to the url ");
	}

}
