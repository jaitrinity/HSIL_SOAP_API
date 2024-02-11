package com.hsil.service;

import java.sql.Clob;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hsil.constant.Response;
import com.hsil.dao.HsilDao;
import com.hsil.dto.SystemParamDTO;
import com.hsil.entity.InputTypeModel;
import com.hsil.request.SOAPRequest;
import com.hsil.response.SOAPResponse;


public class HsilServiceImpl implements HsilService {
	@Autowired
	HsilDao hsilDao;

	public Response<InputTypeModel> getAllInputType() {
		return hsilDao.getAllInputType();
	}

	public Response<Map<String, String>> getSalesReportOfDate(String fromDate,String toDate) {
		return hsilDao.getSalesReportOfDate(fromDate,toDate);
	}
	
	public Response<Map<String, String>> getSalesReport() {
		return hsilDao.getSalesReport();
	}
	
	public Response<Map<String, String>> getPendingOrders() {
		return hsilDao.getPendingOrders();
	}
	
	public Response<Map<String, String>> getPendingOrdersOfDate(String fromDate,String toDate) {
		return hsilDao.getPendingOrdersOfDate(fromDate,toDate);
	}
	
	public Response<Map<String, String>> getInvoiceDetailsOfDate(String fromDate,String toDate) {
		return hsilDao.getInvoiceDetailsOfDate(fromDate,toDate);
	}
	
	public Response<Map<String, String>> getInvoiceDetails() {
		return hsilDao.getInvoiceDetails();
	}
	
	public Response<Map<String, String>> getAllDealers() {
		return hsilDao.getAllDealers();
	}
	
	public Response<Map<String, String>> getAllDealersOfDate(String fromDate,String toDate) {
		return hsilDao.getAllDealersOfDate(fromDate,toDate);
	}

	public Response<Map<String, String>> getAllBilledOrders() {
		return hsilDao.getAllBilledOrders();
	}
	
	public Response<Map<String, String>> getAllBilledOrdersOfDate(String fromDate,String toDate) {
		return hsilDao.getAllBilledOrdersOfDate(fromDate,toDate);
	}

	public Response<SOAPResponse> getOutstandingByEmpId(SOAPRequest jsonData) {
		return hsilDao.getOutstandingByEmpId(jsonData);
	}

	public Response<SOAPResponse> getPendingOrderByEmpId(SOAPRequest jsonData) {
		return hsilDao.getPendingOrderByEmpId(jsonData);
	}
	
	public Response<SOAPResponse> getVisitStatusByEmpId(SOAPRequest jsonData) {
		return hsilDao.getVisitStatusByEmpId(jsonData);
	}

	public Response<SOAPResponse> getNonVisitedDealerListByEmpId(SOAPRequest jsonData) {
		return hsilDao.getNonVisitedDealerListByEmpId(jsonData);
	}

	public Response<SOAPResponse> getNetworkExpansionByEmpId(SOAPRequest jsonData) {
		return hsilDao.getNetworkExpansionByEmpId(jsonData);
	}

	public Response<SOAPResponse> getPerformanceByEmpId(SOAPRequest jsonData) {
		return hsilDao.getPerformanceByEmpId(jsonData);
	}

	public Response<Map<String, String>> saveEmpAttendanceIntegrationData() {
		return hsilDao.saveEmpAttendanceIntegrationData();
	}

	public void saveResponseInTable(Clob requestJson, Clob responseJson, String methodName) {
		hsilDao.saveResponseInTable(requestJson,responseJson,methodName);
	}

	public Response<Map<String, String>> insertOrUpdateEmpByIntergration() {
		return hsilDao.insertOrUpdateEmpByIntergration();
	}

	public Response<Map<String, String>> saveEmpAttendanceIntegrationDataOfDate(String date) {
		return hsilDao.saveEmpAttendanceIntegrationDataOfDate(date);
	}

	public Response<Map<String, String>> insertOrUpdateEmpByIntergrationOfDate(String date) {
		return hsilDao.insertOrUpdateEmpByIntergrationOfDate(date);
	}

	public Response<Map<String, String>> insertDealerInRegistrationByEmpId(String type,String employees) {
		return hsilDao.insertDealerInRegistrationByEmpId(type,employees);
	}

	public Response<Map<String, String>> getAllDealersOfPeriod() {
		return hsilDao.getAllDealersOfPeriod();
	}

	public Response<Map<String, String>> getAllOutstandings() {
		return hsilDao.getAllOutstandings();
	}

	public Response<Map<String, String>> getAllOutstandingsOfDate(String keyDate) {
		return hsilDao.getAllOutstandingsOfDate(keyDate);
	}

	public Response<SOAPResponse> getTerritoryCodeByEmpId(SOAPRequest jsonData) {
		return hsilDao.getTerritoryCodeByEmpId(jsonData);
	}
	
	public Response<Map<String, String>> getPaymentTerms() {
		return hsilDao.getPaymentTerms();
	}
	
	public Response<Map<String, String>> getAllProducts() {
		return hsilDao.getAllProducts();
	}

	public Response<Map<String, String>> getAllStates() {
		return hsilDao.getAllStates();
	}
	
	public Response<Map<String, String>> getAllDepots() {
		return hsilDao.getAllDepots();
	}
	
	public Response<Map<String, String>> getDocumentType() {
		return hsilDao.getDocumentType();
	}

	public Response<Map<String, String>> getAllProductsOfPeriod() {
		return hsilDao.getAllProductsOfPeriod();
	}

	public Response<Map<String, String>> insertOrUpdateTmDealerInRegistration() {
		return hsilDao.insertOrUpdateTmDealerInRegistration();
	}

	public Map<String, Object[]> pjpReportData() {
		return hsilDao.pjpReportData();
	}

	public Response<Map<String, String>> getAllProductsOfDate(String fromDate, String toDate) {
		return hsilDao.getAllProductsOfDate(fromDate,toDate);
	}

	public Response<Map<String, String>> saveAllEmployeePerformanceData() {
		return hsilDao.saveAllEmployeePerformanceData();
	}

	public Response<Map<String, String>> saveAllEmployeeOutstandingData() {
		return hsilDao.saveAllEmployeeOutstandingData();
	}

	public Response<Map<String, String>> saveAllEmployeeVisitStatusData() {
		return hsilDao.saveAllEmployeeVisitStatusData();
	}

	public Response<Map<String, String>> saveAllEmployeePendingOrdersData() {
		return hsilDao.saveAllEmployeePendingOrdersData();
	}

	public Response<Map<String, String>> saveAllEmployeeNonVisitedDealerData() {
		return hsilDao.saveAllEmployeeNonVisitedDealerData();
	}

	public Response<Map<String, String>> saveAllEmployeeNetworkExpansionData() {
		return hsilDao.saveAllEmployeeNetworkExpansionData();
	}

	public Response<Map<String, String>> sendWhatsApp() {
		return hsilDao.sendWhatsApp();
	}

	public Response<Map<String, String>> insertOrUpdateEmpByReadFile() {
		return hsilDao.insertOrUpdateEmpByReadFile();
	}

	public Map<String, Object[]> getAttendanceData(String prevDate) {
		return hsilDao.getAttendanceData(prevDate);
	}

}
