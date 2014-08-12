package com.versionone.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.versionone.selenium.CreateProjectTest;

public class TimeSheetTest {

	public static WebDriver driver;

	public static final String URL = "http://54.224.15.94:8080/";

	/*
	 * settin up action for the test
	 */
	public static void setup() {
		driver = new FirefoxDriver();
		driver.get(URL + "niku/nu");
	}


	public static void main(String[] args) {

		CreateProjectTest.loginApp();

		CreateProjectTest.createProject();

		CreateProjectTest.createAStory();


	}

}
