package commonUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;
	private static String reportDir;

	public static ExtentReports getInstance() {
		if (extent == null) {
			String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
			reportDir = System.getProperty("user.dir") + "/Reports/Execution_" + timestamp;
			new File(reportDir).mkdirs();

			ExtentSparkReporter reporter = new ExtentSparkReporter(reportDir + "/ExecutionReport.html");
			reporter.config().setReportName("Test Execution Report");
			reporter.config().setDocumentTitle("Job Website Automation");

			extent = new ExtentReports();
			extent.attachReporter(reporter);
			extent.setSystemInfo("Tester", "Emily Eldo");
		}
		return extent;
	}

	public static String getReportDirectory() {
		return reportDir;
	}
}