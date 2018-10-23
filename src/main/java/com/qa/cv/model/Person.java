package com.qa.cv.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
public class Person {

	@Id private String id;

	private String email;
	private String name;
	private String role;
	private String password;
	private List<Cv> cvs;
	private String preferedLocation;
	private String currentTrainer;
	private String preferedRole;
	
	public Person()
	{
		cvs = new ArrayList<Cv>();
	}
	
	public Person(String id, String role)
	{
		this.id = id;
		this.role = role;
		cvs = new ArrayList<Cv>();
	}
	
	public Person(String email, String name, String role, String password) {
		super();
		this.email = email;
		this.name = name;
		this.role = role;
		this.password = password;
		cvs = new ArrayList<Cv>();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Cv> getCvs() {
		return cvs;
	}
	
	public Person removeCV(String s)
	{
		cvs.remove(cvs.stream().filter(c -> c.getFiles_id().equals(s)).findFirst().get());
		return this;
	}
	
	public Person changeCVState(String s, String state) {
		cvs.stream().filter(c -> c.getFiles_id().equals(s)).findFirst().get().setState(state);
		return this;
	}
	
	public Person setCv(Cv string) {
		cvs.add(string);
		return this;
	}

	public String getPreferedLocation() {
		return preferedLocation;
	}

	public void setPreferedLocation(String preferedLocation) {
		this.preferedLocation = preferedLocation;
	}

	public String getCurrentTrainer() {
		return currentTrainer;
	}

	public void setCurrentTrainer(String currentTrainer) {
		this.currentTrainer = currentTrainer;
	}

	public String getPreferedRole() {
		return preferedRole;
	}

	public void setPreferedRole(String preferedRole) {
		this.preferedRole = preferedRole;
	}
}
