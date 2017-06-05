package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * A web scraper class with one public function called scrape().
 */
public class IndeedScraper extends WebScraper {
	
	/*
	 *  The main function of this class which scrapes a website for vacancy data.
	 */
	public static String scrape(String jobTitleInput, String locationInput) {
		// Resets the data in the jbs_vacancy table.
		DBConnection.resetTable();
		// Initializes a web driver with a website.
		init("https://www.indeed.nl/?r=us");
		// Finds and fills in input fields with a job title and location.
		fillInSearchTerms(jobTitleInput, locationInput, "#what", "#where", ".input_submit");
		// Loops over all pages and saves the job title, company and location in the database.
		String status = loopOverAllPages();
		// Close browser.
		driver.close();
		return status;
	}
	
	/*
	 * Loops over all the pages and finds the vacancies.
	 */
	private static String loopOverAllPages() {
		String status = "";		
		// Exits the function if there's no vacancies.
		if (driver.findElements(By.cssSelector(".no_results")).size() != 0)
			return status = "noData";
		// Loops over all the pages.
		while (true) {
			// Prevents loading vacancies of the previous page.
			sleep(1);
			// Retrieves a list of vacancies, each containing a job title, company and location.
			List<WebElement> list = driver.findElements(By.cssSelector(".result"));
			// Loops over the list.
			for (WebElement vacancy : list) {
				status = findAndSaveVacancyInfo(vacancy, "#resultsCol .jobtitle", "#resultsCol .company", "#resultsCol .location");
				if (status.equals("noData"))
					break;
			}
			if (status.equals("noData") || driver.findElements(By.partialLinkText("Volgende")).size() == 0)
				break;
			// Waits for the "next" button to appear and then clicks it.
			wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Volgende"))).click();
			// If there's an overlay, it'll be clicked.
			if (driver.findElements(By.cssSelector("#popover-close-link")).size() != 0)
				driver.findElement(By.cssSelector("#popover-close-link")).click();
		}
		return status;
	}
}
