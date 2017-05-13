package com.wixsite.jingmacv.model;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGenerator {
    
    public static boolean generateXML() {
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
        	docBuilder = docBuildFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("http://jingmacv.wixsite.com/home", "palindromes");
            doc.appendChild(mainRootElement);
 
            ArrayList<Jng_word> words = DBConnection.setResultSet();
            // Appends child elements to root element.
            for(Jng_word word : words) {
            	int word_id = word.getWord_id();	            
	            String wrd_word = word.getWrd_word();	            
	            String wrd_is_palin = word.getWrd_is_palin();
	            addPalin(mainRootElement, doc, word_id, wrd_word, wrd_is_palin);
            }
 
            // Generates an XML file.
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            String myHome = System.getenv("userprofile");
            StreamResult result = new StreamResult(myHome + "/Downloads/Oracle2XML.xml");
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
	
	private static void addPalin(Element mainRootElement, Document doc, int words_id, String wrd_word, String wrd_is_palin) {
		Element palin = doc.createElement("palindrome");
        mainRootElement.appendChild(palin);
  
        Element number = doc.createElement("number");
        number.appendChild(doc.createTextNode(Integer.toString(words_id)));
        palin.appendChild(number);
  
        Element word = doc.createElement("word");
        word.appendChild(doc.createTextNode(wrd_word));
        palin.appendChild(word);
        
        Element isPalin = doc.createElement("isPalindrome");
        isPalin.appendChild(doc.createTextNode(wrd_is_palin));
        palin.appendChild(isPalin);
	}
}
