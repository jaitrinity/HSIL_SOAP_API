package com.hsil.dao;

import java.sql.Clob;
import java.util.Map;

import com.hsil.constant.Response;
import com.hsil.dto.SystemParamDTO;
import com.hsil.entity.InputTypeModel;
import com.hsil.request.SOAPRequest;
import com.hsil.response.SOAPResponse;

public interface HsilDao {

	Response<InputTypeModel> getAllInputType();

	Response<Map<String, String>> getSalesReportOfDate(String fromDate,String toDate);
	Response<Map<String, String>> getSalesReport();
	
	Response<Map<String, String>> getPendingOrders();
	Response<Map<String, String>> getPendingOrdersOfDate(String fromDate,String toDate);
	
	Response<Map<String, String>> getInvoiceDetailsOfDate(String fromDate,String toDate);
	Response<Map<String, String>> getInvoiceDetails();

	Response<Map<String, String>> getAllDealers();
	
	Response<Map<String, String>> getAllDealersOfDate(String fromDate,String toDate);

	Response<Map<String, String>> getAllBilledOrders();
	
	Response<Map<String, String>> getAllBilledOrdersOfDate(String fromDate,String toDate);

	Response<SOAPResponse> getOutstandingByEmpId(SOAPRequest jsonData);

	Response<SOAPResponse> getPendingOrderByEmpId(SOAPRequest jsonData);
	
	Response<SOAPResponse> getVisitStatusByEmpId(SOAPRequest jsonData);

	Response<SOAPResponse> getNonVisitedDealerListByEmpId(SOAPRequest jsonData);

	Response<SOAPResponse> getNetworkExpansionByEmpId(SOAPRequest jsonData);

	Response<SOAPResponse> getPerformanceByEmpId(SOAPRequest jsonData);

	//Response<Map<String, String>> saveEmpIntegrationData();

	Response<Map<String, String>> saveEmpAttendanceIntegrationData();

	void saveResponseInTable(Clob requestJson, Clob responseJson, String methodName);

	Response<Map<String, String>> insertOrUpdateEmpByIntergration();

	Response<Map<String, String>> saveEmpAttendanceIntegrationDataOfDate(String date);

	Response<Map<String, String>> insertOrUpdateEmpByIntergrationOfDate(String date);
	Response<Map<String, String>> insertDealerInRegistrationByEmpId(String type,String employees);
	Response<Map<String, String>> getAllDealersOfPeriod();

	Response<Map<String, String>> getAllOutstandings();

	Response<Map<String, String>> getAllOutstandingsOfDate(String keyDate);

	Response<SOAPResponse> getTerritoryCodeByEmpId(SOAPRequest jsonData);
	
	Response<Map<String, String>> getPaymentTerms();
	
	Response<Map<String, String>> getAllProducts();

	/*

	Response<Map<String, String>> getCreditLimit();

	Response<Map<String, String>> getSalesEmpMaster();
	
	Response<Map<String, String>> getCreditLimitByDealerCode(String dealerCode);

	Response<Map<String, String>> getSalesEmpMasterOfPeriod();*/
	
	Response<Map<String, String>> getDocumentType();

	

	Response<Map<String, String>> getAllProductsOfPeriod();
	
	Response<Map<String, String>> getAllDepots();
	
	Response<Map<String, String>> getAllStates();

	/*Map<String, Object[]> pjpCompleteReport();

	Map<String, Object[]> pjpPendingReport();*/

	Response<Map<String, String>> insertOrUpdateTmDealerInRegistration();

	Map<String, Object[]> pjpReportData();

	Response<Map<String, String>> getAllProductsOfDate(String fromDate, String toDate);

	Response<Map<String, String>> saveAllEmployeePerformanceData();

	Response<Map<String, String>> saveAllEmployeeOutstandingData();

	Response<Map<String, String>> saveAllEmployeeVisitStatusData();

	Response<Map<String, String>> saveAllEmployeePendingOrdersData();

	Response<Map<String, String>> saveAllEmployeeNonVisitedDealerData();

	Response<Map<String, String>> saveAllEmployeeNetworkExpansionData();

	Response<Map<String, String>> sendWhatsApp();

	Response<Map<String, String>> insertOrUpdateEmpByReadFile();

	Map<String, Object[]> getAttendanceData(String prevDate);

}
