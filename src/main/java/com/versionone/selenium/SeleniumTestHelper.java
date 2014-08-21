package com.versionone.selenium;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ca.clarity.avee.api.InterfaceSettings;
//import com.versionone.util.V1GrantTool;


public class SeleniumTestHelper {

	// Create a new instance of the Firefox driver
		public static WebDriver driver;

		public static final String baseURL =  "http://54.224.15.94";
		public static final String URL = baseURL +":8080/";
		public static final String OURL = baseURL +"/versionone";
		//private static InterfaceSettings settings;
		
		/*
		 * Setting up action for the test
		 */
		public static void setup() {
			
			driver = new FirefoxDriver();
			driver.get(URL + "niku/nu");
						
		}
		
		
	/*
	 * login application
	 */
	public static void loginApp() {

		WebElement loginButton, userName, passWord;

		loginButton = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("ppm_login_button")));

		// Find the text input element by its name
		userName = driver.findElement(By.name("userName"));

		// Enter something to search for
		userName.sendKeys("v1user");
		// Find the text input element by its name

		passWord = driver.findElement(By.name("passWord"));
		// // Enter something to search for
		passWord.sendKeys("v1user");
		//
		// // Now submit the form. WebDriver will find the form for us from the
		// element
		// element = driver.findElement(By.id("ppm_login_button"));
		loginButton.click();

	}
	/**
	 *Mocks Clarity Project and returns clarity project id 5 million number given
	 */
	public static int createProject(int CID) {
		return CID;
		
	}
	/**
	 *Creates Clarity Project and returns clarity project id 5 million number
	 */
	public static int createProject() {

		WebElement  element;

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("ppm_nav_app_menu")));

		driver.get(URL
				+ "niku/nu#action:projmgr.projectNew&partition_code=NIKU.ROOT&from=Work");

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("name")));

		element.sendKeys("My V1 Projectnw");

		element = driver.findElement(By.name("unique_code"));
		
		element.sendKeys("My_V1_ID" + DateTime.now().getMillis());


		element = (new WebDriverWait(driver, 30)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='retmode=properties']")));

//		element = driver.findElement(By
//				.cssSelector("button[onclick*='retmode=properties']"));
//
		element.click();
		
		
	

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("agile_linked")));
		int CID = Integer.parseInt(driver.getCurrentUrl().replaceFirst(URL, "").substring(44));
		element.click();
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='action=projmgr.projectList']")));


		element.click();
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("h1[title*='Projects']")));
		return CID;
	}

	
	/**
	 *Given  Clarity ID (5000000 number) verifies V1 sync and  returns V1 EID
	 */
	public static String VerifyEID(int CID) {
		
		WebElement element;
		int Cid = CID;
		driver.get(URL
				+ "niku/nu#action:projmgr.projectProperties&odf_view=project.versionone&id=" + Cid + "&odf_pk=" + Cid);
		
		//element = (new WebDriverWait(driver, 20)).until(ExpectedConditions.presenceOfElementLocated(By.className("ppm_read_only_value")));
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//div[4]/table/tbody/tr/td/table/tbody/tr/td[2]/span")));
		String Eid = element.getText().toString();
		
		
		return Eid;
	
	}
	/**
	 *mock for Clarity remote project sync and returns V1 EID
	 *accepts job type  parameters:
	 *"project" for remote project sync
	 *"timesheet" for remote timesheet sync
	 */

	public static void runProjectSyncJob(String type) {

		WebElement element;
		String id=type;
		long delay=8000;
		if (type.toLowerCase() == "project"){
			id="5000028";
			delay = 10000;
			}			
		else if (type.toLowerCase() == "timesheet"){
			id = "5000029";
			delay = 15000;
		}

		driver.get(URL
				+ "niku/nu#action:nmc.jobPropertiesNew&job_definition_id="+id);

		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("remote_api_code")));

		element.sendKeys("VersionOne");

		element = driver.findElement(By
				.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button"));

		element.click();
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("h1[title*='Scheduled Jobs']")));
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
	}

	/**
	 *Gets total hours for clarity user for this weeks timesheet
	 *accepts resource name  parameter, defaults to "Agile, Andre" if none given
	 * @throws ParseException 
	 */
	public static float getResourceHours(String name) throws ParseException {
		
		WebElement element;
		if (name==null||name=="")
			name = "Agile, Andre";
		Date todayDate = V1DateParser.getShortDateFromStringWithTime(DateTime.now().toString());
		SeleniumTestHelper.driver
				.get(SeleniumTestHelper.URL
						+ "niku/nu#action:timeadmin.timesheetBrowser&sortColumn=FULL_NAME&sortDirection=asc&filter_collapsed=false&ff_from_date="
						+ V1DateParser.getShortDateStringFromDate(todayDate)
						+ "&ff_to_date="
						+ V1DateParser.getShortDateStringFromDate(todayDate));

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("input[class='radio'][value='userdefined']")));

		element.click();

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("input[name='ff_res_name']")));

		element.sendKeys(name);

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("Button[name='applyFilter']")));

		element.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[column*='14']")));

		float numHours = Float.valueOf(element.getText());
		return numHours;
		
	}
	
	public static float getResourceHours() throws ParseException {
		return getResourceHours("Agile, Andre");
	}
	
	
	/**
	 * open project team page and add andre agile
	 */
	public static void AddAndre(int CID) throws ParseException {
		WebElement element;
	
	SeleniumTestHelper.driver.get(SeleniumTestHelper.URL + "niku/nu#action:projmgr.roster&id=" + CID);

	element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick*='projmgr.resourceObjectSelectionList']")));

	element.click();

	element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[title*='Agile, Andre']")));

	element.click();

	element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick*='union.closeAndRefreshParent']")));

	element.click();

	element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[title*='Agile, Andre']")));

	// save addition
	element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
			.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick*='npt.gridUpdate']")));

	element.click();
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	}
	
	public static void closeBrowser() {
		// Close the browser
		driver.quit();

	}
}