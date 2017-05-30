package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * A web scraper class with one public function called scrape().
 */
public class NationaleVacaturebankScraper {
	
	// The main function of this class which scrapes a website for vacancy data.
	public static String scrape(String jobTitleInput, String locationInput) {
		// Resets the data in the jbs_vacancy table.
		DBConnection.resetTable();
		// Initializes a web driver with a website.
		WebDriver driver = initWebDriver("https://www.nationalevacaturebank.nl/");
		// If there's a cookie message, it'll be clicked.
		if (driver.findElements(By.cssSelector("#form_save")).size() != 0)
			driver.findElement(By.cssSelector("#form_save")).click();
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
		WebElement input = driver.findElement(By.cssSelector(".form-group input.ng-empty"));
		input.sendKeys(jobTitleInput);
		// Finds and fills in input field "where".
		input = driver.findElement(By.cssSelector(".search-location-group input.ng-empty"));
		input.sendKeys(locationInput);
		// Finds and submits the web form.
		WebElement submit = driver.findElement(By.cssSelector(".search-button button"));
		submit.click();
	}
	
	private static String findAndSaveAllVacancies(WebDriver driver) {
		String status = null;
		int pageCount = 0;
		// Loops over all pages and saves the job title, company and location in the database.
		while (true) {
			pageCount++;
			// To prevent loading vacancies of the previous page.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Retrieves a list of vacancies, each containing a job title, company and location.
			List<WebElement> list = driver.findElements(By.cssSelector("div[data-ng-repeat='job in jobPage.jobs track by $index']"));
			System.out.println("Size: " + list.size());
			// Loops over the vacancies.
			for (WebElement item : list) {
				WebElement jobTitleTag = null;
				WebElement companyTag = null;
				WebElement locationTag = null;
				String jobTitle = null;
				String company = null;
				String location = null;
				// Finds the job title, company and location per vacancy.
				if (item.findElements(By.cssSelector(".job-title")).size() != 0) {
					jobTitleTag = item.findElement(By.cssSelector(".job-title"));
					jobTitle = jobTitleTag.getText();
				}
				if (item.findElements(By.cssSelector(".name")).size() != 0) {
					companyTag = item.findElement(By.cssSelector(".name"));
					company = companyTag.getText();
				}
				if (item.findElements(By.cssSelector(".working-location span")).size() != 0) {
					locationTag = item.findElement(By.cssSelector(".working-location span"));
					location = locationTag.getText();
				}
				// If the company is unknown the user would still want to apply to the job.
				if (jobTitle != null && location != null)
					// Saves the vacancies in the database.
					status = DBConnection.saveVacancy(jobTitle, company, location + ", " + pageCount);
					if (status.equals("noData"))
						break;
				
			}
			if (status.equals("noData") || driver.findElements(By.cssSelector("li .arrow")).size() == 0 
				|| driver.findElement(By.cssSelector("ul.pagination li[data-ng-show='currentPageNumber < totalNumberOfPages']")).getAttribute("class").equals("ng-hide"))
				break;
			// Clicks on "next" and on overlays.
			clickNext(driver);
		}
		return status;
	}
	
	private static void clickNext(WebDriver driver) {
		// If there's a "next" button, it'll be clicked.
		retryClick(driver, By.cssSelector("li .arrow"));
		// If there's an overlay, it'll be clicked.
		if (driver.findElements(By.cssSelector("#popover-close-link")).size() != 0)
			driver.findElement(By.cssSelector("#popover-close-link")).click();		
	}
	
	private static boolean retryClick(WebDriver driver, By by) {
        boolean result = false;
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.findElements(by).get(1).click();
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
