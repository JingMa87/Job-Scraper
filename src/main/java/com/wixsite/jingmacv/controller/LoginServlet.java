package com.wixsite.jingmacv.controller;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wixsite.jingmacv.model.DBUtil;
import com.wixsite.jingmacv.model.Login;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 
	
	/*
	 * Initializes the DataSource.
	 */
	public void init(ServletConfig config) {
		DBUtil.initDataSource();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Gets username and password.
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		// Checks if the user is registered.
		if (Login.checkUsernamePassword(username, password)) {
			// If true, makes the username available in other screens and directs user to the main screen.
			HttpSession session = request.getSession();
	    	session.setAttribute("username", username);
	    	request.getRequestDispatcher("WEB-INF/view/webScraper.jsp").forward(request, response);
		}
		else
			// Sends the user to the register screen.
			request.getRequestDispatcher("WEB-INF/view/register.jsp").forward(request, response);
	}
}
