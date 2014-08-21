package com.versionone.selenium;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTests {

//	// @Ignore
//	@Test
//	public void TestProjectSetup() throws Exception {
//
//		// Notice that the remainder of the code relies on the interface,
//
//		// not the implementation.
//		SeleniumTestHelper.setup();
//
//		// Login app
//		SeleniumTestHelper.loginApp();
//
//		// create a new project
//		int CID = SeleniumTestHelper.createProject();
//
//		// create a new project
//		SeleniumTestHelper.runProjectSyncJob("project");
//		String EID = SeleniumTestHelper.VerifyEID(CID);
//		System.out.println("Parent V1 EID: " + EID + ", Parent Clarity ID: " + CID);
//		Assert.assertTrue(EID.matches("E-\\d{5}"));
//
//		// close browser
//		SeleniumTestHelper.closeBrowser();
//
//	}

//	// @Ignore
//	@Test
//	public void TestWBS() throws Exception {
//
//		SeleniumTestHelper.setup();
//
//		// Login app
//		SeleniumTestHelper.loginApp();
//
//		// create a new project
//		int CID = SeleniumTestHelper.createProject();
//
//		// create a new project and get IDs
//		SeleniumTestHelper.runProjectSyncJob("project");
//
//		String EID = SeleniumTestHelper.VerifyEID(CID);
//		Assert.assertTrue(EID.matches("E-\\d{5}"));
//		String OID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic", EID);
//
//		// create child epic and sync it
//		String childWorkitemName = "Test Child Story";
//		V1TestHelper.createChildStory(SeleniumTestHelper.OURL, childWorkitemName, OID);
//		SeleniumTestHelper.runProjectSyncJob("project");
//
//		// open project task page and click "show all" button to see all tasks
//		SeleniumTestHelper.driver.get(SeleniumTestHelper.URL + "niku/nu#action:projmgr.keyTaskList&id=" + CID);
//
//		WebElement element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
//				.until(ExpectedConditions.presenceOfElementLocated(By.id("page_projmgr.odfKeyTaskList_collapseFilter_action_img")));
//
//		element.click();
//
//		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
//				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick*='clearExpression=true']")));
//
//		element.click();
//
//		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
//				.until(ExpectedConditions.presenceOfElementLocated(By.id("projmgr.taskProperties")));
//		// assert there is a link element titled "test child epic" verifying
//		// successful sync of child item back to clarity
//		Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("a[title*='" + childWorkitemName + "']")).size() > 0);
//		
//		// extra info
//		System.out.println("Parent V1 EID: " + EID + ", Parent Clarity ID: " + CID + ", V1 story Asset ID: " + OID);
//
//		// assert there is a link element titled "test child epic" verifying
//		// successful sync of child item back to clarity
//		Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("a[title*='" + childWorkitemName + "']")).size() > 0);
//
//	}

	// @Ignore
	@Test
	public void TestTimesheet() throws Exception {
		WebElement element;
		float workHours = 5;
		SeleniumTestHelper.setup();

		// Login app
		SeleniumTestHelper.loginApp();

		// create a new project
		int CID = SeleniumTestHelper.createProject();

		// create a new project and get IDs
		SeleniumTestHelper.runProjectSyncJob("project");

		SeleniumTestHelper.AddAndre(CID);

		// get the number of hours resource (andre) worked before actual
		String TURL = SeleniumTestHelper.driver.getCurrentUrl();
		float numHours = SeleniumTestHelper.getResourceHours();
		SeleniumTestHelper.driver.get(TURL);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// get EID and asset id for later use
		String EID = SeleniumTestHelper.VerifyEID(CID);
		
		//assert EID exists in correct form to verify successful project sync
		Assert.assertTrue(EID.matches("E-\\d{5}"));
		String POID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic",	EID);

		// create child epic and sync it
		String childWorkitemName = "Test Child Story";
		String OID = V1TestHelper.createChildStory(SeleniumTestHelper.OURL,	childWorkitemName, POID);
		SeleniumTestHelper.runProjectSyncJob("project");

		// open project task page and click "show all" button to see all tasks
		SeleniumTestHelper.driver.get(SeleniumTestHelper.URL + "niku/nu#action:projmgr.keyTaskList&id=" + CID);

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("page_projmgr.odfKeyTaskList_collapseFilter_action_img")));

		element.click();

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[onclick*='clearExpression=true']")));

		element.click();

		element = (new WebDriverWait(SeleniumTestHelper.driver, 10))
				.until(ExpectedConditions.presenceOfElementLocated(By.id("projmgr.taskProperties")));

		// assert there is a link element titled "test child epic" verifying
		// successful sync of child item back to clarity
		Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("a[title*='" + childWorkitemName + "']")).size() > 0);

		// Get member OID of andre to insert into actual api
		String memberOID = V1TestHelper.getAssetOIDByName(SeleniumTestHelper.OURL, "Member", "Andre Agile");

		// run actual api
		Date todayDate = V1DateParser.getShortDateFromStringWithTime(DateTime.now().toString());
		V1TestHelper.createActual(SeleniumTestHelper.OURL, OID, memberOID,
				V1DateParser.getShortDateStringFromDate(todayDate),	Float.toString(workHours));
		SeleniumTestHelper.runProjectSyncJob("timesheet");

		// get final total hours for andre
		float finalHours = SeleniumTestHelper.getResourceHours();

		// extra info
		System.out.println("Parent V1 EID: " + EID + ", Parent V! AssetID: " + POID + ", Parent Clarity ID: " + CID
				+ ", V1 story Asset ID: " + OID + ", Andres old hours: " + numHours + ", Andres new hours: " + finalHours);

		// assert total hours equal to begining hours plus actual
		Assert.assertTrue("params: " + finalHours + ", " + numHours + ", " + workHours, finalHours == numHours + workHours);

	}

}
