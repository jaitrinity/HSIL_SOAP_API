package com.hsil.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Overdue_invoice")
public class OverdueInvoicesModel implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column(name="ODI_ID")
	private Integer odiId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="CHANNEL_CODE")
	private String channelCode;
	
	@Column(name="DIVISION_CODE")
	private String divisionCode;
	
	@Column(name="CRDT_CTRL_AREA")
	private String crdtCtrlArea;
	
	@Column(name="INVOICE_NUMBER")
	private String invoiceNumber;
	
	@Column(name="INVOICE_DATE")
	private String invoiceDate;
	
	@Column(name="INVOICE_AMOUNT")
	private String invoiceAmount;
	
	@Column(name="DUE_AMT_7DAYS")
	private String dueAmt7Days;
	
	@Column(name="TOTAL_OS")
	private String totalOs;
	
	@Column(name="DUE_DATE")
	private String dueDate;
	
	@Column(name="REMARKS")
	private String remarks;
	
	@Column(name="PAYMENT_TERMS")
	private String paymentTerms;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;

	public Integer getOdiId() {
		return odiId;
	}

	public void setOdiId(Integer odiId) {
		this.odiId = odiId;
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

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getCrdtCtrlArea() {
		return crdtCtrlArea;
	}

	public void setCrdtCtrlArea(String crdtCtrlArea) {
		this.crdtCtrlArea = crdtCtrlArea;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getDueAmt7Days() {
		return dueAmt7Days;
	}

	public void setDueAmt7Days(String dueAmt7Days) {
		this.dueAmt7Days = dueAmt7Days;
	}

	public String getTotalOs() {
		return totalOs;
	}

	public void setTotalOs(String totalOs) {
		this.totalOs = totalOs;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getProfitCenter() {
		return profitCenter;
	}

	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}
	
	

}
