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
@Table(name="All_Employee_Pending_Orders")
public class AllEmployeePendingOrdersModel implements Serializable  {

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
	
	@Column(name="MATERIAL_CODE")
	private String materialCode;
	
	@Column(name="MATERIAL_DESC")
	private String materialDesc;
	
	@Column(name="ORDER_NUMBER")
	private String orderNo;
	
	@Column(name="ORDER_QTY")
	private String orderQty;
	
	@Column(name="BILLED_QTY")
	private String billedQty;
	
	@Column(name="PENDING_QTY")
	private String pendingQty;
	
	@Column(name="PENDING_STATUS")
	private String pendingStatus;
	
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

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getMaterialDesc() {
		return materialDesc;
	}

	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
	}

	public String getBilledQty() {
		return billedQty;
	}

	public void setBilledQty(String billedQty) {
		this.billedQty = billedQty;
	}

	public String getPendingQty() {
		return pendingQty;
	}

	public void setPendingQty(String pendingQty) {
		this.pendingQty = pendingQty;
	}

	public String getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(String pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
	

}
