Selenium.ClarityPPM
===================

Selenium smoke testing for ClarityPPM integration

currently set to run on aws sandbox with both versionone and clarity 
Hard coded IP needs to be changed in SeleniumTestHelper for it to work.
URL's can be split to point at two different servers if needed in the SeleniumTestHelper initialization

test hardcoded for andre agile, needs to exist as resource in both projects
test assume timebox(timesheet) is already created in clarity for todays date
 if run on non sandbox environments with large DB might need to add time to the delays (especially in runProjectSyncJob())
 runs on chrome (easily changed to firefox by commenting out chromedriver intialization)