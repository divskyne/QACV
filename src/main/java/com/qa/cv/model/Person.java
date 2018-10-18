package com.qa.cv.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Document
public class Person {

	@Id private String id;

	private String email;
	private String name;
	private String role;
	private String password;
	private String cv;
	private String state;
	
	@Field
    private String docType;

    @Field
    private Binary file;
	
	public Person()
	{
		
	}
	
	public Person(String email, String name, String role, String password, String cv, String state) {
		super();
		this.email = email;
		this.name = name;
		this.role = role;
		this.password = password;
		this.cv = cv;
		this.state = state;
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
	public String getCv() {
		return cv;
	}
	public void setCv(String cv) {
		this.cv = cv;
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
