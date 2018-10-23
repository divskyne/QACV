package com.qa.cv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class PageNotFound extends RuntimeException
{
	private String path;
	
	public PageNotFound(String path) {
		super(String.format("%s", path));
		}
	
	public String getpath()
	{
		return path;
	}
}
