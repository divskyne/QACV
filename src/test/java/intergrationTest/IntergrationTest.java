package intergrationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.qa.cv.Application;
import com.qa.cv.model.Person;
import com.qa.cv.repo.PersonRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class IntergrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private PersonRepository myRepo;
	
	@Before
	public void clearDB() {
		myRepo.deleteAll();
}
	@After
	public void clearDBED() {
		
		myRepo.deleteAll();
	}
	
	@Test
	public void findingAndRetrieveingPersonFromDatabase()throws Exception{
		//myRepo.save(new Person("me@example.com", "melvin","trainee","1234"," ","approved"));
		mvc.perform(get("/api/people")
		.contentType(MediaType.APPLICATION_JSON))
//		.content("{\"name\":\"Robert\",\"address\":\"Atlantis\",\"age\":200}"))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}
	
	@Test
	public void addPersonToDatabaseTest()throws Exception{
//		myRepo.save(new Person("me@example.com", "melvin","trainee","1234"," ","approved"));
		mvc.perform(get("/api/people")
		.contentType(MediaType.APPLICATION_JSON)
		.content("{\"email\":\"me@example.com\",\"name\":\"melvin\",\"role\":\"melvin\",\"password\":\"melvin\",\"cv\":\"melvin\",\"state\":\"melvin\"}"))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
}