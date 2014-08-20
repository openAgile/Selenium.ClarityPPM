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
	

	//private V1OAuthHttpClient httpClient;


	@Ignore
	@Test
	public  void  TestProjectSetup() throws Exception {

		// Notice that the remainder of the code relies on the interface,

		
		
				// not the implementation.
				SeleniumTestHelper.setup();
				

				// Login app
				SeleniumTestHelper.loginApp();

				
				// create a new project
				int CID = SeleniumTestHelper.createProject();
				
				// create a new project
				SeleniumTestHelper.runProjectSyncJob("project");
				String EID = SeleniumTestHelper.VerifyEID(CID);
				Assert.assertTrue(EID.matches("E-\\d{5}"));
				
				// close browser
				SeleniumTestHelper.closeBrowser();

	}
	
	@Ignore
	@Test
	public  void  TestWBS() throws Exception {

		
		SeleniumTestHelper.setup();

		// Login app
		SeleniumTestHelper.loginApp();

		// create a new project
		int CID = SeleniumTestHelper.createProject();
		
		// create a new project and get IDs
		SeleniumTestHelper.runProjectSyncJob("project");
		
		String EID =  SeleniumTestHelper.VerifyEID(CID);
		String OID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic", EID);
		
		//create child epic and sync it
		String childWorkitemName = "Test Child Story";
		V1TestHelper.createChildStory(SeleniumTestHelper.OURL, childWorkitemName, OID);
		SeleniumTestHelper.runProjectSyncJob("project");
		
		//open project task page and show all to see a;; tasks
		SeleniumTestHelper.driver.get(SeleniumTestHelper.URL
				+ "niku/nu#action:projmgr.keyTaskList&id=" + CID);
		
		WebElement element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("page_projmgr.odfKeyTaskList_collapseFilter_action_img")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='clearExpression=true']")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("projmgr.taskProperties")));
		
		//assert there is a link element titled "test child epic" veryfying successful sync of child item back to clarity
		Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("a[title*='"+childWorkitemName+"']")).size()>0);

		

	}
	
	//@Ignore
	@Test
	public  void TestTimesheet() throws Exception {
		WebElement element;
		float workHours = 4;
		SeleniumTestHelper.setup();

		// Login app
		SeleniumTestHelper.loginApp();

		// create a new project
			int CID = SeleniumTestHelper.createProject();
		
		// create a new project and get IDs
		SeleniumTestHelper.runProjectSyncJob("project");

		
		//open project team page and add andre agile
		SeleniumTestHelper.driver.get(SeleniumTestHelper.URL
				+ "niku/nu#action:projmgr.roster&id=" + CID);
				
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='projmgr.resourceObjectSelectionList']")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("input[title*='Agile, Andre']")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='union.closeAndRefreshParent']")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("a[title*='Agile, Andre']")));
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='npt.gridUpdate']")));
		
		element.click();
		//get the number of hours resource (andre) worked
		String TURL = SeleniumTestHelper.driver.getCurrentUrl();
		float numHours = SeleniumTestHelper.getResourceHours();
		SeleniumTestHelper.driver.get(TURL);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String EID =  SeleniumTestHelper.VerifyEID(CID);
		String POID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic", EID);
		
		//create child epic and sync it
		String childWorkitemName = "Test Child Story";
		String OID = V1TestHelper.createChildStory(SeleniumTestHelper.OURL, childWorkitemName, POID);
		SeleniumTestHelper.runProjectSyncJob("project");

		
		//open project task page and show all to see a;; tasks
		SeleniumTestHelper.driver.get(SeleniumTestHelper.URL
				+ "niku/nu#action:projmgr.keyTaskList&id=" + CID);
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("page_projmgr.odfKeyTaskList_collapseFilter_action_img")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.cssSelector("button[onclick*='clearExpression=true']")));
		
		element.click();
		
		element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
				.presenceOfElementLocated(By.id("projmgr.taskProperties")));
		
		//assert there is a link element titled "test child epic" veryfying successful sync of child item back to clarity
		Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("a[title*='"+childWorkitemName+"']")).size()>0);
		
		//Get member OID
		String memberOID = V1TestHelper.getAssetOIDByName(SeleniumTestHelper.OURL, "Member", "Andre Agile");

		Date todayDate = V1DateParser.getShortDateFromStringWithTime(DateTime.now().toString());
		 V1TestHelper.createActual(SeleniumTestHelper.OURL, OID, memberOID, V1DateParser.getShortDateStringFromDate(todayDate), Float.toString(workHours));
		 SeleniumTestHelper.runProjectSyncJob("timesheet");
		 
		 float finalHours = SeleniumTestHelper.getResourceHours();
		System.out.println("Parent V1 EID: " + EID + ", Parent V! AssetID: " + POID + ", Parent Clarity ID: " + CID + ", V1 story Asset ID: " + OID + ", Andres old hours: " + numHours + ", Andres new hours: " + finalHours);	
		 //TODO find the best way to verify how many hours -  one option is get text from cell before actual and assert  preactual+actual = postactual
		 Assert.assertTrue("params: " + finalHours +", "+ numHours +", "+ workHours ,finalHours == numHours + workHours);
		 
		
	}

}
