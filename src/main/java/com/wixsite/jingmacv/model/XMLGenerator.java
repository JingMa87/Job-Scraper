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
            Element mainRootElement = doc.createElementNS("http://jingmacv.wixsite.com/home", "vacancies");
            doc.appendChild(mainRootElement);
 
            ArrayList<Vacancy> vacancies = WebScraper.getVacancies();
            // Appends child elements to root element.
            for (Vacancy vacancy : vacancies) {
            	String vacancyID = Integer.toString(vacancy.getVacancyID());	            
	            String jobTitle = vacancy.getJobTitle();	            
	            String company = (vacancy.getCompany() == null ? "" : vacancy.getCompany());            
	            String location = vacancy.getLocation();
	            addVacancy(mainRootElement, doc, vacancyID, jobTitle, company, location);
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
	
	private static void addVacancy(Element mainRootElement, Document doc, String vacancyID, String jobTitle, String company, String location) {
		// Adds the vacancy tag to the vacancies tag.
		Element vacancy = doc.createElement("vacancy");
        mainRootElement.appendChild(vacancy);
        // Adds children tags to the vacancy tag.
        addTags(doc, vacancy, "vacancyID", vacancyID);
        addTags(doc, vacancy, "jobTitle", jobTitle);
        addTags(doc, vacancy, "company", company);
        addTags(doc, vacancy, "location", location);
	}
	
	private static void addTags(Document doc, Element vacancy, String tagName, String tagValue) {
		Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(tagValue));
        vacancy.appendChild(element);
	}
}
