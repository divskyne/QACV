package com.qa.cv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserPage {

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/h1")
	private WebElement header;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/nav/div/ul[2]/li[2]/a")
	private WebElement logoutButton;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div/div/div[2]/div[1]/code/table/tbody/tr/td/div/table/tbody/tr/td[1]/input")
	private WebElement chooseFile;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div/div/div[2]/div[1]/code/table/tbody/tr/td/div/table/tbody/tr/td[2]/button")
	private WebElement uploadButton;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div/div/div[2]/div[2]/table/tbody/div/tr/td[1]/a")
	private WebElement cv;
	
	@FindBy(xpath = "//*[@id=\"t1\"]")
	private WebElement deleteButton;
	
	@FindBy(xpath = "//*[@id=\"search\"]")
	private WebElement searchBox;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[3]/div/div[2]/div[2]/code/table/tbody")
	private WebElement resultTable;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[3]/div/div[2]/div[2]/code/table/tbody/tr/td[1]")
	private WebElement trainee;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[3]/div/div[1]/code/table/tbody/div/tr[2]/td")
	private WebElement traineeName;
	
	@FindBy(xpath = "//*[@id=\"1\"]")
	private WebElement flagButton;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[3]/div/div[3]/code/table/tbody/div/tr/td[2]")
	private WebElement status;
	
	public String getHeaderText() {		
		return header.getText();
	}
	
	public void logout() {		
		logoutButton.click();
	}
	
	public void uploadCV() {	
		chooseFile.click();
		chooseFile.sendKeys("C:\\Users\\Admin\\CV.docx");	
		uploadButton.click();
	}
	
	public void deleteCV() {
		deleteButton.click();
	}
	
	public String checkCVUpload() {	
		return cv.getText();
	}
	
	public void search(String term) {
		searchBox.sendKeys(term);
	}
	
	public boolean checkSearch(String term) {
		
		boolean output;
		
		if(resultTable.findElement(By.tagName("td")).getText().contains(term)){
			output = true;
		} else output = false;
		return output;
	}
	
	public void clickTrainee() {
		trainee.click();
	}
	
	public String getTraineeName() {
		return traineeName.getText();
	}
	
	public void clickFlag() {
		flagButton.click();
	}
	
	public String getStatus() {
		return status.getText();
	}
}