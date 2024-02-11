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
@Table(name="Network_Expansion_Report")
public class NetworkExpansionReportModel implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Integer id;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="DEALER_NAME")
	private String dealerName;
	
	@Column(name="TERRITORY")
	private String territory;
	
	@Column(name="TER_NAME")
	private String terName;
	
	@Column(name="NUM_OF_BILLING")
	private Integer numOfBilling;
	
	@Column(name="YTD_BILLING_VALUE")
	private String ytdBillingValue;
	
	@Column(name="IS_ACTIVE")
	private Integer isActive;
	
	@Column(name="CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getNumOfBilling() {
		return numOfBilling;
	}

	public void setNumOfBilling(Integer numOfBilling) {
		this.numOfBilling = numOfBilling;
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

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getTerName() {
		return terName;
	}

	public void setTerName(String terName) {
		this.terName = terName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	
	
}
