package com.wixsite.jingmacv.model;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Abstract superclass that initializes shared objects and functions. 
 */
public abstract class WebScraper {

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor js;

	/*
	 * Initializes a Chrome web driver that represents a website DOM. 
	 */
	public static void init(String url) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, 3000);
	}
	
	/*
	 * Finds and fills in the job title and location in the search engine, then clicks submit.
	 */
	public static void fillInSearchTerms(String jobTitleInput, String locationInput, 
										 String jobTitleCss, String locationCss, String submitCss) {
		// Finds and fills in the job title.
		WebElement input = driver.findElement(By.cssSelector(jobTitleCss));
		input.sendKeys(jobTitleInput);
		// Finds and fills in the location.
		input = driver.findElement(By.cssSelector(locationCss));
		input.sendKeys(locationInput);
		// Finds and presses submit.
		WebElement submit = driver.findElement(By.cssSelector(submitCss));
		submit.click();
	}
	
	/*
	 * Finds the job title, company and location of the vacancy and saves it in the database.
	 */
	public static String findAndSaveVacancyInfo(WebElement vacancy, String jobTitleCss, String companyCss, String locationCss) {
		String status = "";
		String jobTitle = null;
		String company = null;
		String location = null;
		// Finds the job title, company and location per vacancy.
		if (vacancy.findElements(By.cssSelector(jobTitleCss)).size() != 0) {
			WebElement jobTitleTag = vacancy.findElement(By.cssSelector(jobTitleCss));
			jobTitle = jobTitleTag.getText();
		}
		if (vacancy.findElements(By.cssSelector(companyCss)).size() != 0) {
			WebElement companyTag = vacancy.findElement(By.cssSelector(companyCss));
			company = companyTag.getText();
		}
		if (vacancy.findElements(By.cssSelector(locationCss)).size() != 0) {
			WebElement locationTag = vacancy.findElement(By.cssSelector(locationCss));
			location = locationTag.getText();
		}
		// If the company is unknown the user would still want to apply to the job.
		if (jobTitle != null && location != null)
			// Saves the vacancies in the database.
			status = DBConnection.saveVacancy(jobTitle, company, location);
		return status;
	}
	
	/*
	 * Lets the app wait n seconds before the next operation.
	 */
	public static void sleep(int seconds) {
		seconds *= 1000;
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
