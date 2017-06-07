package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * A web scraper class with one public function called scrape().
 */
public class MonsterboardScraper extends WebScraper {
	
	// The main function of this class which scrapes a website for vacancy data.
	public static String scrape(String jobTitleInput, String locationInput) {
		// Resets the data in the jbs_vacancy table.
		resetTable();
		// Initializes a web driver with a website.
		init("https://www.monsterboard.nl");
		// Finds and fills in input fields with a job title and location.
		fillInSearchTerms(jobTitleInput, locationInput, "#q1", "#where1", "#doQuickSearch");
		// Waits for the cookie message to appear and then clicks it.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.fa-times"))).click();
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
			// Retrieves a list of vacancies, each containing a job title, company and location.
			List<WebElement> list = driver.findElements(By.cssSelector(".js_result_container.primary"));
			// Loops over the list.
			for (WebElement vacancy : list) {
				status = findAndSaveVacancyInfo(vacancy, ".jobTitle", ".company", ".job-specs-location");
				if (status.equals("noData"))
					break;
			}
			if (status.equals("noData") || driver.findElements(By.cssSelector(".next")).size() == 0)
				break;
			// Waits for the "next" button to appear and then clicks it.
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".next"))).click();
		}
		return status;
	}
}
