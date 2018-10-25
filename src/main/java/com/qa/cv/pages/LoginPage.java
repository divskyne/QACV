package com.qa.cv.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

	@FindBy(xpath = "//*[@id=\"email\"]")
	private WebElement emailBox;
	
	@FindBy(xpath = "//*[@id=\"password\"]")
	private WebElement passwordBox;
	
	@FindBy(xpath = "//*[@id=\"loginFormId\"]/form/div/button")
	private WebElement loginButton;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/h2")
	private WebElement header;
	
	public void login(String email, String password) {
		
		emailBox.sendKeys(email);
		passwordBox.sendKeys(password);
		
		loginButton.click();
	}
	
	public String getHeaderText() {
		
		return header.getText();
	}
}