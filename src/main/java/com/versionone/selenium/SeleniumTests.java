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
				
		WebElement element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
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
		
		String EID =  SeleniumTestHelper.VerifyEID(CID);
		String OID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic", EID);
		
		//create child epic and sync it
		String childWorkitemName = "Test Child Story";
		OID = V1TestHelper.createChildStory(SeleniumTestHelper.OURL, childWorkitemName, OID);
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
		 V1TestHelper.createActual(SeleniumTestHelper.OURL, OID, memberOID, V1DateParser.getShortDateStringFromDate(todayDate), "4");
		 SeleniumTestHelper.runProjectSyncJob("timesheet");
		 
			SeleniumTestHelper.driver.get(SeleniumTestHelper.URL
					+ "niku/nu#action:timeadmin.timesheetBrowser&sortColumn=FULL_NAME&sortDirection=asc&filter_collapsed=false&ff_from_date=" + V1DateParser.getShortDateStringFromDate(todayDate) +"&ff_to_date="+ V1DateParser.getShortDateStringFromDate(todayDate));

			//TODO not working
			element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
					.presenceOfElementLocated(By.cssSelector("input[class='radio'][name='ff_date_type']")));
			
			element.click();
			
			element = (new WebDriverWait(SeleniumTestHelper.driver, 10)).until(ExpectedConditions
					.presenceOfElementLocated(By.cssSelector("button[name='applyFilter']")));
			
			element.click();
			
		 //TODO find the best way to verify how many hours -  one option is get text from cell before actual and assert  preactual+actual = postactual
		 Assert.assertTrue(SeleniumTestHelper.driver.findElements(By.cssSelector("td[column='14'][text='4']")).size()>0);
		 
		
	}

}
