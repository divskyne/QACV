package integrationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.qa.cv.Application;
import com.qa.cv.ReportFile;
import com.qa.cv.model.Person;
import com.qa.cv.repo.PersonRepository;
import com.relevantcodes.extentreports.LogStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@AutoConfigureMockMvc
public class IntegrationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private PersonRepository myRepo;

	@Before
	public void clearDB() throws Exception {
		myRepo.deleteAll();
		mvc.perform(post("/api/people").contentType(MediaType.APPLICATION_JSON).content(
				"{\"email\":\"me@example.com\",\"name\":\"melvin\",\"role\":\"trainer\",\"password\":\"melvin\",\"cv\":\" \",\"state\":\"melvin\"}"));
		ReportFile.createReport();
	}

	@After
	public void teardown() {
		ReportFile.endTest();
	}

	@AfterClass
	public static void afterClass() {
		ReportFile.endReport();
//		myRepo.deleteAll();
	}


	@Test

	public void findingAndRetrievingPeopleFromDatabase() throws Exception {
		ReportFile.createTest("Finding all the people in the system");

		MvcResult result = mvc.perform(get("/api/people").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		if (result.getResponse().getStatus() == 200) {
			ReportFile.logStatusTest(LogStatus.PASS, "All people have been retrieved");
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "There is an error in retrieving people");
		}
	}


	@Test
	public void findAndRetrieveAPersonFromDatabase() throws Exception {
		MvcResult result = null;
		ReportFile.createTest("Finding a person in the database");
		List<Person> pList = myRepo.findByEmail("me@example.com");
		for (Person p : pList) {
			String id = p.getId();
			result = mvc.perform(get("/api/people/" + id).contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$.email", is("me@example.com"))).andReturn();
		}

		if (result.getResponse().getStatus() == 200) {
			ReportFile.logStatusTest(LogStatus.PASS, "Person has been retrieved by email");
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Retrieve person by email failed");
		}

	}

	@Test
	public void addPersonToDatabaseTest() throws Exception {
		ReportFile.createTest("Adding a person to the database");
		MvcResult result = mvc.perform(post("/api/people").contentType(MediaType.APPLICATION_JSON).content(
				"{\"email\":\"me@example1.com\",\"name\":\"melvin\",\"role\":\"trainer\",\"password\":\"melvin\",\"cv\":\" \",\"state\":\"melvin\"}"))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andReturn();

		if (result.getResponse().getStatus() == 200) {
			ReportFile.logStatusTest(LogStatus.PASS, "Person has been added to the database");
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Failed to add person to database");
		}


	}


	@Test
	public void removeAPersonToDatabaseTest() throws Exception {
		ReportFile.createTest("Removing a person from the database");
		Long count = myRepo.count();
		List<Person> pList = myRepo.findByEmail("me@example.com");
		for (Person p : pList) {
			String id = p.getId();
			mvc.perform(delete("/api/people/" + id).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		}
		if (myRepo.count() == (count - 1)) {
			ReportFile.logStatusTest(LogStatus.PASS, "Person has been removed");
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Failed to remove person");
		}


	}

	@Test
	public void updateAPersonToDatabaseTest() throws Exception {
		MvcResult result = null;
		ReportFile.createTest("Updating a person");
		List<Person> updateList = myRepo.findByEmail("me@example.com");
		for (Person p : updateList) {
			String id = p.getId();
			result = mvc
					.perform(put("/api/people/" + id).contentType(MediaType.APPLICATION_JSON)
							.content("{\n" + "        \"id\": \"" + id + "\",\n" + "        \"email\": \"me@example.com\",\n"
									+ "        \"name\": \"test1\",\n" + "        \"role\": \"trainer\",\n"
									+ "        \"password\": \"melvin\",\n" + "        \"cv\": \"{ }\",\n"
									+ "        \"state\": \"melvin\",\n" + "        \"docType\": null,\n"
									+ "        \"file\": null\n" + "    }"))
					.andExpect(status().isOk()).andExpect(jsonPath("$.email", is("me@example.com"))).andReturn();
		}
		if(result.getResponse().getContentAsString().contains("test1")) {
			ReportFile.logStatusTest(LogStatus.PASS, "Person has been updated");
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Failed to update person");
		}
	}

	@Test
	public void checkLoginTest() throws Exception {

		ReportFile.createTest("Check Login");
		MvcResult result = mvc
				.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"me@example.com\",\"password\":\"melvin\"}"))
				.andExpect(status().isOk()).andReturn();

		if (result.getResponse().getStatus() == 200) {
			ReportFile.logStatusTest(LogStatus.PASS, "Login Successful");
			System.out.println(result.getResponse().getContentAsString());
		} else {
			ReportFile.logStatusTest(LogStatus.FAIL, "Login failed");
			System.out.println(result.getResponse().getContentAsString());
		}
	}

}
