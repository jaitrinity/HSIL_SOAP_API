package com.hsil.response;

public class PerformanceResponse {
	private String empId="",product="";
	private float lyBase=0,tgt=0,tyAct=0,grPercentate=0,achPercentate=0;
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public float getLyBase() {
		return lyBase;
	}
	public void setLyBase(float lyBase) {
		this.lyBase = lyBase;
	}
	public float getTgt() {
		return tgt;
	}
	public void setTgt(float tgt) {
		this.tgt = tgt;
	}
	public float getTyAct() {
		return tyAct;
	}
	public void setTyAct(float tyAct) {
		this.tyAct = tyAct;
	}
	public float getGrPercentate() {
		return grPercentate;
	}
	public void setGrPercentate(float grPercentate) {
		this.grPercentate = grPercentate;
	}
	public float getAchPercentate() {
		return achPercentate;
	}
	public void setAchPercentate(float achPercentate) {
		this.achPercentate = achPercentate;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	
}
