package com.wixsite.jingmacv.model;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Abstract superclass that initializes shared objects and functions. 
 */
public abstract class WebScraper extends DBUtil {

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor js;
	
	/*
	 * Resets the jbs_vacancy table.
	 */
	public static void resetTable() {
		// Deletes all data from the jbs_vacancy table.
		try {
			conn = DBUtil.getConn();
			stmt = conn.createStatement();
			stmt.executeQuery("DELETE FROM jbs_vacancy");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
	    	// Closes all transaction objects.
		    DBUtil.close(stmt);
		    DBUtil.close(conn);
	    }
	}

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
			status = saveVacancy(jobTitle, company, location);
		return status;
	}
	
	// Adds a vacancy object to the database.
	public static String saveVacancy(String jobTitle, String company, String location) {
		String status = null;
		try {
			// Insert vacancy into database.
	    	conn = DBUtil.getConn();
	    	pstmt = conn.prepareStatement("INSERT INTO jbs_vacancy (id, job_title, company, location) " +
	    								 "VALUES ((SELECT NVL(MAX(id) + 1, 1) FROM jbs_vacancy), ?, ?, ?)");
	    	pstmt.setString(1, jobTitle);
	    	pstmt.setString(2, company);
	    	pstmt.setString(3, location);
	    	pstmt.executeUpdate();
	    	status = "saved";
	    } catch (SQLIntegrityConstraintViolationException se) {
	    	se.printStackTrace();
	    } catch (SQLException se) {
	    	status = "noData";
	    	se.printStackTrace();
	    } finally {
	    	// Closes all transaction objects.
		    DBUtil.close(pstmt);
		    DBUtil.close(conn);
	    }
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
	
	/*
	 * Returns all the vacancies in ArrayList format.
	 */
	public static ArrayList<Vacancy> getVacancies() {
		ArrayList<Vacancy> vacancies = new ArrayList<>();
		try {
	    	// Retrieves vacancy objects into the ResultSet.
	    	String sqlGetData = "SELECT id, job_title, company, location FROM jbs_vacancy ORDER BY id";
	    	conn = DBUtil.getConn();
	    	stmt = conn.createStatement();
	    	rs = stmt.executeQuery(sqlGetData);
	    	while (rs.next()) {
	            Vacancy vacancy = new Vacancy(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
	            vacancies.add(vacancy);
	    	}
	    } catch (SQLException se) {
	    	se.printStackTrace();
	    } finally {
	    	// Closes all transaction objects.
		    DBUtil.close(rs);
		    DBUtil.close(stmt);
		    DBUtil.close(conn);
	    }
		return vacancies;
	}
}
