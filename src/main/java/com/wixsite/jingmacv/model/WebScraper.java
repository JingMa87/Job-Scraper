package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebScraper {
	
	public static String scrape(String jobTitleInput, String locationInput) {
		// Use this code block for running Selenium headless.
//		DesiredCapabilities caps = new DesiredCapabilities();
//        caps.setJavascriptEnabled(true);  
//        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:/Program Files (x86)/phantomjs-2.1.1/bin/phantomjs.exe");
//		WebDriver driver = new PhantomJSDriver(caps);
		
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.indeed.nl/?r=us");
		// Finding and filling in input field "what".
		WebElement input = driver.findElement(By.id("what"));
		input.sendKeys(jobTitleInput);
		// Finding and filling in input field "where".
		input = driver.findElement(By.id("where"));
		input.sendKeys(locationInput);
		// Find and submit the web form.
		WebElement submit = driver.findElement(By.id("fj"));
		submit.click();
		// Retrieve job title, company and location of Java vacancies.
		List<WebElement> jobTitles = driver.findElements(By.cssSelector("#resultsCol .jobtitle"));
		List<WebElement> companies = driver.findElements(By.cssSelector("#resultsCol .company"));
		List<WebElement> locations = driver.findElements(By.cssSelector("#resultsCol .location"));
		// Reset the data in the jng_vacancy table.
		DBConnection.resetTable();
		String status = null;
		for (int i = 0; i < jobTitles.size(); i++) {
			String jobTitle = jobTitles.get(i).getText();
			String company = companies.get(i).getText();
			String location = locations.get(i).getText();
			if (jobTitle != null && location != null)
				status = DBConnection.saveVacancy(jobTitle, company, location);
				if (status.equals("databaseError"))
					break;
		}
		// Close browser.
		driver.close();
		return status;
	}
}
