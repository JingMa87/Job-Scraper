package com.wixsite.jingmacv.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wixsite.jingmacv.model.DBConnection;
import com.wixsite.jingmacv.model.MonsterboardScraper;
import com.wixsite.jingmacv.model.NationaleVacaturebankScraper;
import com.wixsite.jingmacv.model.IndeedScraper;

@WebServlet("/web-scrape")
public class WebScrapeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private HttpSession session;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jobTitle = request.getParameter("jobTitle");
		String location = request.getParameter("location");
		String status = null;
		session = request.getSession();
		
		if (jobTitle != null && location != null) {
			if (jobTitle.equals("") || location.equals("")) {
				request.setAttribute("emptyField", "Fill in both fields.");
			}
			else {
				String searchEngine = request.getParameter("searchEngine");
				setSelectedOption(session, searchEngine);
				if (searchEngine.equals("indeed")) {
					status = IndeedScraper.scrape(jobTitle, location);
				}
				else if (searchEngine.equals("nationaleVacaturebank")) {
					status = NationaleVacaturebankScraper.scrape(jobTitle, location);
				}
				else if (searchEngine.equals("monsterboard")) {
					status = MonsterboardScraper.scrape(jobTitle, location);
				}
				// Checks if the scraping was successful.
				if (status.equals("saved")) {
					request.setAttribute("successScrape", "Successfully scraped.");
					// Show the table in webScrape.jsp.
					boolean scraped = true;
					request.setAttribute("scraped", scraped);
				}
				else {
					request.setAttribute("noData", "No data has been scraped.");
				}
			}
		}
		// Makes a list of headers to generate table headers in webScrape.jsp.
		request.setAttribute("headers", getHeaders());
		// Retrieves vacancy data from the database and makes it available as a list in webScrape.jsp.
		request.setAttribute("vacancies", DBConnection.getResultSet());
		// Set values for the input fields.
		if (jobTitle != null && location != null) {
	    	session.setAttribute("jobTitle", jobTitle);
	    	session.setAttribute("location", location);
		}
		// Redirects user to index.jsp.
		request.getRequestDispatcher("WEB-INF/view/webScrape.jsp").forward(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
    }
	
	// Sets the html default option to the selected option.
	private static void setSelectedOption(HttpSession session, String searchEngine) {
		if (searchEngine.equals("indeed")) {
			session.setAttribute("indeedSelected", "selected=\"selected\"");
			session.setAttribute("nationaleVacaturebankSelected", "");
			session.setAttribute("monsterboardSelected", "");
		}
		else if (searchEngine.equals("nationaleVacaturebank")) {
			session.setAttribute("indeedSelected", "");
			session.setAttribute("nationaleVacaturebankSelected", "selected=\"selected\"");
			session.setAttribute("monsterboardSelected", "");
		}
		else {
			session.setAttribute("indeedSelected", "");
			session.setAttribute("nationaleVacaturebankSelected", "");
			session.setAttribute("monsterboardSelected", "selected=\"selected\"");
		}
	}
	
	// Returns a list of headers for the html table.
	private static ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<>();
		headers.add("Vacancy ID");
		headers.add("Job title");
		headers.add("Company");
		headers.add("Location");
		return headers;
	}
}