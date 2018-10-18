package com.qa.cv;

//import java.io.File;
//import java.io.IOException;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ReportFile {

	private static ExtentReports report;
	private static  ExtentTest test;
	
	
	public static void createReport()
	{
		if(report == null)
		{
			report = new ExtentReports(System.getProperty("user.dir") + "\\" + Constants.REPORTFILEPATH,false);			
		}
	}
	
	public static void createTest(String testName)
	{
		if(test == null)
		{
			test = report.startTest(testName);
		}
	}
		
	//.INFO, .PASS, .FAIL
	public static void logStatusTest(LogStatus status,String text)
	{
		test.log(status,text);
	}
		
	public static void endTest()
	{
		report.endTest(test);
		test = null;
		
	}
	
	public static void endReport()
	{
		report.flush();
	}
	
}
