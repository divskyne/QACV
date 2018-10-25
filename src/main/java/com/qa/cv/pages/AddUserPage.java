package com.qa.cv.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddUserPage {

	@FindBy(xpath = "//*[@id=\"name\"]")
	private WebElement nameBox;
	
	@FindBy(xpath = "//*[@id=\"emailstr\"]")
	private WebElement emailBox;
	
	@FindBy(xpath = "//*[@id=\"passstr\"]")
	private WebElement passwordBox;
	
	@FindBy(xpath = "//*[@id=\"roleSelect\"]")
	private WebElement roleBox;
	
	@FindBy(xpath = "//*[@id=\"roleSelect\"]/option[1]")
	private WebElement trainee;
	
	@FindBy(xpath = "//*[@id=\"roleSelect\"]/option[2]")
	private WebElement trainer;
	
	@FindBy(xpath = "//*[@id=\"roleSelect\"]/option[3]")
	private WebElement sales;
	
	@FindBy(xpath = "//*[@id=\"roleSelect\"]/option[4]")
	private WebElement manager;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/form/div/button")
	private WebElement register;
	
	public void enterDetails(String name, String email, String password) {
		
		nameBox.sendKeys(name);
		emailBox.sendKeys(email);
		passwordBox.sendKeys(password);
		
		roleBox.click();
	}
	
	public void enterRole(String role) {
		
		switch(role) {
		
		case "Trainee": trainee.click();
			break;
		case "Trainer": trainee.click();
			break;
		case "Sales": trainee.click();
			break;
		case "Training Manager": trainee.click();
		}
	}
	
	public void submitDetails() {
		
		register.click();
	}
}
