package com.wixsite.jingmacv.model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGenerator {
	
	public static void generatePDF() {
		Document doc = new Document();
		try {
			String myHome = System.getenv("userprofile");
			PdfWriter.getInstance(doc, new FileOutputStream(myHome + "/Downloads/Oracle2PDF.pdf"));
	        doc.open();
	        // Generates a table with three columns.
	        PdfPTable table = new PdfPTable(3);
	        // Generates a table cell.
	        PdfPCell cell = null;
	        
	        ArrayList<Vacancy> vacancies = DBConnection.setResultSet();
	        for (Vacancy vacancy : vacancies) {
	        	int vacancy_id = vacancy.getVacancyID();
	        	cell = new PdfPCell(new Phrase(Integer.toString(vacancy_id)));
	            table.addCell(cell);
	            
	            String jobTitle = vacancy.getJobTitle();
	            cell = new PdfPCell(new Phrase(jobTitle));
	            table.addCell(cell);
	            
	            String company = vacancy.getCompany();
	            cell = new PdfPCell(new Phrase(company));
	            table.addCell(cell);
	            
	            String location = vacancy.getLocation();
	            cell = new PdfPCell(new Phrase(location));
	            table.addCell(cell);
	        }	    	
            // Adds the table to the PDF.
            doc.add(table);                       
            doc.close();
        } catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
