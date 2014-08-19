package com.versionone.selenium;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.versionone.integration.tests.V1TestHelper;
//import com.versionone.util.V1OAuthHttpClient;



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
				SeleniumTestHelper.runProjectSyncJob();
				String EID = SeleniumTestHelper.VerifyEID(CID);
				Assert.assertTrue(EID.matches("E-\\d{5}"));
				
				// close browser
				SeleniumTestHelper.closeBrowser();

	}
	
	@Ignore
	@Test
	public  String  TestWBS() throws Exception {

		
		SeleniumTestHelper.setup();

		// Login app
		SeleniumTestHelper.loginApp();

		// create a new project
		int CID = SeleniumTestHelper.createProject();
		
		// create a new project and get IDs
		SeleniumTestHelper.runProjectSyncJob();
		
		String EID =  SeleniumTestHelper.VerifyEID(CID);
		String OID = V1TestHelper.getAssetOID(SeleniumTestHelper.OURL, "Epic", EID);
		
		//create child epic and sync it
		String childWorkitemName = "Test Child Story";
		V1TestHelper.createChildStory(SeleniumTestHelper.OURL, childWorkitemName, OID);
		SeleniumTestHelper.runProjectSyncJob();
		
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

		return OID;

	}
	
	//@Ignore
	@Test
	public  void TestTimesheet() throws Exception {

		String OID = TestWBS();
		V1TestHelper.createActual(SeleniumTestHelper.OURL, OID, "8/19/2014", "4");
		
	}

}
