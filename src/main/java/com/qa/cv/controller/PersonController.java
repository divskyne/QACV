package com.qa.cv.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qa.cv.SpringMongoConfig;
import com.qa.cv.model.Person;
import com.qa.cv.repo.PersonRepository;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class PersonController {
	@Autowired
	private PersonRepository repository;
	
	private String storeFile(MultipartFile multipart) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

		InputStream inputStream = null;
		try {
			inputStream = multipart.getInputStream();
			//inputStream = new FileInputStream("C:\\Users\\Admin\\Desktop\\doc.txt");
			gridOperations.store(inputStream, multipart.getOriginalFilename());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "fail";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return "fail";
				}
			}
		}
		return "pass";
	}
	
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile multipart) {
		
		return storeFile(multipart);
	}
	
	@RequestMapping(value = "/people", method = RequestMethod.GET)
	public List<Person> getPeople() {
		return repository.findAll();
	}
	
	@RequestMapping(value = "/people/{id}", method = RequestMethod.PUT)
	  public Person modifyPersonById(@PathVariable String id, @RequestBody Person person) {
	    repository.save(person);
	    return person;
	  }
	
	@RequestMapping(value="/people",method=RequestMethod.POST)
	public Person createPerson(@RequestBody Person person) {
		repository.save(person);
		return person;
	}
	
	@RequestMapping(value = "/people/{id}", method = RequestMethod.GET)
	  public Optional<Person> getPersonById(@PathVariable("id") String id) {
	    return(repository.findById(id));
	    
	}
	
	@RequestMapping(value="/people/{id}",method=RequestMethod.DELETE)
	public Person deletePerson(@PathVariable String id, Person person) {
		repository.delete(person);
		return person;
	}
	
	@RequestMapping(value="/people/{id}/n",method=RequestMethod.POST)
	public Person updateState(@PathVariable("id") String id, @RequestBody String state) {
		return repository.save(repository.findById(id).get().setState(state));
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String checkLogin(@RequestBody Person user) {
		List<Person> p = repository.findByEmail(user.getEmail());
		
		for (Person o : p) {
			if (o.getPassword().equals(user.getPassword())) {
				return o.getRole();
			}
		}
		return "Person Not Found";
	}
	
}
