package com.qa.cv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (value = HttpStatus.EXPECTATION_FAILED)
public class ResourceNotFoundException extends RuntimeException {
private String resourceName;
private String fieldName;
private Object fieldValue;

public ResourceNotFoundException(String resourceName,String fieldName,Object fieldValue) {
super(String.format("%s", resourceName, fieldName, fieldValue));
}

public String getResourceName() {
	return resourceName;
}

public String getFieldName() {
	return fieldName;
}

public Object getFieldValue() {
	return fieldValue;
}

}