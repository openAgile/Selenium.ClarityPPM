package com.versionone.selenium;

import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ca.clarity.avee.api.InterfaceSettings;
//import com.versionone.util.V1GrantTool;


public class SeleniumTestHelper {

	// Create a new instance of the Firefox driver
		public static WebDriver driver;
		
		//need to change IP to fit box being tested
		public static final String baseURL =  "http://54.224.15.94";
		//clarity url (if on same machine)
		public static final String URL = baseURL +":8080/";
		//versionone url (if on same machine)
		public static final String OURL = baseURL +"/versionone";

		
		/*
		 * Setting up action for the test
		 */
		public static void setup() {

			System.setProperty("webdriver.chrome.driver", "C:\\Dev\\selenium-2.42.2\\chromedriver.exe");
		    // To remove message "You are using an unsupported command-line flag: --ignore-certificate-errors.
		    // Stability and security will suffer."

		    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		    ChromeOptions options = new ChromeOptions();
		    options.addArguments("test-type");
		    capabilities.setCapability("chrome.binary","C:\\Dev\\selenium-2.42.2\\chromedriver.exe");
		    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(capabilities);
			//for firefox
			//driver = new FirefoxDriver();
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
	 *for testing purposes
	 *	 */
	public static int createProject(int CID) {
		return CID;
		
	}
	/**
	 *Creates Clarity Project and returns clarity project id 5 million number
	 */
	public static int createProject() {

		WebElement  element;

		//element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.id("ppm_nav_app_menu")));

		driver.get(URL
				+ "niku/nu#action:projmgr.projectNew&partition_code=NIKU.ROOT&from=Work");

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("name")));

		element.sendKeys("My V1 Projectnw");

		element = driver.findElement(By.name("unique_code"));
		
		element.sendKeys("My_V1_ID" + DateTime.now().getMillis());


		element = (new WebDriverWait(driver, 30)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='retmode=properties']")));

		element.click();
		
		
	
		//verify save worked by checking for "linked to agile" checkbox
		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("agile_linked")));
		
		//get clarity 5000000 by parsing url
		int CID = Integer.parseInt(driver.getCurrentUrl().replaceFirst(URL, "").substring(44));
		
		
		element.click();
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='action=projmgr.projectList']")));


		element.click();
		//verify return to projects page
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("h1[title*='Projects']")));
		return CID;
	}

	
	/**
	 *Given  Clarity ID (5000000 number) verifies V1 sync and  returns V1 EID
	 */
	public static String VerifyEID(int CID) {
		
		WebElement element;
		driver.get(URL
				+ "niku/nu#action:projmgr.projectProperties&odf_view=project.versionone&id=" + CID + "&odf_pk=" + CID);
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.xpath("//div[4]/table/tbody/tr/td/table/tbody/tr/td[2]/span")));
		String Eid = element.getText().toString();
		
		
		return Eid;
	
	}
	/**
	 *runs Clarity remote project sync and returns V1 EID
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
		// open specific job page
		driver.get(URL
				+ "niku/nu#action:nmc.jobPropertiesNew&job_definition_id="+id);

		
		//enter versionone in parameter and click run
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("remote_api_code")));

		element.sendKeys("VersionOne");

		element = driver.findElement(By
				.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button"));

		element.click();
		
		//verify return to Scheduled Jobs screen
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
	 *runs using todays date for timesheet search
	 * @throws ParseException 
	 */
	public static float getResourceHours(String name) throws ParseException {
		
		WebElement element;
		
		if (name==null||name=="")
			name = "Agile, Andre";
		
		//browse to timesheets with todays date prepopulated
		Date todayDate = V1DateParser.getShortDateFromStringWithTime(DateTime.now().toString());
		SeleniumTestHelper.driver
				.get(SeleniumTestHelper.URL
						+ "niku/nu#action:timeadmin.timesheetBrowser&sortColumn=FULL_NAME&sortDirection=asc&filter_collapsed=false&ff_from_date="
						+ V1DateParser.getShortDateStringFromDate(todayDate)
						+ "&ff_to_date="
						+ V1DateParser.getShortDateStringFromDate(todayDate));
		
		// click custom date range for todays date
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("input[class='radio'][value='userdefined']")));

		element.click();
		
		//enter resource name
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector("input[name='ff_res_name']")));

		element.sendKeys(name);
		
		//apply filter
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("Button[name='applyFilter']")));

		element.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//check total resource hours
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("td[column*='14']")));

		float numHours = Float.valueOf(element.getText());
		return numHours;
		
	}
	
	/**
	 * overload with andre's name
	 */
	public static float getResourceHours() throws ParseException {
		return getResourceHours("Agile, Andre");
	}
	
	
	/**
	 * open project team page and add andre agile - clicks throough screens to add him
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
	
	/**
	 * C-l-o-s-e-   T-h-e   B-r-o-w-s-e-r
	 */
	public static void closeBrowser() {
		// Close the browser
		driver.quit();

	}
}