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
@Table(name="Invoice_Details")
public class InvoiceDetailsModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue
	@Column(name="S_ID")
	private Integer sId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="INV_DOC")
	private String invDoc;
	
	@Column(name="DOCUMENT_TYPE")
	private String documentType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INV_DATE")
	private Date invDate;
	
	@Column(name="MATERIAL")
	private String material;
	
	@Column(name="MAT_DESC")
	private String matDesc;
	
	@Column(name="MATERIAL_GRP2")
	private String materialGrp2;
	
	@Column(name="PROVG")
	private String provg;
	
	@Column(name="PRD_CAT_TEXT")
	private String prdCatText;
	
	@Column(name="INV_QTY")
	private String invQty;
	
	@Column(name="NET_AMOUNT")
	private String netAmount;
	
	@Column(name="BILL_VALUE")
	private String bilValue;
	
	@Column(name="DIST_CHNL")
	private String distChnl;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="EMPLOYEE")
	private String employee;
	
	@Column(name="EMPLOYEE_NAME")
	private String employeeName;
	
	@Column(name="EMP_MAIL_ID")
	private String empEmailId;
	
	@Column(name="TERRITORY_CODE")
	private String territoryCode;
	
	@Column(name="TERRITORY_NAME")
	private String territoryName;
	
	@Column(name="ORDER_NO")
	private String orderNo;
	
	@Column(name="INVOICE_COPY")
	private String invoiceCopy;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getsId() {
		return sId;
	}

	public void setsId(Integer sId) {
		this.sId = sId;
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

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getInvDate() {
		return invDate;
	}

	public void setInvDate(Date invDate) {
		this.invDate = invDate;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMatDesc() {
		return matDesc;
	}

	public void setMatDesc(String matDesc) {
		this.matDesc = matDesc;
	}

	public String getMaterialGrp2() {
		return materialGrp2;
	}

	public void setMaterialGrp2(String materialGrp2) {
		this.materialGrp2 = materialGrp2;
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

	public String getInvQty() {
		return invQty;
	}

	public void setInvQty(String invQty) {
		this.invQty = invQty;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getBilValue() {
		return bilValue;
	}

	public void setBilValue(String bilValue) {
		this.bilValue = bilValue;
	}

	public String getDistChnl() {
		return distChnl;
	}

	public void setDistChnl(String distChnl) {
		this.distChnl = distChnl;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
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

	public String getEmpEmailId() {
		return empEmailId;
	}

	public void setEmpEmailId(String empEmailId) {
		this.empEmailId = empEmailId;
	}

	public String getTerritoryCode() {
		return territoryCode;
	}

	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getInvoiceCopy() {
		return invoiceCopy;
	}

	public void setInvoiceCopy(String invoiceCopy) {
		this.invoiceCopy = invoiceCopy;
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

	public String getTerritoryName() {
		return territoryName;
	}

	public void setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
	}
	
	

}
