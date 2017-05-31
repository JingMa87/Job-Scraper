package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * A web scraper class with one public function called scrape().
 */
public class MonsterboardScraper {
	
	// The main function of this class which scrapes a website for vacancy data.
	public static String scrape(String jobTitleInput, String locationInput) {
		// Resets the data in the jbs_vacancy table.
		DBConnection.resetTable();
		// Initializes a web driver with a website.
		WebDriver driver = initWebDriver("https://www.monsterboard.nl");
		// Waits for the cookie message to appear and then clicks it.
		WebDriverWait wait = new WebDriverWait(driver, 3000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.fa-times"))).click();
		// Finds and fills in input fields with a job title and location.
		fillInSearchTerms(driver, jobTitleInput, locationInput);
		// Loops over all pages and saves the job title, company and location in the database.
		String status = findAndSaveAllVacancies(driver);
		// Close browser.
		//driver.close();
		return status;
	}
	
	private static WebDriver initWebDriver(String url) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		return driver;
	}
	
	private static void fillInSearchTerms(WebDriver driver, String jobTitleInput, String locationInput) {
		// Finds and fills in input field "what".
		WebElement input = driver.findElement(By.cssSelector("#q1"));
		input.sendKeys(jobTitleInput);
		// Finds and fills in input field "where".
		input = driver.findElement(By.cssSelector("#where1"));
		input.sendKeys(locationInput);
		// Finds and submits the web form.
		WebElement submit = driver.findElement(By.cssSelector("#doQuickSearch"));
		submit.click();
	}
	
	private static String findAndSaveAllVacancies(WebDriver driver) {
		String status = null;
		int pageCount = 0;
		// Loops over all pages and saves the job title, company and location in the database.
		while (true) {
			pageCount++;
			// Retrieves a list of vacancies, each containing a job title, company and location.
			List<WebElement> list = driver.findElements(By.cssSelector(".js_result_container.primary"));
			// Loops over the vacancies.
			for (WebElement item : list) {
				WebElement jobTitleTag = null;
				WebElement companyTag = null;
				WebElement locationTag = null;
				String jobTitle = null;
				String company = null;
				String location = null;
				// Finds the job title, company and location per vacancy.
				if (item.findElements(By.cssSelector(".jobTitle")).size() != 0) {
					jobTitleTag = item.findElement(By.cssSelector(".jobTitle"));
					jobTitle = jobTitleTag.getText();
				}
				if (item.findElements(By.cssSelector(".company")).size() != 0) {
					companyTag = item.findElement(By.cssSelector(".company"));
					company = companyTag.getText();
				}
				if (item.findElements(By.cssSelector(".job-specs-location")).size() != 0) {
					locationTag = item.findElement(By.cssSelector(".job-specs-location"));
					location = locationTag.getText();
				}
				// If the company is unknown the user would still want to apply to the job.
				if (jobTitle != null && location != null)
					// Saves the vacancies in the database.
					status = DBConnection.saveVacancy(jobTitle, company, location + ", " + pageCount);
					if (status.equals("noData"))
						break;
				
			}
			WebDriverWait wait = new WebDriverWait(driver, 3000);			
			if (status.equals("noData") || wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.fa-times"))) == null)
				break;
			// Clicks on "next" and on overlays.
			clickNext(driver);
		}
		return status;
	}
	
	private static void clickNext(WebDriver driver) {
		// If there's an overlay, it'll be clicked.
		if (driver.findElements(By.cssSelector(".popup-close-icon")).size() != 0) {
			driver.findElement(By.cssSelector(".popup-close-icon")).click();
		}
		// Waits for the "next" button to appear and then clicks it.
		WebDriverWait wait = new WebDriverWait(driver, 3000);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".next"))).click();	
	}
	
	private static boolean retryClick(WebDriver driver, By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.findElement(by).click();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
            	e.printStackTrace();
            }
            attempts++;
        }
        return result;
	}
}
