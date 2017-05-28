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
 
            ArrayList<Vacancy> vacancies = DBConnection.getResultSet();
            // Appends child elements to root element.
            for(Vacancy vacancy : vacancies) {
            	int vacancyID = vacancy.getVacancyID();	            
	            String jobTitle = vacancy.getJobTitle();	            
	            String company = vacancy.getCompany();            
	            String location = vacancy.getLocation();
	            System.out.println(vacancyID + ", " + jobTitle + ", " + company + ", " + location);
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
	
	private static void addVacancy(Element mainRootElement, Document doc, int vacancyID, String jobTitle, String company, String location) {
		Element vacancy = doc.createElement("vacancy");
        mainRootElement.appendChild(vacancy);
  
        Element vacancyIDElement = doc.createElement("vacancyID");
        vacancyIDElement.appendChild(doc.createTextNode(Integer.toString(vacancyID)));
        vacancy.appendChild(vacancyIDElement);
  
        Element jobTitleElement = doc.createElement("jobTitle");
        jobTitleElement.appendChild(doc.createTextNode(jobTitle));
        vacancy.appendChild(jobTitleElement);
  
        Element companyElement = doc.createElement("company");
        companyElement.appendChild(doc.createTextNode(company));
        vacancy.appendChild(companyElement);
        
        Element locationElement = doc.createElement("location");
        locationElement.appendChild(doc.createTextNode(location));
        vacancy.appendChild(locationElement);
	}
}
