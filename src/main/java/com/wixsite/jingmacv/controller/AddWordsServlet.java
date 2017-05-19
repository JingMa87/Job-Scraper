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

@WebServlet("/add-words")
public class AddWordsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jobTitle = request.getParameter("jobTitle");
		String location = request.getParameter("location");
		if (jobTitle.equals("") || location.equals("")) {
			request.setAttribute("emptyField", "Fill in a word.");
		}
		else if (jobTitle != null && location != null) {			
			try {
				WebScraper.scrape(jobTitle, location);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Vacancy> vacancies = DBConnection.setResultSet();
		// Will be available as ${vacancies} in JSP.
		request.setAttribute("vacancies", vacancies);
		// Redirects user to index.jsp.
		request.getRequestDispatcher("WEB-INF/view/data.jsp").forward(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
    }
}