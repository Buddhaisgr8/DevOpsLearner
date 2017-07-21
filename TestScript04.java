/*Script for Day 4
 */




import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * 
 * @author Krishna Rungta 
 * The Test Script 04:
 * Verify the Login Section. 
 * The script uses parameterization to verify more test cases. 
 * Parameterization using TestNG
 * 
 */
public class TestScript04 extends Util {

	 WebDriver driver; // Selenium control driver
	 String baseUrl; // baseUrl of website Guru99

	/**
	 * create test data for testing The test data include set of username,
	 * password
	 * 
	 * @return
	 */
	@DataProvider(name = "GuruTest")
	public Object[][] testData() throws Exception {
		System.out.println();
		return Util.getDataFromExcel(Util.FILE_PATH, Util.SHEET_NAME,
				Util.TABLE_NAME);
	}

	/**
	 * Before Testing Setup test environment before executing test
	 * 
	 * @throws Exception
	 * 
	 */
	@BeforeMethod
	public void setUp() throws Exception {

		driver = new FirefoxDriver();

		// Setting Base URL of website Guru99
		baseUrl = Util.BASE_URL;
		driver.manage().timeouts()
				.implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);
		// Go to http://www.demo.guru99.com/V4/
		driver.get(baseUrl + "/V4/");
	}

	/**
	 * Above test script executed several times for each set of data used in @DataProvider
	 * annotation. Any failed test does not impact other set of execution.
	 * 
	 * SS1: Enter valid userid & password 
	 * Expected: Login successful home page shown 
	 * SS2: Enter invalid userid & valid password 
	 * SS3: Enter valid userid & invalid password 
	 * SS4: Enter invalid userid & invalid password 
	 * Expected:
	 * A pop-up “User or Password is not valid” is shown
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */

	@Test(dataProvider = "GuruTest")
	public void testCase04(String username, String password) throws Exception {
		String actualTitle;
		
		String actualBoxMsg;
		driver.findElement(By.name("uid")).clear();
		driver.findElement(By.name("uid")).sendKeys(username);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.name("btnLogin")).click();

		// delay some seconds
		// Use this statement if your internet speed is slow
		// driver.manage().timeouts()
		// .implicitlyWait(Util.WAIT_TIME, TimeUnit.SECONDS);

		/* Determine Pass Fail Status of the Script
         * If login credentials are correct,  Alert(Pop up) is NOT present. An Exception is thrown and code in catch block is executed	
         * If login credentials are invalid, Alert is present. Code in try block is executed 	    
         */
	    try{ 
	    
	       	Alert alt = driver.switchTo().alert();
			actualBoxMsg = alt.getText(); // get content of the Alter Message
			alt.accept();
			 // Compare Error Text with Expected Error Value					
			assertEquals(actualBoxMsg,Util.EXPECT_ERROR);
			
		}    
	    
    	
	    catch (NoAlertPresentException Ex){ 
	    	actualTitle = driver.getTitle();
			// On Successful login compare Actual Page Title with Expected Title
	    	WebElement MgrId=driver.findElement(By.xpath("//td[text()='Manger Id : mngr87157']"));
	    	System.out.println(MgrId.getText());
	    	
	    	if(actualTitle.contains(Util.EXPECT_TITLE) &&  Util.Manager_ID.contains(MgrId.getText())) {
	    		   //Convert web driver object to TakeScreenshot
	    		TakesScreenshot scrShot =((TakesScreenshot)driver);
	    		// Take screenshot and store as a file format
	    		File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
	    		File dstFile= new File("C://Selenium material//Banking Project//ScreenDir");
	    		
	    		// now copy the  screenshot to desired location using copyFile //method
	    		FileUtils.copyFileToDirectory(SrcFile, dstFile);
	    		
	    		System.out.println("Pass");
	    			    	}
	    	else {
	    		System.out.println("Failed");
	    	}
	    	
        } 
	}

	/**
	 * Complete the testing
	 * 
	 * @throws Exception
	 */
	@AfterMethod
	public void tearDown() throws Exception {
		driver.quit();
	}
}