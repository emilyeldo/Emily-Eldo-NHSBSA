package runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import commonUtils.BaseUtil;
import commonUtils.ConfigReader;
import commonUtils.ExcelUtil;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import io.cucumber.testng.TestNGCucumberRunner;

@CucumberOptions(features = "src/test/java/features", glue = {"stepDefinitions"},plugin = {
        "pretty",
        "html:target/cucumber-reports",
        "json:target/cucumber.json",
        "rerun:target/failed_scenarios.txt"
    }
)
public class TestRunner {

    private TestNGCucumberRunner testRunner;
    private static List<String> excelTestCases;
    private static Map<String, Map<String, String>> scenarioDataMap;

    @BeforeClass(alwaysRun = true)
    public void setup() throws IOException {
    	  ExcelUtil.clearTestResultsColumn(); 
        testRunner = new TestNGCucumberRunner(this.getClass());
        BaseUtil.setDriver();
        scenarioDataMap = ExcelUtil.getAllTestDataMap();
        excelTestCases = ExcelUtil.getTestCasesToBeRun();
    }
/*
    @DataProvider(parallel = false) 
    public Object[][] getTestCases() {
        Object[][] allScenarios = testRunner.provideScenarios();
        return Arrays.stream(allScenarios)
            .filter(objArr -> {
                PickleWrapper pickleWrapper = (PickleWrapper) objArr[0];
                String scenarioName = pickleWrapper.getPickle().getName().trim();
                return excelTestCases.stream()
                        .anyMatch(excelName -> excelName.equalsIgnoreCase(scenarioName));
            })
            .toArray(Object[][]::new);
    }
    
   */
    @DataProvider(parallel = false)
    public Object[][] getTestCases() {
        Object[][] allScenarios = testRunner.provideScenarios();
        String runMode = System.getProperty("runMode", "excel"); // "excel" or "tag"
        String tagFilter = System.getProperty("tagFilter", "@Regression").toLowerCase();

        List<Object[]> filteredScenarios = new ArrayList<>();

        for (Object[] scenario : allScenarios) {
            PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
            String scenarioName = pickleWrapper.getPickle().getName().trim();

            if ("tag".equalsIgnoreCase(runMode)) {
                List<String> tags = new ArrayList<>();
                for (Object tagObj : pickleWrapper.getPickle().getTags()) {
                    String tagName = tagObj.toString().toLowerCase(); // Safe fallback
                    tags.add(tagName);
                }
                if (tags.contains(tagFilter)) {
                    filteredScenarios.add(scenario);
                }
            } else {
                for (String excelName : excelTestCases) {
                    if (excelName.equalsIgnoreCase(scenarioName)) {
                        filteredScenarios.add(scenario);
                        break;
                    }
                }
            }
        }

        return filteredScenarios.toArray(new Object[0][]);
    }
    @Test(dataProvider = "getTestCases")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) throws JsonProcessingException {
        String scenarioName = pickleWrapper.getPickle().getName().trim();
        Map<String, String> scenarioData = scenarioDataMap.get(scenarioName);
        String jsonData = new ObjectMapper().writeValueAsString(scenarioData);
        ConfigReader.setProperty("testdata", jsonData);
        testRunner.runScenario(pickleWrapper.getPickle());
    }

    @AfterClass(alwaysRun = true)
    public void finish() {
        if (testRunner != null) {
            testRunner.finish();
        }
        BaseUtil.tearDown(); 
    }
}