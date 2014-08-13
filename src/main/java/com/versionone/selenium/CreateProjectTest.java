package com.versionone.selenium;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateProjectTest {

	// Create a new instance of the Firefox driver
	public static WebDriver driver;

	public static final String URL = "http://54.224.15.94:8080/";

	/*
	 * settin up action for the test
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
	 *returns clarity project id 5 million number
	 */
	public static int createProject() {

		WebElement myHomeMenu, element;

		myHomeMenu = (new WebDriverWait(driver, 10)).until(ExpectedConditions
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
		return CID;
	}

	/**
	 *
	 */
	public static void createAStory() {

		WebElement element;

		element = driver.findElement(By
				.cssSelector("button[onclick*='action=projmgr.projectList']"));

		element.click();

		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By
						.cssSelector("h1[title*='Projects']")));

		driver.get(URL
				+ "niku/nu#action:nmc.jobPropertiesNew&job_definition_id=5000028");

		element = (new WebDriverWait(driver, 20)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("remote_api_code")));

		element.sendKeys("VersionOne");

		element = driver.findElement(By
				.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button"));

		element.click();
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

	public static void main(String[] args) {

		// Notice that the remainder of the code relies on the interface,
		// not the implementation.
		setup();

		// Login app
		loginApp();

		
		// create a new project
		int CID = createProject();

		// createa a new project
		createAStory();

		//todo - get the versionone ie-12345 id from clarity - in order to to that:
		//todo - get clarity projectid in order to use the number for direct link to project page where the E id is 
			//other option is put exact name in filter and clickresults
		// createa a new story
		//createAStory();

		// close browser
		// closeBrowser();

	}


//    // open | /niku/nu |
//    driver.get(baseUrl + "/niku/nu");
//    // type | id=ppm_login_username | v1user
//    driver.findElement(By.id("ppm_login_username")).clear();
//    driver.findElement(By.id("ppm_login_username")).sendKeys("v1user");
//    // type | id=ppm_login_password | v1user
//    driver.findElement(By.id("ppm_login_password")).clear();
//    driver.findElement(By.id("ppm_login_password")).sendKeys("v1user");
//    // click | id=ppm_login_button |
//    driver.findElement(By.id("ppm_login_button")).click();
//    // click | link=Projects |
//    driver.findElement(By.linkText("Projects")).click();
//    // click | xpath=(//button[@type='button'])[6] |
//    driver.findElement(By.xpath("(//button[@type='button'])[6]")).click();
//    // type | id=d8952e21 | newProject
//    driver.findElement(By.id("d8952e21")).clear();
//    driver.findElement(By.id("d8952e21")).sendKeys("newProject");
//    // type | id=d8952e28 | newId
//    driver.findElement(By.id("d8952e28")).clear();
//    driver.findElement(By.id("d8952e28")).sendKeys("newId");
//    // click | css=#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button. |
//    driver.findElement(By.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.")).click();
//    // click | id=d9093e330 |
//    driver.findElement(By.id("d9093e330")).click();
//    // click | xpath=(//button[@type='button'])[5] |
//    driver.findElement(By.xpath("(//button[@type='button'])[5]")).click();
//    // click | link=Reports and Jobs |
//    driver.findElement(By.linkText("Reports and Jobs")).click();
//    // click | xpath=(//button[@type='button'])[2] |
//    driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
//    // click | link=Jobs |
//    driver.findElement(By.linkText("Jobs")).click();
//    // click | id=nextPageButton |
//    driver.findElement(By.id("nextPageButton")).click();
//    // click | link=Remote Project Sync |
//    driver.findElement(By.linkText("Remote Project Sync")).click();
//    // select | id=d9753e117 | label=VersionOne
//    new Select(driver.findElement(By.id("d9753e117"))).selectByVisibleText("VersionOne");
//    // click | css=#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button |
//    driver.findElement(By.cssSelector("#ppm_workspace_bb > div.ppm_button_bar > button.ppm_button.button")).click();
//    // click | css=td.ppm_umenu_section > a[title="Projects"] |
//    driver.findElement(By.cssSelector("td.ppm_umenu_section > a[title=\"Projects\"]")).click();
//    // click | link=newProject |
//    driver.findElement(By.linkText("newProject")).click();
//    // click | link=Properties |
//    driver.findElement(By.linkText("Properties")).click();
//    // click | link=VersionOne |
//    driver.findElement(By.linkText("VersionOne")).click();


}
