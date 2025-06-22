package stepDefinitions;

import commonUtils.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    
    @Before
    public void beforeScenario(Scenario scenario) {
    	ConfigReader.setProperty("currentScenario", scenario.getName());
    }

}
