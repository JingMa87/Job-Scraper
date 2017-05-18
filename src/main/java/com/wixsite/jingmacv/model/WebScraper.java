package com.wixsite.jingmacv.model;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
	
	public static void scrape() {
		try {
			System.out.println("HOME MENU SCRAPES:");
			Document doc = Jsoup.connect("https://www.mcdonalds.com/us/en-us.html").timeout(6000).get();
			// Selects all headline text.
			Elements divs = doc.select("div.headline-text");
			for (Element div: divs) {
				for (Element p: div.select("p")) {
					String text = p.text();
					if (!text.equals(""))
						System.out.println(text);
				}
				System.out.println();
			}
			
			System.out.println("TRENDING NOW SCRAPES:");
			doc = Jsoup.connect("https://www.mcdonalds.com/us/en-us/full-menu/drinks.html").timeout(6000).get();
			// Selects all slider text. 
			divs = doc.select("div.text-wrapper h3, div.text-wrapper h4, div.text-wrapper p");
			for (Element div: divs) {
				String text = div.text();
				System.out.println(text);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		scrape();
	}
}
