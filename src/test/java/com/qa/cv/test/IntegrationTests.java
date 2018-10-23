package com.qa.cv.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bson.types.Binary;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import com.qa.cv.CvManagerApplication;
import com.qa.cv.ReportFile;
import com.qa.cv.model.Cv;
import com.qa.cv.model.Person;
import com.qa.cv.repo.CVRepository;
import com.qa.cv.repo.PersonRepository;
import com.relevantcodes.extentreports.LogStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CvManagerApplication.class})
@AutoConfigureMockMvc
public class IntegrationTests {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private PersonRepository personRepo;
	
	@Autowired
	private CVRepository cvRepo;
	
	@BeforeClass
	public static void beforeClass() {
		ReportFile.createReport();
	}
	
	@Before
	public void startup() {
		personRepo.deleteAll();
		cvRepo.deleteAll();
	}
	
	@After
	public void tearDown() {
		ReportFile.endTest();
	}
	
	@AfterClass
	public static void afterClass() {
		ReportFile.endReport();
	}
	
	//Add person to database
	@Test
	public void addPersonToDatabase() throws Exception {
		
		ReportFile.createTest("Test: Add person to database");
		
		Person person = new Person("hsimpson2@springfieldnuclearpowerplant.com", "Homer", "Trainee", "Donut");		
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created");
	
		ReportFile.logStatusTest(LogStatus.INFO, "Post request sent");
		
		MvcResult result = mvc.perform(post("/api/people")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(person.toString()))
							  .andExpect(status().isOk())
							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							  .andExpect(jsonPath("$.name", is("Homer")))
							  .andReturn();
		
		if(result.getResponse().getStatus() == 200) {			
			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
		
		if(result.getResponse().getContentType().contains("json")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Content returned as JSON object");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content type returned");
		
		if(result.getResponse().getContentAsString().contains(person.toString())) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Expected content found in response body");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content found in response body");
	}
	
	//Get person from database by ID
	@Test @Ignore
	public void getPersonFromDatabase() throws Exception {
		
		ReportFile.createTest("Test: Retrieve person from database by ID");
		
		Person person = new Person("benderbrodriguez@planetexpress.com", "Bender", "Bending Unit", "001010110");
		personRepo.save(person);
		String id = person.getId();	
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created and added to database");
		
		ReportFile.logStatusTest(LogStatus.INFO, "Get request sent");
		
		MvcResult result = mvc.perform(get("/api/people/" + id)
							  .contentType(MediaType.APPLICATION_JSON))
							  .andExpect(status().isOk())
							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							  .andExpect(jsonPath("$.name", is("Bender")))
							  .andReturn();
		
		if(result.getResponse().getStatus() == 200) {			
			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
		
		if(result.getResponse().getContentType().contains("json")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Content returned as JSON object");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content type returned");
		
		if(result.getResponse().getContentAsString().contains(person.toString())) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Expected content found in response body");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content found in response body");
	}
	
	//Get all people from database
	@Test @Ignore
	public void getAllPeopleFromDatabase() throws Exception {
		
		ReportFile.createTest("Test: Retrieve all persons from database");
		
		Person person1 = new Person("turangaleela@cookievilleminimumsecurityorphanarium.com", "Leela", "Trainer", "1i");
		Person person2 = new Person("proffarnsworth@planetexpress.com", "Hubert", "Professor", "Good news everyone!");

		personRepo.save(person1);
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity 1 created and added to database");
		personRepo.save(person2);	
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity 2 created and added to database");

		ReportFile.logStatusTest(LogStatus.INFO, "Get request sent");
		
		MvcResult result = mvc.perform(get("/api/people")
							  .contentType(MediaType.APPLICATION_JSON))
							  .andExpect(status().isOk())
							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							  .andReturn();
		
		ReportFile.logStatusTest(LogStatus.INFO, "HTTP status code " + result.getResponse().getStatus() + " returned");

		if(result.getResponse().getStatus() == 200) {			
			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
		
		if(result.getResponse().getContentType().contains("json")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Content returned as JSON object");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content type returned");
		
		if(result.getResponse().getContentAsString().contains(person1.toString()) && result.getResponse().getContentAsString().contains(person2.toString())) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Expected content found in response body");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content found in response body");
	}
	
