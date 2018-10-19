package com.qa.cv.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.qa.cv.SpringMongoConfig;
import com.qa.cv.exception.ResourceNotFoundException;
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
	
	@GetMapping("/{id}/cv")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response singleFileRetrieve(@PathVariable String id)
	{
		try {
			return findFileFromDB(id);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	@PostMapping("/{id}/upload")
	public String singleFileUpload(@PathVariable String id, @RequestParam("file") MultipartFile multipart) {
		
		return saveFileToDB(multipart);
	}
	
	public Response findFileFromDB(String id) throws IOException {
		
		String cvFile = findFileFromPerson(id);
		ObjectId objectid = new ObjectId(cvFile);
		
		MongoClient mongo = new MongoClient("localhost", 27017);
	    DB db = mongo.getDB("disco1");

	    GridFS gridFs = new GridFS(db);

	    GridFSDBFile outputFile = gridFs.find(objectid);

	    String location = outputFile.getFilename();
	    
	    File file = new File(location);
	    outputFile.writeTo(file);
	    mongo.close();
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=newfile.zip");
		response.entity(file);
		return response.build();
	}
	
	private String findFileFromPerson(String id) {
		return repository.findById(id).get().getCv();
	}

	public File convert(MultipartFile file) throws IOException
	{    
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
	
	private String saveFileToDB(MultipartFile multipart) {
		
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		GridFsOperations gridOperations = (GridFsOperations) ctx.getBean("gridFsTemplate");

		InputStream inputStream = null;
		try {
			inputStream = multipart.getInputStream();
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

	@RequestMapping(value = "/people", method = RequestMethod.GET)
	public List<Person> getPeople() {
		return repository.findAll();
	}
	
	@RequestMapping(value = "/people/{id}", method = RequestMethod.PUT)
	  public Person modifyPersonById(@PathVariable String id, @RequestBody Person person) {
		List<Person> personList = repository.findByEmail(person.getEmail());
		for(Person p:personList) {
			if(person.getEmail().equalsIgnoreCase(p.getEmail())) {
				repository.save(person);
				return person;
			} else {
				throw new ResourceNotFoundException("Can't change email address", "Can't change email address", person);
			}
		}
		throw new ResourceNotFoundException("Can't change email address", "Can't change email address", person);
	  }
	
	@RequestMapping(value="/people",method=RequestMethod.POST)
	public Person createPerson(@RequestBody Person person) {
		List<Person> personList = repository.findByEmail(person.getEmail());
		for(Person p:personList) {
			if(person.getEmail().equalsIgnoreCase(p.getEmail())) {
				throw new ResourceNotFoundException("Duplicate email", "Duplicate email", p);
			}
		}
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
