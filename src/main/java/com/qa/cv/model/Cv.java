package com.qa.cv.model;

import java.io.File;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.gridfs.GridFSInputFile;

@Document
public class Cv {
	
	@Id
	private String id;

	private String cvid;

	private String state;
	
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
	
	public Cv(String files_id) {
		super();
		this.cvid = files_id;
		state = "";
	}
	
	public String getId() {
		return id;
	}
}
