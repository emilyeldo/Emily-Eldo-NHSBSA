package stepDefinitions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.fasterxml.jackson.core.type.TypeReference;

import commonUtils.BaseUtil;
import commonUtils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;

import locators.JobDetails;
import locators.Search;
import pages.SearchPage;

import com.fasterxml.jackson.databind.ObjectMapper; // ✅ Use this one

public class SearchStep {
	public WebDriver driver;
	String jsonData = "";
	Map<String, String> testData;

	@Given("I am a jobseeker on NHS Jobs website")
	public void i_am_a_jobseeker_on_nhs_jobs_website() {

		driver = BaseUtil.getDriver();
		driver.findElement(By.xpath(Search.SEARCH_LINK_XPATH)).click();
		BaseUtil.waitForDOMToLoad(driver);

	}

	@When("I put my {string} preferences into the Search functionality")
	public void i_put_my_preferences_into_the_search_functionality(String preferenceType) {
		try {
			// 1. Load JSON string from config
			jsonData = ConfigReader.readProperty("testdata");
			// 2. Convert JSON back to a Map
			/*
			 * ObjectMapper mapper = new ObjectMapper(); testData =
			 * mapper.readValue(jsonData, new TypeReference<Map<String, String>>() {});
			 */
			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> fullTestData = mapper.readValue(jsonData, new TypeReference<Map<String, String>>() {
			});
System.out.println(fullTestData);
			// Remove null or blank values
			testData = fullTestData.entrySet().stream()
					.filter(entry -> entry.getValue() != null && !entry.getValue().trim().isEmpty())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			// 3. Use the preference type to control what fields to fill
			if (preferenceType.equalsIgnoreCase("location")) {
				SearchPage.enterDataInLocationField(driver, testData.get("location"));
			}
			if (preferenceType.equalsIgnoreCase("employer")) {
				SearchPage.ClickMoreSearchOption(driver);
				SearchPage.enterDataInEmployerField(driver, testData.get("Employer"));

			}
			if (preferenceType.equalsIgnoreCase("JobReference")) {
				SearchPage.ClickMoreSearchOption(driver);
				SearchPage.enterDataInJobReferenceField(driver, testData.get("Job_Reference"));

			}
			if (preferenceType.equalsIgnoreCase("salary")) {
				SearchPage.ClickMoreSearchOption(driver);
				SearchPage.selectPayRange(driver, testData.get("Pay_Range"));

			}
			if (preferenceType.equalsIgnoreCase("Job_Title_Or_Skill")) {
				SearchPage.enterDataInJobTitleField(driver, testData.get("Job_Title_Or_Skill"));

			}
			if (preferenceType.equalsIgnoreCase("few")) {

				SearchPage.enterDataInJobTitleField(driver, testData.get("Job_Title_Or_Skill"));
				SearchPage.enterDataInLocationField(driver, testData.get("location"));

			}
			if (preferenceType.equalsIgnoreCase("all")) {

				SearchPage.enterDataInJobTitleField(driver, testData.get("Job_Title_Or_Skill"));
				SearchPage.enterDataInLocationField(driver, testData.get("location"));
				SearchPage.ClickMoreSearchOption(driver);
				SearchPage.enterDataInJobReferenceField(driver, testData.get("Job_Reference"));
				SearchPage.enterDataInEmployerField(driver, testData.get("Employer"));
				SearchPage.selectPayRange(driver, testData.get("Pay_Range"));

			}
			if (preferenceType.equalsIgnoreCase("no")) {

			}
			SearchPage.ClickSearchButton(driver);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to populate search fields with test data", e);
		}

	}

