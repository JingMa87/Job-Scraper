package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Test {
	
	private static WebDriverWait wait;
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.monsterboard.nl");
		wait = new WebDriverWait(driver, 3000);

		// Finds and fills in input field "what".
		WebElement input = driver.findElement(By.cssSelector("#q1"));		
		input.sendKeys("python");
		// Finds and fills in input field "where".
		input = driver.findElement(By.cssSelector("#where1"));
		input.sendKeys("amsterdam");
		// Finds and submits the web form.
		WebElement submit = driver.findElement(By.cssSelector("#doQuickSearch"));
		submit.click();
		
		// Waits for the cookie message to appear and then clicks it.
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.fa-times"))).click();

		int pageCount = 0;
		while (true) {
			System.out.println(++pageCount);
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".js_result_container.primary")));
			List<WebElement> list = driver.findElements(By.cssSelector(".js_result_container.primary"));
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
					System.out.println(jobTitle + ", " + company + ", " + location + ", " + pageCount);
				
			}
			// Checks for layover and clicks it if it exists.
			WebElement layover = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".popup-close-icon")));
			if (layover.isDisplayed()) {
				layover.click();
			}
			// Waits for the "next" button to appear and then clicks it.			
			if (driver.findElements(By.cssSelector(".next")).size() == 0) {
				System.out.println("BREAK!");
				break;
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".next"))).click();
		}
		driver.close();
	}
}
