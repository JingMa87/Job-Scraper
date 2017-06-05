package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * A web scraper class with one public function called scrape().
 */
public class NationaleVacaturebankScraper extends WebScraper {
	
	// The main function of this class which scrapes a website for vacancy data.
	public static String scrape(String jobTitleInput, String locationInput) {
		// Resets the data in the jbs_vacancy table.
		DBConnection.resetTable();
		// Initializes a web driver with a website.
		init("https://www.nationalevacaturebank.nl");
		// Waits for the cookie message to appear and then clicks it.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#form_save"))).click();
		// Finds and fills in input fields with a job title and location.
		fillInSearchTerms(jobTitleInput, locationInput, ".form-group input.ng-empty", ".search-location-group input.ng-empty", ".search-button button");
		// Loops over all pages and saves the job title, company and location in the database.
		String status = loopOverAllPages();
		// Close browser.
		driver.close();
		return status;
	}
	
	private static String loopOverAllPages() {
		String status = "";
		// Loops over all the pages.
		while (true) {
			// Prevents loading vacancies of the previous page.
			sleep(2);
			// Retrieves a list of vacancies, each containing a job title, company and location.
			List<WebElement> list = driver.findElements(By.cssSelector("div[data-ng-repeat='job in jobPage.jobs track by $index']"));
			// Loops over the list.
			for (WebElement vacancy : list) {
				status = findAndSaveVacancyInfo(vacancy, ".job-title", ".name", ".working-location span");
				if (status.equals("noData"))
					break;				
			}
			if (status.equals("noData") || driver.findElements(By.cssSelector("li .arrow")).size() == 0 
				|| driver.findElement(By.cssSelector("ul.pagination li[data-ng-show='currentPageNumber < totalNumberOfPages']")).getAttribute("class").equals("ng-hide"))
				break;
			// If there's a "next" button, it'll be clicked.
			driver.findElements(By.cssSelector("li .arrow")).get(1).click();
		}
		return status;
	}
}
