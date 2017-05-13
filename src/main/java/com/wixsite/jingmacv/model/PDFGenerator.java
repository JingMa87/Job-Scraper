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
	        
	        ArrayList<Jng_word> words = DBConnection.setResultSet();
	        for (Jng_word word : words) {
	        	int word_id = word.getWord_id();
	        	cell = new PdfPCell(new Phrase(Integer.toString(word_id)));
	            table.addCell(cell);
	            
	            String wrd_word = word.getWrd_word();
	            cell = new PdfPCell(new Phrase(wrd_word));
	            table.addCell(cell);
	            
	            String wrd_is_palin = word.getWrd_is_palin();
	            cell = new PdfPCell(new Phrase(wrd_is_palin));
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
