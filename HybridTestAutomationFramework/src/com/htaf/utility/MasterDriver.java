package com.htaf.utility;

import java.io.IOException;
import org.apache.log4j.Logger;
//import org.apache.xmlbeans.*;

import com.htaf.libraryfunctions.*;

public class MasterDriver {
	
	final static Logger logger = Logger.getLogger(MasterDriver.class);
	public static Keywords oKeywords = new Keywords();

	
	
	
	//function to read all the modules for which Run Mode set to yes. 
	public static void readYesModules() throws NumberFormatException, IOException, InterruptedException {
		
		logger.info("Reading all the modules which have run mode Yes.");
		ExcelReaderWriter datatableTestSuite = new ExcelReaderWriter (System.getProperty("user.dir") + 
	    		"/src/Test Configurations And Data/Test Suite Configurations.xlsx");
		
		String sheetName = "Test Suite";
		int rowTestSuiteCount = datatableTestSuite.getRowCount(sheetName);
		rowTestSuiteCount = rowTestSuiteCount -1;
		
    	int testSuiteXCoordinate = 0;
    	int testSuiteYCoordinate = 2;
    	int runModeXCoordinate = 2;
    	int runModeYCoordinate = 2;
		String testModuleName = null;
		
		for (int i = 0; i < rowTestSuiteCount; i++){
			
			if (datatableTestSuite.getCellData(sheetName, runModeXCoordinate, runModeYCoordinate).equalsIgnoreCase("Yes")){
				
				testModuleName =  datatableTestSuite.getCellData(sheetName, testSuiteXCoordinate, testSuiteYCoordinate);
				//System.out.println("-----------------------------------");
				//System.out.println(testModuleName);
        		//System.out.println("------####  #####  ###### ------");
        		readYesTestCases(testModuleName);
			}
			runModeYCoordinate ++;
			testSuiteYCoordinate ++;
		}
		
		
	}
	
	public static void readYesTestCases(String testFileName) throws NumberFormatException, IOException, InterruptedException {
		
		String fileName = testFileName + ".xlsx";
				
		ExcelReaderWriter datatableTestCases = new ExcelReaderWriter (System.getProperty("user.dir") + 
	    		"/src/Test Configurations And Data/" + fileName);
		
		int rowTestSuiteCount = datatableTestCases.getRowCount("Test Cases");
		rowTestSuiteCount = rowTestSuiteCount -1;
		
    	int testCaseXCoordinate = 0;
    	int testCaseYCoordinate = 2;
    	int runModeXCoordinate = 2;
    	int runModeYCoordinate = 2;
		String testCaseName = null;
		
		for (int i = 0; i < rowTestSuiteCount; i++){
			
			if (datatableTestCases.getCellData("Test Cases", runModeXCoordinate, runModeYCoordinate).equalsIgnoreCase("Yes")){
				
				testCaseName =  datatableTestCases.getCellData("Test Cases", testCaseXCoordinate, testCaseYCoordinate);
				//System.out.println("-----------------------------------");
				System.out.println(testCaseName);
        		//System.out.println("------####  #####  ###### ------");
        		logger.info(testCaseName);
        		readTestSteps(fileName, testCaseName);
			}
			runModeYCoordinate ++;
			testCaseYCoordinate ++;
		}
		
		
	}
	
	
	public static void readTestSteps(String fileName, String testCase) throws IOException, NumberFormatException, InterruptedException {
		
		ExcelReaderWriter datatableTestSteps = new ExcelReaderWriter (System.getProperty("user.dir") + 
	    		"/src/Test Configurations And Data/" + fileName);
		
		int rowTestSuiteCount = datatableTestSteps.getRowCount("Test Steps");
		rowTestSuiteCount = rowTestSuiteCount -1;
		
		//System.out.println(rowTestSuiteCount);
		
    	int testStepsXCoordinate = 0;
    	int testStepsYCoordinate = 2;
    	int testStepsDataXCoordinate = 3;
    	int testStepsDataYCoordinate = 2;
    	String testModuleName = null;
		
		String testCaseNames = testCase;
		
		for (int i = 0; i < rowTestSuiteCount; i++){
			
			if (datatableTestSteps.getCellData("Test Steps", testStepsXCoordinate, testStepsYCoordinate).equalsIgnoreCase(testCaseNames)){
				
				testStepsDataYCoordinate = testStepsYCoordinate;
				
				testModuleName =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate, testStepsDataYCoordinate);
				//System.out.println(testModuleName);
				
				if (testModuleName.equalsIgnoreCase("OPENBROWSER")) {
					String browser = datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 3, testStepsDataYCoordinate);
					String testStepResult = oKeywords.openBrowser(browser);
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult.toString());
	
				}
				
				
				else if (testModuleName.equalsIgnoreCase("NAVIGATE")) {
					String testStepResult = oKeywords.navigate(datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 3, testStepsDataYCoordinate));
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);

				}
				
				else if (testModuleName.equalsIgnoreCase("SETTEXT")) {
					String objectType, actualObject, dataValue;
					objectType =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 1, testStepsDataYCoordinate);
					actualObject =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 2, testStepsDataYCoordinate);
					dataValue =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 3, testStepsDataYCoordinate);
					String testStepResult = oKeywords.setText(testCaseNames, objectType, actualObject, dataValue);
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);
				}
				
				else if (testModuleName.equalsIgnoreCase("CLICK")) {
					String objectType, actualObject;
					objectType =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 1, testStepsDataYCoordinate);
					actualObject =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 2, testStepsDataYCoordinate);
					String testStepResult = oKeywords.click(testCaseNames, objectType, actualObject);
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);
					
				}
				
				else if (testModuleName.equalsIgnoreCase("ISTEXTPRESENT") || testModuleName.equalsIgnoreCase("VERIFYTEXTPRESENT")) {
					String objectType, actualObject, textToVerify;
					objectType =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 1, testStepsDataYCoordinate);
					actualObject =  datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 2, testStepsDataYCoordinate);

					textToVerify = datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 3, testStepsDataYCoordinate);
					String testStepResult = oKeywords.isTextPresent(testCaseNames, objectType, actualObject, textToVerify);
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);
					
				}
				
				else if (testModuleName.equalsIgnoreCase("PAUSE")) {
					String pauseValue;
					pauseValue = datatableTestSteps.getCellData("Test Steps", testStepsDataXCoordinate + 3, testStepsDataYCoordinate);
					String testStepResult = oKeywords.pause(testCaseNames, pauseValue);
					
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);

				}
				
				else if (testModuleName.equalsIgnoreCase("CLOSEBROWSER") || testModuleName.equalsIgnoreCase("QUITEBROSWER")) {
					String testStepResult = oKeywords.quitBrowser();
					datatableTestSteps.setCellData("Test Steps", "I", testStepsDataYCoordinate, testStepResult);

				}
				
				testStepsDataXCoordinate = testStepsXCoordinate + 3; 
				testStepsDataYCoordinate = testStepsYCoordinate;
			}

			testStepsYCoordinate ++;
		}
		
	}
	
	public static void main (String [] str) throws IOException, NumberFormatException, InterruptedException {
		//logger.info("Reading all the modules which have run mode Yes.");
        readYesModules();
       
        //System.out.println("Test Finished");
        //readYesTestCases();
        
        //readTestSteps();
	}


}
