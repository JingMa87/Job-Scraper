package com.wixsite.jingmacv.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wixsite.jingmacv.model.DBConnection;
import com.wixsite.jingmacv.model.Vacancy;
import com.wixsite.jingmacv.model.WebScraper;

@WebServlet("/web-scrape")
public class WebScrapeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jobTitle = request.getParameter("jobTitle");
		String location = request.getParameter("location");
		String status = null;
//		if (jobTitle.equals("") || location.equals("")) {
//			request.setAttribute("emptyField", "Fill in both fields.");
//		}
		if (jobTitle != null && location != null) {			
			status = WebScraper.scrape(jobTitle, location);
			if (status == "saved") {
				request.setAttribute("successCrawl", "Successfully scraped.");
			}
			else {
				request.setAttribute("databaseError", "Something went wrong in the database.");
			}
		}
		ArrayList<Vacancy> vacancies = DBConnection.getResultSet();
		// Will be available as ${vacancies} in JSP.
		request.setAttribute("vacancies", vacancies);
		// Set values in the input fields.
		request.setAttribute("jobTitle", jobTitle);
		request.setAttribute("location", location);
		// Redirects user to index.jsp.
		request.getRequestDispatcher("WEB-INF/view/webScrape.jsp").forward(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
    }
}