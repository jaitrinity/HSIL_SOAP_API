package com.hsil.controller;


import java.sql.Clob;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialClob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hsil.constant.CommonFunction;
import com.hsil.constant.Constant;
import com.hsil.constant.Email;
import com.hsil.constant.MyCalendar;
import com.hsil.constant.Response;
import com.hsil.constant.WriteExcel;
import com.hsil.entity.InputTypeModel;
import com.hsil.request.SOAPRequest;
import com.hsil.response.SOAPResponse;
import com.hsil.service.HsilService;

@CrossOrigin
@RestController
@RequestMapping(value = "/hsil")
public class HsilController {
	@Autowired
	HsilService hsilService;
	
	private void saveResponseInTable(Object request,Object response, String methodName){
		Clob requestJson = null;
		Clob responseJson = null;
		try {
			if(request != null){
				requestJson = new SerialClob(CommonFunction.printResponseJson(request).toCharArray());
			}
			responseJson = new SerialClob(CommonFunction.printResponseJson(response).toCharArray());
			hsilService.saveResponseInTable(requestJson,responseJson,methodName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value ="/getAllInputType",method = RequestMethod.GET)
	public Response<InputTypeModel> getAllInputType() {
		
		Response<InputTypeModel> response = null;
		try {
			response = hsilService.getAllInputType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getAllOutstandings",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllOutstandings() {
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllOutstandings();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllOutstandings");
		return response;
	}
	
	@RequestMapping(value ="/getAllOutstandingsOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllOutstandingsOfDate(@RequestParam("keyDate") String keyDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllOutstandingsOfDate(keyDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllOutstandingsOfDate :: keyDate - "+keyDate);
		return response;
	}

	
	// create cron tab
	@RequestMapping(value ="/getSalesReport",method = RequestMethod.GET)
	public Response<Map<String, String>> getSalesReport() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getSalesReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getSalesReport");
		return response;
	}
	
	@RequestMapping(value ="/getSalesReportOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getSalesReportOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getSalesReportOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getSalesReportOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getPendingOrders",method = RequestMethod.GET)
	public Response<Map<String, String>> getPendingOrders() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getPendingOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getPendingOrders");
		return response;
	}
	
