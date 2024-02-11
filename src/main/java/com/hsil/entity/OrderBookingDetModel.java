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
@Table(name="Order_Booking_Det")
public class OrderBookingDetModel implements Serializable {
	
	private static final long serialVersionUID = 7831585051655470404L;

	@Id
	@GeneratedValue
	@Column(name="Id")
	private Integer id;
	
	@Column(name="Transaction_Id")
	private String transactionId;
	
	@Column(name="Material_Desc")
	private String materialDesc;
	
	@Column(name="Catelog_Number")
	private String catelogNumber;
	
	@Column(name="Material_Code")
	private String materialCode;
	
	@Column(name="Division_Code")
	private String divisionCode;
	
	@Column(name="Quantity")
	private String quantity;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Create_Date")
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMaterialDesc() {
		return materialDesc;
	}

	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}

	public String getCatelogNumber() {
		return catelogNumber;
	}

	public void setCatelogNumber(String catelogNumber) {
		this.catelogNumber = catelogNumber;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	

}
