package pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import commonUtils.BaseUtil;
import locators.Search;

public class SearchPage {

	public static void enterDataInLocationField(WebDriver driver, String location) {
		WebElement locationInput = driver.findElement(By.id(Search.LOCATION_ID));
		locationInput.clear();
		locationInput.sendKeys(location);

	}

	public static void enterDataInJobTitleField(WebDriver driver, String jobTitle) {
		WebElement locationInput = driver.findElement(By.id(Search.JOB_TITLE_ID));
		locationInput.clear();
		locationInput.sendKeys(jobTitle);

	}

	public static void enterDataInJobReferenceField(WebDriver driver, String jobReferenceID) {
		WebElement locationInput = driver.findElement(By.id(Search.JOB_REFERENCE_ID));
		locationInput.clear();
		locationInput.sendKeys(jobReferenceID);

	}

	public static void enterDataInEmployerField(WebDriver driver, String employer) {
		WebElement locationInput = driver.findElement(By.id(Search.EMPLOYER_ID));
		locationInput.clear();
		locationInput.sendKeys(employer);
		if (locationInput.getText().equalsIgnoreCase(employer)) {
			System.out.println("data entered");
		}

	}

	public static void selectPayRange(WebDriver driver, String payRange) {
		BaseUtil.waitForElementVisible(driver, By.id(Search.PAY_RANGE_ID));
		driver.findElement(By.id(Search.PAY_RANGE_ID)).click();
		Select oSelect = new Select(driver.findElement(By.id(Search.PAY_RANGE_ID)));
		List<WebElement> options = oSelect.getOptions();

		boolean found = false;

		for (WebElement option : options) {
			if (option.getText().equalsIgnoreCase(payRange)) {
				option.click();
				found = true;
				break;
			}
		}

		if (!found) {
			System.out.println("Option not found.");
		}

	}

	public static void ClickSearchButton(WebDriver driver) {
		driver.findElement(By.id(Search.SEARCH_BUTTON_ID)).click();

	}

	public static void ClickMoreSearchOption(WebDriver driver) {
		driver.findElement(By.id(Search.SEARCH_OPTIONS_LINK_ID)).click();
		BaseUtil.waitForDOMToLoad(driver);

	}

	public static boolean isSalaryInRange(String salaryText, int minSalary, int maxSalary) {
		Pattern pattern = Pattern.compile("£(\\d{1,3}(,\\d{3})*)(?:\\s*to\\s*£(\\d{1,3}(,\\d{3})*))?");
		Matcher matcher = pattern.matcher(salaryText);

		if (matcher.find()) {
			String lowerStr = matcher.group(1).replace(",", "");
			String upperStr = matcher.group(3) != null ? matcher.group(3).replace(",", "") : lowerStr;

			int lower = Integer.parseInt(lowerStr);
			int upper = Integer.parseInt(upperStr);

			return lower >= minSalary && upper <= maxSalary;
		}

		return false;
	}

	public static void SortJobResults(WebDriver driver, String sort) {
		if (sort.equalsIgnoreCase("newest Date Posted")) {
			driver.findElement(By.id(Search.SORT_BY_ID)).click();
			Select sortDropDown = new Select(driver.findElement(By.id(Search.SORT_BY_ID)));
			List<WebElement> options = sortDropDown.getOptions();

			boolean found = false;

			for (WebElement option : options) {
				if (option.getText().equalsIgnoreCase("Date Posted (newest)")) {
					option.click();
					found = true;
					BaseUtil.waitForDOMToLoad(driver);
					break;
				}
			}

			if (!found) {
				System.out.println("Option not found.");
			}

		}
	}
	
	public static void verifyPageSortOrder(List<Date> dates,WebDriver driver) {
	    for (int i = 0; i < dates.size() - 1; i++) {
	        Assert.assertTrue(
	            dates.get(i).compareTo(dates.get(i + 1)) >= 0,
	            "Dates not in descending order: " + 
	            dates.get(i) + " should be after " + dates.get(i + 1)
	        );
	    }
	}

	public static boolean navigateToNextPage(WebDriver driver) {
	    try {
	        WebElement nextButton = driver.findElement(
	            By.xpath("//a[contains(@class, 'nhsuk-pagination__link--next')]")
	        );
	        
	        if (nextButton.isEnabled()) {
	            nextButton.click();
	            BaseUtil.waitForDOMToLoad(driver);
	            return true;
	        }
	    } catch (NoSuchElementException e) {
	        // Next button not found
	    }
	    return false;
	}

	public static void verifyFullSortOrder(List<Date> allDates,WebDriver driver) {
	    for (int i = 0; i < allDates.size() - 1; i++) {
	        Assert.assertTrue(
	            allDates.get(i).compareTo(allDates.get(i + 1)) >= 0,
	            "Cross-page sort violation: " + 
	            allDates.get(i) + " should be after " + allDates.get(i + 1)
	        );
	    }
	}
	public static List<Date> collectPublicationDates(WebDriver driver) {
	    List<WebElement> dateElements = driver.findElements(
	        By.xpath(Search.DATE_PUBLISHED_XPATH)
	    );
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.UK);
	    List<Date> dates = new ArrayList<>();
	    
	    for (WebElement element : dateElements) {
	        String dateText = element.getText();
	        try {
	            Date date = formatter.parse(dateText);
	            dates.add(date);
	        } catch (ParseException e) {
	            throw new RuntimeException("Failed to parse date: " + dateText, e);
	        }
	    }
	    return dates;
	}
	public static void verifyFullSortOrder(List<Date> allDates) {
	    for (int i = 0; i < allDates.size() - 1; i++) {
	        Assert.assertTrue(
	            allDates.get(i).compareTo(allDates.get(i + 1)) >= 0,
	            "Cross-page sort violation: " + 
	            allDates.get(i) + " should be after " + allDates.get(i + 1)
	        );
	    }
	}

	public static boolean isSortedDescending(List<Date> dates) {
	    for (int i = 0; i < dates.size() - 1; i++) {
	        if (dates.get(i).before(dates.get(i + 1))) {
	            return false;
	        }
	    }
	    return true;
	}
	
	
}
