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
@Table(name="All_Products_25Aug")
public class AllProductModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Integer id;
	
	@Column(name="PRODUCT_CODE")
	private String productCode;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="PRODUCT_NAME_SALEABLE_ITEM")
	private String productNameSaleableItem;
	
	@Column(name="DIVISION_CODE")
	private String divisionCode;
	
	@Column(name="CATELOG_NUMBER")
	private String catelogNumber;
	
	@Column(name="PC_PN_CN")
	private String PcPnCn;
	
	@Column(name="SALES_ORG")
	private String salesOrg;
	
	@Column(name="DISTRIBUTION_CHANEL")
	private String distributionChanel;
	
	@Column(name="PLANT")
	private String plant;
	
	@Column(name="SALEABLE_ITEM")
	private String saleableItem;
	
	@Column(name="BASE_UOM")
	private String baseUom;
	
	@Column(name="PROCUREMENT_TYPE")
	private String procurementType;
	
	@Column(name="MAT_STS_DIV")
	private String matStsDiv;
	
	@Column(name="MATERIAL_TYPE")
	private String materialType;
	
	@Column(name="Is_Active")
	private Integer isActive;
	
	@Column(name="PRODUCT_INSERT_DATE")
	private String productInsertDate;
	
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

	public String getProductNameSaleableItem() {
		return productNameSaleableItem;
	}

	public void setProductNameSaleableItem(String productNameSaleableItem) {
		this.productNameSaleableItem = productNameSaleableItem;
	}

	public String getDivisionCode() {
		return divisionCode;
	}

	public void setDivisionCode(String divisionCode) {
		this.divisionCode = divisionCode;
	}

	public String getCatelogNumber() {
		return catelogNumber;
	}

	public void setCatelogNumber(String catelogNumber) {
		this.catelogNumber = catelogNumber;
	}

	public String getPcPnCn() {
		return PcPnCn;
	}

	public void setPcPnCn(String pcPnCn) {
		PcPnCn = pcPnCn;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getDistributionChanel() {
		return distributionChanel;
	}

	public void setDistributionChanel(String distributionChanel) {
		this.distributionChanel = distributionChanel;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public String getSaleableItem() {
		return saleableItem;
	}

	public void setSaleableItem(String saleableItem) {
		this.saleableItem = saleableItem;
	}

	public String getBaseUom() {
		return baseUom;
	}

	public void setBaseUom(String baseUom) {
		this.baseUom = baseUom;
	}

	public String getProcurementType() {
		return procurementType;
	}

	public void setProcurementType(String procurementType) {
		this.procurementType = procurementType;
	}

	public String getMatStsDiv() {
		return matStsDiv;
	}

	public void setMatStsDiv(String matStsDiv) {
		this.matStsDiv = matStsDiv;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getProductInsertDate() {
		return productInsertDate;
	}

	public void setProductInsertDate(String productInsertDate) {
		this.productInsertDate = productInsertDate;
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
