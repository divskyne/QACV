package com.qa.cv.model;

import java.io.File;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.gridfs.GridFSInputFile;

@Document
public class Cv {
	
	@Id
	private String id;

	private String files_id;
	
	public String getFiles_id() {
		return files_id;
	}
	
	public Cv() {
		
	}
	
	public Cv(String files_id) {
		super();
		this.files_id = files_id;
	}
	
	public String getId() {
		return id;
	}
}