	//Update person in database by ID
	@Test @Ignore
	public void editOwnerInDatabase() throws Exception {
		
		ReportFile.createTest("Test: Update person in database by ID");
		
		Person person = new Person("supergeniusrick@universec137.com", "Rick", "Training Manager", "wubbalubbadubdub");
		personRepo.save(person);	
		String id = person.getId();	
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created and added to database");
		
		person.setPassword("Rikkitikkitaki");
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity password updated");
		
		ReportFile.logStatusTest(LogStatus.INFO, "Put request sent");
		
		MvcResult result = mvc.perform(put("/api/people/" + id)
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(person.toString()))
							  .andExpect(status().isOk())
							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							  .andExpect(jsonPath("$.password", is("Rikkitikkitaki")))
							  .andReturn();
		
		if(result.getResponse().getStatus() == 200) {			
			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
		
		if(result.getResponse().getContentType().contains("json")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Content returned as JSON object");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content type returned");
		
		if(result.getResponse().getContentAsString().contains("\"password\":\"Rikkitikkitaki\"")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Correct password found in response body");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect password found in response body");
	}
	
	//Delete person from database by ID
	@Test @Ignore
	public void deletePersonFromDatabase() throws Exception {
		
		ReportFile.createTest("Test: Delete person from database by ID");
		
		Person person = new Person("stansmith@cia.com", "Stan", "Trainer", "bush4life");
		personRepo.save(person);	
		String id = person.getId();	
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created and added to database");
		
		ReportFile.logStatusTest(LogStatus.INFO, "Delete request sent");
		
		MvcResult result = mvc.perform(delete("/api/people/" + id)
							  .contentType(MediaType.APPLICATION_JSON))
							  .andExpect(status().isOk())
							  .andReturn();
		
		if(result.getResponse().getStatus() == 200) {			
			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
		
		if(personRepo.findByEmail("stansmith@cia.com").isEmpty()) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Test entity not found in database");
		}
		else ReportFile.logStatusTest(LogStatus.FAIL, "Test entity found in database");
	}
	
	//Search for person in database
	@Test @Ignore
	public void search() throws Exception {
		
		ReportFile.createTest("Integration Test: Search for trainee");
		
		Person person = new Person("zappbrannigan@theinfosphere.com", "Zapp", "Training Manager", "Shampagon");
		personRepo.save(person);
		
		MvcResult result = mvc.perform(get("/api/find")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content("Zap"))
							  .andExpect(status().isOk())
							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
							  .andReturn();
		
		ReportFile.logStatusTest(LogStatus.INFO, "HTTP status code " + result.getResponse().getStatus() + " returned");

		if(result.getResponse().getStatus() == 200 && result.getResponse().getContentAsString().contains("Zap")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Correct HTTP status code returned and expected content found in response body");
		}
		else if (result.getResponse().getStatus() != 200 && !result.getResponse().getContentAsString().contains("Zap")) {
			ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect HTTP status code returned and expected content not found in response body");
		}
		else if (!result.getResponse().getContentAsString().contains("Zap")) {
			ReportFile.logStatusTest(LogStatus.FAIL, "Correct HTTP status code returned but expected content not found in response body");
		}
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Person not successfully retrieved by search");
		}
	}
	
