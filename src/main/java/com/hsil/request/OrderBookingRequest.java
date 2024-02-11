package com.hsil.request;

import java.util.List;

import com.hsil.response.OrderBookingAddedItems;

public class OrderBookingRequest {
	private String transactionId,empId,territoryCode,territoryName,dealerCode,dealerName,orgId,orgName,divisionName,
	disttibutionChannel,disttibutionChannelDesc,documentType,plant,paymentTerm,submitType,isNewTransaction="Y",transactionDetId="";
	
	private List<OrderBookingAddedItems> addItemsList;
	private List<OrderBookingAddedItems> alreadyExistItemList;
	private List<OrderBookingAddedItems> newAddItemsList;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getTerritoryCode() {
		return territoryCode;
	}

	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	public String getTerritoryName() {
		return territoryName;
	}

	public void setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDisttibutionChannel() {
		return disttibutionChannel;
	}

	public void setDisttibutionChannel(String disttibutionChannel) {
		this.disttibutionChannel = disttibutionChannel;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public List<OrderBookingAddedItems> getAddItemsList() {
		return addItemsList;
	}

	public void setAddItemsList(List<OrderBookingAddedItems> addItemsList) {
		this.addItemsList = addItemsList;
	}

	public String getDisttibutionChannelDesc() {
		return disttibutionChannelDesc;
	}

	public void setDisttibutionChannelDesc(String disttibutionChannelDesc) {
		this.disttibutionChannelDesc = disttibutionChannelDesc;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public List<OrderBookingAddedItems> getAlreadyExistItemList() {
		return alreadyExistItemList;
	}

	public void setAlreadyExistItemList(List<OrderBookingAddedItems> alreadyExistItemList) {
		this.alreadyExistItemList = alreadyExistItemList;
	}

	public List<OrderBookingAddedItems> getNewAddItemsList() {
		return newAddItemsList;
	}

	public void setNewAddItemsList(List<OrderBookingAddedItems> newAddItemsList) {
		this.newAddItemsList = newAddItemsList;
	}

	public String getIsNewTransaction() {
		return isNewTransaction;
	}

	public void setIsNewTransaction(String isNewTransaction) {
		this.isNewTransaction = isNewTransaction;
	}

	public String getTransactionDetId() {
		return transactionDetId;
	}

	public void setTransactionDetId(String transactionDetId) {
		this.transactionDetId = transactionDetId;
	}
	
	
}
