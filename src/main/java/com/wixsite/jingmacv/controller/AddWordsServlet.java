package com.wixsite.jingmacv.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wixsite.jingmacv.model.DBConnection;
import com.wixsite.jingmacv.model.Jng_word;

@WebServlet("/add-words")
public class AddWordsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String status = null;
		String word = request.getParameter("word");
		if (word == "") {
			request.setAttribute("emptyField", "Fill in a word.");
		}
		else if (word != null) {			
			status = DBConnection.addWord(word);
			if (status.equals("wordAdded")) {
				request.setAttribute("wordAdded", "Word added.");
			}
			else {
				request.setAttribute("wordAlreadyInList", "This word is already in the list.");
			}
		}
		ArrayList<Jng_word> words = DBConnection.setResultSet();
		// Will be available as ${words} in JSP.
		request.setAttribute("words", words);
		// Redirects user to index.jsp.
		request.getRequestDispatcher("WEB-INF/view/data.jsp").forward(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
    }
}