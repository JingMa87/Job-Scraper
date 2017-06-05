package com.wixsite.jingmacv.model;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

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
}
