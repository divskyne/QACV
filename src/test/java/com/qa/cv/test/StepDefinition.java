package com.qa.cv.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import com.qa.cv.Constants;
import com.qa.cv.ReportFile;
import com.qa.cv.pages.AddUserPage;
import com.qa.cv.pages.LoginPage;
import com.qa.cv.pages.UserPage;
import com.relevantcodes.extentreports.LogStatus;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition {
	
	static WebDriver driver = null;
	
	@BeforeClass
	public static void beforeClass() {
		
		ReportFile.createReport();
	}
	
	@Before	
	public void setup() {	
		
		System.setProperty(Constants.DRIVERNAME, Constants.DRIVERPATH);		
		driver = new ChromeDriver();
	}
	
	@After
	public void teardown() {
		
		driver.quit();
	}
	
	@AfterClass
	public static void afterClass() {
		
		ReportFile.endReport();
	}

	@Given("^a user is on the add user page$")
	public void a_user_is_on_the_add_user_page() {
	    
		ReportFile.createTest("Test: Create user");

		driver.get(Constants.ADDUSERPAGE);
	
		ReportFile.logStatusTest(LogStatus.INFO, "Browser opened and navigated to the login page");
	}

	@When("^name \"([^\"]*)\", email \"([^\"]*)\", password \"([^\"]*)\" are entered$")
	public void name_email_password_are_entered(String arg1, String arg2, String arg3) {
	    	
		AddUserPage addUserPage = PageFactory.initElements(driver, AddUserPage.class);
		addUserPage.enterDetails(arg1, arg2, arg3);
	
		ReportFile.logStatusTest(LogStatus.INFO, "User details entered");
	}

	@When("^role \"([^\"]*)\" is selected$")
	public void role_is_selected(String arg1) {
	    
		AddUserPage addUserPage = PageFactory.initElements(driver, AddUserPage.class);
		addUserPage.enterRole(arg1);

		ReportFile.logStatusTest(LogStatus.INFO, "User role selected");

		addUserPage.enterRole(arg1);

		ReportFile.logStatusTest(LogStatus.INFO, "Details submitted");
	}

//	@Then("^the user is created$")
//	public void the_user_is_created() {
//	    // Write code here that turns the phrase above into concrete actions
//	}

	@Given("^a user is on the login page$")
	public void a_user_is_on_the_login_page() {

		ReportFile.createTest("Test: Login");
		
		driver.get(Constants.LOGINPAGE);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Browser opened and navigated to the login page");
	}

	@When("^email \"([^\"]*)\" and password \"([^\"]*)\" are entered$")
	public void email_and_password_are_entered(String arg1, String arg2) {
	    
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.login(arg1, arg2);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Login details submitted and login button clicked");
	}

	@Then("^the user is taken to the page \"([^\"]*)\"$")
	public void the_user_is_taken_to_the_page(String arg1) {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);		
		String page = userPage.getHeaderText();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Navigated to page " + page);
	
		if(page.equals(arg1)) {
			ReportFile.logStatusTest(LogStatus.PASS, "Navigated to correct page");
		}
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Did not navigate to correct page");
		}
		
		assertEquals(arg1, page);
	}

	@Given("^a user is logged in$")
	public void a_user_is_logged_in() {

		ReportFile.createTest("Test: Logout");

		driver.get(Constants.TRAININGMANAGERPAGE);

		ReportFile.logStatusTest(LogStatus.INFO, "Browser opened and navigated to a user page");
	}

	@When("^logout is clicked$")
	public void logout_is_clicked() {
		
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.logout();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Logout button clicked");
	}

	@Then("^the user is taken to the login page$")
	public void the_user_is_taken_to_the_login_page() {
		
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		
		if(loginPage.getHeaderText() == "Login") {		
			ReportFile.logStatusTest(LogStatus.PASS, "Logged out successfully");
		}
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Could not log out");
		}
		
		assertEquals("Login", loginPage.getHeaderText());
	}

	@When("^contact is clicked$")
	public void contact_is_clicked() {
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.goToContact();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Contact button clicked");
	}

