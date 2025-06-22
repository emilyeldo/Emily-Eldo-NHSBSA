package commonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseUtil {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    static ConfigReader cr = new ConfigReader();

    public static void setDriver() {
        if (driver.get() == null) {
            String browser = cr.getBrowser("browser").toLowerCase();
            switch (browser) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver());
                    break;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver());
                    break;
            }

            driver.get().get(ConfigReader.readProperty("url"));
            driver.get().manage().window().maximize();
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
           driver.remove(); 
        }
    }
    public static void waitForDOMToLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(5000)).until(
            webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete")
        );
    }
    public static  void waitForElementVisible(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5000));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
 

    public static String captureScreenshot(String testName) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = testName.replaceAll("\\s+", "_") + "_" + timestamp + ".png";
        String relativePath = "Screenshots/" + filename;
        String fullPath = ExtentManager.getReportDirectory() + "/" + relativePath;

        try {
            File src = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(Paths.get(ExtentManager.getReportDirectory(), "Screenshots"));
            Files.copy(src.toPath(), Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return relativePath;
    }



}