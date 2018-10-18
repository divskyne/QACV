package com.qa.cv.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cv {
	
	@Id
	private String id;

	private String files_id;
	private Binary file;
	
	public Cv() {
		
	}
	
	public Cv(String s) {
		
	}
	
	public Cv(Binary file) {
		super();
		this.file = file;
	}
	
	public String getId() {
		return id;
	}
	
	public Binary getFile() {
		return file;
	}
	public void setFile(Binary file) {
		this.file = file;
	}
	
	@Override
	public String toString() {
		return this.files_id;
	}

}
