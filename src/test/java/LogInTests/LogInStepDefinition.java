package LogInTests;

import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.qa.cv.Constants;
import com.qa.cv.LogInPage;
import com.qa.cv.ReportFile;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class LogInStepDefinition {

	WebDriver driver = null; 
	
	@Before
	public void setUp()
	{
		System.setProperty("webdriver.chrome.driver", Constants.CHROMEDRIVER);
		driver = new ChromeDriver(); 
		driver.manage().window().maximize();
		ReportFile.createReport();
	}
	
	@Given("^I'm at the Log In page$")
	public void i_m_at_the_Log_In_page() 
	{
	    driver.get(Constants.LOGINPAGE);
	    ReportFile.createTest("Testing for a Successfull Log In");
	}
	
	@When("^I Log In with the \"([^\"]*)\" and the \"([^\"]*)\"$")
	public void i_Log_In_with_the_and_the(String arg1, String arg2) 
	{
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReportFile.logStatusTest(LogStatus.INFO, "Attempting to log in using Username: " + arg1 + " and Password: " + arg2);
	    LogInPage logIn = PageFactory.initElements(driver, LogInPage.class);
	    logIn.logIn(arg1, arg2);
	}
		
	@Then("^I'm successfully logged in$")
	public void i_m_successfully_logged_in() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}

	@Then("^return the correct \"([^\"]*)\"$")
	public void return_the_correct(String arg1) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
	    throw new PendingException();
	}
	
	@After
	public void tearDown()
	{
		ReportFile.endTest();
		driver.quit();
	}
	
	@AfterClass
	public void close()
	{
		ReportFile.endReport();
	}
}
