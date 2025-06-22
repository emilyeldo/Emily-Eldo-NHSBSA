package listeners;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import commonUtils.BaseUtil;
import commonUtils.ConfigReader;
import commonUtils.ExcelUtil;

public class Listener implements ITestListener{
    private static ExtentReports extent = commonUtils.ExtentManager.getInstance();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Override
    public void onTestStart(ITestResult result) {
        // Called each time a test method starts
        System.out.println("Test started: " + ConfigReader.readProperty("currentScenario"));
        ExtentTest extentTest = extent.createTest(ConfigReader.readProperty("currentScenario"));
        test.set(extentTest);
        // Store start time uniquely per test
        ConfigReader.setProperty( ConfigReader.readProperty("currentScenario") + "_starttime", String.valueOf(System.currentTimeMillis()));

    }

    @Override
    public void onTestSuccess(ITestResult result) {
     /*
        try {
            long endTimeMillis = System.currentTimeMillis();
            ConfigReader.setProperty("endTime", String.valueOf(endTimeMillis));
            test.get().log(Status.PASS, "Test passed");
            String screenshotPath = BaseUtil.captureScreenshot(ConfigReader.readProperty("currentScenario"));
            test.get().addScreenCaptureFromPath(screenshotPath);
			ExcelUtil.updateTestResultsInExcel("Pass",ConfigReader.readProperty("currentScenario"));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}*/
    	
    	
    	String scenarioName = ConfigReader.readProperty("currentScenario");
    	long endTimeMillis = System.currentTimeMillis();
    	ConfigReader.setProperty("endTime", String.valueOf(endTimeMillis));

    	String startTimeStr = ConfigReader.readProperty(scenarioName + "_starttime");
    	 String screenshotPath = BaseUtil.captureScreenshot(ConfigReader.readProperty("currentScenario"));
         test.get().addScreenCaptureFromPath(screenshotPath);
    	if (startTimeStr != null) {
    	    long startMillis = Long.parseLong(startTimeStr);
    	    long durationMillis = endTimeMillis - startMillis;
    	    String formattedDuration = formatDuration(durationMillis);

    	    test.get().log(Status.INFO, "Execution time: " + formattedDuration);
    	    try {
				ExcelUtil.updateTestResultsInExcel("Pass", scenarioName, formattedDuration);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }

    private String formatDuration(long durationMillis) {
        long minutes = durationMillis / (60 * 1000);
        long seconds = (durationMillis % (60 * 1000)) / 1000;
        long millis = durationMillis % 1000;

        if (minutes > 0) {
            return String.format("%d minute%s %d second%s", 
                minutes, minutes == 1 ? "" : "s", 
                seconds, seconds == 1 ? "" : "s");
        } else if (seconds > 0) {
            return String.format("%d second%s", seconds, seconds == 1 ? "" : "s");
        } else {
            return String.format("%d second%s %03d milliseconds", 
                seconds, seconds == 1 ? "" : "s", millis);
        }
    }

	@Override
    public void onTestFailure(ITestResult result) {
    	/* test.get().log(Status.FAIL, "Test failed: " + result.getThrowable());
    	 
    	   String screenshotPath = BaseUtil.captureScreenshot(ConfigReader.readProperty("currentScenario"));
    	   test.get().addScreenCaptureFromPath(screenshotPath, "Screenshot");
      /* 	try {
			//ExcelUtil.updateTestResultsInExcel("Fail",ConfigReader.readProperty("currentScenario"));
       		ExcelUtil.updateTestResultsInExcel("Pass", scenarioName, formattedDuration);
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

    }*/
		
		
		String scenarioName = ConfigReader.readProperty("currentScenario");
    	long endTimeMillis = System.currentTimeMillis();
    	ConfigReader.setProperty("endTime", String.valueOf(endTimeMillis));

    	String startTimeStr = ConfigReader.readProperty(scenarioName + "_starttime");
    	 String screenshotPath = BaseUtil.captureScreenshot(ConfigReader.readProperty("currentScenario"));
         test.get().addScreenCaptureFromPath(screenshotPath);
    	if (startTimeStr != null) {
    	    long startMillis = Long.parseLong(startTimeStr);
    	    long durationMillis = endTimeMillis - startMillis;
    	    String formattedDuration = formatDuration(durationMillis);

    	    test.get().log(Status.INFO, "Execution time: " + formattedDuration);
    	    try {
				ExcelUtil.updateTestResultsInExcel("Fail", scenarioName, formattedDuration);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}

    @Override
    public void onTestSkipped(ITestResult result) {
 
        System.out.println("Test skipped: " + result.getName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
   
        System.out.println("Test failed but within success percentage: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {

        System.out.println("Starting test: " + context.getName());
        long startTimeMillis = System.currentTimeMillis();
        ConfigReader.setProperty("starttime", String.valueOf(startTimeMillis));
    }


	@Override
    public void onFinish(ITestContext context) {
      
		 extent.flush();

 
    }

}