	//Login
	@Test @Ignore
	public void login() throws Exception {
		
		ReportFile.createTest("Integratoin Test: Login");
		
		Person person = new Person("pgriffin@happygoluckytoyfactory", "Peter", "Trainee", "Nyahhhh");
		personRepo.save(person);
		
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created and saved to the database");

		ReportFile.logStatusTest(LogStatus.INFO, "Post request sent");
		MvcResult result = mvc.perform(post("/api/login")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content("{\"email\":\"pgriffin@happygoluckytoyfactory.com\",\"password\":\"Nyahhhh\"}"))
							  .andExpect(status().isOk())
							  .andReturn();
		
		if(result.getResponse().getStatus() == 200 && result.getResponse().getContentAsString().contains("\"name\":\"Peter\"")) {			
			ReportFile.logStatusTest(LogStatus.PASS, "Correct HTTP status code returned and expected content found in response body");
		}
		else if (result.getResponse().getStatus() != 200 && !result.getResponse().getContentAsString().contains("\"name\":\"Peter\"")) {
			ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect HTTP status code returned and expected content not found in response body");
		}
		else if (!result.getResponse().getContentAsString().contains("\"name\":\"Rick\"")) {
			ReportFile.logStatusTest(LogStatus.FAIL, "Correct HTTP status code returned but expected content not found in response body");
		}
		else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Login successful");
		}
	}

	//Add CV to Database
	@Test @Ignore
	public void addCVToDatabase() throws Exception {
		
		ReportFile.createTest("Integration Test: Add CV to database");
		
		Person person = new Person("stansmith@cia.com", "Stan", "Trainer", "bush4life");
		personRepo.save(person);	
		String id = person.getId();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		
		File f = new File("Installing node.docx");
		builder.addBinaryBody(
		    "file",
		    new FileInputStream(f),
		    ContentType.APPLICATION_OCTET_STREAM,
		    f.getName()
		);
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity attached to post request");

		HttpPost uploadFile = new HttpPost("http://localhost:8090/api/" + id + "/upload");
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		CloseableHttpResponse response = httpClient.execute(uploadFile);
		
		assertEquals(response.getStatusLine().getStatusCode(), 200);
	}

	//Get CV from database by ID
	@Test @Ignore
	public void getCVFromDatabase() throws Exception {
				
		ReportFile.createTest("Integration Test: Get CV from database");

		Person person = new Person("stansmith@cia.com", "Stan", "Trainer", "bush4life");
		personRepo.save(person);	
		String id = person.getId();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
		
		File f = new File("Installing node.docx");
		builder.addBinaryBody(
		    "file",
		    new FileInputStream(f),
		    ContentType.APPLICATION_OCTET_STREAM,
		    f.getName()
		);
		ReportFile.logStatusTest(LogStatus.INFO, "Test entity attached to post request");

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("http://localhost:8090/api/" + id + "/upload");
		HttpEntity multipart = builder.build();
		uploadFile.setEntity(multipart);
		httpClient.execute(uploadFile);
		
		HttpGet getFile = new HttpGet("http://localhost:8090/api/" + id + "/cv");
		CloseableHttpResponse response = httpClient.execute(getFile);
		
		assertEquals(response.getStatusLine().getStatusCode(), 200);
	}
	
	//Update CV in database
	
	//Delete CV from database
	
	//Update state of CV in database by personID
//	@Test @Ignore
//	public void updateStatus() throws Exception {
//		
//		ReportFile.createTest("Test: Update state of CV in database");
//		
//		Person person = new Person("psherman@42wallabyway.com", "P Sherman", "Trainer", "Password", "CV", "Unapproved");
//		personRepo.save(person);
//		ReportFile.logStatusTest(LogStatus.INFO, "Test entity created and added to database");
//				
//		person.setState("Approved");
//		ReportFile.logStatusTest(LogStatus.INFO, "Test entity state updated");
//		
//		ReportFile.logStatusTest(LogStatus.INFO, "Put request sent");
//		MvcResult result = mvc.perform(post("/api/people/" + id + "/n")
//							  .contentType(MediaType.APPLICATION_JSON)
//							  .content(person.toString()))
//							  .andExpect(status().isOk())
//							  .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//							  .andExpect(jsonPath("$.state", is("Approved")))
//							  .andReturn();
//		
//		ReportFile.logStatusTest(LogStatus.INFO, "HTTP status code " + result.getResponse().getStatus() + " returned");
//
//		if(result.getResponse().getStatus() == 200) {			
//			ReportFile.logStatusTest(LogStatus.PASS, "HTTP status code " + result.getResponse().getStatus() + " returned");
//		}
//		else ReportFile.logStatusTest(LogStatus.FAIL, "HTTP status code " + result.getResponse().getStatus() + " returned");
//		
//		if(result.getResponse().getContentType().contains("json")) {			
//			ReportFile.logStatusTest(LogStatus.PASS, "Content returned as JSON object");
//		}
//		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect content type returned");
//		
//		if(result.getResponse().getContentAsString().contains("\"state\":\"Approved\"")) {			
//			ReportFile.logStatusTest(LogStatus.PASS, "Correct password found in response body");
//		}
//		else ReportFile.logStatusTest(LogStatus.FAIL, "Incorrect password found in response body");
//	}
	
}