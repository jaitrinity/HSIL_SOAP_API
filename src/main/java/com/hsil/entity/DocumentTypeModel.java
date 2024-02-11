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
@Table(name="Document_Type")
public class DocumentTypeModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Integer id;
	
	@Column(name="DOCUMENT_TYPE")
	private String documentType;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="SALE_ORG")
	private String salesOrg;
	
	@Column(name="SALE_ORG_DESC")
	private String salesOrgDesc;
	
	@Column(name="DISTRIBUTION_CHANNEL")
	private String distributionChannel;
	
	@Column(name="DISTRIBUTION_CHANNEL_DESC")
	private String distributionChannelDesc;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="DIVISION_DESC")
	private String divisionDesc;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="IS_ACTIVE")
	private String isActive;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_DATE")
	private Date lastUpdateDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getDivisionDesc() {
		return divisionDesc;
	}

	public void setDivisionDesc(String divisionDesc) {
		this.divisionDesc = divisionDesc;
	}

	public String getCreditControlArea() {
		return creditControlArea;
	}

	public void setCreditControlArea(String creditControlArea) {
		this.creditControlArea = creditControlArea;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	
}
