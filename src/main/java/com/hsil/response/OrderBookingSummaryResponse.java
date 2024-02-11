package com.hsil.response;

import java.util.List;

public class OrderBookingSummaryResponse {
	private String transactionId,territoryCode,territoryName,dealerCode,dealerName,divisionName,distChannel,distChannelDesc,
	documentType,plant,paymentTerm,salDoc,docDate,netValue,type,organizationId,createDate;

	private List<OrderItemsList> orderItemsList;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getDistChannel() {
		return distChannel;
	}

	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
	}

	public String getDistChannelDesc() {
		return distChannelDesc;
	}

	public void setDistChannelDesc(String distChannelDesc) {
		this.distChannelDesc = distChannelDesc;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public List<OrderItemsList> getOrderItemsList() {
		return orderItemsList;
	}

	public void setOrderItemsList(List<OrderItemsList> orderItemsList) {
		this.orderItemsList = orderItemsList;
	}

	public String getSalDoc() {
		return salDoc;
	}

	public void setSalDoc(String salDoc) {
		this.salDoc = salDoc;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNetValue() {
		return netValue;
	}

	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
}
