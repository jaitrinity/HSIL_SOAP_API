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
@Table(name="Outstanding_Master_New")
public class OutstandingMasterNewModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="O_ID")
	private Integer oId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="DIVISION")
	private String division;
	
	@Column(name="OUTSTA_AMOUNT")
	private String outstaAmount;
	
	@Column(name="NOT_DUE")
	private String notDue;
	
	@Column(name="Z0_30")
	private String z0_30;
	
	@Column(name="Z31_60")
	private String z31_60;
	
	@Column(name="Z61_90")
	private String z61_90;
	
	@Column(name="Z91_180")
	private String z91_180;
	
	@Column(name="Z181_365")
	private String z181_365;
	
	@Column(name="Z366_ABOVE")
	private String z366_Above;
	
	@Column(name="Z366_730")
	private String z366_730;
	
	@Column(name="Z731_1095")
	private String z731_1095;
	
	@Column(name="Z1096_ABOVE")
	private String z1096_Above;
	
	@Column(name="CREDIT_CONTROL_AREA")
	private String creditControlArea;
	
	@Column(name="PROFIT_CENTER")
	private String profitCenter;
	
	@Column(name="DOC_TYPE")
	private String docType;
	
	@Column(name="DOC_NUM")
	private String docNum;
	
	@Column(name="DOC_DATE")
	private String docDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_DATE")
	private Date createDate;

	public Integer getoId() {
		return oId;
	}

	public void setoId(Integer oId) {
		this.oId = oId;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getOutstaAmount() {
		return outstaAmount;
	}

	public void setOutstaAmount(String outstaAmount) {
		this.outstaAmount = outstaAmount;
	}

	public String getNotDue() {
		return notDue;
	}

	public void setNotDue(String notDue) {
		this.notDue = notDue;
	}

	public String getZ0_30() {
		return z0_30;
	}

	public void setZ0_30(String z0_30) {
		this.z0_30 = z0_30;
	}

	public String getZ31_60() {
		return z31_60;
	}

	public void setZ31_60(String z31_60) {
		this.z31_60 = z31_60;
	}

	public String getZ61_90() {
		return z61_90;
	}

	public void setZ61_90(String z61_90) {
		this.z61_90 = z61_90;
	}

	public String getZ91_180() {
		return z91_180;
	}

	public void setZ91_180(String z91_180) {
		this.z91_180 = z91_180;
	}

	public String getZ181_365() {
		return z181_365;
	}

	public void setZ181_365(String z181_365) {
		this.z181_365 = z181_365;
	}

	public String getZ366_730() {
		return z366_730;
	}

	public void setZ366_730(String z366_730) {
		this.z366_730 = z366_730;
	}

	public String getZ731_1095() {
		return z731_1095;
	}

	public void setZ731_1095(String z731_1095) {
		this.z731_1095 = z731_1095;
	}

	

	public String getZ1096_Above() {
		return z1096_Above;
	}

	public void setZ1096_Above(String z1096_Above) {
		this.z1096_Above = z1096_Above;
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

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getZ366_Above() {
		return z366_Above;
	}

	public void setZ366_Above(String z366_Above) {
		this.z366_Above = z366_Above;
	}
	

}
