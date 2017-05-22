package com.wixsite.jingmacv.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wixsite.jingmacv.model.DBConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L; 
	private DBConnection conn = new DBConnection();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (conn.checkUsernamePassword(username, password)) {
			HttpSession session = request.getSession();
	    	session.setAttribute("username", username);
	    	request.getRequestDispatcher("web-scrape").forward(request, response);
		}
		else
			request.getRequestDispatcher("WEB-INF/view/register.jsp").forward(request, response);
	}

}
