package com.versionone.selenium;

import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Selenium {

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
	 * 
	 */
	private static void createProject() {

		WebElement myHomeMenu, element;

		myHomeMenu = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("ppm_nav_app_menu")));

		driver.get(URL
				+ "niku/nu#action:projmgr.projectNew&partition_code=NIKU.ROOT&from=Work");

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("name")));

		element.sendKeys("My V1 Project");

		element = driver.findElement(By.name("unique_code"));

		element.sendKeys("My_V1_ID" + DateTime.now().getMillis());

		element = driver.findElement(By
				.cssSelector("button[onclick*='retmode=properties']"));

		element.click();

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("agile_linked")));

		element.click();
	}

	/**
	 * 
	 */
	private static void createAStory() {

		WebElement element;

		element = driver.findElement(By
				.cssSelector("button[onclick*='action=projmgr.projectList']"));

		element.click();

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By
						.cssSelector("h1[title*='Projects']")));

		driver.get(URL
				+ "niku/nu#action:nmc.jobPropertiesNew&job_definition_id=5000028");

		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.name("remote_api_code")));

		element.sendKeys("VersionOne");

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

		// createa a new project
		createProject();
		// createa a new project
		createAStory();

		// createa a new story
		createAStory();

		// close browser
		// closeBrowser();

	}

}
