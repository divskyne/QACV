package com.qa.cv.model;

import java.io.File;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.gridfs.GridFSInputFile;

@Document
public class Cv {
	
	@Id
	private String id;

	private String cvid;

	private String state;
	private String name;
	private Date modifiedDate;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getState() {
		return state;
	}

	public Cv setState(String status) {
		state = status;
		return this;
	}
	
	public String getFiles_id() {
		return cvid;
	}
	
	public Cv() {
		
	}
	
	public Cv(String files_id, String name, Date date) {
		super();
		this.cvid = files_id;
		state = "Unapproved";
		this.name = name;
		this.modifiedDate = date;
	}
	
	public String getId() {
		return id;
	}
}