//	@Then("^the user is taken to the contact page$")
//	public void the_user_is_taken_to_the_contact_page() {
//	    // Write code here that turns the phrase above into concrete actions
//	}

	@Given("^a trainee is logged in$")
	public void a_trainee_is_logged_in() {
		
		ReportFile.createTest("Test: Upload CV");
		ReportFile.createTest("Test: Delete CV");

		driver.get(Constants.LOGINPAGE);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Browser opened and navigated to login page");
		
		LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
		loginPage.login("jordan@email.com", "pass");
		
		ReportFile.logStatusTest(LogStatus.INFO, "Logged in as trainee");
	}

	@When("^a post request with a new CV is sent$")
	public void a_post_request_with_a_new_CV_is_sent() {
	    
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.uploadCV();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Upload CV clicked");
		ReportFile.logStatusTest(LogStatus.INFO, "Post request sent");
	}

	@Then("^the new CV should be visible on the trainee dashboard$")
	public void the_new_CV_should_be_visible_on_the_trainee_dashboard() {
	    
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		
		if(userPage.checkCVUpload().equals("CV.docx")) {
			
			ReportFile.logStatusTest(LogStatus.PASS, "CV uploaded successfully");
		} 
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "CV upload failed");
		}
		
		assertEquals("CV.docx", userPage.checkCVUpload());
	}

	@When("^a delete request is sent$")
	public void a_delete_request_is_sent() throws InterruptedException {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.deleteCV();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Delete CV clicked");
		ReportFile.logStatusTest(LogStatus.INFO, "Delete request sent");
	}

	@Then("^the CV should no longer be visible on the trainee dashboard$")
	public void the_CV_should_no_longer_be_visible_on_the_trainee_dashboard() {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		
		if(userPage.checkCVUpload().equals(null)) {
			
			ReportFile.logStatusTest(LogStatus.PASS, "CV deleted successfully");
		} 
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Delete CV failed");
		}
		
		assertEquals(null, userPage.checkCVUpload());
	}

	@Given("^a training manager is logged in$")
	public void a_user_other_than_a_trainee_is_logged_in() {
		
		ReportFile.createTest("Test: Search for trainee");
		ReportFile.createTest("Test: View trainee");
		ReportFile.createTest("Test: Update status");

		driver.get(Constants.TRAININGMANAGERPAGE);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Browser opened and navigated to a training manager page");
	}

	@When("^search term \"([^\"]*)\" is searched for$")
	public void search_term_is_searched_for(String arg1) {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.search(arg1);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Search term entered");
	}

	@Then("^all users with the search term \"([^\"]*)\" in their name or email should appear$")
	public void all_users_with_the_search_term_in_their_name_or_email_should_appear(String arg1) {
		
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		
		if(userPage.checkSearch(arg1)) {
			ReportFile.logStatusTest(LogStatus.PASS, "Expected results found in search");
		} 
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Unexpected results found in search");
		}
		
		assertEquals(true, userPage.checkSearch(arg1));
	}

	@When("^a trainee is searched for$")
	public void a_trainee_is_searched_for() {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.search("jordan");
		
		ReportFile.logStatusTest(LogStatus.INFO, "Search term entered");
	}

	@When("^a trainee is clicked on$")
	public void a_trainee_is_clicked_on() {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.clickTrainee();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Trainee profile clicked");
	}

	@Then("^the trainee profile should appear on screen$")
	public void the_trainee_profile_should_appear_on_screen() {

		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		
		if(!userPage.getTraineeName().equals(null)) {
			ReportFile.logStatusTest(LogStatus.PASS, "Trainee profile successfuly displayed on screen");
		} 
		else {	
			ReportFile.logStatusTest(LogStatus.FAIL, "Trainee profile not displayed on screen");
		}
		
		assertNotEquals(null, userPage.getTraineeName(arg1));
	}

	@When("^flag is clicked$")
	public void flag_is_clicked() {
		
		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
		userPage.clickFlag();
		
		ReportFile.logStatusTest(LogStatus.INFO, "Flag button clicked");
	}

//	@Then("^the updated status should appear next to the CV$")
//	public void the_updated_status_should_appear_next_to_the_CV() {
//
//		UserPage userPage = PageFactory.initElements(driver, UserPage.class);
//		System.out.println(userPage.getStatus());
//	}
}
