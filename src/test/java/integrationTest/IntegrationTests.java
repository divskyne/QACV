package integrationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.qa.cv.Application;
import com.qa.cv.model.Person;
import com.qa.cv.repo.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class IntegrationTests {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private PersonRepository myRepo;
	
	@Before
	public void clearDB() throws Exception {
		mvc.perform(post("/api/people")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"me@example.com\",\"name\":\"melvin\",\"role\":\"trainer\",\"password\":\"melvin\",\"cv\":\" \",\"state\":\"melvin\"}"));
//		myRepo.deleteAll();
//		ReportFile.createReport();
}
//	@After
//	public void clearDBED() {
//		ReportFile.endReport;
//		ReportFile.flushReport
//		myRepo.deleteAll();
//	}
////	
	@Test
//	@Ignore
	public void findingAndRetrieveingPeopleFromDatabase()throws Exception{
		//ReportFile.createTest("Finding all the people in the system");
		
		mvc.perform(get("/api/people")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		
//		if(status().equals(200))
//		{
//			ReportFile.LogStatusTest(Logstatus.PASS, "All people have been retrieved");
//		}
//		else
//		{
//			ReportFile.LogStatusTest(Logstatus.FAIL, "There is an error in retrieving people");
//		}
	}
//	@Ignore
	@Test
	public void findAndRetrieveAPersonFromDatabase () throws Exception{
		//ReportFile.createTest("Finding a person in the database");
		List<Person> pList =myRepo.findByEmail("me@example.com");
		for(Person p:pList) {
		String id = p.getId();
		 mvc.perform(get("/api/people/"+ id)
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is("me@example.com")));
		}
		
//		if(status().equals(200))
//		{
//			ReportFile.LogStatusTest(Logstatus.PASS, "All people have been retrieved");
//		}
//		else
//		{
//			ReportFile.LogStatusTest(Logstatus.FAIL, "There is an error in retrieving people");
//		}
		
		
		
	}
	
	@Test
	public void addPersonToDatabaseTest()throws Exception{


//		int a=0;
		mvc.perform(post("/api/people")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"email\":\"me@example.com\",\"name\":\"melvin\",\"role\":\"trainer\",\"password\":\"melvin\",\"cv\":\" \",\"state\":\"melvin\"}"))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		
		
//		for(Person p: myRepo.findByEmail("me@example.com")) {
//			if(p.getEmail().equals("me@example.com")) {
//				ReportFile.LogStatusTest(Logstatus.PASS, "Person has been added");
//				ReportFile.LogStatusTest(Logstatus.INFO, " ");
//			}
//			else {
//				ReportFile.LogStatusTest(Logstatus.FAIL, "There are no users added");
//			}
//		}

	}
//	
//	@Ignore
	@Test
	public void removeAPersonToDatabaseTest() throws Exception{
		Long count=myRepo.count();
		List<Person> pList =myRepo.findByEmail("me@example.com");
		for(Person p:pList) 
		{
		String id = p.getId();
		mvc.perform(delete("/api/people/"+ id)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		}
		
//		if(myRepo.count()==(count-1))
//		{
//			ReportFile.LogStatusTest(Logstatus.PASS, "Person has been removed");
//		}
//		else
//		{
//			ReportFile.LogStatusTest(Logstatus.FAIL, "There are no users have been removed");
//		}
//		}
////	
	}

	@Test
	public void updateAPersonToDatabaseTest() throws Exception{
		
		List<Person> updateList =myRepo.findByEmail("me@example.com");
		for(Person p:updateList) {
		String id = p.getId();
		mvc.perform(put("/api/people/"+ id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\n" + 
						"        \"id\": \""+id+"\",\n" + 
						"        \"email\": \"D\",\n" + 
						"        \"name\": \"test1\",\n" + 
						"        \"role\": \"trainer\",\n" + 
						"        \"password\": \"melvin\",\n" + 
						"        \"cv\": \"{ }\",\n" + 
						"        \"state\": \"melvin\",\n" + 
						"        \"docType\": null,\n" + 
						"        \"file\": null\n" + 
						"    }"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("D")));}
		}
	
	@Test
	public void checkLoginTest()throws Exception{

		MvcResult result = mvc.perform(post("/api/login")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"email\":\"me@example.com\",\"password\":\"melvin\"}"))
		.andExpect(status().isOk()).andReturn();
		
		if(result.getResponse().getContentAsString().equals("trainer")) {
//			ReportFile.LogStatusTest(Logstatus.PASS, "Login Successful");
			System.out.println(result.getResponse().getContentAsString());
		}
		else {
//			ReportFile.LogStatusTest(Logstatus.FAIL, "Login fail");
			System.out.println(result.getResponse().getContentAsString());
		}
	}
	
}
