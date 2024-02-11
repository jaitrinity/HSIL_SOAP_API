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
@Table(name="Pending_Orders")
public class PendingOrdersModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="PO_ID")
	private Integer poId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="CHANNEL_CODE")
	private String channelCode;
	
	@Column(name="DIVISION_CODE")
	private String divisionCode;
	
	@Column(name="ORDER_TYPE")
	private String orderType;
	
	@Column(name="ORDER_NUMBER")
	private String orderNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name="ORDER_DATE")
	private Date orderDate;
	
	@Column(name="PLANT_CODE")
	private String plantCode;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="PENDING_QUA")
	private String pendingQua;
	
	@Column(name="PENDING_AMT")
	private String pendingAmt;
	
	@Column(name="ORDER_VALUE")
	private String orderValue;
	
	@Column(name="CREDIT_BLOCK")
	private String creditBlock;
	
	@Column(name="MAT_NOT_AVAIL")
	private String matNotAvail;
	
	@Column(name="REL_YET_TO_PROC")
	private String relYetToProc;
	
	@Column(name="REASON_FOR_PENDING")
	private String reasonForPending;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getPendingQua() {
		return pendingQua;
	}

	public void setPendingQua(String pendingQua) {
		this.pendingQua = pendingQua;
	}

	public String getPendingAmt() {
		return pendingAmt;
	}

	public void setPendingAmt(String pendingAmt) {
		this.pendingAmt = pendingAmt;
	}

	public String getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

	public String getCreditBlock() {
		return creditBlock;
	}

	public void setCreditBlock(String creditBlock) {
		this.creditBlock = creditBlock;
	}

	public String getMatNotAvail() {
		return matNotAvail;
	}

	public void setMatNotAvail(String matNotAvail) {
		this.matNotAvail = matNotAvail;
	}

	public String getRelYetToProc() {
		return relYetToProc;
	}

	public void setRelYetToProc(String relYetToProc) {
		this.relYetToProc = relYetToProc;
	}

	public String getReasonForPending() {
		return reasonForPending;
	}

	public void setReasonForPending(String reasonForPending) {
		this.reasonForPending = reasonForPending;
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

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	

}
