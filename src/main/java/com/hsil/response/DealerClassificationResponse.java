package com.hsil.response;

import java.util.List;

public class DealerClassificationResponse {
	private List<AllDealerClassification> classAList;
	private List<AllDealerClassification> classBList;
	private List<AllDealerClassification> classCList;
	public List<AllDealerClassification> getClassAList() {
		return classAList;
	}
	public void setClassAList(List<AllDealerClassification> classAList) {
		this.classAList = classAList;
	}
	public List<AllDealerClassification> getClassBList() {
		return classBList;
	}
	public void setClassBList(List<AllDealerClassification> classBList) {
		this.classBList = classBList;
	}
	public List<AllDealerClassification> getClassCList() {
		return classCList;
	}
	public void setClassCList(List<AllDealerClassification> classCList) {
		this.classCList = classCList;
	}
	
	
}
