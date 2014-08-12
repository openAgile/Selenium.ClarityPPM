package com.versionone.selenium;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.ca.clarity.avee.api.InterfaceSettings;
import com.versionone.selenium.V1Exception;
import com.versionone.selenium.V1GrantTool;

@RunWith(Suite.class)

//NOTE: Add classes here to include in integration test run using Maven "verify" goal.
//@Suite.SuiteClasses({
//	ProjectSync.class,
//	WBSSync.class,
//	TimesheetSync.class
//})

/**
 * Checks the environment variable to determine which V1 test instance to run the integration tests against.
 */
public class ClaritySuiteIT {

	private static String instanceUrl;
	private static InterfaceSettings settings;

	@BeforeClass
	public static void beforeRun() throws ClientProtocolException, IOException, JDOMException, ParseException, OAuthSystemException, OAuthProblemException, JSONException, V1Exception {

		System.out.println("\n*** Beginning Integration Test Run ***\n");

		//Read the instance URL from the properties file in current directory.
		Properties properties;
		try (FileReader reader = new FileReader("EnvFile.properties")) {
			properties = new Properties();
			properties.load(reader);
			instanceUrl = properties.getProperty("V1_TEST_INSTANCE");
			System.out.println("TARGET INSTANCE: " + instanceUrl + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Check that we have an instance URL.
		if (null == instanceUrl) {
			String message = "No instance URL found. Ending integration test run.";
			System.out.println(message);
			throw new RuntimeException(message);
		}

		//Inject the configurations.
		System.out.println("Injecting the configurations...");
		V1TestHelper.injectConfigurations(instanceUrl);

		//Inject the schedule.
		System.out.println("Injecting the schedule...");
		V1TestHelper.injectSchedule(instanceUrl);

		//Inject the admin email address.
		System.out.println("Injecting the admin email address..");
		V1TestHelper.injectAdminEmailAddress(instanceUrl);

		//Inject the OAuth credentials.
		System.out.println("Injecting the OAuth credentials...");
		settings = V1GrantTool.createOAuthCredsInterfaceSettings(instanceUrl, "admin", "admin");
		System.out.println(settings.toString() + "\n");

	}

	@AfterClass
	public static void afterRun() {
		System.out.println("\n*** Ending Integration Test Run ***\n");
	}

	public static InterfaceSettings getInterfaceSettings() {
		return settings;
	}

}
