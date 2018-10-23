package com.qa.cv.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.qa.cv.exception.PageNotFound;
import com.qa.cv.exception.ResourceNotFoundException;
import com.qa.cv.model.Cv;
import com.qa.cv.model.Person;
import com.qa.cv.repo.CVRepository;
import com.qa.cv.repo.PersonRepository;

@CrossOrigin(origins = "*", maxAge=3600)
@RestController
@RequestMapping("/api")
public class PersonController {
	
	private static final String WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	
	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private CVRepository cvrepository;
	
	private String saveFileToDB(MultipartFile multipart, String id) {
		
		if (!multipart.getContentType().equals(WORD))
		{
			return "Unsupported file type! "+multipart.getContentType();
		}
		
		else
		{
			MongoClient mongo = new MongoClient("localhost", 27017);
			DB db = mongo.getDB("disco1");

			GridFS gridFs = new GridFS(db);

			GridFSInputFile gridFsInputFile = null;
			File convFile = null;
			try {
				convFile = new File(multipart.getOriginalFilename());
			    convFile.createNewFile(); 
			    FileOutputStream fos = new FileOutputStream(convFile); 
			    fos.write(multipart.getBytes());
			    fos.close(); 
				gridFsInputFile = gridFs.createFile(convFile);
			} catch (IOException e) {
				e.printStackTrace();
				return "Failed to upload file: "+multipart.getOriginalFilename();
			}

			gridFsInputFile.setFilename(multipart.getOriginalFilename());
			gridFsInputFile.setContentType(multipart.getContentType());
			Cv cv2 = new Cv(gridFsInputFile.getId().toString(), gridFsInputFile.getFilename(), gridFsInputFile.getUploadDate());
			cvrepository.save(cv2);
			repository.save(repository.findById(id).get().setCv(cv2));

			gridFsInputFile.save();
			return multipart.getOriginalFilename()+" has been uploaded!";
		}
		
		}
	
	@GetMapping("/{id}/cv/{cvid}")
	public ResponseEntity<byte[]> findCV(@PathVariable String id, @PathVariable String cvid) throws IOException
	{
		return findFile(id, cvid);
	}
	
	public ResponseEntity<byte[]> findFile(String id, String cvid) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HttpHeaders headers = new HttpHeaders();
		GridFSDBFile outputFile = null;
		try {
				String cvFile = cvid;
				ObjectId objectid = new ObjectId(cvFile);
				MongoClient mongo = new MongoClient("localhost", 27017);
				DB db = mongo.getDB("disco1");
			    GridFS gridFs = new GridFS(db);
			    outputFile = gridFs.find(objectid);
			    
				InputStream inputImage = outputFile.getInputStream();
		        byte[] buffer = new byte[512];
		        int l = inputImage.read(buffer);
		        while(l >= 0) {
		            outputStream.write(buffer, 0, l);
		            l = inputImage.read(buffer);
		        }
		        for (Object b : outputStream.toByteArray()) {
					b.toString();
				}
	        mongo.close();
	        headers.set("Content-Type", outputFile.getContentType());
	        headers.set("Accept-Ranges", "bytes");
	        headers.set("Connection", "keep-alive");
	        headers.set("Content-Length", String.valueOf(outputFile.getLength()));
	        headers.set("Content-Disposition", "inline; "+"filename="+outputFile.getFilename());
	        headers.set("ETag", outputFile.getMD5());
	        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			String error = "This Person Doesn't Have a CV";
			headers.set("Content-Type",MediaType.TEXT_PLAIN.toString());
			outputStream.write(error.getBytes());
			return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/upload")
	public String singleFileUpload(@PathVariable String id, @RequestParam("file") MultipartFile multipart) {
		if (!(repository.findById(id).get().getCvs().size()>2)) {
			return saveFileToDB(multipart, id);
		}
		return "You can't upload any more CV's";
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
	public Person createPerson(@RequestBody Person person) throws NoSuchAlgorithmException {
		List<Person> personList = repository.findByEmail(person.getEmail());
		for(Person p:personList) {
			if(person.getEmail().equalsIgnoreCase(p.getEmail())) {
				throw new ResourceNotFoundException("Duplicate email", "Duplicate email", p);
			}
		}
		
		// Comment this out if front end encryption is enabled
/*		MessageDigest md5 = MessageDigest.getInstance("MD5");
		person.setPassword(HexUtils.toHexString(md5.digest(person.getPassword().getBytes())));*/
		
		repository.save(person);
		return person;
	}
	
	@RequestMapping(value = "/people/{id}", method = RequestMethod.GET)
	  public Optional<Person> getPersonById(@PathVariable("id") String id) {
		try {
			return repository.findById(id);
		} catch (Exception e) {
			throw new PageNotFound("/people/"+id);
		} 
	}
	
	@RequestMapping(value = "/{id}/cvs", method = RequestMethod.GET)
	  public List<Cv> findAllCVs(@PathVariable("id") String id) {
		try {
			return repository.findById(id).get().getCvs();
		} catch (NoSuchElementException e) {
			throw new PageNotFound(id+"/cvs");
		}
	}
	
	@RequestMapping(value="/people/{id}",method=RequestMethod.DELETE)
	public Person deletePerson(@PathVariable String id, Person person) {
		repository.delete(person);
		return person;
	}
	
	@RequestMapping(value="/people/{id}/state/{cvid}",method=RequestMethod.GET)
	public String getState(@PathVariable("id") String id, @PathVariable("cvid") String cvid) {
		try {
			return repository.findById(id).get().getCvs().stream().filter(c -> c.getFiles_id().equals(cvid)).findFirst().get().getState();
		} catch (Exception e) {
			return "Either this person Doesn't Ecist or This CV doesn't exist";
		}
	}
	
	@RequestMapping(value="/people/{id}/state/{cvid}",method=RequestMethod.POST)
	public Person updateState(@PathVariable("id") String id,  @PathVariable("cvid") String cvid,  @RequestBody String state) {
		cvrepository.save(cvrepository.findByCvid(cvid).setState(state));
		repository.findById(id).get().getCvs().removeIf(c -> c.getFiles_id().equals(cvid));
		repository.save(repository.findById(id).get().changeCVState(cvid, state));
		return repository.findById(id).get();
	}
	
	@RequestMapping(value="/find",method=RequestMethod.GET)
	public List<Person> search(@RequestParam(value="search") String search) {
		List<Person> people = repository.findAll().stream().filter(p -> {
			if(p.getEmail().contains(search))
			{
				return true;
			}
			else if (p.getName().contains(search))
			{
				return true;
			}
			return false;
			}).collect(Collectors.toList());
		return people;
	}
	
	@RequestMapping(value="/people/{id}/cv/{cvid}",method=RequestMethod.DELETE)
	public Person removeCV(@PathVariable("id") String id,  @PathVariable("cvid") String cvid) {
		repository.save(repository.findById(id).get().removeCV(cvid));
		return repository.findById(id).get();
	}
	
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public Person checkLogin(@RequestBody Person user) {
		List<Person> p = repository.findByEmail(user.getEmail());
		for (Person o : p) {
			if (o.getPassword().equals(user.getPassword())) {
				Person person = new Person(o.getId(), o.getRole());
				return person;
			}
		}
		return new Person();
	}	
}
