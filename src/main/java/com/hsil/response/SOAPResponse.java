package com.hsil.response;

import java.util.List;

import com.hsil.dto.SystemParamDTO;
import com.hsil.entity.BillOrderMaster;
import com.hsil.entity.InvoiceDetailsModel;
import com.hsil.entity.OutstandingMasterModel;
import com.hsil.entity.OverdueInvoicesModel;
import com.hsil.entity.PendingOrdersModel;
import com.hsil.entity.SalesReportModel;

public class SOAPResponse {
	private String dealerCode,isSyncButtonShow;
	
	//private List<BillOrderMaster> billOrder;
	//private List<InvoiceDetailsModel> invoiceDetails;
	private List<OutstandingResponse> oustanding;
	//private List<OverdueInvoicesModel> overdueInvoices;
	private List<PendingOrderStatusResponse> pendingOrderStatusList;
	//private List<SalesReportModel> salesReport;
	private List<VisitStatusResponse> visitStatusList;
	private List<NonVisitedDealerResponse> nonVisitedDealerList;
	private List<NetworkExpansionResponse> networkExpansionList;
	private List<PerformanceResponse> performance;
	private List<SystemParamDTO> terricodeList;
	private List<SystemParamDTO> organizationList;
	private List<SystemParamDTO> distributionChannelList;
	private List<SystemParamDTO> materialCodeList;
	private List<SystemParamDTO> materialDescList;
	private List<SystemParamDTO> catelogNumberListList;
	private List<SystemParamDTO> divisionCodeList;
	private List<SystemParamDTO> divisionNameList;
	private List<SystemParamDTO> documentTypeList;
	private List<SystemParamDTO> plantList;
	private List<SystemParamDTO> paymentTermList;
	
	public String getDealerCode() {
		return dealerCode;
	}
	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}
	public List<OutstandingResponse> getOustanding() {
		return oustanding;
	}
	public void setOustanding(List<OutstandingResponse> oustanding) {
		this.oustanding = oustanding;
	}
	public List<PendingOrderStatusResponse> getPendingOrderStatusList() {
		return pendingOrderStatusList;
	}
	public void setPendingOrderStatusList(List<PendingOrderStatusResponse> pendingOrderStatusList) {
		this.pendingOrderStatusList = pendingOrderStatusList;
	}
	public List<VisitStatusResponse> getVisitStatusList() {
		return visitStatusList;
	}
	public void setVisitStatusList(List<VisitStatusResponse> visitStatusList) {
		this.visitStatusList = visitStatusList;
	}
	public List<NonVisitedDealerResponse> getNonVisitedDealerList() {
		return nonVisitedDealerList;
	}
	public void setNonVisitedDealerList(List<NonVisitedDealerResponse> nonVisitedDealerList) {
		this.nonVisitedDealerList = nonVisitedDealerList;
	}
	public List<NetworkExpansionResponse> getNetworkExpansionList() {
		return networkExpansionList;
	}
	public void setNetworkExpansionList(List<NetworkExpansionResponse> networkExpansionList) {
		this.networkExpansionList = networkExpansionList;
	}
	public List<PerformanceResponse> getPerformance() {
		return performance;
	}
	public void setPerformance(List<PerformanceResponse> performance) {
		this.performance = performance;
	}
	public List<SystemParamDTO> getTerricodeList() {
		return terricodeList;
	}
	public void setTerricodeList(List<SystemParamDTO> terricodeList) {
		this.terricodeList = terricodeList;
	}
	public List<SystemParamDTO> getOrganizationList() {
		return organizationList;
	}
	public void setOrganizationList(List<SystemParamDTO> organizationList) {
		this.organizationList = organizationList;
	}
	public List<SystemParamDTO> getDistributionChannelList() {
		return distributionChannelList;
	}
	public void setDistributionChannelList(List<SystemParamDTO> distributionChannelList) {
		this.distributionChannelList = distributionChannelList;
	}
	public List<SystemParamDTO> getMaterialCodeList() {
		return materialCodeList;
	}
	public void setMaterialCodeList(List<SystemParamDTO> materialCodeList) {
		this.materialCodeList = materialCodeList;
	}
	public List<SystemParamDTO> getMaterialDescList() {
		return materialDescList;
	}
	public void setMaterialDescList(List<SystemParamDTO> materialDescList) {
		this.materialDescList = materialDescList;
	}
	public List<SystemParamDTO> getCatelogNumberListList() {
		return catelogNumberListList;
	}
	public void setCatelogNumberListList(List<SystemParamDTO> catelogNumberListList) {
		this.catelogNumberListList = catelogNumberListList;
	}
	public List<SystemParamDTO> getDivisionCodeList() {
		return divisionCodeList;
	}
	public void setDivisionCodeList(List<SystemParamDTO> divisionCodeList) {
		this.divisionCodeList = divisionCodeList;
	}
	public List<SystemParamDTO> getDocumentTypeList() {
		return documentTypeList;
	}
	public void setDocumentTypeList(List<SystemParamDTO> documentTypeList) {
		this.documentTypeList = documentTypeList;
	}
	public List<SystemParamDTO> getPlantList() {
		return plantList;
	}
	public void setPlantList(List<SystemParamDTO> plantList) {
		this.plantList = plantList;
	}
	public List<SystemParamDTO> getPaymentTermList() {
		return paymentTermList;
	}
	public void setPaymentTermList(List<SystemParamDTO> paymentTermList) {
		this.paymentTermList = paymentTermList;
	}
	public List<SystemParamDTO> getDivisionNameList() {
		return divisionNameList;
	}
	public void setDivisionNameList(List<SystemParamDTO> divisionNameList) {
		this.divisionNameList = divisionNameList;
	}
	public String getIsSyncButtonShow() {
		return isSyncButtonShow;
	}
	public void setIsSyncButtonShow(String isSyncButtonShow) {
		this.isSyncButtonShow = isSyncButtonShow;
	}
	
	

}
