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
@Table(name="Outstanding_Master")
public class OutstandingMasterModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="O_ID")
	private Integer oId;
	
	@Column(name="DEALER_CODE")
	private String dealerCode;
	
	@Column(name="DEALER_Name")
	private String dealerName;
	
	@Column(name="DIVISION_CODE")
	private String divisionCode;
	
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

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
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

	public String getZ366_Above() {
		return z366_Above;
	}

	public void setZ366_Above(String z366_Above) {
		this.z366_Above = z366_Above;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	

}
