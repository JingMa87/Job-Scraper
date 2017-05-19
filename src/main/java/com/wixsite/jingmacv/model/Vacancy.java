package com.wixsite.jingmacv.model;

public class Vacancy {

	private int vacancyID;
	private String jobTitle;
	private String company;
	private String location;
	
	public Vacancy(int vacancyID, String jobTitle, String company, String location) {
		this.vacancyID = vacancyID;
		this.jobTitle = jobTitle;
		this.company = company;
		this.location = location;
	}

	public int getVacancyID() {
		return vacancyID;
	}
	
	public void setVacancyID(int vacancyID) {
		this.vacancyID = vacancyID;
	}
	
	public String getJobTitle() {
		return jobTitle;
	}
	
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
}