	@Then("I should get a list of jobs which matches my preferences")
	public void i_should_get_list_of_jobs_matching_preferences() {

		if (testData.containsKey("location")) {
			BaseUtil.waitForElementVisible(driver,By.xpath(Search.JOB_RESULT_LOCATION_LIST_XPATH));
			List<WebElement> jobLocations = driver.findElements(By.xpath(Search.JOB_RESULT_LOCATION_LIST_XPATH));
			 boolean matchFound = jobLocations.stream()
				        .anyMatch(element -> element.getText()
				            .toLowerCase()
				            .contains(testData.get("location").toLowerCase()));
				    
				    Assert.assertTrue(matchFound,
				        "No job result matched the expected location: " + testData.get("location"));
				    /*

			for (WebElement loc : jobLocations) {
				
				if (loc.getText().contains(testData.get("location"))) {
					System.out.println("Matched location: " + loc.getText());
					matchFound = true;
					break;
				}
			}*/
				
				

			   		}
		
		if (testData.containsKey("Employer")) {
			BaseUtil.waitForElementVisible(driver,By.xpath(Search.JOB_RESULT_EMPLOYER_LIST_XPATH));

			List<WebElement> jobEmployer = driver.findElements(By.xpath(Search.JOB_RESULT_EMPLOYER_LIST_XPATH));
			boolean matchFoundEMployer = false;

			for (WebElement emp : jobEmployer) {

				if (emp.getText().contains(testData.get("Employer"))) {
					System.out.println("Matched employer: " + emp.getText());
					matchFoundEMployer = true;
					break;
				}
			}

			Assert.assertTrue(matchFoundEMployer,
					"No job result matched the expected employer: " + testData.get("Employer"));
		}
		if (testData.containsKey("Job_Title_Or_Skill")) {
			BaseUtil.waitForElementVisible(driver,By.xpath(Search.JOB_RESULT_TITLE_LIST_XPATH));
			List<WebElement> jobTitle = driver.findElements(By.xpath(Search.JOB_RESULT_TITLE_LIST_XPATH));
			boolean matchFoundJobTitle = false;

			for (WebElement title : jobTitle) {

				if (title.getText().contains(testData.get("Job_Title_Or_Skill"))) {
					System.out.println("Matched job title: " + title.getText());
					matchFoundJobTitle = true;
					break;
				}
			}

			Assert.assertTrue(matchFoundJobTitle,
					"No job result matched the expected jobtitle: " + testData.get("Job_Title_Or_Skill"));
		}

		if (testData.containsKey("Job_Reference")) {
			BaseUtil.waitForElementVisible(driver,By.xpath(Search.JOB_RESULT_TITLE_LIST_XPATH));
			List<WebElement> jobTitle = driver.findElements(By.xpath(Search.JOB_RESULT_TITLE_LIST_XPATH));
			boolean matchFoundJobReference = false;
			for (WebElement title : jobTitle) {
				title.click();

				BaseUtil.waitForDOMToLoad(driver);
				if (driver.findElement(By.xpath(JobDetails.JOB_PAGE_REFERENCE_XPATH)).getText()
						.contains(testData.get("Job_Reference"))) {
					System.out.println("Matched job reference: " + testData.get("Job_Reference"));
					matchFoundJobReference = true;
					driver.findElement(By.id(JobDetails.JOB_PAGE_BACKLINK_ID)).click();
					BaseUtil.waitForDOMToLoad(driver);
					break;
				}
			}

			Assert.assertTrue(matchFoundJobReference,
					"No job result matched the expected jobReference: " + testData.get("Job_Reference"));
		}

	}

	@Then("I should see {string} message")
	public void no__results_found(String message) {

		Assert.assertEquals(driver.findElement((By.id(Search.NO_RESULT_FOUND_ID))).getText(),message);
		

	}
	@Then("sort my search results with the {string}")
	public void sort_search_results_with_newest_date_posted(String sort) {

		SearchPage.SortJobResults(driver, sort);
		 // Set max pages to check (prevents infinite loops)
		// 2. Verify sorting across pages (with assertion)
	    final int MAX_PAGES_TO_CHECK = 3; // Optimal for NHS Jobs
	    List<Date> allDates = new ArrayList<>();
	    int currentPage = 1;
	    
	    do {
	        // Collect and verify current page
	        List<Date> pageDates = SearchPage.collectPublicationDates(driver);
	        allDates.addAll(pageDates);
	        
	        // Page-level assertion
	        assertPageSorting(pageDates, currentPage);
	        
	        currentPage++;
	    } while (currentPage <= MAX_PAGES_TO_CHECK && SearchPage.navigateToNextPage(driver));
	    
	    // Final cross-page assertion
	    assertCrossPageSorting(allDates);
	}

	private void assertPageSorting(List<Date> dates, int pageNumber) {
	    for (int i = 0; i < dates.size() - 1; i++) {
	        Assert.assertTrue(
	            dates.get(i).compareTo(dates.get(i + 1)) >= 0,
	            String.format("Sorting violation on page %d, position %d: %s should not be before %s",
	                pageNumber, i, dates.get(i), dates.get(i + 1))
	        );
	    }
	}

	private void assertCrossPageSorting(List<Date> allDates) {
	    for (int i = 0; i < allDates.size() - 1; i++) {
	        Assert.assertTrue(
	            allDates.get(i).compareTo(allDates.get(i + 1)) >= 0,
	            String.format("Cross-page sorting violation at index %d: %s should not be before %s",
	                i, allDates.get(i), allDates.get(i + 1))
	        );
	    }
	    
	    
	    Assert.assertFalse(allDates.isEmpty(), "No dates were collected for sorting verification");
	    System.out.println("Verified sorting for " + allDates.size() + " job postings");
	}

	@Then("the results should be sorted by {string} by default")
	public void the_results_should_be_sorted_by_default(String sort) {
		
		String actualSort = new Select(driver.findElement(By.id("sort")))
			    .getFirstSelectedOption()
			    .getText();
			Assert.assertEquals(actualSort, "Best Match");
		//Assert.assertEquals((driver.findElement(By.xpath(Search.DEFAULT_SORT_SELECTION)).getText()),"Best Match");
	}
	}

