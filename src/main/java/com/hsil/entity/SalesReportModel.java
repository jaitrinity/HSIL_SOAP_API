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
@Table(name="Sales_Report")
public class SalesReportModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="S_ID")
	private Integer id;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="INV_DOC")
	private String invDoc;
	
	@Temporal(TemporalType.DATE)
	@Column(name="BILLING_DATE")
	private Date billingDate;
	
	@Column(name="BILLING_TYPE")
	private String billingType;
	
	@Column(name="MATERIAL")
	private String material;
	
	@Column(name="DISTRIBUTION_CHANNEL")
	private String distributionChannel;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="MATERIAL_DISCRIPTION")
	private String materialDiscription;
	
	@Column(name="QUANTITY")
	private String quantity;
	
	@Column(name="INVOICE_VALUE")
	private String invoiceValue;
	
	@Column(name="EMPLOYEE")
	private String employee;
	
	@Column(name="EMPLOYEE_NAME")
	private String employeeName;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;
	
	@Column(name="PROVG")
	private String provg;
	
	@Column(name="PRD_CAT_TEXT")
	private String prdCatText;
	
	@Column(name="MATERIAL_GRP2")
	private String materialGrp2;
	
	@Column(name="MATERIAL_GRP2_TEXT")
	private String materialGrp2Text;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
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

	public String getInvDoc() {
		return invDoc;
	}

	public void setInvDoc(String invDoc) {
		this.invDoc = invDoc;
	}

	

	public Date getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(Date billingDate) {
		this.billingDate = billingDate;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getDistributionChannel() {
		return distributionChannel;
	}

	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getMaterialDiscription() {
		return materialDiscription;
	}

	public void setMaterialDiscription(String materialDiscription) {
		this.materialDiscription = materialDiscription;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getInvoiceValue() {
		return invoiceValue;
	}

	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCreditControlArea() {
		return creditControlArea;
	}

	public void setCreditControlArea(String creditControlArea) {
		this.creditControlArea = creditControlArea;
	}

	public String getProfitCenter() {
		return profitCenter;
	}

	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}

	public String getProvg() {
		return provg;
	}

	public void setProvg(String provg) {
		this.provg = provg;
	}

	public String getPrdCatText() {
		return prdCatText;
	}

	public void setPrdCatText(String prdCatText) {
		this.prdCatText = prdCatText;
	}

	public String getMaterialGrp2() {
		return materialGrp2;
	}

	public void setMaterialGrp2(String materialGrp2) {
		this.materialGrp2 = materialGrp2;
	}

	public String getMaterialGrp2Text() {
		return materialGrp2Text;
	}

	public void setMaterialGrp2Text(String materialGrp2Text) {
		this.materialGrp2Text = materialGrp2Text;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	

}
