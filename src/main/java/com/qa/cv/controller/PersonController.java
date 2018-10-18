package com.qa.cv.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.buf.HexUtils;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
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
import com.qa.cv.model.Cv;
import com.qa.cv.model.Person;
import com.qa.cv.repo.CVRepository;
import com.qa.cv.repo.PersonRepository;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class PersonController {
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private CVRepository cvRepository;
	
	private String storeFile(MultipartFile multipart, String id) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

		InputStream inputStream = null;
		try {
			inputStream = multipart.getInputStream();
			ObjectId o = gridOperations.store(inputStream, multipart.getOriginalFilename());
			//repository.save(repository.findById(id).get().setCv(o.toHexString()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "fail";
		} catch (IOException e) {
			e.printStackTrace();
			return "fail";
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
	
	@PostMapping("/{id}/upload")
	public String singleFileUpload(@PathVariable String id, @RequestParam("file") MultipartFile multipart) {
		//return storeFile(multipart, id);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return "fail";
		}
		Binary b = null;
		try {
			b = new Binary(multipart.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return "fail";
		}
		Cv c = new Cv(b);
		cvRepository.save(c);
		cvRepository.findById(c.getId());
		repository.findByEmail(id);
		return "pass";
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
