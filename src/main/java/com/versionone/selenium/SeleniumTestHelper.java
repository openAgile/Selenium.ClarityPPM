package com.versionone.selenium;

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

		public static final String URL = "http://54.224.15.94:8080/";
		public static final String OURL = "http://54.224.15.94/versionone";
		//private static InterfaceSettings settings;
		
		/*
		 * settin up action for the test
		 */
		public static void setup() {
			
			driver = new FirefoxDriver();
			driver.get(URL + "niku/nu");
						
		}
		/*
		 * settin up action for the test
		 */
//		public static InterfaceSettings setOAuth() throws Exception {
//		//Inject the OAuth credentials.
//			
//				System.out.println("Injecting the OAuth credentials...");
//				settings = V1GrantTool.createOAuthCredsInterfaceSettings(OURL, "admin", "admin");
//				System.out.println(settings.toString() + "\n");
//				return settings;
//		}
		
		
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
	 *Runs Clarity remote project sync and returns V1 EID
	 */
	public static void runProjectSyncJob() {

		WebElement element;


		driver.get(URL
				+ "niku/nu#action:nmc.jobPropertiesNew&job_definition_id=5000028");

		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("remote_api_code")));

		element.sendKeys("VersionOne");

		element = driver.findElement(By
				.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button"));

		element.click();
		
		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("h1[title*='Scheduled Jobs']")));
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	    //driver.findElement(By.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button")).click();
	    // click | css=td.ppm_umenu_section > a[title="Projects"] |
	   // driver.findElement(By.cssSelector("td.ppm_umenu_section > a[title=\"Projects\"]")).click();


		// element = (new WebDriverWait(driver, 10))
		// .until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[onclick*='nmc.addSchedulerProperties']")));

		// element.click();

		// element = (new WebDriverWait(driver, 10))
		// .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1[title*='Jobs: Scheduled Jobs']")));

		// WebElement myNewButton = (new WebDriverWait(driver, 10))
		// .until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("New")));
		// myNewButton.click();
	}


	public static void closeBrowser() {
		// Close the browser
		driver.quit();

	}
}