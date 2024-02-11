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
@Table(name="Dealer_Master_25Aug")
public class DealerMasterModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="D_ID")
	private Integer dId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="DEALER_NAME")
	private String dealerName;
	
	@Column(name="DEALER_TYPE")
	private String dealerType;
	
	@Column(name="DEALER_TYPE_DESC")
	private String dealerTypeDesc;
	
	@Column(name="SHOW_ROOM")
	private String showRoom;
	
	@Column(name="CONT_PERSON")
	private String contPerson;
	
	@Column(name="CONT_ADR1")
	private String contAdr1;
	
	@Column(name="CONT_ADR1_PIN")
	private String contAdr1Pin;
	
	@Column(name="PRIM_EMAIL_ID")
	private String primEmailId;
	
	@Column(name="PRIM_MOBILE_NO")
	private String primMobileNumber;
	
	@Column(name="MOBILE_NO")
	private String mobileNo;
	
	@Column(name="STREET")
	private String street;
	
	@Column(name="SALES_OFFICE")
	private String salesOffice;
	
	@Column(name="REGION")
	private String region;
	
	@Column(name="TERRITORY")
	private String territory;
	
	@Column(name="TER_NAME")
	private String terName;
	
	@Column(name="CITY_CODE")
	private String cityCode;
	
	@Column(name="TIN_NO")
	private String tinNo;
	
	@Column(name="PAN_NO")
	private String panNo;
	
	@Column(name="DIVISION_CODE")
	private String divisionCode;
	
	@Column(name="CHANNEL_CODE")
	private String channelCode;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Column(name="GSTIN")
	private String gstin;
	
	@Column(name="PRICE_GROUP")
	private String priceGroup;
	
	@Column(name="CUST_STATUS")
	private String custStatus;
	
	@Column(name="CUST_STATUS_DESC")
	private String custStatusDesc;
	
	@Column(name="SALES_ORG")
	private String salesOrg;
	
	@Column(name="SALES_ORG_DESC")
	private String salesOrgDesc;
	
	@Column(name="ACCOUNT_GROUP")
	private String accountGroup;
	
	@Column(name="ACCOUNT_GROUP_DESC")
	private String accountGroupDesc;
	
	@Column(name="DEALER_INSERT_DATE")
	private String dealerInsertDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	public Integer getdId() {
		return dId;
	}

	public void setdId(Integer dId) {
		this.dId = dId;
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

	public String getDealerType() {
		return dealerType;
	}

	public void setDealerType(String dealerType) {
		this.dealerType = dealerType;
	}

	public String getShowRoom() {
		return showRoom;
	}

	public void setShowRoom(String showRoom) {
		this.showRoom = showRoom;
	}

	public String getContPerson() {
		return contPerson;
	}

	public void setContPerson(String contPerson) {
		this.contPerson = contPerson;
	}

	public String getContAdr1() {
		return contAdr1;
	}

	public void setContAdr1(String contAdr1) {
		this.contAdr1 = contAdr1;
	}

	public String getContAdr1Pin() {
		return contAdr1Pin;
	}

	public void setContAdr1Pin(String contAdr1Pin) {
		this.contAdr1Pin = contAdr1Pin;
	}

	public String getPrimEmailId() {
		return primEmailId;
	}

	public void setPrimEmailId(String primEmailId) {
		this.primEmailId = primEmailId;
	}

	public String getPrimMobileNumber() {
		return primMobileNumber;
	}

	public void setPrimMobileNumber(String primMobileNumber) {
		this.primMobileNumber = primMobileNumber;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getSalesOffice() {
		return salesOffice;
	}

	public void setSalesOffice(String salesOffice) {
		this.salesOffice = salesOffice;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getTinNo() {
		return tinNo;
	}

	public void setTinNo(String tinNo) {
		this.tinNo = tinNo;
	}

	

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPriceGroup() {
		return priceGroup;
	}

	public void setPriceGroup(String priceGroup) {
		this.priceGroup = priceGroup;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDealerInsertDate() {
		return dealerInsertDate;
	}

	public void setDealerInsertDate(String dealerInsertDate) {
		this.dealerInsertDate = dealerInsertDate;
	}

	public String getCustStatus() {
		return custStatus;
	}

	public void setCustStatus(String custStatus) {
		this.custStatus = custStatus;
	}

	public String getCustStatusDesc() {
		return custStatusDesc;
	}

	public void setCustStatusDesc(String custStatusDesc) {
		this.custStatusDesc = custStatusDesc;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getSalesOrgDesc() {
		return salesOrgDesc;
	}

	public void setSalesOrgDesc(String salesOrgDesc) {
		this.salesOrgDesc = salesOrgDesc;
	}

	public String getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(String accountGroup) {
		this.accountGroup = accountGroup;
	}

	public String getAccountGroupDesc() {
		return accountGroupDesc;
	}

	public void setAccountGroupDesc(String accountGroupDesc) {
		this.accountGroupDesc = accountGroupDesc;
	}

	public String getDealerTypeDesc() {
		return dealerTypeDesc;
	}

	public void setDealerTypeDesc(String dealerTypeDesc) {
		this.dealerTypeDesc = dealerTypeDesc;
	}
}