	@RequestMapping(value ="/getPendingOrdersOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getPendingOrdersOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getPendingOrdersOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getPendingOrdersOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getInvoiceDetails",method = RequestMethod.GET)
	public Response<Map<String, String>> getInvoiceDetails() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getInvoiceDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getInvoiceDetails");
		return response;
	}
	
	@RequestMapping(value ="/getInvoiceDetailsOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getInvoiceDetailsOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getInvoiceDetailsOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getInvoiceDetailsOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getAllDealers",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllDealers() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllDealers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllDealers");
		return response;
	}
	
	@RequestMapping(value ="/getAllDealersOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllDealersOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllDealersOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllDealersOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	@RequestMapping(value ="/getAllDealersOfPeriod",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllDealersOfPeriod() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllDealersOfPeriod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllDealersOfPeriod");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getAllBilledOrders",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllBilledOrders() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllBilledOrders();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllBilledOrders");
		return response;
	}
	
	@RequestMapping(value ="/getAllBilledOrdersOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllBilledOrdersOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllBilledOrdersOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllBilledOrdersOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	@RequestMapping(value ="/getTerritoryCodeByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getTerritoryCodeByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getTerritoryCodeByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getTerritoryCodeByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getOutstandingByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getOutstandingByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getOutstandingByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(jsonData.getIsSyncOutstanding().equalsIgnoreCase("Yes")){
			saveResponseInTable(null, response, "getSyncOutstandingByEmpId - :: empId - "+jsonData.getEmpId());
		}
		//saveResponseInTable(null, response, "getOutstandingByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getPendingOrderByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getPendingOrderByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getPendingOrderByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getPendingOrderByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getVisitStatusByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getVisitStatusByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getVisitStatusByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getVisitStatusByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getNonVisitedDealerListByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getNonVisitedDealerListByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getNonVisitedDealerListByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getNonVisitedDealerListByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getNetworkExpansionByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getNetworkExpansionByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getNetworkExpansionByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getNetworkExpansionByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getPerformanceByEmpId",method = RequestMethod.POST)
	public Response<SOAPResponse> getPerformanceByEmpId(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = hsilService.getPerformanceByEmpId(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getPerformanceByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	//crontab
	@RequestMapping(value ="/insertOrUpdateEmpByIntergration",method = RequestMethod.GET)
	public Response<Map<String, String>> updateEmpByIntergration() {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			response = hsilService.insertOrUpdateEmpByIntergration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "insertOrUpdateEmpByIntergration");
		return response;
	}
	
	//crontab
	@RequestMapping(value ="/saveEmpAttendanceIntegrationData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveEmpAttendanceIntegrationData() {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			response = hsilService.saveEmpAttendanceIntegrationData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*saveResponseInTable(response.getSoapApiRequest(), 
				response.getResponseCode()+" :: "+response.getResponseDesc()+" :: "+response.getSoapApiResponse(),
				"saveEmpAttendanceIntegrationData");*/
		return response;
	}
	
	@RequestMapping(value ="/saveEmpAttendanceIntegrationDataOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> saveEmpAttendanceIntegrationDataOfDate(@RequestParam("date") String date) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			response = hsilService.saveEmpAttendanceIntegrationDataOfDate(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*saveResponseInTable(response.getSoapApiRequest(), 
				response.getResponseCode()+" :: "+response.getResponseDesc()+" :: "+response.getSoapApiResponse(),
				"saveEmpAttendanceIntegrationData");*/
		return response;
	}
	
	@RequestMapping(value ="/insertOrUpdateEmpByIntergrationOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> insertOrUpdateEmpByIntergrationOfDate(@RequestParam("date") String date) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			response = hsilService.insertOrUpdateEmpByIntergrationOfDate(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "insertOrUpdateEmpByIntergrationOfDate :: date - "+date);
		return response;
	}
	
	@RequestMapping(value ="/insertDealerInRegistrationByEmpId",method = RequestMethod.GET)
	public Response<Map<String, String>> insertDealerInRegistrationByEmpId(@RequestParam("type") String type,@RequestParam("employees") String employees) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			// type = Dealers, Sub Dealers, Distributor
			response = hsilService.insertDealerInRegistrationByEmpId(type,employees);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "insertDealerInRegistrationByEmpId");
		return response;
	}
	
	@RequestMapping(value ="/sendPjpReport",method = RequestMethod.GET)
	public Response<String> sendPjpReport(){
		Response<String> response = new Response<String>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String extension = ".xlsx";
			String path = Constant.DAILY_REPORT_FOLDER;
			String curDate = MyCalendar.getCurrentDateForExcel();
			
//			String pjpReportFileName = "Pjp_Report_"+curDate;
			String pjpReportFileName = "Pjp_Report";
			Map<String, Object[]> pjpReportData = hsilService.pjpReportData();
			String pjpReportResponse = WriteExcel.writeExcel(pjpReportData,path+pjpReportFileName+extension);
			successMap.put("PJP_REPORT", pjpReportResponse);
			
			
			String to = "GV.KRISHNA@hindware.co.in",
					cc="ayush.agarwal@trinityapplab.co.in,pushkar.tyagi@trinityapplab.co.in,jai.prakash@trinityapplab.co.in",
					subject="PJP Report - "+curDate,
					msg="<div>Hi Krishna,</div> <br> "
						+ "<div>Plz find the attached PJP report till "+curDate+". </div> <br>"
						+ "<div>PFA</div> <br><br>"
						+ "<div>Regards,</div>"
						+ "<div>Trinity Helpdesk</div> <br><br>"
						+ "<div>Note : This is an auto generated mail, please don't reply.</div> <br>";
			
			String attachFiles [] = {path+pjpReportFileName+extension};
			String emailSendResponse = Email.sendMail(to, cc, subject, msg, attachFiles);
			successMap.put("MAIL_RESPONSE", emailSendResponse);
			response.setErrorsMap(successMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null,response,"sendPjpReport");
		return response;
		
	}
	
	
	// create cron tab
	@RequestMapping(value ="/getAllProducts",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllProducts() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllProducts");
		return response;
	}
	
	@RequestMapping(value ="/getAllProductsOfDate",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllProductsOfDate(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate) {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllProductsOfDate(fromDate,toDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllProductsOfDate :: fromDate - "+fromDate);
		return response;
	}
	
	@RequestMapping(value ="/getAllProductsOfPeriod",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllProductsOfPeriod() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllProductsOfPeriod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllProductsOfPeriod");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getPaymentTerms",method = RequestMethod.GET)
	public Response<Map<String, String>> getPaymentTerms() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getPaymentTerms();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getPaymentTerms");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getAllStates",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllStates() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllStates();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllStates");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getAllDepots",method = RequestMethod.GET)
	public Response<Map<String, String>> getAllDepots() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getAllDepots();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getAllDepots");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/getDocumentType",method = RequestMethod.GET)
	public Response<Map<String, String>> getDocumentType() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.getDocumentType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "getDocumentType");
		return response;
	}
	
	// create cron tab
	@RequestMapping(value ="/insertOrUpdateTmDealerInRegistration",method = RequestMethod.GET)
	public Response<Map<String, String>> insertOrUpdateTmDealerInRegistration() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.insertOrUpdateTmDealerInRegistration();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "insertOrUpdateTmDealerInRegistration");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeePerformanceData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmpPerformanceData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeePerformanceData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeePerformanceData");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeeOutstandingData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmployeeOutstandingData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeeOutstandingData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeeOutstandingData");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeeVisitStatusData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmployeeVisitStatusData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeeVisitStatusData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeeVisitStatusData");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeePendingOrdersData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmployeePendingOrdersData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeePendingOrdersData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeePendingOrdersData");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeeNonVisitedDealerData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmployeeNonVisitedDealerData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeeNonVisitedDealerData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeeNonVisitedDealerData");
		return response;
	}
	
	@RequestMapping(value ="/saveAllEmployeeNetworkExpansionData",method = RequestMethod.GET)
	public Response<Map<String, String>> saveAllEmployeeNetworkExpansionData() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.saveAllEmployeeNetworkExpansionData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "saveAllEmployeeNetworkExpansionData");
		return response;
	}
	
	@RequestMapping(value ="/sendWhatsApp",method = RequestMethod.GET)
	public Response<Map<String, String>> sendWhatsApp() {
		
		Response<Map<String, String>> response = null;
		try {
			response = hsilService.sendWhatsApp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null, response, "sendWhatsApp");
		return response;
	}
	
	@RequestMapping(value ="/insertOrUpdateEmpByReadFile",method = RequestMethod.GET)
	public Response<Map<String, String>> insertOrUpdateEmpByReadFile() {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			response = hsilService.insertOrUpdateEmpByReadFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "insertOrUpdateEmpByReadFile :: date - "+date);
		return response;
	}
	
	@RequestMapping(value ="/attendanceReport",method = RequestMethod.GET)
	public Response<String> attendanceReport(){
		Response<String> response = new Response<String>();
		Map<String, String> successMap = new HashMap<String, String>();
		String prevDate = null;
		try {
			String extension = ".xlsx";
			String path = Constant.ATTENDANCE_FOLDER;
//			String path = "G:/jpm/myFolder/";
			prevDate = MyCalendar.getPreviousDateFromCurrentDate();
			
			String attendanceFileName = "Attendance_"+prevDate;
			Map<String, Object[]> attendanceData = hsilService.getAttendanceData(prevDate);
			String pjpReportResponse = WriteExcel.writeAttendanceExcel(attendanceData,path+attendanceFileName+extension);
			successMap.put("attendance_Report", pjpReportResponse);
			
			String to= "nagasai@hindware.co.in",
					cc= "varunreddy.e@hindware.co.in,prashanthreddy.t@hindware.co.in,vijayreddy.k@hindware.co.in,"
							+ "chalmareddy@hindware.co.in,pushkar.tyagi@trinityapplab.co.in,jai.prakash@trinityapplab.co.in",
					subject="Attendance - "+prevDate,
					msg="<div>Hi Naga,</div> <br> "
						+ "<div>Plz find the attached Attendance of "+prevDate+". </div> <br>"
						+ "<div>PFA</div> <br><br>"
						+ "<div>Regards,</div>"
						+ "<div>Trinity Helpdesk</div> <br><br>"
						+ "<div>Note : This is an auto generated mail, please don't reply.</div> <br>";
			
			String attachFiles [] = {path+attendanceFileName+extension};
			String emailSendResponse = Email.sendMail(to, cc, subject, msg, attachFiles);
			successMap.put("MAIL_RESPONSE", emailSendResponse);
			response.setErrorsMap(successMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(null,response,"attendanceReport - "+prevDate);
		return response;
		
	}
	
}
