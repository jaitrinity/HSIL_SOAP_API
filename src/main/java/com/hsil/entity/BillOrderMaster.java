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
@Table(name="Bill_Order_Master")
public class BillOrderMaster implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="BO_ID")
	private Integer boId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="CHANNEL_CODE")
	private String channelCode;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="ORDER_NUMBER")
	private String orderNumber;
	
	@Column(name="ORDER_TYPE")
	private String orderType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="ORDER_DATE")
	private Date orderDate;
	
	@Column(name="ORDER_NEXF")
	private String orderNexf;
	
	@Column(name="BILL_NO")
	private String billNo;
	
	@Column(name="BILLED_VALUE")
	private String billedValue;
	
	@Column(name="ORDER_QTY")
	private String orderQty;
	
	@Column(name="BILLED_QTY")
	private String billedQty;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;
	
	@Column(name="TERRITORY")
	private String territory;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	public Integer getBoId() {
		return boId;
	}

	public void setBoId(Integer boId) {
		this.boId = boId;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderNexf() {
		return orderNexf;
	}

	public void setOrderNexf(String orderNexf) {
		this.orderNexf = orderNexf;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBilledValue() {
		return billedValue;
	}

	public void setBilledValue(String billedValue) {
		this.billedValue = billedValue;
	}

	public String getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(String orderQty) {
		this.orderQty = orderQty;
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

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getBilledQty() {
		return billedQty;
	}

	public void setBilledQty(String billedQty) {
		this.billedQty = billedQty;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
}

