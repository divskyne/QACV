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
	private Cv cv;
	private String state;
    private String docType;
    private Binary file;
	
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
	
	public Person(String email, String name, String role, String password, String state) {
		super();
		this.email = email;
		this.name = name;
		this.role = role;
		this.password = password;
		this.state = state;
		this.cv = new Cv();
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
	public Cv getCv() {
		return cv;
		//return HexUtils.toHexString(cv.getFile().getData());
	}
	
	public List<Cv> getCvs() {
		return cvs;
		//return HexUtils.toHexString(cv.getFile().getData());
	}
	
	public Person setCv(Cv string) {
		this.cv = string;
		cvs.add(string);
		return this;
	}
	
	public String getState() {
		return state;
	}
	public Person setState(String state) {
		this.state = state;
		return this;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}	
}
