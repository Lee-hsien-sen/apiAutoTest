package com.qingzi.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.qingzi.testUtil.ReadProperties;
import com.qingzi.testUtil.fileUntil;



public class MyTestListener implements ITestListener{

	public static Integer SkippedCount=0;
	
	@Override
	public void onFinish(ITestContext arg0) {
		String Percentage=ReadProperties.GetTestPropertyByKey("Percentage");
		Integer Percentage_Integer=Integer.valueOf(Percentage);
		fileUntil.WriterFile_map(Percentage_Integer);
	}

	@Override
	public void onStart(ITestContext arg0) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	@Override
	public void onTestFailure(ITestResult arg0) {
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		SkippedCount++;
	}

	@Override
	public void onTestStart(ITestResult arg0) {
	}

	@Override
	public void onTestSuccess(ITestResult arg0) {
	}

}
