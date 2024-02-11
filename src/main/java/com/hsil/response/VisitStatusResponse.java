package com.hsil.response;

public class VisitStatusResponse {
	private String type="",classification="";
	private int noOfDealer=0,visitPerDealer=0,totalVisit=0,noOfVisitDone=0,balOfVisit=0,adharancePercentage=0;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	public int getAdharancePercentage() {
		return adharancePercentage;
	}
	public void setAdharancePercentage(int adharancePercentage) {
		this.adharancePercentage = adharancePercentage;
	}
	public int getBalOfVisit() {
		return balOfVisit;
	}
	public void setBalOfVisit(int balOfVisit) {
		this.balOfVisit = balOfVisit;
	}
	public int getNoOfDealer() {
		return noOfDealer;
	}
	public void setNoOfDealer(int noOfDealer) {
		this.noOfDealer = noOfDealer;
	}
	public int getVisitPerDealer() {
		return visitPerDealer;
	}
	public void setVisitPerDealer(int visitPerDealer) {
		this.visitPerDealer = visitPerDealer;
	}
	public int getTotalVisit() {
		return totalVisit;
	}
	public void setTotalVisit(int totalVisit) {
		this.totalVisit = totalVisit;
	}
	public int getNoOfVisitDone() {
		return noOfVisitDone;
	}
	public void setNoOfVisitDone(int noOfVisitDone) {
		this.noOfVisitDone = noOfVisitDone;
	}
	
	
	
	

}
