package com.hsil.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="Order_Booking_Hdr")
public class OrderBookingHdrModel implements Serializable {

	private static final long serialVersionUID = 267718920485355942L;

	@Id
	@GeneratedValue
	@Column(name="Id")
	private Integer id;
	
	@Column(name="Transaction_Id")
	private String transactionId;
	
	@Column(name="Emp_Id")
	private String empId;
	
	@Column(name="Territory_Code")
	private String territoryCode;
	
	@Column(name="Territory_Name")
	private String territoryName;
	
	@Column(name="Dealer_Code")
	private String dealerCode;
	
	@Column(name="Dealer_Name")
	private String dealerName;
	
	@Column(name="Division_Name")
	private String divisionName;
	
	@Column(name="Organization_Id")
	private String organizationId;
	
	@Column(name="Organization_Name")
	private String organizationName;
	
	@Column(name="Distribution_Channel")
	private String distributionChannel;
	
	@Column(name="Distribution_Channel_Desc")
	private String distributionChannelDesc;
	
	@Column(name="Document_Type")
	private String documentType;
	
	@Column(name="Plant")
	private String plant;
	
	@Column(name="Payment_Term")
	private String payementTerm;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Date")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getDistributionChannelDesc() {
		return distributionChannelDesc;
	}

	public void setDistributionChannelDesc(String distributionChannelDesc) {
		this.distributionChannelDesc = distributionChannelDesc;
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

	public String getPayementTerm() {
		return payementTerm;
	}

	public void setPayementTerm(String payementTerm) {
		this.payementTerm = payementTerm;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	
	

}
