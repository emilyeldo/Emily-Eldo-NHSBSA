package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
	    features = "@target/failed_scenarios.txt",
	    glue = {"stepDefinitions"},
	    plugin = {
	        "pretty",
	        "html:target/rerun-reports",
	        "json:target/rerun.json"
	    }
	)
	public class FailedTestRunner extends AbstractTestNGCucumberTests {}