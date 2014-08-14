package com.versionone.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//import com.thoughtworks.selenium.webdriven.commands.CreateCookie;
//import com.versionone.selenium.*;


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

		int CID = CreateProjectTest.createProject();

		CreateProjectTest.createAStory(CID);
		
//		RemoteProjectImpl project = createNewProject("WBS TEST CASE 4: Returns single epic workitem");
//		V1HttpClient httpClient = new V1HttpClient();
//		V1TestHelper.createChildStory(httpClient, "story", parentOID)
//		//createChildEpic(V1OAuthHttpClient httpClient, String name, String parentOID, String startDate, String endDate) throws UnsupportedEncodingException, OAuthSystemException, OAuthProblemException, IOException, V1Exception, JDOMException {
//
//		String childWorkitemName = "Test Child Epic";
//		V1TestHelper.createChildEpic(remoteAPI.getOAuthHTTPClient(), childWorkitemName, project.getAssetOid(), "", "");

	}

}
