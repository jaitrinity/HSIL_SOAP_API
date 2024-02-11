package com.hsil.response;

public class PendingOrderStatusResponse {
	private String dealerCode="",dealerName="",materialCode="",materialDesc="",orderNo="",orderQty="",billedQty="",
			pendingQty="",pendingStatus="";

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
	
	
	
	
}
