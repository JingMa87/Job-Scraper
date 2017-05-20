package com.wixsite.jingmacv.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wixsite.jingmacv.model.DBConnection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String status = DBConnection.registerUsernamePassword(username, password);
	    if (status == "registered") {
	    	HttpSession session = request.getSession();
	    	session.setAttribute("username", username);
	    	request.getRequestDispatcher("login.jsp").forward(request, response);
	    }
	    else if (status == "uniqueConstraint") {
	    	request.setAttribute("uniqueConstraint", "Username and password already exist.");
	    	request.getRequestDispatcher("WEB-INF/view/register.jsp").forward(request, response);
	    }
	    else {
	    	request.setAttribute("incorrectUsernamePassword", "Enter a correct username and password.");
	    	request.getRequestDispatcher("WEB-INF/view/register.jsp").forward(request, response);
	    }
	}
}
