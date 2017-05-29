package com.wixsite.jingmacv.model;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Test {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/chromedriver_win32/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.nationalevacaturebank.nl/");

		// If there's a cookie message, it'll be clicked.
		if (driver.findElements(By.cssSelector("#form_save")).size() != 0)
			driver.findElement(By.cssSelector("#form_save")).click();
		
		// Finds and fills in input field "what".
		WebElement input = driver.findElement(By.cssSelector(".form-group input.ng-empty"));		
		input.sendKeys("java");
		// Finds and fills in input field "where".
		input = driver.findElement(By.cssSelector(".search-location-group input.ng-empty"));
		input.sendKeys("den haag");
		// Finds and submits the web form.
		WebElement submit = driver.findElement(By.cssSelector(".search-button button"));
		submit.click();
		
		// If there's a "next" button, it'll be clicked.
		List<WebElement> list = driver.findElements(By.cssSelector("li .arrow"));
		if (list.size() != 0) {
			list.get(1).click();
		}
	}
}
