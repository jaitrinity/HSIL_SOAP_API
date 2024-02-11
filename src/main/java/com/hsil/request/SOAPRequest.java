package com.hsil.request;

public class SOAPRequest {
	private String empId,period,empTerritoryCodeList;
	private String dealerCode="",territoryCode="",isSyncOutstanding="No",
			fromDate="", // First date of current month
			toDate="", // today's date
			keyDate=""; // today's date
	private String matCode="",matDesc="",catNum="",searchBy="";
	private String actionDoneOnMatCode = "",actionDoneOnMatDesc="",actionDoneOnCatNum="";
	private String salesOrg="",divisionCode="",divisionName = "",distributionChannel="";
	private String operator,month,type,excelBase64Str;

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getKeyDate() {
		return keyDate;
	}

	public void setKeyDate(String keyDate) {
		this.keyDate = keyDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getTerritoryCode() {
		return territoryCode;
	}

	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	public String getIsSyncOutstanding() {
		return isSyncOutstanding;
	}

	public void setIsSyncOutstanding(String isSyncOutstanding) {
		this.isSyncOutstanding = isSyncOutstanding;
	}

	public String getMatCode() {
		return matCode;
	}

	public void setMatCode(String matCode) {
		this.matCode = matCode;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public String getCatNum() {
		return catNum;
	}

	public void setCatNum(String catNum) {
		this.catNum = catNum;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getActionDoneOnMatCode() {
		return actionDoneOnMatCode;
	}

	public void setActionDoneOnMatCode(String actionDoneOnMatCode) {
		this.actionDoneOnMatCode = actionDoneOnMatCode;
	}

	public String getActionDoneOnMatDesc() {
		return actionDoneOnMatDesc;
	}

	public void setActionDoneOnMatDesc(String actionDoneOnMatDesc) {
		this.actionDoneOnMatDesc = actionDoneOnMatDesc;
	}

	public String getActionDoneOnCatNum() {
		return actionDoneOnCatNum;
	}

	public void setActionDoneOnCatNum(String actionDoneOnCatNum) {
		this.actionDoneOnCatNum = actionDoneOnCatNum;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExcelBase64Str() {
		return excelBase64Str;
	}

	public void setExcelBase64Str(String excelBase64Str) {
		this.excelBase64Str = excelBase64Str;
	}

	public String getEmpTerritoryCodeList() {
		return empTerritoryCodeList;
	}

	public void setEmpTerritoryCodeList(String empTerritoryCodeList) {
		this.empTerritoryCodeList = empTerritoryCodeList;
	}

	
	
	
}
