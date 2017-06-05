package com.wixsite.jingmacv.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wixsite.jingmacv.model.PDFGenerator;
import com.wixsite.jingmacv.model.XMLGenerator;

@WebServlet("/generate-document")
public class GenerateDocumentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Checks for the type of button pressed (XML or PDF) and generates the document.
		String documentType = request.getParameter("doc");
		if (documentType.equals("XML")) {
			XMLGenerator.generateXML();
			request.setAttribute("xmlDownloaded", "XML successfully downloaded.");
		}
		else if (documentType.equals("PDF")) {
			PDFGenerator.generatePDF();
			request.setAttribute("pdfDownloaded", "PDF successfully downloaded.");
		}
		else {
			request.setAttribute("documentError", "Something went wrong.");
		}
		// Puts the job title and location in the input fields.
		request.getRequestDispatcher("web-scrape").forward(request, response);
	}

}
