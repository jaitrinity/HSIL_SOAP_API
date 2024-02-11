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
@Table(name="All_Employee_Network_Expansion")
public class AllEmployeeNetworkExpansionModel implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="Id")
	private Integer id;
	
	@Column(name="Emp_Id")
	private String empId;
	
	@Column(name="Territory_Code")
	private String territoryCode;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="DEALER_NAME")
	private String dealerName;
	
	@Column(name="NO_OF_BILLING")
	private String noOfBilling;
	
	@Column(name="YTD_BILLING_VALUE")
	private String ytdBillingValue;
	
	@Temporal(TemporalType.DATE)
	@Column(name="Create_Date")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getNoOfBilling() {
		return noOfBilling;
	}

	public void setNoOfBilling(String noOfBilling) {
		this.noOfBilling = noOfBilling;
	}

	public String getYtdBillingValue() {
		return ytdBillingValue;
	}

	public void setYtdBillingValue(String ytdBillingValue) {
		this.ytdBillingValue = ytdBillingValue;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	

}
