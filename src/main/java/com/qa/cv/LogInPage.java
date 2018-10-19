package com.qa.cv;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LogInPage {

	@FindBy(xpath = "//*[@id=\'email\']")
	private WebElement usernameInput; 
	
	@FindBy(xpath = "//*[@id='password']")
	private WebElement passwordInput; 
	
	@FindBy(xpath = "//*[@id=\'root\']/div/div/form/button")
	private WebElement submitButton; 
	
	public void logIn(String username, String password)
	{
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		submitButton.click();
	}
}
