package com.hsil.dao;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Query;
import javax.sql.rowset.serial.SerialClob;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.hsil.constant.CommonFunction;
import com.hsil.constant.Constant;
import com.hsil.constant.MyCalendar;
import com.hsil.constant.Response;
import com.hsil.constant.ReturnsCode;
import com.hsil.constant.SOAP;
import com.hsil.constant.SendSMS;
import com.hsil.dto.BasicDealerInfo;
import com.hsil.dto.SystemParamDTO;
import com.hsil.entity.AllEmployeeNetworkExpansionModel;
import com.hsil.entity.AllEmployeeNonVisitedDealerModel;
import com.hsil.entity.AllEmployeeOutstandingModel;
import com.hsil.entity.AllEmployeePendingOrdersModel;
import com.hsil.entity.AllProductModel;
import com.hsil.entity.BillOrderMaster;
import com.hsil.entity.DealerMasterModel;
import com.hsil.entity.DocumentTypeModel;
import com.hsil.entity.EmployeeMasterModel;
import com.hsil.entity.InputTypeModel;
import com.hsil.entity.InvoiceDetailsModel;
import com.hsil.entity.NetworkExpansionReportModel;
import com.hsil.entity.OutstandingMasterModel;
import com.hsil.entity.OutstandingMasterNewModel;
import com.hsil.entity.PendingOrdersModel;
import com.hsil.entity.ResponseTableModel;
import com.hsil.entity.SalesReportModel;
import com.hsil.generic.common.GenericDaoImpl;
import com.hsil.request.SOAPRequest;
import com.hsil.response.NetworkExpansionResponse;
import com.hsil.response.NonVisitedDealerResponse;
import com.hsil.response.OutstandingResponse;
import com.hsil.response.PendingOrderStatusResponse;
import com.hsil.response.PerformanceResponse;
import com.hsil.response.SOAPResponse;
import com.hsil.response.VisitStatusResponse;
import com.sun.net.httpserver.Authenticator.Success;

import jdk.nashorn.internal.scripts.JS;

public class HsilDaoImpl<T> extends GenericDaoImpl<Object> implements HsilDao {

	@SuppressWarnings("unchecked")
	public Response<InputTypeModel> getAllInputType() {
		Response<InputTypeModel> response = new Response<InputTypeModel>();
		try {
			String sql = "select i from InputTypeModel i ";
			Query query = getEm().createQuery(sql);
			List<InputTypeModel> qtList =  query.getResultList();
			if(qtList.size()!=0){
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setWrappedList(qtList);
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getSalesReport() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			Map<String, String> successMap = new HashMap<String, String>();
			String dealerCode = "";
			String fromDate = "";
			String toDate = MyCalendar.getCurrentDate();

			String currentDate = MyCalendar.getCurrentDate();
			String eightDateOfMonth = MyCalendar.getEightDateOfMonthInStr();
			int compareValue = MyCalendar.compareTwoStrDate(currentDate, eightDateOfMonth);
			
			if(compareValue <= 0){
				fromDate = MyCalendar.getPreviousMonthFirstDateInStr();
			}
			else{
				fromDate = MyCalendar.getFirstDateOfCurrentMonth();
			}
			//System.out.println(fromDate+" :: "+toDate);
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_SALES_REPORT> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_SALES> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <INV_DOC></INV_DOC> "+
					"               <BILLING_DATE></BILLING_DATE> "+
					"               <BILLING_TYPE></BILLING_TYPE> "+
					"               <MATERIAL></MATERIAL> "+
					"               <DISTRIBUTION_CHANNEL></DISTRIBUTION_CHANNEL> "+
					"               <DIVISION></DIVISION> "+
					"               <MATERIAL_DISCRIPTION></MATERIAL_DISCRIPTION> "+
					"               <QUANTITY></QUANTITY> "+
					"               <INVOICE_VALUE></INVOICE_VALUE> "+
					"               <EMPLOYEE></EMPLOYEE> "+
					"               <EMPLOYEE_NAME></EMPLOYEE_NAME> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <PROVG></PROVG> "+
					"               <PRD_CAT_TEXT></PRD_CAT_TEXT> "+
					"               <MATERIAL_GRP2></MATERIAL_GRP2> "+
					"              <MATERIAL_GRP2_TEXT></MATERIAL_GRP2_TEXT> "+
					"           </item> "+
					"        </TB_SALES> "+
					"     </urn:Z_SF_GET_SALES_REPORT> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Sales_Report where BILLING_DATE >= '"+fromDate+"' and BILLING_DATE <= '"+toDate+"' ").executeUpdate();
					
					SalesReportModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new SalesReportModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setInvDoc(node.getElementsByTagName("INV_DOC").item(0).getTextContent());
						String billingDate = node.getElementsByTagName("BILLING_DATE").item(0).getTextContent();
						e.setBillingDate(MyCalendar.convertStrDateToUtilDate(billingDate));
						e.setBillingType(node.getElementsByTagName("BILLING_TYPE").item(0).getTextContent());
						e.setMaterial(node.getElementsByTagName("MATERIAL").item(0).getTextContent());
						e.setDistributionChannel(node.getElementsByTagName("DISTRIBUTION_CHANNEL").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setMaterialDiscription(node.getElementsByTagName("MATERIAL_DISCRIPTION").item(0).getTextContent());
						e.setQuantity(node.getElementsByTagName("QUANTITY").item(0).getTextContent());
						e.setInvoiceValue(node.getElementsByTagName("INVOICE_VALUE").item(0).getTextContent());
						e.setEmployee(node.getElementsByTagName("EMPLOYEE").item(0).getTextContent());
						e.setEmployeeName(node.getElementsByTagName("EMPLOYEE_NAME").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setProvg(node.getElementsByTagName("PROVG").item(0).getTextContent());
						e.setPrdCatText(node.getElementsByTagName("PRD_CAT_TEXT").item(0).getTextContent());
						e.setMaterialGrp2(node.getElementsByTagName("MATERIAL_GRP2").item(0).getTextContent());
						e.setMaterialGrp2Text(node.getElementsByTagName("MATERIAL_GRP2_TEXT").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete record : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("SALES_REPORT", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("SALES_REPORT", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Sales_Report : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getSalesReportOfDate(String fromDate,String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			Map<String, String> successMap = new HashMap<String, String>();
			String dealerCode = "";

			//System.out.println(fromDate+" :: "+toDate);
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_SALES_REPORT> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_SALES> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <INV_DOC></INV_DOC> "+
					"               <BILLING_DATE></BILLING_DATE> "+
					"               <BILLING_TYPE></BILLING_TYPE> "+
					"               <MATERIAL></MATERIAL> "+
					"               <DISTRIBUTION_CHANNEL></DISTRIBUTION_CHANNEL> "+
					"               <DIVISION></DIVISION> "+
					"               <MATERIAL_DISCRIPTION></MATERIAL_DISCRIPTION> "+
					"               <QUANTITY></QUANTITY> "+
					"               <INVOICE_VALUE></INVOICE_VALUE> "+
					"               <EMPLOYEE></EMPLOYEE> "+
					"               <EMPLOYEE_NAME></EMPLOYEE_NAME> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <PROVG></PROVG> "+
					"               <PRD_CAT_TEXT></PRD_CAT_TEXT> "+
					"               <MATERIAL_GRP2></MATERIAL_GRP2> "+
					"              <MATERIAL_GRP2_TEXT></MATERIAL_GRP2_TEXT> "+
					"           </item> "+
					"        </TB_SALES> "+
					"     </urn:Z_SF_GET_SALES_REPORT> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Sales_Report where BILLING_DATE >= '"+fromDate+"' and BILLING_DATE <= '"+toDate+"' ").executeUpdate();
					
					SalesReportModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new SalesReportModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setInvDoc(node.getElementsByTagName("INV_DOC").item(0).getTextContent());
						String billingDate = node.getElementsByTagName("BILLING_DATE").item(0).getTextContent();
						e.setBillingDate(MyCalendar.convertStrDateToUtilDate(billingDate));
						e.setBillingType(node.getElementsByTagName("BILLING_TYPE").item(0).getTextContent());
						e.setMaterial(node.getElementsByTagName("MATERIAL").item(0).getTextContent());
						e.setDistributionChannel(node.getElementsByTagName("DISTRIBUTION_CHANNEL").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setMaterialDiscription(node.getElementsByTagName("MATERIAL_DISCRIPTION").item(0).getTextContent());
						e.setQuantity(node.getElementsByTagName("QUANTITY").item(0).getTextContent());
						e.setInvoiceValue(node.getElementsByTagName("INVOICE_VALUE").item(0).getTextContent());
						e.setEmployee(node.getElementsByTagName("EMPLOYEE").item(0).getTextContent());
						e.setEmployeeName(node.getElementsByTagName("EMPLOYEE_NAME").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setProvg(node.getElementsByTagName("PROVG").item(0).getTextContent());
						e.setPrdCatText(node.getElementsByTagName("PRD_CAT_TEXT").item(0).getTextContent());
						e.setMaterialGrp2(node.getElementsByTagName("MATERIAL_GRP2").item(0).getTextContent());
						e.setMaterialGrp2Text(node.getElementsByTagName("MATERIAL_GRP2_TEXT").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete record : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("SALES_REPORT", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("SALES_REPORT", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getPendingOrders() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String dealerCode = "";
			String fromDate = "";
			String toDate = MyCalendar.getCurrentDate();

			String currentDate = MyCalendar.getCurrentDate();
			String eightDateOfMonth = MyCalendar.getEightDateOfMonthInStr();
			int compareValue = MyCalendar.compareTwoStrDate(currentDate, eightDateOfMonth);
			
			if(compareValue <= 0){
				fromDate = MyCalendar.getPreviousMonthFirstDateInStr();
			}
			else{
				fromDate = MyCalendar.getFirstDateOfCurrentMonth();
			}
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_PENDING_ORDERS> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_PENDING_ORDERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <ORDER_TYPE></ORDER_TYPE> "+
					"               <ORDER_NUMBER></ORDER_NUMBER> "+
					"               <ORDER_DATE></ORDER_DATE> "+
					"               <PLANT_CODE></PLANT_CODE> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PENDING_QUA></PENDING_QUA> "+
					"               <PENDING_AMT></PENDING_AMT> "+
					"               <ORDER_VALUE></ORDER_VALUE> "+
					"               <CREDIT_BLOCK></CREDIT_BLOCK> "+
					"               <MAT_NOT_AVAIL></MAT_NOT_AVAIL> "+
					"               <REL_YET_TO_PROC></REL_YET_TO_PROC> "+
					"               <REASON_FOR_PENDING></REASON_FOR_PENDING> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"            </item> "+
					"         </TB_PENDING_ORDERS> "+
					"      </urn:Z_SF_GET_PENDING_ORDERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Pending_Orders where ORDER_DATE >= '"+fromDate+"' and ORDER_DATE <= '"+toDate+"' ").executeUpdate();
					
					PendingOrdersModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new PendingOrdersModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
						e.setOrderType(node.getElementsByTagName("ORDER_TYPE").item(0).getTextContent());
						e.setOrderNumber(node.getElementsByTagName("ORDER_NUMBER").item(0).getTextContent());
						String orderDate = node.getElementsByTagName("ORDER_DATE").item(0).getTextContent();
						e.setOrderDate(MyCalendar.convertStrDateToUtilDate(orderDate));
						e.setPlantCode(node.getElementsByTagName("PLANT_CODE").item(0).getTextContent());
						e.setProductCode(node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent());
						e.setPendingQua(node.getElementsByTagName("PENDING_QUA").item(0).getTextContent());
						e.setPendingAmt(node.getElementsByTagName("PENDING_AMT").item(0).getTextContent());
						e.setOrderValue(node.getElementsByTagName("ORDER_VALUE").item(0).getTextContent());
						e.setCreditBlock(node.getElementsByTagName("CREDIT_BLOCK").item(0).getTextContent());
						e.setMatNotAvail(node.getElementsByTagName("MAT_NOT_AVAIL").item(0).getTextContent());
						e.setRelYetToProc(node.getElementsByTagName("REL_YET_TO_PROC").item(0).getTextContent());
						e.setReasonForPending(node.getElementsByTagName("REASON_FOR_PENDING").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("PENDING_ORDERS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("PENDING_ORDERS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Pending_Order : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getPendingOrdersOfDate(String fromDate,String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String dealerCode = "";

			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_PENDING_ORDERS> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_PENDING_ORDERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <ORDER_TYPE></ORDER_TYPE> "+
					"               <ORDER_NUMBER></ORDER_NUMBER> "+
					"               <ORDER_DATE></ORDER_DATE> "+
					"               <PLANT_CODE></PLANT_CODE> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PENDING_QUA></PENDING_QUA> "+
					"               <PENDING_AMT></PENDING_AMT> "+
					"               <ORDER_VALUE></ORDER_VALUE> "+
					"               <CREDIT_BLOCK></CREDIT_BLOCK> "+
					"               <MAT_NOT_AVAIL></MAT_NOT_AVAIL> "+
					"               <REL_YET_TO_PROC></REL_YET_TO_PROC> "+
					"               <REASON_FOR_PENDING></REASON_FOR_PENDING> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"            </item> "+
					"         </TB_PENDING_ORDERS> "+
					"      </urn:Z_SF_GET_PENDING_ORDERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Pending_Orders where ORDER_DATE >= '"+fromDate+"' and ORDER_DATE <= '"+toDate+"' ").executeUpdate();
					
					PendingOrdersModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new PendingOrdersModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
						e.setOrderType(node.getElementsByTagName("ORDER_TYPE").item(0).getTextContent());
						e.setOrderNumber(node.getElementsByTagName("ORDER_NUMBER").item(0).getTextContent());
						String orderDate = node.getElementsByTagName("ORDER_DATE").item(0).getTextContent();
						e.setOrderDate(MyCalendar.convertStrDateToUtilDate(orderDate));
						e.setPlantCode(node.getElementsByTagName("PLANT_CODE").item(0).getTextContent());
						e.setProductCode(node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent());
						e.setPendingQua(node.getElementsByTagName("PENDING_QUA").item(0).getTextContent());
						e.setPendingAmt(node.getElementsByTagName("PENDING_AMT").item(0).getTextContent());
						e.setOrderValue(node.getElementsByTagName("ORDER_VALUE").item(0).getTextContent());
						e.setCreditBlock(node.getElementsByTagName("CREDIT_BLOCK").item(0).getTextContent());
						e.setMatNotAvail(node.getElementsByTagName("MAT_NOT_AVAIL").item(0).getTextContent());
						e.setRelYetToProc(node.getElementsByTagName("REL_YET_TO_PROC").item(0).getTextContent());
						e.setReasonForPending(node.getElementsByTagName("REASON_FOR_PENDING").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("PENDING_ORDERS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("PENDING_ORDERS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getInvoiceDetails() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String dealerCode = "";
			String currentDate = MyCalendar.getCurrentDate();
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_INVOICE_DETAILS> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+currentDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE></IM_TO_DATE> "+
					"      </urn:Z_SF_GET_INVOICE_DETAILS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					
					InvoiceDetailsModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new InvoiceDetailsModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setInvDoc(node.getElementsByTagName("INV_DOC").item(0).getTextContent());
						e.setDocumentType(node.getElementsByTagName("DOCUMENT_TYPE").item(0).getTextContent());
						String invDate = node.getElementsByTagName("INV_DATE").item(0).getTextContent();
						e.setInvDate(MyCalendar.convertStrDateToUtilDate(invDate));
						e.setMaterial(node.getElementsByTagName("MATERIAL").item(0).getTextContent());
						e.setMatDesc(node.getElementsByTagName("MAT_DESC").item(0).getTextContent());
						e.setMaterialGrp2(node.getElementsByTagName("MATERIAL_GRP2").item(0).getTextContent());
						e.setProvg(node.getElementsByTagName("PROVG").item(0).getTextContent());
						e.setPrdCatText(node.getElementsByTagName("PRD_CAT_TEXT").item(0).getTextContent());
						e.setInvQty(node.getElementsByTagName("INV_QTY").item(0).getTextContent());
						e.setNetAmount(node.getElementsByTagName("NET_AMOUNT").item(0).getTextContent());
						e.setBilValue(node.getElementsByTagName("BILL_VALUE").item(0).getTextContent());
						e.setDistChnl(node.getElementsByTagName("DIST_CHNL").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setEmployee(node.getElementsByTagName("EMPLOYEE").item(0).getTextContent());
						e.setEmployeeName(node.getElementsByTagName("EMPLOYEE_NAME").item(0).getTextContent());
						e.setEmpEmailId(node.getElementsByTagName("EMP_MAIL_ID").item(0).getTextContent());
						e.setTerritoryCode(node.getElementsByTagName("TERRITORY_CODE").item(0).getTextContent());
						e.setTerritoryName(node.getElementsByTagName("TERRITORY_NAME").item(0).getTextContent());
						e.setOrderNo(node.getElementsByTagName("ORDER_NO").item(0).getTextContent());
						e.setInvoiceCopy(node.getElementsByTagName("INVOICE_COPY").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Record : "+String.valueOf(successRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INVOICE_DETAILS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("INVOICE_DETAILS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Invoice_Details : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getInvoiceDetailsOfDate(String fromDate,String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String dealerCode = "";
			String currentDate = fromDate;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_INVOICE_DETAILS> "+
					"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
					"         <IM_FROM_DATE>"+currentDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE></IM_TO_DATE> "+
					"      </urn:Z_SF_GET_INVOICE_DETAILS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					
					InvoiceDetailsModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new InvoiceDetailsModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setInvDoc(node.getElementsByTagName("INV_DOC").item(0).getTextContent());
						e.setDocumentType(node.getElementsByTagName("DOCUMENT_TYPE").item(0).getTextContent());
						String invDate = node.getElementsByTagName("INV_DATE").item(0).getTextContent();
						e.setInvDate(MyCalendar.convertStrDateToUtilDate(invDate));
						e.setMaterial(node.getElementsByTagName("MATERIAL").item(0).getTextContent());
						e.setMatDesc(node.getElementsByTagName("MAT_DESC").item(0).getTextContent());
						e.setMaterialGrp2(node.getElementsByTagName("MATERIAL_GRP2").item(0).getTextContent());
						e.setProvg(node.getElementsByTagName("PROVG").item(0).getTextContent());
						e.setPrdCatText(node.getElementsByTagName("PRD_CAT_TEXT").item(0).getTextContent());
						e.setInvQty(node.getElementsByTagName("INV_QTY").item(0).getTextContent());
						e.setNetAmount(node.getElementsByTagName("NET_AMOUNT").item(0).getTextContent());
						e.setBilValue(node.getElementsByTagName("BILL_VALUE").item(0).getTextContent());
						e.setDistChnl(node.getElementsByTagName("DIST_CHNL").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setEmployee(node.getElementsByTagName("EMPLOYEE").item(0).getTextContent());
						e.setEmployeeName(node.getElementsByTagName("EMPLOYEE_NAME").item(0).getTextContent());
						e.setEmpEmailId(node.getElementsByTagName("EMP_MAIL_ID").item(0).getTextContent());
						e.setTerritoryCode(node.getElementsByTagName("TERRITORY_CODE").item(0).getTextContent());
						e.setTerritoryName(node.getElementsByTagName("TERRITORY_NAME").item(0).getTextContent());
						e.setOrderNo(node.getElementsByTagName("ORDER_NO").item(0).getTextContent());
						e.setInvoiceCopy(node.getElementsByTagName("INVOICE_COPY").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Record : "+String.valueOf(successRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INVOICE_DETAILS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("INVOICE_DETAILS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Response<Map<String, String>> getAllDealers() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			//String currentDate = MyCalendar.getCurrentDate();
			String previousDate = MyCalendar.getPreviousDateFromCurrentDate(); 
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_DEALERS> "+
					"         <IM_FROM_DATE>"+previousDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE></IM_TO_DATE> "+
					"         <TB_DEALERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <DEALER_NAME></DEALER_NAME> "+
					"               <DEALER_TYPE></DEALER_TYPE> "+
					"               <DEALER_TYPE_DESC></DEALER_TYPE_DESC> "+
					"               <SHOW_ROOM></SHOW_ROOM> "+
					"               <CONT_PERSON></CONT_PERSON> "+
					"               <CONT_ADR1></CONT_ADR1> "+
					"               <CONT_ADR1_PIN></CONT_ADR1_PIN> "+
					"               <PRIM_EMAIL_ID></PRIM_EMAIL_ID> "+
					"               <PRIM_MOBILE_NO></PRIM_MOBILE_NO> "+
					"               <MOBILE_NO></MOBILE_NO> "+
					"               <STREET></STREET> "+
					"               <SALES_OFFICE></SALES_OFFICE> "+
					"               <REGION></REGION> "+
					"               <TERRITORY></TERRITORY> "+
					"               <TER_NAME></TER_NAME> "+
					"               <CITY_CODE></CITY_CODE> "+
					"               <TIN_NO></TIN_NO> "+
					"               <PAN_NO></PAN_NO> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <IS_ACTIVE></IS_ACTIVE> "+
					"               <GSTIN></GSTIN> "+
					"               <PRICE_GROUP></PRICE_GROUP> "+
					"				<CUST_STATUS></CUST_STATUS> "+
					"				<CUST_STATUS_DESC></CUST_STATUS_DESC> "+
					"				<SALES_ORG></SALES_ORG> "+
					"				<SALES_ORG_DESC></SALES_ORG_DESC> "+
					"				<ACCOUNT_GROUP></ACCOUNT_GROUP> "+
					"				<ACCOUNT_GROUP_DESC></ACCOUNT_GROUP_DESC> "+
					"            </item> "+
					"         </TB_DEALERS> "+
					"      </urn:Z_SF_GET_ALL_DEALERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					/*List<BasicDealerInfo> updatedDealerList = new ArrayList<BasicDealerInfo>();
					BasicDealerInfo info = null;*/
					
					NetworkExpansionReportModel model = null;
					List<String> distinctDealerCode = new ArrayList<String>();
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dealerCode = node.getElementsByTagName("DEALER_CODE").item(0).getTextContent();
						String dealerName = node.getElementsByTagName("DEALER_NAME").item(0).getTextContent();
						/*String dealerType = node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent();
						String contPerson = node.getElementsByTagName("CONT_PERSON").item(0).getTextContent();
						String primEmailId = node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent();
						String primMobileNo = node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent();*/
						String terr_code = node.getElementsByTagName("TERRITORY").item(0).getTextContent();
						String terr_name = node.getElementsByTagName("TER_NAME").item(0).getTextContent();
						String cust_status = node.getElementsByTagName("CUST_STATUS").item(0).getTextContent();
						int isActive = cust_status.equalsIgnoreCase("") || cust_status.equalsIgnoreCase("01") ? 1 : 0;
						if(!distinctDealerCode.contains(dealerCode)){
							distinctDealerCode.add(dealerCode);
							
							String sql = "select `DEALER_CODE` from `Network_Expansion_Report` where `DEALER_CODE` = '"+dealerCode+"' ";
							Query query = getEm().createNativeQuery(sql);
							List<String> neResult = query.getResultList();
							if(neResult.size() == 0){
								model = new NetworkExpansionReportModel();
								model.setDealerCode(dealerCode);
								model.setDealerName(dealerName);
								model.setTerritory(terr_code);
								model.setTerName(terr_name);
								model.setIsActive(isActive);
								model.setCreateDate(new Date());
								save(model);
							}
							
							/*if(dealerType.equalsIgnoreCase("Dealers") || dealerType.equalsIgnoreCase("Distributor")){
								info = new BasicDealerInfo();
								info.setDealerCode(dealerCode);
								info.setDealerName(dealerName);
								info.setDealerType(dealerType);
								info.setContPerson(contPerson);
								info.setPrimEmailId(primEmailId);
								info.setPrimMobileNo(primMobileNo);
								info.setTerriCode(terr_code);
								info.setTerriName(terr_name);
								updatedDealerList.add(info);
							}*/
						}
					}
					
					/*if(updatedDealerList.size() != 0){
						updateDealerAndDistributorInfoInRegistrationTable(updatedDealerList,successMap);
					}*/
					
					
					int deleteCount = 0;
					for(String dealerCode : distinctDealerCode){
						String sql = "delete from Dealer_Master_25Aug where DEALER_CODE = '"+dealerCode+"' ";
						deleteCount += getEm().createNativeQuery(sql).executeUpdate();
						//System.out.println("delete count :: "+deleteCount);
					}
					
					
					DealerMasterModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new DealerMasterModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setDealerName(node.getElementsByTagName("DEALER_NAME").item(0).getTextContent());
						e.setDealerType(node.getElementsByTagName("DEALER_TYPE").item(0).getTextContent());
						e.setDealerTypeDesc(node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent());
						e.setShowRoom(node.getElementsByTagName("SHOW_ROOM").item(0).getTextContent());
						e.setContPerson(node.getElementsByTagName("CONT_PERSON").item(0).getTextContent());
						e.setContAdr1(node.getElementsByTagName("CONT_ADR1").item(0).getTextContent());
						e.setContAdr1Pin(node.getElementsByTagName("CONT_ADR1_PIN").item(0).getTextContent());
						e.setPrimEmailId(node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent());
						e.setPrimMobileNumber(node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent());
						e.setMobileNo(node.getElementsByTagName("MOBILE_NO").item(0).getTextContent());
						e.setStreet(node.getElementsByTagName("STREET").item(0).getTextContent());
						e.setSalesOffice(node.getElementsByTagName("SALES_OFFICE").item(0).getTextContent());
						e.setRegion(node.getElementsByTagName("REGION").item(0).getTextContent());
						e.setTerritory(node.getElementsByTagName("TERRITORY").item(0).getTextContent());
						e.setTerName(node.getElementsByTagName("TER_NAME").item(0).getTextContent());
						e.setCityCode(node.getElementsByTagName("CITY_CODE").item(0).getTextContent());
						e.setTinNo(node.getElementsByTagName("TIN_NO").item(0).getTextContent());
						e.setPanNo(node.getElementsByTagName("PAN_NO").item(0).getTextContent());
						e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						//e.setIsActive(node.getElementsByTagName("IS_ACTIVE").item(0).getTextContent());
						e.setGstin(node.getElementsByTagName("GSTIN").item(0).getTextContent());
						e.setPriceGroup(node.getElementsByTagName("PRICE_GROUP").item(0).getTextContent());
						e.setCustStatus(node.getElementsByTagName("CUST_STATUS").item(0).getTextContent());
						if(e.getCustStatus().equalsIgnoreCase("") || e.getCustStatus().equalsIgnoreCase("01")){
							e.setIsActive("X"); // active
						}
						else{
							e.setIsActive("XX"); // inactive
						}
						e.setCustStatusDesc(node.getElementsByTagName("CUST_STATUS_DESC").item(0).getTextContent());
						e.setSalesOrg(node.getElementsByTagName("SALES_ORG").item(0).getTextContent());
						e.setSalesOrgDesc(node.getElementsByTagName("SALES_ORG_DESC").item(0).getTextContent());
						e.setAccountGroup(node.getElementsByTagName("ACCOUNT_GROUP").item(0).getTextContent());
						e.setAccountGroupDesc(node.getElementsByTagName("ACCOUNT_GROUP_DESC").item(0).getTextContent());
						e.setDealerInsertDate(previousDate);
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_DEALER", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_DEALER", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in All_Dealer_Master : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	private void updateDealerAndDistributorInfoInRegistrationTable(List<BasicDealerInfo> updatedDealerList, Map<String, String> successMap) {
		try {
			for(BasicDealerInfo info : updatedDealerList){
				String type="";
				if(info.getDealerType().equalsIgnoreCase("Dealers")){
					type = "Dealer";
				}
				String sql = "update `Registration` set"
						+ " `F2` = '"+info.getDealerName()+"',"
						+ " `F3` = '"+info.getPrimEmailId()+"',"
						+ " `F4` = '"+info.getPrimMobileNo()+"',"
						+ " `modified_date` = CURRENT_TIMESTAMP "
						+ " where `F1` = '"+info.getDealerCode()+"'"
								+ " and abs(`territory_code`) = abs('"+info.getTerriCode()+"')"
								+ " and `Type` = '"+type+"'"
								+ " and `Is_Active` = 'Y' ";
				
				int updateCount = getEm().createNativeQuery(sql).executeUpdate();
				System.out.println("updateCount : "+updateCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_In_updateDealerAndDistributorInfoInRegistrationTable", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Response<Map<String, String>> getAllDealersOfDate(String fromDate,String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_DEALERS> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_DEALERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <DEALER_NAME></DEALER_NAME> "+
					"               <DEALER_TYPE></DEALER_TYPE> "+
					"               <DEALER_TYPE_DESC></DEALER_TYPE_DESC> "+
					"               <SHOW_ROOM></SHOW_ROOM> "+
					"               <CONT_PERSON></CONT_PERSON> "+
					"               <CONT_ADR1></CONT_ADR1> "+
					"               <CONT_ADR1_PIN></CONT_ADR1_PIN> "+
					"               <PRIM_EMAIL_ID></PRIM_EMAIL_ID> "+
					"               <PRIM_MOBILE_NO></PRIM_MOBILE_NO> "+
					"               <MOBILE_NO></MOBILE_NO> "+
					"               <STREET></STREET> "+
					"               <SALES_OFFICE></SALES_OFFICE> "+
					"               <REGION></REGION> "+
					"               <TERRITORY></TERRITORY> "+
					"               <TER_NAME></TER_NAME> "+
					"               <CITY_CODE></CITY_CODE> "+
					"               <TIN_NO></TIN_NO> "+
					"               <PAN_NO></PAN_NO> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <IS_ACTIVE></IS_ACTIVE> "+
					"               <GSTIN></GSTIN> "+
					"               <PRICE_GROUP></PRICE_GROUP> "+
					"				<CUST_STATUS></CUST_STATUS> "+
					"				<CUST_STATUS_DESC></CUST_STATUS_DESC> "+
					"				<SALES_ORG></SALES_ORG> "+
					"				<SALES_ORG_DESC></SALES_ORG_DESC> "+
					"				<ACCOUNT_GROUP></ACCOUNT_GROUP> "+
					"				<ACCOUNT_GROUP_DESC></ACCOUNT_GROUP_DESC> "+
					"            </item> "+
					"         </TB_DEALERS> "+
					"      </urn:Z_SF_GET_ALL_DEALERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					/*List<BasicDealerInfo> updatedDealerList = new ArrayList<BasicDealerInfo>();
					BasicDealerInfo info = null;*/
					
					NetworkExpansionReportModel model = null;
					List<String> distinctDealerCode = new ArrayList<String>();
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dealerCode = node.getElementsByTagName("DEALER_CODE").item(0).getTextContent();
						String dealerName = node.getElementsByTagName("DEALER_NAME").item(0).getTextContent();
						/*String dealerType = node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent();
						String contPerson = node.getElementsByTagName("CONT_PERSON").item(0).getTextContent();
						String primEmailId = node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent();
						String primMobileNo = node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent();*/
						String terr_code = node.getElementsByTagName("TERRITORY").item(0).getTextContent();
						String terr_name = node.getElementsByTagName("TER_NAME").item(0).getTextContent();
						String cust_status = node.getElementsByTagName("CUST_STATUS").item(0).getTextContent();
						int isActive = cust_status.equalsIgnoreCase("") || cust_status.equalsIgnoreCase("01") ? 1 : 0;
						if(!distinctDealerCode.contains(dealerCode)){
							distinctDealerCode.add(dealerCode);
							
							String sql = "select `DEALER_CODE` from `Network_Expansion_Report` where `DEALER_CODE` = '"+dealerCode+"' ";
							Query query = getEm().createNativeQuery(sql);
							List<String> neResult = query.getResultList();
							if(neResult.size() == 0){
								model = new NetworkExpansionReportModel();
								model.setDealerCode(dealerCode);
								model.setDealerName(dealerName);
								model.setTerritory(terr_code);
								model.setTerName(terr_name);
								model.setIsActive(isActive);
								model.setCreateDate(new Date());
								save(model);
							}
							
							/*if(dealerType.equalsIgnoreCase("Dealers") || dealerType.equalsIgnoreCase("Distributor")){
								info = new BasicDealerInfo();
								info.setDealerCode(dealerCode);
								info.setDealerName(dealerName);
								info.setDealerType(dealerType);
								info.setContPerson(contPerson);
								info.setPrimEmailId(primEmailId);
								info.setPrimMobileNo(primMobileNo);
								info.setTerriCode(terr_code);
								info.setTerriName(terr_name);
								updatedDealerList.add(info);
							}*/
							
						}
					}
					
					/*if(updatedDealerList.size() != 0){
						updateDealerAndDistributorInfoInRegistrationTable(updatedDealerList,successMap);
					}*/
					
					int deleteCount = 0;
					for(String dealerCode : distinctDealerCode){
						String sql = "delete from Dealer_Master_25Aug where DEALER_CODE = '"+dealerCode+"' ";
						deleteCount += getEm().createNativeQuery(sql).executeUpdate();
						//System.out.println("delete count :: "+deleteCount);
					}
					
					
					DealerMasterModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new DealerMasterModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setDealerName(node.getElementsByTagName("DEALER_NAME").item(0).getTextContent());
						e.setDealerType(node.getElementsByTagName("DEALER_TYPE").item(0).getTextContent());
						e.setDealerTypeDesc(node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent());
						e.setShowRoom(node.getElementsByTagName("SHOW_ROOM").item(0).getTextContent());
						e.setContPerson(node.getElementsByTagName("CONT_PERSON").item(0).getTextContent());
						e.setContAdr1(node.getElementsByTagName("CONT_ADR1").item(0).getTextContent());
						e.setContAdr1Pin(node.getElementsByTagName("CONT_ADR1_PIN").item(0).getTextContent());
						e.setPrimEmailId(node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent());
						e.setPrimMobileNumber(node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent());
						e.setMobileNo(node.getElementsByTagName("MOBILE_NO").item(0).getTextContent());
						e.setStreet(node.getElementsByTagName("STREET").item(0).getTextContent());
						e.setSalesOffice(node.getElementsByTagName("SALES_OFFICE").item(0).getTextContent());
						e.setRegion(node.getElementsByTagName("REGION").item(0).getTextContent());
						e.setTerritory(node.getElementsByTagName("TERRITORY").item(0).getTextContent());
						e.setTerName(node.getElementsByTagName("TER_NAME").item(0).getTextContent());
						e.setCityCode(node.getElementsByTagName("CITY_CODE").item(0).getTextContent());
						e.setTinNo(node.getElementsByTagName("TIN_NO").item(0).getTextContent());
						e.setPanNo(node.getElementsByTagName("PAN_NO").item(0).getTextContent());
						e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						//e.setIsActive(node.getElementsByTagName("IS_ACTIVE").item(0).getTextContent());
						e.setGstin(node.getElementsByTagName("GSTIN").item(0).getTextContent());
						e.setPriceGroup(node.getElementsByTagName("PRICE_GROUP").item(0).getTextContent());
						e.setCustStatus(node.getElementsByTagName("CUST_STATUS").item(0).getTextContent());
						if(e.getCustStatus().equalsIgnoreCase("") || e.getCustStatus().equalsIgnoreCase("01")){
							e.setIsActive("X"); // active
						}
						else{
							e.setIsActive("XX"); // inactive
						}
						e.setCustStatusDesc(node.getElementsByTagName("CUST_STATUS_DESC").item(0).getTextContent());
						e.setSalesOrg(node.getElementsByTagName("SALES_ORG").item(0).getTextContent());
						e.setSalesOrgDesc(node.getElementsByTagName("SALES_ORG_DESC").item(0).getTextContent());
						e.setAccountGroup(node.getElementsByTagName("ACCOUNT_GROUP").item(0).getTextContent());
						e.setAccountGroupDesc(node.getElementsByTagName("ACCOUNT_GROUP_DESC").item(0).getTextContent());
						e.setDealerInsertDate(fromDate);
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_DEALER", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_DEALER", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getAllBilledOrders() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String fromDate = "";
			String toDate = MyCalendar.getCurrentDate();

			String currentDate = MyCalendar.getCurrentDate();
			String eightDateOfMonth = MyCalendar.getEightDateOfMonthInStr();
			int compareValue = MyCalendar.compareTwoStrDate(currentDate, eightDateOfMonth);
			
			if(compareValue <= 0){
				fromDate = MyCalendar.getPreviousMonthFirstDateInStr();
			}
			else{
				fromDate = MyCalendar.getFirstDateOfCurrentMonth();
			}
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_BILLED_ORDERS> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_BILLED_ORDERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <DIVISION></DIVISION> "+
					"               <ORDER_NUMBER></ORDER_NUMBER> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PRODUCT_NAME></PRODUCT_NAME> "+
					"               <ORDER_TYPE></ORDER_TYPE> "+
					"               <ORDER_DATE></ORDER_DATE> "+
					"               <ORDER_NEXF></ORDER_NEXF> "+
					"               <BILL_NO></BILL_NO> "+
					"               <BILLED_VALUE></BILLED_VALUE> "+
					"               <ORDER_QTY></ORDER_QTY> "+
					"               <BILLED_QTY></BILLED_QTY> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <TERRITORY></TERRITORY> "+
					"            </item> "+
					"         </TB_BILLED_ORDERS> "+
					"      </urn:Z_SF_GET_ALL_BILLED_ORDERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Bill_Order_Master where ORDER_DATE >= '"+fromDate+"' and ORDER_DATE <= '"+toDate+"'  ").executeUpdate();
					
					BillOrderMaster e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new BillOrderMaster();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setOrderNumber(node.getElementsByTagName("ORDER_NUMBER").item(0).getTextContent());
						e.setProductCode(node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent());
						e.setProductName(node.getElementsByTagName("PRODUCT_NAME").item(0).getTextContent());
						e.setOrderType(node.getElementsByTagName("ORDER_TYPE").item(0).getTextContent());
						String orderDate = node.getElementsByTagName("ORDER_DATE").item(0).getTextContent();
						e.setOrderDate(MyCalendar.convertStrDateToUtilDate(orderDate));
						e.setOrderNexf(node.getElementsByTagName("ORDER_NEXF").item(0).getTextContent());
						e.setBillNo(node.getElementsByTagName("BILL_NO").item(0).getTextContent());
						e.setBilledValue(node.getElementsByTagName("BILLED_VALUE").item(0).getTextContent());
						e.setOrderQty(node.getElementsByTagName("ORDER_QTY").item(0).getTextContent());
						e.setBilledQty(node.getElementsByTagName("BILLED_QTY").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setTerritory(node.getElementsByTagName("TERRITORY").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_BILLED_ORDER", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_BILLED_ORDER", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in All_Billed_Order : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getAllBilledOrdersOfDate(String fromDate,String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_BILLED_ORDERS> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_BILLED_ORDERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <DIVISION></DIVISION> "+
					"               <ORDER_NUMBER></ORDER_NUMBER> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PRODUCT_NAME></PRODUCT_NAME> "+
					"               <ORDER_TYPE></ORDER_TYPE> "+
					"               <ORDER_DATE></ORDER_DATE> "+
					"               <ORDER_NEXF></ORDER_NEXF> "+
					"               <BILL_NO></BILL_NO> "+
					"               <BILLED_VALUE></BILLED_VALUE> "+
					"               <ORDER_QTY></ORDER_QTY> "+
					"               <BILLED_QTY></BILLED_QTY> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <TERRITORY></TERRITORY> "+
					"            </item> "+
					"         </TB_BILLED_ORDERS> "+
					"      </urn:Z_SF_GET_ALL_BILLED_ORDERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					int deleteCount = getEm().createNativeQuery("delete from Bill_Order_Master where ORDER_DATE >= '"+fromDate+"' and ORDER_DATE <= '"+toDate+"'  ").executeUpdate();
					
					BillOrderMaster e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new BillOrderMaster();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setOrderNumber(node.getElementsByTagName("ORDER_NUMBER").item(0).getTextContent());
						e.setProductCode(node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent());
						e.setProductName(node.getElementsByTagName("PRODUCT_NAME").item(0).getTextContent());
						e.setOrderType(node.getElementsByTagName("ORDER_TYPE").item(0).getTextContent());
						String orderDate = node.getElementsByTagName("ORDER_DATE").item(0).getTextContent();
						e.setOrderDate(MyCalendar.convertStrDateToUtilDate(orderDate));
						e.setOrderNexf(node.getElementsByTagName("ORDER_NEXF").item(0).getTextContent());
						e.setBillNo(node.getElementsByTagName("BILL_NO").item(0).getTextContent());
						e.setBilledValue(node.getElementsByTagName("BILLED_VALUE").item(0).getTextContent());
						e.setOrderQty(node.getElementsByTagName("ORDER_QTY").item(0).getTextContent());
						e.setBilledQty(node.getElementsByTagName("BILLED_QTY").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setTerritory(node.getElementsByTagName("TERRITORY").item(0).getTextContent());
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_BILLED_ORDER", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_BILLED_ORDER", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	private List<OutstandingResponse> prepareOutstandingData(SOAPRequest jsonData,Map<String, String> errorMap) {
		List<OutstandingResponse> response = new ArrayList<OutstandingResponse>();
		List<OutstandingMasterNewModel> omResultList = new ArrayList<OutstandingMasterNewModel>();
		try {
			/*if(jsonData.getIsSyncOutstanding().equalsIgnoreCase("Yes")){
				String startTime = MyCalendar.getCurrentTimestamp();
				String endTime = null;
				String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+ 
						"   <soap:Header/> "+
						"   <soap:Body> "+
						"      <urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
						"         <IM_DEALER_CODE>"+jsonData.getDealerCode()+"</IM_DEALER_CODE> "+
						"         <KEY_DATE>"+jsonData.getKeyDate()+"</KEY_DATE> "+
						"         <TB_ALL_OUTSTANDING> "+
						"            <item> "+
						"               <DEALER_CODE></DEALER_CODE> "+
						"               <DIVISION></DIVISION> "+
						"               <OUTSTA_AMOUNT></OUTSTA_AMOUNT> "+
						"               <NOT_DUE></NOT_DUE> "+
						"               <Z0_30></Z0_30> "+
						"               <Z31_60></Z31_60> "+
						"               <Z61_90></Z61_90> "+
						"               <Z91_180></Z91_180> "+
						"               <Z181_365></Z181_365> "+
						"               <Z366_730></Z366_730> "+
						"               <Z731_1095></Z731_1095> "+
						"               <Z1096_ABOVE></Z1096_ABOVE> "+
						"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
						"               <PROFIT_CENTER></PROFIT_CENTER> "+
						"               <DOC_TYPE></DOC_TYPE> "+
						"               <DOC_NUM></DOC_NUM> "+
						"               <DOC_DATE></DOC_DATE> "+
						"            </item> "+
						"         </TB_ALL_OUTSTANDING> "+
						"      </urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
						"   </soap:Body> "+
						"</soap:Envelope> ";
						
				String soapResponse = SOAP.method(soapRequest,errorMap);
				
				if(soapResponse != null){
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(soapResponse));
					Document doc = builder.parse(src);
					NodeList nd = doc.getElementsByTagName("item");
					if(nd.getLength() != 0){
						getEm().createNativeQuery("delete from Outstanding_Master_New where DEALER_CODE = '"+jsonData.getDealerCode()+"' ").executeUpdate();
						
						OutstandingMasterNewModel e = null;
						for (int i = 0; i < nd.getLength(); i++) {
							Element node = (Element) nd.item(i);
							e = new OutstandingMasterNewModel();
							e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
							e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
							e.setOutstaAmount(node.getElementsByTagName("OUTSTA_AMOUNT").item(0).getTextContent());
							e.setNotDue(node.getElementsByTagName("NOT_DUE").item(0).getTextContent());
							e.setZ0_30(node.getElementsByTagName("Z0_30").item(0).getTextContent());
							e.setZ31_60(node.getElementsByTagName("Z31_60").item(0).getTextContent());
							e.setZ61_90(node.getElementsByTagName("Z61_90").item(0).getTextContent());
							e.setZ91_180(node.getElementsByTagName("Z91_180").item(0).getTextContent());
							e.setZ181_365(node.getElementsByTagName("Z181_365").item(0).getTextContent());
							e.setZ366_730(node.getElementsByTagName("Z366_730").item(0).getTextContent());
							e.setZ731_1095(node.getElementsByTagName("Z731_1095").item(0).getTextContent());
							e.setZ1096_Above(node.getElementsByTagName("Z1096_ABOVE").item(0).getTextContent());
							e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
							e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
							e.setDocType(node.getElementsByTagName("DOC_TYPE").item(0).getTextContent());
							e.setDocNum(node.getElementsByTagName("DOC_NUM").item(0).getTextContent());
							e.setDocDate(node.getElementsByTagName("DOC_DATE").item(0).getTextContent());
							double z366_Above = Double.parseDouble(e.getZ366_730()) +
									Double.parseDouble(e.getZ731_1095()) +
									Double.parseDouble(e.getZ1096_Above());
							e.setZ366_Above(String.valueOf(z366_Above));
							e.setCreateDate(new Date());
							save(e);
							omResultList.add(e);
						}
						endTime = MyCalendar.getCurrentTimestamp();
					}
					else{
						endTime = MyCalendar.getCurrentTimestamp();
					}
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					errorMap.put("OUTSTANDING", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				
				if(soapResponse == null){
					String mobileNumber = CommonFunction.readTextFile();
					//System.out.println(mobileNumber);
					int isSend = SendSMS.sendSms("Error in Get_All_Outstanding, when sync outstanding : "+errorMap.get("SOAP_API"), mobileNumber);
					System.out.println("isSendSMS :: "+isSend);
				}
			}*/
			
			
			if(errorMap.size() == 0){
				String divisionCodeInclude = null;
				String profitCenterExclude = null;
				String creditControlAreaInclude = null;
				
				
				String sql = "SELECT `Config_Type`,`Config_Value` FROM `Outstanding_Report_Config` ";
				Query query = getEm().createNativeQuery(sql);
				List<Object[]> result =  query.getResultList();
				if(result.size() != 0){
					for(Object[] obj : result){
						String configType = String.valueOf(obj[0]);
						String configValue = String.valueOf(obj[1]);
						if(configType.equalsIgnoreCase("divisionCodeIncludeList")){
							divisionCodeInclude = configValue;
						}
						else if(configType.equalsIgnoreCase("profitCenterExcludeList")){
							profitCenterExclude = configValue;
						}
						else if(configType.equalsIgnoreCase("creditControlAreaIncludeList")){
							creditControlAreaInclude = configValue;
						}
					}
				}
				
				List<String> diviCodeIncludeList = CommonFunction.getListOfUniqueStringTokens(divisionCodeInclude, ",");
				List<String> profitCenterExcludeList = CommonFunction.getListOfUniqueStringTokens(profitCenterExclude, ",");
				List<String> creditControlAreaIncludeList = CommonFunction.getListOfUniqueStringTokens(creditControlAreaInclude, ",");
				
				if(jsonData.getIsSyncOutstanding().equalsIgnoreCase("No")){
					sql = "select om from OutstandingMasterNewModel om where om.dealerCode = '"+jsonData.getDealerCode()+"' ";
					Query omQuery = getEm().createQuery(sql);
					omResultList =  omQuery.getResultList();
				}
				
				List<OutstandingMasterNewModel> response2 = new ArrayList<OutstandingMasterNewModel>();
				for(OutstandingMasterNewModel om : omResultList){
					if(!profitCenterExcludeList.contains(om.getProfitCenter()) ){
						if(om.getCreditControlArea() != null){
							if(creditControlAreaIncludeList.contains(om.getCreditControlArea())){
								response2.add(om);
							}
						}
						else{
							if(diviCodeIncludeList.contains(om.getDivision())){
								response2.add(om);
							}
						}
					}
				}
				
				if(response2.size() !=0){
					getEm().createNativeQuery("delete from Outstanding_Master where DEALER_CODE = '"+jsonData.getDealerCode()+"' ").executeUpdate();
					
					OutstandingMasterModel en = null;
					for(OutstandingMasterNewModel om : response2){
						en = new OutstandingMasterModel();
						en.setDealerCode(om.getDealerCode());
						en.setDivisionCode(om.getDivision());
						en.setOutstaAmount(om.getOutstaAmount());
						en.setNotDue(om.getNotDue());
						en.setZ0_30(om.getZ0_30());
						en.setZ31_60(om.getZ31_60());
						en.setZ61_90(om.getZ61_90());
						en.setZ91_180(om.getZ91_180());
						en.setZ181_365(om.getZ181_365());
						en.setZ366_Above(om.getZ366_Above());
						en.setCreateDate(new Date());
						save(en);
					}
					
					sql = "SELECT max(`DEALER_CODE`),max(`DEALER_NAME`),max(`DIVISION_CODE`),sum(`OUTSTA_AMOUNT`),sum(`NOT_DUE`),sum(`Z0_30`),sum(`Z31_60`),sum(`Z61_90`),sum(`Z91_180`),sum(`Z181_365`),sum(`Z366_ABOVE`) FROM `Outstanding_Master` where `DEALER_CODE` = '"+jsonData.getDealerCode()+"' GROUP BY `DEALER_CODE`,`DIVISION_CODE` ";
					query = getEm().createNativeQuery(sql);
					List<Object[]> res =  query.getResultList();

					OutstandingResponse om = null;
					for(Object[] obj : res){
						om = new OutstandingResponse();
						om.setDealerCode(CommonFunction.removePrefixZero(String.valueOf(obj[0])));
						om.setDealerName(String.valueOf(obj[1]));
						om.setDivision(String.valueOf(obj[2]));
						om.setOutstaAmount(String.valueOf(new BigDecimal(String.valueOf(obj[3])).setScale(2,0)));
						om.setNotDue(String.valueOf(new BigDecimal(String.valueOf(obj[4])).setScale(2,0)));
						om.setZ0_30(String.valueOf(new BigDecimal(String.valueOf(obj[5])).setScale(2,0)));
						om.setZ31_60(String.valueOf(new BigDecimal(String.valueOf(obj[6])).setScale(2,0)));
						om.setZ61_90(String.valueOf(new BigDecimal(String.valueOf(obj[7])).setScale(2,0)));
						om.setZ91_180(String.valueOf(new BigDecimal(String.valueOf(obj[8])).setScale(2,0)));
						om.setZ181_365(String.valueOf(new BigDecimal(String.valueOf(obj[9])).setScale(2,0)));
						om.setZ366_Above(String.valueOf(new BigDecimal(String.valueOf(obj[10])).setScale(2,0)));
						response.add(om);
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getOutstandingByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "SELECT `DEALER_CODE`, `DEALER_NAME`, `DIVISION_CODE`, `OUTSTA_AMOUNT`, `NOT_DUE`, `Z0_30`, `Z31_60`, `Z61_90`, `Z91_180`, `Z181_365`, `Z366_ABOVE` FROM `All_Employee_Outstanding` WHERE `Emp_id` = '"+jsonData.getEmpId()+"' and `Territory_Code` = '"+jsonData.getTerritoryCode()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() !=0){
				List<OutstandingResponse> outstandingList = new ArrayList<OutstandingResponse>();
				OutstandingResponse out = null;
				for(Object[] obj : result){
					out = new OutstandingResponse();
					out.setDealerCode(String.valueOf(obj[0]));
					out.setDealerName(String.valueOf(obj[1]));
					out.setDivision(String.valueOf(obj[2]));
					out.setOutstaAmount(String.valueOf(obj[3]));
					out.setNotDue(String.valueOf(obj[4]));
					out.setZ0_30(String.valueOf(obj[5]));
					out.setZ31_60(String.valueOf(obj[6]));
					out.setZ61_90(String.valueOf(obj[7]));
					out.setZ91_180(String.valueOf(obj[8]));
					out.setZ181_365(String.valueOf(obj[9]));
					out.setZ366_Above(String.valueOf(obj[10]));
					outstandingList.add(out);
				}
				soapObj.setOustanding(outstandingList);
				wrappedList.add(soapObj);
				response.setWrappedList(wrappedList);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	private void prepareNonVisitedDealer(String fromDate, String toDate, String empId, NonVisitedDealerResponse res, String dealerCode, Map<String, String> errorMap) {
		try {
			String sql = " SELECT `DEALER_CODE`, sum(`INVOICE_VALUE`) as invoice_value FROM `Sales_Report` where `DEALER_CODE` like '%"+dealerCode+"%' and `BILLING_DATE` >= '"+fromDate+"' and `BILLING_DATE` <= '"+toDate+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() != 0){
				Object[] obj = rList.get(0);
				if(obj[1] != null){
					res.setYtdSale(String.valueOf(new BigDecimal(String.valueOf(obj[1])).setScale(2,0)));
				}
				
				prepareLastVisitDateForNonVisitedDealer(empId, dealerCode, res);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
	}

	@SuppressWarnings("unchecked")
	private void prepareLastVisitDateForNonVisitedDealer(String empId, String dealerCode, NonVisitedDealerResponse res) {
		try {
			String sql = " SELECT 'last_visit', Date(max(`visits`.`visit_date`)) FROM `visits` join `Registration` on `visits`.`rno` = `Registration`.`reg_id` where `Registration`.`dealer_code` like '%"+dealerCode+"%' and `Registration`.`emp_id` = '"+empId+"' and `Registration`.`Type` = 'Dealer' and `visits`.`mid` is not null ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null){
					res.setLastVisitDate(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getFinancialYearDate(){
		String fromDate = "";
		String toDate = "";
		String currentDate = MyCalendar.getCurrentDate();
		int currentYear = MyCalendar.getYearFromCurrentDate();
		int nextYear = currentYear+1;
		String sql = "SELECT `Start_Date`,`End_Date`  FROM `Financial_Year_Date` WHERE `Start_Date` <= '"+currentDate+"' AND `End_Date` >= '"+currentDate+"' And Status = 'Current' ";
		Query query = getEm().createNativeQuery(sql);
		List<Object[]> dList = query.getResultList();
		if(dList.size() != 0){
			Object[] obj = dList.get(0);
			fromDate = String.valueOf(obj[0]);
			toDate = String.valueOf(obj[1]);
		}
		else{
			int d = getEm().createNativeQuery("delete from Financial_Year_Date where Status = 'Previous' ").executeUpdate();
			if(d != 0){
				d = getEm().createNativeQuery("update Financial_Year_Date set Status = 'Previous' where Status = 'Current' ").executeUpdate();
				if(d !=0){
					getEm().createNativeQuery("INSERT INTO `Financial_Year_Date` (`Start_Date`, `End_Date`, `Status`) "
							+ "VALUES ("+currentYear+"'-04-01', "+nextYear+"'-03-31', 'Current') ").executeUpdate();
				}
			}
			fromDate = currentYear+"-04-01";
			toDate = nextYear+"-03-31";
		}
		
		return fromDate+":"+toDate;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void prepareInsertSalesReportOfDealer(List<String> includeDealerCode, Map<String, String> errorMap) {
		try {
			
			String financialYearDate = getFinancialYearDate();
			String fromDate = financialYearDate.split(":")[0];
			String toDate = financialYearDate.split(":")[1];
			
			String divisionCodeInclude = null;
			String profitCenterExclude = null;
			String creditControlAreaInclude = null;
			
			
			String sql = "SELECT `Config_Type`,`Config_Value` FROM `Outstanding_Report_Config` ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result =  query.getResultList();
			if(result.size() != 0){
				for(Object[] obj : result){
					String configType = String.valueOf(obj[0]);
					String configValue = String.valueOf(obj[1]);
					if(configType.equalsIgnoreCase("divisionCodeIncludeList")){
						divisionCodeInclude = configValue;
					}
					else if(configType.equalsIgnoreCase("profitCenterExcludeList")){
						profitCenterExclude = configValue;
					}
					else if(configType.equalsIgnoreCase("creditControlAreaIncludeList")){
						creditControlAreaInclude = configValue;
					}
				}
			}
			
			
			List<String> diviCodeIncludeList = CommonFunction.getListOfUniqueStringTokens(divisionCodeInclude, ",");
			
			List<String> profitCenterExcludeList = CommonFunction.getListOfUniqueStringTokens(profitCenterExclude, ",");
			
			List<String> creditControlAreaIncludeList = CommonFunction.getListOfUniqueStringTokens(creditControlAreaInclude, ",");
			
			for(String dealerCode : includeDealerCode){
				String startTime = MyCalendar.getCurrentTimestamp();
				String endTime = null;
				String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
						"   <soap:Header/> "+
						"   <soap:Body> "+
						"      <urn:Z_SF_GET_SALES_REPORT> "+
						"         <IM_DEALER_CODE>"+dealerCode+"</IM_DEALER_CODE> "+
						"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
						"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
						"         <TB_SALES> "+
						"            <item> "+
						"               <DEALER_CODE></DEALER_CODE> "+
						"               <INV_DOC></INV_DOC> "+
						"               <BILLING_DATE></BILLING_DATE> "+
						"               <BILLING_TYPE></BILLING_TYPE> "+
						"               <MATERIAL></MATERIAL> "+
						"               <DISTRIBUTION_CHANNEL></DISTRIBUTION_CHANNEL> "+
						"               <DIVISION></DIVISION> "+
						"               <MATERIAL_DISCRIPTION></MATERIAL_DISCRIPTION> "+
						"               <QUANTITY></QUANTITY> "+
						"               <INVOICE_VALUE></INVOICE_VALUE> "+
						"               <EMPLOYEE></EMPLOYEE> "+
						"               <EMPLOYEE_NAME></EMPLOYEE_NAME> "+
						"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
						"               <PROFIT_CENTER></PROFIT_CENTER> "+
						"               <PROVG></PROVG> "+
						"               <PRD_CAT_TEXT></PRD_CAT_TEXT> "+
						"               <MATERIAL_GRP2></MATERIAL_GRP2> "+
						"              <MATERIAL_GRP2_TEXT></MATERIAL_GRP2_TEXT> "+
						"           </item> "+
						"        </TB_SALES> "+
						"     </urn:Z_SF_GET_SALES_REPORT> "+
						"   </soap:Body> "+
						"</soap:Envelope> ";
				
				String soapResponse = SOAP.method(soapRequest,errorMap);
				if(soapResponse != null){
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(soapResponse));
					Document doc = builder.parse(src);
					NodeList nd = doc.getElementsByTagName("item");
					if(nd.getLength() != 0){
						getEm().createNativeQuery("delete from Sales_Report where BILLING_DATE >= '"+fromDate+"' and BILLING_DATE <= '"+toDate+"' and DEALER_CODE = '"+dealerCode+"' ").executeUpdate();
						
						SalesReportModel e = null;
						int successRecord = 0;
						for (int i = 0; i < nd.getLength(); i++) {
							Element node = (Element) nd.item(i);
							String division = node.getElementsByTagName("DIVISION").item(0).getTextContent();
							String profitCenter = node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent();
							String creditControlArea = node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent();
							
							boolean isAllTrue = false;
							
							if(!profitCenterExcludeList.contains(profitCenter) ){
								if(creditControlArea != null){
									if(creditControlAreaIncludeList.contains(creditControlArea)){
										isAllTrue = true;
									}
								}
								else{
									if(diviCodeIncludeList.contains(division)){
										isAllTrue = true;
									}
								}
							}
							
							if(isAllTrue){
								e = new SalesReportModel();
								e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
								e.setInvDoc(node.getElementsByTagName("INV_DOC").item(0).getTextContent());
								String billingDate = node.getElementsByTagName("BILLING_DATE").item(0).getTextContent();
								e.setBillingDate(MyCalendar.convertStrDateToUtilDate(billingDate));
								e.setBillingType(node.getElementsByTagName("BILLING_TYPE").item(0).getTextContent());
								e.setMaterial(node.getElementsByTagName("MATERIAL").item(0).getTextContent());
								e.setDistributionChannel(node.getElementsByTagName("DISTRIBUTION_CHANNEL").item(0).getTextContent());
								e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
								e.setMaterialDiscription(node.getElementsByTagName("MATERIAL_DISCRIPTION").item(0).getTextContent());
								e.setQuantity(node.getElementsByTagName("QUANTITY").item(0).getTextContent());
								e.setInvoiceValue(node.getElementsByTagName("INVOICE_VALUE").item(0).getTextContent());
								e.setEmployee(node.getElementsByTagName("EMPLOYEE").item(0).getTextContent());
								e.setEmployeeName(node.getElementsByTagName("EMPLOYEE_NAME").item(0).getTextContent());
								e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
								e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
								e.setProvg(node.getElementsByTagName("PROVG").item(0).getTextContent());
								e.setPrdCatText(node.getElementsByTagName("PRD_CAT_TEXT").item(0).getTextContent());
								e.setMaterialGrp2(node.getElementsByTagName("MATERIAL_GRP2").item(0).getTextContent());
								e.setMaterialGrp2Text(node.getElementsByTagName("MATERIAL_GRP2_TEXT").item(0).getTextContent());
								e.setCreateDate(new Date());
								save(e);
								successRecord++;
							}
							
						}
						endTime = MyCalendar.getCurrentTimestamp();
						errorMap.put("SALES_REPORT - "+dealerCode+" ", "Record : "+String.valueOf(successRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
					}
					else{
						endTime = MyCalendar.getCurrentTimestamp();
						errorMap.put("SALES_REPORT - "+dealerCode+" ", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
					}
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					errorMap.put("SALES_REPORT - "+dealerCode+" ", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	private void prepareVisitPendingOrderStatusByDealerCode(String dealerName, List<PendingOrderStatusResponse> pendingOrderStatusList,
			String dealerCode,Map<String, String> errorMap) {
		try {
			
			PendingOrderStatusResponse poResponse = null;
			String sql = "select pom from PendingOrdersModel pom where pom.dealerCode = '"+dealerCode+"' ";
			Query query = getEm().createQuery(sql);
			List<PendingOrdersModel> poList =  query.getResultList();
			for(PendingOrdersModel po : poList){
				String orderNumber = po.getOrderNumber();
				String productCode = po.getProductCode();
				String orderQty = null;
				String billedQty = null;
				sql = "select bom from BillOrderMaster bom where bom.dealerCode = '"+dealerCode+"' and bom.orderNumber = '"+orderNumber+"' and bom.productCode = '"+productCode+"' ";
				query = getEm().createQuery(sql);
				List<BillOrderMaster> boList =  query.getResultList();
				for(BillOrderMaster bo : boList){
					if(orderQty == null)
						orderQty = bo.getOrderQty();
					
					if(billedQty == null)
						billedQty = bo.getBilledQty();
					
					if(orderQty != null && billedQty != null){
						break;
					}
				}
				poResponse = new PendingOrderStatusResponse();
				poResponse.setDealerCode(CommonFunction.removePrefixZero(dealerCode));
				poResponse.setDealerName(dealerName);
				poResponse.setMaterialCode(productCode);
				poResponse.setMaterialDesc(po.getProductName());
				poResponse.setOrderNo(orderNumber);
				if(orderQty != null){
					poResponse.setOrderQty(orderQty);
				}
				else{
					poResponse.setOrderQty(po.getPendingQua());
				}
				
				if(billedQty != null){
					poResponse.setBilledQty(billedQty);
				}
				else{
					poResponse.setBilledQty("0.0");
				}
				
				poResponse.setPendingQty(po.getPendingQua());
				poResponse.setPendingStatus(po.getReasonForPending());
				pendingOrderStatusList.add(poResponse);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private String getClassficationByDealerCode(String dealerCode){
		String classification = null;
		try {
			String sql = " SELECT `Rev_Classification` FROM `Dealer_Classification` where `Sold_to_Party` = '"+dealerCode+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> result =  query.getResultList();
			if(result.size() !=0){
				classification = result.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return classification;
	}

	@SuppressWarnings("unchecked")
	private void prepareVisitStatusByDealerCode(int subDealerInClassA, int subDealerInClassB, int subDealerInClassC, 
			int subDealerVisitDoneClassA,int subDealerVisitDoneClassB,int subDealerVisitDoneClassC,List<VisitStatusResponse> visitStatusList, List<String> dealerCodeList,
			String empId) {
		try {
			int dealerInClassA=0,dealerVisitDoneClassA=0;
			int dealerInClassB=0,dealerVisitDoneClassB=0;
			int dealerInClassC=0,dealerVisitDoneClassC=0;
					
			for(String dealerCode : dealerCodeList){
				int dealerVisitDone = prepareVisitDoneForAllType(dealerCode,empId,Constant.DEALER);
				String classification = getClassficationByDealerCode(dealerCode);
				if(classification == null){
					classification = Constant.DEFAULT_CLASSIFICATION;
					dealerInClassC++;
					dealerVisitDoneClassC += dealerVisitDone;
				}
				else if(classification.equalsIgnoreCase("A")){
					dealerInClassA++;
					dealerVisitDoneClassA += dealerVisitDone;
				}else if(classification.equalsIgnoreCase("B")){
					dealerInClassB++;
					dealerVisitDoneClassB += dealerVisitDone;
				}else if(classification.equalsIgnoreCase("C")){
					dealerInClassC++;
					dealerVisitDoneClassC += dealerVisitDone;
				}
			}
			
			String sql = " SELECT `Type`,`Classfication`,`Visit_Per_Dealer` from `Visit_Status_Config` ORDER by `Config_Id` ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> configResult =  query.getResultList();
			
			VisitStatusResponse res = null;
			int grandTotalVisit = 0;
			int grandBalOfVisit = 0;
			int grandNoOfVisitDone = 0;
			for(Object[] obj : configResult){
				res = new VisitStatusResponse();
				String type = String.valueOf(obj[0]);
				String classfication = String.valueOf(obj[1]);
				int visitPerDealer = Integer.parseInt(String.valueOf(obj[2]));
				int noOfDealer = 0;
				int totalVisit = 0;
				int noOfVisitDone = 0;
				int balOfVisit = 0;
				if(type.equalsIgnoreCase("Dealer")){
					if(classfication.equalsIgnoreCase("A")){
						noOfDealer = dealerInClassA;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = dealerVisitDoneClassA;
					}else if(classfication.equalsIgnoreCase("B")){
						noOfDealer = dealerInClassB;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = dealerVisitDoneClassB;
					}else if(classfication.equalsIgnoreCase("C")){
						noOfDealer = dealerInClassC;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = dealerVisitDoneClassC;
					}
				}
				else if(type.equalsIgnoreCase("Sub Dealer")){
					if(classfication.equalsIgnoreCase("A")){
						noOfDealer = subDealerInClassA;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = subDealerVisitDoneClassA;
					}else if(classfication.equalsIgnoreCase("B")){
						noOfDealer = subDealerInClassB;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = subDealerVisitDoneClassB;
					}else if(classfication.equalsIgnoreCase("C")){
						noOfDealer = subDealerInClassC;
						totalVisit = noOfDealer*visitPerDealer;
						noOfVisitDone = subDealerVisitDoneClassC;
					}
				}
				else if(type.equalsIgnoreCase("Project Site")){
					noOfDealer = getProjectSiteByEmpId(empId);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getProjectSiteDoneByEmpId(empId);
					}
				}
				else if(type.equalsIgnoreCase("Distributor")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Architect")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Builder")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Contractor")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Office")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Plumber")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				else if(type.equalsIgnoreCase("Prospect")){
					noOfDealer = getNoOfTypeByEmpId(empId,type);
					totalVisit = noOfDealer*visitPerDealer;
					if(noOfDealer != 0){
						noOfVisitDone = getTypeVisitDoneByEmpId(empId,type);
					}
				}
				
				
				balOfVisit = totalVisit - noOfVisitDone;
				
				grandTotalVisit += totalVisit;
				grandBalOfVisit += balOfVisit;
				grandNoOfVisitDone += noOfVisitDone;
				
				float totalVisitF = totalVisit;
				float noOfVisitDoneF = noOfVisitDone;
				float adharancePercentage = 0;
				if(totalVisit != 0){
					adharancePercentage = (noOfVisitDoneF / totalVisitF) * 100 ;
				}
				
				res.setType(type);
				res.setClassification(classfication);
				res.setNoOfDealer(noOfDealer);
				res.setVisitPerDealer(visitPerDealer);
				res.setTotalVisit(totalVisit);
				res.setNoOfVisitDone(noOfVisitDone);
				res.setAdharancePercentage(Math.round(adharancePercentage));
				res.setBalOfVisit(balOfVisit);
				visitStatusList.add(res);
			}
			
			float grandTotalVisitF = grandTotalVisit;
			float grandNoOfVisitDoneF = grandNoOfVisitDone;
			float grandAdharancePercentage = 0;
			if(grandTotalVisit != 0){
				grandAdharancePercentage = (grandNoOfVisitDoneF / grandTotalVisitF)  * 100;
			}
			
			res = new VisitStatusResponse();
			res.setType("Grand Total");
			res.setTotalVisit(grandTotalVisit);
			res.setNoOfVisitDone(grandNoOfVisitDone);
			res.setAdharancePercentage(Math.round(grandAdharancePercentage));
			res.setBalOfVisit(grandBalOfVisit);
			visitStatusList.add(res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private int prepareVisitDoneForAllType(String dealerCode, String empId,String type) {
		// type = Dealer, Sub dealer, 
		int visitDone = 0;
		try {
			String sql = " SELECT 'No_Of_Visit_Done', count(*) FROM `visits` join `Registration` on `visits`.`rno` = `Registration`.`reg_id` where `Registration`.`dealer_code` = '"+dealerCode+"' "
					+ "and `Registration`.`emp_id` = '"+empId+"' "
							+ "and `Registration`.`Type` = '"+type+"' "
									+ "and `visits`.`mid` is not null "
									+ "and `visits`.`visit_date` >= '2020-05-18' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null && !String.valueOf(obj[1]).equalsIgnoreCase("0")){
					visitDone = Integer.parseInt(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return visitDone;
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> getExcludeDealerCodeList(List<String> territoryCodeList) {
		List<Object[]> response = new ArrayList<Object[]>();
		try {
			String sql = " SELECT `DEALER_CODE`,`DEALER_NAME` FROM `Dealer_Master_Exclude_List` ";
			if(territoryCodeList.size() !=0){
				sql += "where `Territory_Code` in (:territoryCodeList) ";
			}
			Query query = getEm().createNativeQuery(sql);
			if(territoryCodeList.size() != 0){
				query.setParameter("territoryCodeList", territoryCodeList);
			}
			response =  query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getPendingOrderByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		Map<String, String> errorMap = new HashMap<String, String>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "SELECT `DEALER_CODE`, `DEALER_NAME`, `MATERIAL_CODE`, `MATERIAL_DESC`, `ORDER_NUMBER`, `ORDER_QTY`, `BILLED_QTY`, `PENDING_QTY`, `PENDING_STATUS` FROM `All_Employee_Pending_Orders` WHERE `Emp_Id` = '"+jsonData.getEmpId()+"' and `Territory_Code` = '"+jsonData.getTerritoryCode()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() !=0){
				List<PendingOrderStatusResponse> pendingOrderStatusList = new ArrayList<PendingOrderStatusResponse>();
				PendingOrderStatusResponse po = null;
				for(Object[] obj : result){
					po = new PendingOrderStatusResponse();
					po.setDealerCode(String.valueOf(obj[0]));
					po.setDealerName(String.valueOf(obj[1]));
					po.setMaterialCode(String.valueOf(obj[2]));
					po.setMaterialDesc(String.valueOf(obj[3]));
					po.setOrderNo(String.valueOf(obj[4]));
					po.setOrderQty(String.valueOf(obj[5]));
					po.setBilledQty(String.valueOf(obj[6]));
					po.setPendingQty(String.valueOf(obj[7]));
					po.setPendingStatus(String.valueOf(obj[8]));
					pendingOrderStatusList.add(po);
				}
				
				
				soapObj.setPendingOrderStatusList(pendingOrderStatusList);
				wrappedList.add(soapObj);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setWrappedList(wrappedList);
				response.setErrorsMap(errorMap);
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getNonVisitedDealerListByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		Map<String, String> errorMap = new HashMap<String, String>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "SELECT `DEALER_CODE`, `DEALER_NAME`, `DEALER_CLASS`, `YTD_SALE`, `LAST_VISIT_DATE` FROM `All_Employee_Non_Visited_Dealer` WHERE `Emp_Id` = '"+jsonData.getEmpId()+"' and `Territory_Code` = '"+jsonData.getTerritoryCode()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() !=0){
				List<NonVisitedDealerResponse> nonVisitedDealerList = new ArrayList<NonVisitedDealerResponse>();
				NonVisitedDealerResponse  res = null;
				for(Object[] obj : result){
					res = new NonVisitedDealerResponse();
					res.setDealerCode(String.valueOf(obj[0]));
					res.setDealerName(String.valueOf(obj[1]));
					res.setDealerClass(String.valueOf(obj[2]));
					res.setYtdSale(String.valueOf(obj[3]));
					res.setLastVisitDate(String.valueOf(obj[4]));
					nonVisitedDealerList.add(res);
				}
				
				
				soapObj.setNonVisitedDealerList(nonVisitedDealerList);
				wrappedList.add(soapObj);
				response.setWrappedList(wrappedList);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setErrorsMap(errorMap);
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getVisitStatusByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		Map<String, String> errorMap = new HashMap<String, String>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "SELECT `Type`, `Classification`, `No_of_Dealer`, `Visit_Per_Dealer`, `Total_Visit`, `No_of_Visit_Done`, `Adharance_Percentage`, `Bal_to_Visit` FROM `All_Employee_Visit_Status` WHERE `Emp_Id` = '"+jsonData.getEmpId()+"' and `Territory_Code` = '"+jsonData.getTerritoryCode()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() !=0){
				List<VisitStatusResponse> visitStatusList = new ArrayList<VisitStatusResponse>();
				VisitStatusResponse res = null;
				for(Object[] obj : result){
					res = new VisitStatusResponse();
					res.setType(String.valueOf(obj[0]));
					res.setClassification(String.valueOf(obj[1]));
					res.setNoOfDealer(Integer.parseInt(String.valueOf(obj[2])));
					res.setVisitPerDealer(Integer.parseInt(String.valueOf(obj[3])));
					res.setTotalVisit(Integer.parseInt(String.valueOf(obj[4])));
					res.setNoOfVisitDone(Integer.parseInt(String.valueOf(obj[5])));
					res.setAdharancePercentage(Integer.parseInt(String.valueOf(obj[6])));
					res.setBalOfVisit(Integer.parseInt(String.valueOf(obj[7])));
					visitStatusList.add(res);
				}
				soapObj.setVisitStatusList(visitStatusList);
				wrappedList.add(soapObj);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setWrappedList(wrappedList);
				response.setErrorsMap(errorMap);
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	

	@SuppressWarnings("unchecked")
	private int getProjectSiteByEmpId(String empId) {
		int projectSite = 0;
		try {
			String sql = " SELECT 'project_sites', count(*) FROM `Registration` where `Registration`.`emp_id` = '"+empId+"' and `Registration`.`Type` = '"+Constant.PROJECT_SITE+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null && !String.valueOf(obj[1]).equalsIgnoreCase("0")){
					projectSite = Integer.parseInt(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectSite;
	}
	
	@SuppressWarnings("unchecked")
	private int getProjectSiteDoneByEmpId(String empId) {
		int projectSiteDone = 0;
		try {
			String sql = " SELECT 'project_Site_Done', count(*) FROM `visits` join `Registration` on `visits`.`rno` = `Registration`.`reg_id` where "
					+ "`Registration`.`emp_id` = '"+empId+"' "
					+ "and `Registration`.`Type` = '"+Constant.PROJECT_SITE+"' "
					+ "and `visits`.`mid` is not null "
					+ "and `visits`.`visit_date` >= '2020-05-18' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null && !String.valueOf(obj[1]).equalsIgnoreCase("0")){
					projectSiteDone = Integer.parseInt(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectSiteDone;
	}
	
	@SuppressWarnings("unchecked")
	private int getNoOfTypeByEmpId(String empId,String type) {
		int projectSite = 0;
		try {
			String sql = " SELECT 'project_sites', count(*) FROM `Registration` where `Registration`.`emp_id` = '"+empId+"' and `Registration`.`Type` = '"+type+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null && !String.valueOf(obj[1]).equalsIgnoreCase("0")){
					projectSite = Integer.parseInt(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectSite;
	}
	
	@SuppressWarnings("unchecked")
	private int getTypeVisitDoneByEmpId(String empId,String type) {
		int projectSiteDone = 0;
		try {
			String sql = " SELECT 'project_Site_Done', count(*) FROM `visits` join `Registration` on `visits`.`rno` = `Registration`.`reg_id` where "
					+ "`Registration`.`emp_id` = '"+empId+"' "
					+ "and `Registration`.`Type` = '"+type+"' "
					+ "and `visits`.`mid` is not null "
					+ "and `visits`.`visit_date` >= '2020-05-18' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> rList = query.getResultList();
			if(rList.size() !=0){
				Object [] obj = rList.get(0);
				if(obj[1] != null && !String.valueOf(obj[1]).equalsIgnoreCase("0")){
					projectSiteDone = Integer.parseInt(String.valueOf(obj[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectSiteDone;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getNetworkExpansionByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		Map<String, String> errorMap = new HashMap<String, String>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "SELECT `DEALER_CODE`, `DEALER_NAME`, `NO_OF_BILLING`, `YTD_BILLING_VALUE` FROM `All_Employee_Network_Expansion` WHERE `Emp_Id` = '"+jsonData.getEmpId()+"' and `Territory_Code` = '"+jsonData.getTerritoryCode()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() !=0){
				List<NetworkExpansionResponse> networkExpansionList = new ArrayList<NetworkExpansionResponse>();
				NetworkExpansionResponse n = null;
				for(Object[] obj : result){
					n = new NetworkExpansionResponse();
					n.setDealerCode(String.valueOf(obj[0]));
					n.setDealerName(String.valueOf(obj[1]));
					n.setNoOfBilling(String.valueOf(obj[2]));
					n.setYtdBillingValue(String.valueOf(obj[3]));
					networkExpansionList.add(n);
				}
				
				soapObj.setNetworkExpansionList(networkExpansionList);
				wrappedList.add(soapObj);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setWrappedList(wrappedList);
				response.setErrorsMap(errorMap);				
			}
			else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getPerformanceByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		Map<String, String> errorMap = new HashMap<String, String>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse soapObj = new SOAPResponse();
		try {
			String sql = "Select `EmpId`, `Product`, `LyBase`, `Tgt`, `TyAct`, `GrPer`, `AchPer` from `All_Employee_Performance` where `EmpId` = '"+jsonData.getEmpId()+"' and `TerritoryCode` = '"+jsonData.getTerritoryCode()+"' and `Period` = '"+jsonData.getPeriod()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() != 0){
				List<PerformanceResponse> performance = new ArrayList<PerformanceResponse>();
				PerformanceResponse per = null;
				for(Object[] obj : result){
					per = new PerformanceResponse();
					per.setEmpId(String.valueOf(obj[0]));
					per.setProduct(String.valueOf(obj[1]));
					per.setLyBase(Float.parseFloat(String.valueOf(obj[2])));
					per.setTgt(Float.parseFloat(String.valueOf(obj[3])));
					per.setTyAct(Float.parseFloat(String.valueOf(obj[4])));
					per.setGrPercentate(Float.parseFloat(String.valueOf(obj[5])));
					per.setAchPercentate(Float.parseFloat(String.valueOf(obj[6])));
					performance.add(per);
				}
				
				soapObj.setPerformance(performance);
				wrappedList.add(soapObj);
				response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				response.setResponseDesc(ReturnsCode.SUCCESSFUL);
				response.setWrappedList(wrappedList);
				response.setErrorsMap(errorMap);
			}else{
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}


	@SuppressWarnings("unchecked")
	private void prepareLyBaseForPerformance(PerformanceResponse per, List<Integer> monthList, List<String> includeDealerCode, List<String> territoryCodeList) {
		try {
			Map<Integer, Integer> correctMonthMap = new HashMap<Integer, Integer>();
			correctMonthMap.put(1, 4);correctMonthMap.put(2, 5);correctMonthMap.put(3, 6);
			correctMonthMap.put(4, 7);correctMonthMap.put(5, 8);correctMonthMap.put(6, 9);
			correctMonthMap.put(7, 10);correctMonthMap.put(8, 11);correctMonthMap.put(9, 12);
			correctMonthMap.put(10, 1);correctMonthMap.put(11, 2);correctMonthMap.put(12, 3);
			
			Integer currentMonth = MyCalendar.getMonthNumberFromCurrentDate();
			Integer currectYear = MyCalendar.getYearFromCurrentDate();
			Integer lastYear = currectYear-1;
			Integer secondLastYear = lastYear-1;
			String queryStr = "";
			int i = 0;
			for(Integer mn : monthList){
				String month = String.format("%02d", correctMonthMap.get(mn));  
				if(mn >= 10){
					queryStr += "invd.`INV_DATE` like '%"+lastYear+"-"+month+"%'";
				}
				else{
					if(currentMonth >= 4){
						queryStr += "invd.`INV_DATE` like '%"+currectYear+"-"+month+"%'";
					}
					else{
						queryStr += "invd.`INV_DATE` like '%"+secondLastYear+"-"+month+"%'";
					}
					
				}
				
				if(i<monthList.size()-1){
					queryStr += " OR ";
				}
				i++;
			}
			
			//System.out.println(queryStr);
			
			String sql = "SELECT 'INV' ,sum(invd.NET_AMOUNT) FROM `Invoice_Details` invd where abs(invd.Employee) = '"+per.getEmpId()+"' and abs(invd.`TERRITORY_CODE`) in (:empTerritoryCode) ";
			sql += "and ("+queryStr+") ";
			sql += "and invd.DIST_CHNL in ('10','21','20','27','11','23','16') ";
			sql += "and invd.`DOCUMENT_TYPE` in  ('ZMIS','ZEXP','ZDTS','ZDTR','ZDRW','ZDRT','ZDPS','ZDEP','ZDE1','ZDCS') ";
			if(per.getProduct().equalsIgnoreCase("Sanitary")){
				sql += "and invd.DIVISION in ('10') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Faucet")){
				sql += "and invd.DIVISION in ('16') ";
			}
			else if(per.getProduct().equalsIgnoreCase("PVC Cistern")){
				sql += "and invd.DIVISION in ('11') ";
				sql += "and invd.MATERIAL_GRP2 in ('202') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Concealo")){
				sql += "and invd.DIVISION in ('11') ";
				sql += "and invd.MATERIAL_GRP2 in ('201') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Achymi")){
				sql += "and invd.DIVISION in ('38','39','40') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Fitting Seat Cover")){
				sql += "and invd.DIVISION in ('12') ";
			}
			sql += "and invd.`DEALER_CODE` in (:includeDealerCode) ";
			//System.out.println(sql);
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("empTerritoryCode", territoryCodeList);
			query.setParameter("includeDealerCode", includeDealerCode);
			List<Object[]> rs = query.getResultList();
			for(Object[] obj : rs){
				String inv = String.valueOf(obj[1]);
				if(inv != null && !inv.equalsIgnoreCase("null")){
					float lyBase = Float.parseFloat(inv);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					lyBase = lyBase/100000; // Converting in LAKH
					float twoDigitsF = Float.valueOf(decimalFormat.format(lyBase));
					per.setLyBase(twoDigitsF);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	private void prepareTyActForPerformance(PerformanceResponse per, List<Integer> monthList,List<String> includeDealerCode, List<String> territoryCodeList) {
		// <incorrectMonthNumber, correctMonthNumber>
		Map<Integer, Integer> correctMonthMap = new HashMap<Integer, Integer>();
		correctMonthMap.put(1, 4);correctMonthMap.put(2, 5);correctMonthMap.put(3, 6);
		correctMonthMap.put(4, 7);correctMonthMap.put(5, 8);correctMonthMap.put(6, 9);
		correctMonthMap.put(7, 10);correctMonthMap.put(8, 11);correctMonthMap.put(9, 12);
		correctMonthMap.put(10, 1);correctMonthMap.put(11, 2);correctMonthMap.put(12, 3);
		
		Integer currectYear = MyCalendar.getYearFromCurrentDate();
		Integer previousYear = currectYear-1;
		String queryStr = "";
		int i = 0;
		for(Integer mn : monthList){
			String month = String.format("%02d", correctMonthMap.get(mn));  
			if(mn >= 10){
				queryStr += "invd.`INV_DATE` like '%"+currectYear+"-"+month+"%'";
			}
			else{
				queryStr += "invd.`INV_DATE` like '%"+previousYear+"-"+month+"%'";
			}
			
			if(i<monthList.size()-1){
				queryStr += " OR ";
			}
			i++;
		}
		//System.out.println(queryStr);
		try {
			String sql = "SELECT 'INV' ,sum(invd.NET_AMOUNT) FROM `Invoice_Details` invd where abs(invd.Employee) = '"+per.getEmpId()+"' and abs(invd.`TERRITORY_CODE`) in (:empTerritoryCode) ";
			sql += "and ("+queryStr+") ";
			sql += "and invd.DIST_CHNL in ('10','21','20','27','11','23','16') ";
			sql += "and invd.`DOCUMENT_TYPE` in  ('ZMIS','ZEXP','ZDTS','ZDTR','ZDRW','ZDRT','ZDPS','ZDEP','ZDE1','ZDCS') ";
			if(per.getProduct().equalsIgnoreCase("Sanitary")){
				sql += "and invd.DIVISION in ('10') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Faucet")){
				sql += "and invd.DIVISION in ('16') ";
			}
			else if(per.getProduct().equalsIgnoreCase("PVC Cistern")){
				sql += "and invd.DIVISION in ('11') ";
				sql += "and invd.MATERIAL_GRP2 in ('202') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Concealo")){
				sql += "and invd.DIVISION in ('11') ";
				sql += "and invd.MATERIAL_GRP2 in ('201') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Achymi")){
				sql += "and invd.DIVISION in ('38','39','40') ";
			}
			else if(per.getProduct().equalsIgnoreCase("Fitting Seat Cover")){
				sql += "and invd.DIVISION in ('12') ";
			}
			sql += "and invd.`DEALER_CODE` in (:includeDealerCode) ";
			//System.out.println(sql);
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("empTerritoryCode", territoryCodeList);
			query.setParameter("includeDealerCode", includeDealerCode);
			List<Object[]> rs = query.getResultList();
			for(Object[] obj : rs){
				String inv = String.valueOf(obj[1]);
				if(inv != null && !inv.equalsIgnoreCase("null")){
					float tyAct = Float.parseFloat(inv);
					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					tyAct = tyAct/100000; // Converting in LAKH
					float twoDigitsF = Float.valueOf(decimalFormat.format(tyAct));
					per.setTyAct(twoDigitsF);
				}
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void preparePerformanceAsYTD(PerformanceResponse per, String target, Integer monthNumber,List<String> includeDealerCode, List<String> territoryCodeList) {
		System.out.println("---- In preparePerformanceAsYTD ----");
		List<Integer> monthList = new ArrayList<Integer>();
		for(int i=monthNumber;i>=1;i--){
			monthList.add(i);
		}
		//System.out.println(monthList);
		try {
			String sql = " SELECT `Month`, `Month_Factor` FROM `Factor` where `Sequence` in (:monthList) ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("monthList", monthList);
			List<Object[]> perConfigList =  query.getResultList();
			float Month_Factor = 0;
			for(Object [] obj : perConfigList){
				Month_Factor += Float.parseFloat(String.valueOf(obj[1]));
			}
			float targetF =  Float.parseFloat(target);
			//System.out.println(targetF+" "+Month_Factor);
			float tgt = targetF * (Month_Factor/100);
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			float twoDigitsF = Float.valueOf(decimalFormat.format(tgt));
			per.setTgt(twoDigitsF);
			
			prepareLyBaseForPerformance(per,monthList,includeDealerCode,territoryCodeList);
			prepareTyActForPerformance(per,monthList,includeDealerCode,territoryCodeList);
			
			float achPer = 0;
			if(per.getTgt() != 0){
				achPer = per.getTyAct() / per.getTgt() ;
			}
			achPer = Float.valueOf(decimalFormat.format(achPer));
			per.setAchPercentate(achPer);
			
			float grPer =  per.getTyAct() / (per.getLyBase() - 1 ) ;
			grPer = Float.valueOf(decimalFormat.format(grPer));
			per.setGrPercentate(grPer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	private void preparePerformanceAsQTD(PerformanceResponse per, String target, Integer monthNumber, List<String> includeDealerCode, List<String> territoryCodeList) {
		System.out.println("---- In preparePerformanceAsQTD ----");
		List<Integer> monthList = new ArrayList<Integer>();
		try {
			if(monthNumber == 10 || monthNumber == 11 || monthNumber == 12){
				monthList.add(10);monthList.add(11);monthList.add(12);
			}
			else if(monthNumber == 7 || monthNumber == 8 || monthNumber == 9){
				monthList.add(7);monthList.add(9);monthList.add(9);
			}
			else if(monthNumber == 4 || monthNumber == 5 || monthNumber == 6){
				monthList.add(4);monthList.add(5);monthList.add(6);
			}
			else if(monthNumber == 1 || monthNumber == 2 || monthNumber == 3){
				monthList.add(1);monthList.add(2);monthList.add(3);
			}
			//System.out.println(monthList);
			
			String sql = " SELECT `Month`, `Month_Factor` FROM `Factor` where `Sequence` in (:monthList) ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("monthList", monthList);
			List<Object[]> perConfigList =  query.getResultList();
			float Month_Factor = 0;
			for(Object [] obj : perConfigList){
				Month_Factor += Float.parseFloat(String.valueOf(obj[1]));
			}
			float targetF =  Float.parseFloat(target);
			//System.out.println(targetF+" "+Month_Factor);
			float tgt = targetF * (Month_Factor/100);
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			float twoDigitsF = Float.valueOf(decimalFormat.format(tgt));
			per.setTgt(twoDigitsF);
			
			prepareLyBaseForPerformance(per,monthList,includeDealerCode,territoryCodeList);
			prepareTyActForPerformance(per,monthList,includeDealerCode,territoryCodeList);
			
			float achPer = 0;
			if(per.getTgt() != 0){
				achPer =  per.getTyAct() / per.getTgt() ;
			}
			achPer = Float.valueOf(decimalFormat.format(achPer));
			per.setAchPercentate(achPer);
			
			float grPer =  per.getTyAct() / ( per.getLyBase() - 1 ) ;
			grPer = Float.valueOf(decimalFormat.format(grPer));
			per.setGrPercentate(grPer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	private void preparePerformanceAsMTD(PerformanceResponse per, String target, Integer monthNumber,List<String> includeDealerCode, List<String> territoryCodeList) {
		System.out.println("---- In preparePerformanceAsMTD ----");
		List<Integer> monthList = new ArrayList<Integer>();
		monthList.add(monthNumber);
		//System.out.println(monthList);
		try {
			String sql = " SELECT `Month`, `Month_Factor` FROM `Factor` where `Sequence` in (:monthList) ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("monthList", monthList);
			List<Object[]> perConfigList =  query.getResultList();
			for(Object [] obj : perConfigList){
				float targetF =  Float.parseFloat(target);
				float Month_Factor = Float.parseFloat(String.valueOf(obj[1]));
				//System.out.println(targetF+" "+Month_Factor);
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				float tgt = targetF * (Month_Factor/100);
				tgt = Float.valueOf(decimalFormat.format(tgt));
				per.setTgt(tgt);
				
				prepareLyBaseForPerformance(per,monthList,includeDealerCode,territoryCodeList);
				prepareTyActForPerformance(per,monthList,includeDealerCode,territoryCodeList);
				
				float achPer = 0;
				if(per.getTgt() != 0){
					achPer =  per.getTyAct() / per.getTgt() ;
				}
				achPer = Float.valueOf(decimalFormat.format(achPer));
				per.setAchPercentate(achPer);
				
				float grPer = per.getTyAct() / (per.getLyBase() - 1 ) ;
				grPer = Float.valueOf(decimalFormat.format(grPer));
				per.setGrPercentate(grPer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveEmpAttendanceIntegrationData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> errorMap = new HashMap<String, String>();
		int totalRecord = 0;
		int successRecord = 0;
		int failureRecord = 0;
		try {
			String currentDate = MyCalendar.getCurrentDate(); // yyyy-MM-dd
			String sql = " SELECT `EMP_ID`,`Name`,`IN_TIME`,`OUT_TIME` FROM `Attendence` where Date = '"+currentDate+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result =  query.getResultList();
			StringBuilder soapRequest = new StringBuilder();
			String soapResponse = null;
			if(result.size() !=0){
				soapRequest.append("<root>");
				for(Object[] obj : result){
					totalRecord++;
					String inTime = String.valueOf(obj[2]).replace(".0", "");
					String outTime = String.valueOf(obj[3]).replace(".0", "");
					boolean inOutMan = false;
					if(!outTime.equalsIgnoreCase(inTime)){
						inOutMan = true;
					}
					soapRequest.append("<item>");
						soapRequest.append("<row>"+totalRecord+"</row>");
						soapRequest.append("<emp_code>"+String.valueOf(obj[0])+"</emp_code>");
						soapRequest.append("<roster_date>"+currentDate+"</roster_date>");
						soapRequest.append("<in_time>"+inTime+"</in_time>");
						soapRequest.append("<out_time>"+outTime+"</out_time>");
						soapRequest.append("<in_out_man>"+inOutMan+"</in_out_man>");
					soapRequest.append("</item>");
				}
				soapRequest.append("</root>");
				//System.out.println(soapRequest.toString());
				soapResponse = SOAP.empAttendanceIntigration(soapRequest.toString(),errorMap);
				if(soapResponse != null){
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(soapResponse));
					Document doc = builder.parse(src);
					NodeList nd = doc.getElementsByTagName("item");
					if(nd.getLength() != 0){
						for (int i = 0; i < nd.getLength(); i++) {
							Element node = (Element) nd.item(i);
							String status = node.getElementsByTagName("status").item(0).getTextContent();
							if(status !=null){
								if(status.equalsIgnoreCase("1")){
									successRecord++;
								}
								else if(status.equalsIgnoreCase("0")){
									failureRecord++;
								}
							}
						}
					}
					
					response.setResponseDesc(ReturnsCode.SUCCESSFUL);
					response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				}
				else{
					failureRecord = totalRecord;
					response.setResponseDesc("Response not geting from Emp Attendence Intigration Api");
					response.setResponseCode(ReturnsCode.GENERIC_ERROR);
				}
				
				response.setErrorsMap(errorMap);
				response.setSoapApiRequest(soapRequest.toString());
				response.setSoapApiResponse(soapResponse);
				
				if(soapResponse == null){
					String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
					//System.out.println(mobileNumber);
					int isSend = SendSMS.sendSms("Error in HSIL_Attendance_Integration : "+response.getErrorsMap().get("ATTENDANCE_SOAP_API_ERROR"), mobileNumber);
					System.out.println("isSendSMS :: "+isSend);
				}
			}
			else{
				response.setSoapApiRequest(soapRequest.toString());
				response.setSoapApiResponse(soapResponse);
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(new SerialClob(response.getSoapApiRequest().toCharArray()));
			/*if(response.getSoapApiResponse() != null){
				res.setResponseJson(new SerialClob(response.getSoapApiResponse().toCharArray()));
			}
			else{
				res.setResponseJson(new SerialClob(response.getResponseDesc().toCharArray()));
			}*/
			response.setSoapApiRequest("");
			res.setResponseJson(new SerialClob(CommonFunction.printResponseJson(response).toCharArray()));
			res.setTotalCount(totalRecord);
			res.setSuccessCount(successRecord);
			res.setFailureCount(failureRecord);
			res.setMethodName("saveEmpAttendanceIntegrationData");
			res.setCreatedDate(new Date());
			save(res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public void saveResponseInTable(Clob requestJson, Clob responseJson, String methodName) {
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(requestJson);
			res.setResponseJson(responseJson);
			res.setMethodName(methodName);
			res.setCreatedDate(new Date());
			save(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> insertOrUpdateEmpByIntergration() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		String soapResponse = null;
		int totalRecord = 0;
		int insertRecord = 0;
		int updateRecord = 0;
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			Map<String, String> successMap = new HashMap<String, String>();
			String currentDate = MyCalendar.getCurrentDateInDdMMyyyy(); // ddMMyyyy
			soapResponse = SOAP.empIntegration(2,currentDate,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("employee");
				if(nd.getLength() != 0){
					EmployeeMasterModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String empId = node.getElementsByTagName("emp_code").item(0).getTextContent();
						
						String sql = " SELECT emp from EmployeeMasterModel emp where emp.empId = :empId ";
						Query query = getEm().createQuery(sql);
						query.setParameter("empId", empId);
						List<EmployeeMasterModel> empResult = query.getResultList();
						if(empResult.size() !=0){
							e = empResult.get(0);
							//e.seteCode(10000); // change if required
							e.setLastUpdateDate(new Date());
							updateRecord++;
						}
						else{
							e = new EmployeeMasterModel();
							e.setEmpId(empId);
							e.seteCode(10000); // change if required
							e.setPassword("tr"+e.getEmpId());
							e.setCreateDate(new Date());
							insertRecord++;
						}
						String doj = node.getElementsByTagName("doj").item(0).getTextContent();
						if(doj != null && !doj.equalsIgnoreCase("")){
							e.setDoj(MyCalendar.convertStrDateToUtilDateForDoj(doj));
						}
						String company = node.getElementsByTagName("company").item(0).getTextContent();
						if(company != null && company.equalsIgnoreCase("HSIL Limited")){
							e.setOrgId(1000);
						}
						else if(company != null && company.equalsIgnoreCase("Somany Home Innovation Limited")){
							e.setOrgId(2000);
						}
						else if(company != null && company.equalsIgnoreCase("Brilloca Limited")){
							e.setOrgId(4000);
						}
						e.setOrganizationName(company);
						e.setOrganizationNameUpdated(company);
						e.setName(node.getElementsByTagName("user_name").item(0).getTextContent());
						e.setMobile(node.getElementsByTagName("mobile_no").item(0).getTextContent());
						e.setRole(node.getElementsByTagName("role").item(0).getTextContent());
						e.setRoleCode(node.getElementsByTagName("role_code").item(0).getTextContent());
						e.setRmId(node.getElementsByTagName("sup_code").item(0).getTextContent());
						e.setEmailId(node.getElementsByTagName("email").item(0).getTextContent());
						e.setOfficialEmailId(e.getEmailId());
						e.setLocationName(node.getElementsByTagName("location").item(0).getTextContent());
						int sI = e.getLocationName().indexOf("(");
						int eI = e.getLocationName().indexOf(")");
						if(sI >= 0 && eI >= 0){
							String territoryCode = e.getLocationName().substring(sI+1,eI);
							territoryCode = territoryCode.replaceAll(",", "");
							territoryCode = territoryCode.replace(" & ", ",");
							e.setTerritoryCode(territoryCode);
						}
						else{
							e.setTerritoryCode(null);
						}
						e.setLocationCode(node.getElementsByTagName("location_code").item(0).getTextContent());
						e.setGradeTitle(node.getElementsByTagName("grade").item(0).getTextContent());
						e.setDepartmentName(node.getElementsByTagName("function").item(0).getTextContent());
						e.setBussinessUnit(node.getElementsByTagName("bu").item(0).getTextContent());
						e.setDesignation(node.getElementsByTagName("design").item(0).getTextContent());
						e.setStatus(node.getElementsByTagName("status").item(0).getTextContent());
						if(e.getStatus().equalsIgnoreCase("Active")){
							e.setActive(1);
						}
						else{
							e.setActive(0);
						}
						/*if(e.getTerritoryCode() != null){
							e.setActive(1); // this is a field user
						}
						else{
							e.setActive(0); // this is not a field user
						}*/
						save(e);
						totalRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERT_OR_UPDATE", "Record : "+String.valueOf(totalRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in HSIL_Employee_Master_Integration : "+response.getErrorsMap().get("EMPLOYEEWISE_SOAP_API_ERROR"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(null);
			res.setResponseJson(new SerialClob(CommonFunction.printResponseJson(response).toCharArray()));
			res.setSoapResponse(soapResponse);
			res.setTotalCount(totalRecord);
			res.setInsertCount(insertRecord);
			res.setUpdateCount(updateRecord);
			res.setMethodName("insertOrUpdateEmpByIntergration");
			res.setCreatedDate(new Date());
			save(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	
	

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveEmpAttendanceIntegrationDataOfDate(String date) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> errorMap = new HashMap<String, String>();
		int totalRecord = 0;
		int successRecord = 0;
		int failureRecord = 0;
		try {
			//date in  yyyy-MM-dd formate
			String sql = " SELECT `EMP_ID`,`Name`,`IN_TIME`,`OUT_TIME` FROM `Attendence` where Date = '"+date+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result =  query.getResultList();
			StringBuilder soapRequest = new StringBuilder();
			String soapResponse = null;
			if(result.size() !=0){
				soapRequest.append("<root>");
				for(Object[] obj : result){
					totalRecord++;
					String inTime = String.valueOf(obj[2]).replace(".0", "");
					String outTime = String.valueOf(obj[3]).replace(".0", "");
					boolean inOutMan = false;
					if(!outTime.equalsIgnoreCase(inTime)){
						inOutMan = true;
					}
					soapRequest.append("<item>");
						soapRequest.append("<row>"+totalRecord+"</row>");
						soapRequest.append("<emp_code>"+String.valueOf(obj[0])+"</emp_code>");
						soapRequest.append("<roster_date>"+date+"</roster_date>");
						soapRequest.append("<in_time>"+inTime+"</in_time>");
						soapRequest.append("<out_time>"+outTime+"</out_time>");
						soapRequest.append("<in_out_man>"+inOutMan+"</in_out_man>");
					soapRequest.append("</item>");
				}
				soapRequest.append("</root>");
				//System.out.println(soapRequest.toString());
				soapResponse = SOAP.empAttendanceIntigration(soapRequest.toString(),errorMap);
				if(soapResponse != null){
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					InputSource src = new InputSource();
					src.setCharacterStream(new StringReader(soapResponse));
					Document doc = builder.parse(src);
					NodeList nd = doc.getElementsByTagName("item");
					if(nd.getLength() != 0){
						for (int i = 0; i < nd.getLength(); i++) {
							Element node = (Element) nd.item(i);
							String status = node.getElementsByTagName("status").item(0).getTextContent();
							if(status !=null){
								if(status.equalsIgnoreCase("1")){
									successRecord++;
								}
								else if(status.equalsIgnoreCase("0")){
									failureRecord++;
								}
							}
						}
					}
					
					response.setResponseDesc(ReturnsCode.SUCCESSFUL);
					response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
				}
				else{
					failureRecord = totalRecord;
					response.setResponseDesc("Response not geting from Emp Attendence Intigration Api");
					response.setResponseCode(ReturnsCode.GENERIC_ERROR);
				}
				response.setErrorsMap(errorMap);
				response.setSoapApiRequest(soapRequest.toString());
				response.setSoapApiResponse(soapResponse);
				
				if(soapResponse == null){
					String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
					//System.out.println(mobileNumber);
					int isSend = SendSMS.sendSms("Error in HSIL_Attendance_Integration : "+response.getErrorsMap().get("ATTENDANCE_SOAP_API_ERROR"), mobileNumber);
					System.out.println("isSendSMS :: "+isSend);
				}
			}
			else{
				response.setSoapApiRequest(soapRequest.toString());
				response.setSoapApiResponse(soapResponse);
				response.setResponseCode(ReturnsCode.NO_RECORDS_FOUND_CODE);
				response.setResponseDesc(ReturnsCode.NO_RECORD_FOUND);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(new SerialClob(response.getSoapApiRequest().toCharArray()));
			/*if(response.getSoapApiResponse() != null){
				res.setResponseJson(new SerialClob(response.getSoapApiResponse().toCharArray()));
			}
			else{
				res.setResponseJson(new SerialClob(response.getResponseDesc().toCharArray()));
			}*/
			response.setSoapApiRequest("");
			res.setResponseJson(new SerialClob(CommonFunction.printResponseJson(response).toCharArray()));
			res.setTotalCount(totalRecord);
			res.setSuccessCount(successRecord);
			res.setFailureCount(failureRecord);
			res.setMethodName("saveEmpAttendanceIntegrationDataOfDate : date "+date);
			res.setCreatedDate(new Date());
			save(res);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> insertOrUpdateEmpByIntergrationOfDate(String date) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		String soapResponse = null;
		int totalRecord = 0;
		int insertRecord = 0;
		int updateRecord = 0;
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			Map<String, String> successMap = new HashMap<String, String>();
			// date in ddMMyyyy format
			soapResponse = SOAP.empIntegration(2,date,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("employee");
				if(nd.getLength() != 0){
					EmployeeMasterModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String empId = node.getElementsByTagName("emp_code").item(0).getTextContent();
						
						String sql = " SELECT emp from EmployeeMasterModel emp where emp.empId = :empId ";
						Query query = getEm().createQuery(sql);
						query.setParameter("empId", empId);
						List<EmployeeMasterModel> empResult = query.getResultList();
						if(empResult.size() !=0){
							e = empResult.get(0);
							//e.seteCode(10000); // change if required
							e.setLastUpdateDate(new Date());
							updateRecord++;
						}
						else{
							e = new EmployeeMasterModel();
							e.setEmpId(empId);
							e.seteCode(10000); // change if required
							e.setPassword("tr"+e.getEmpId());
							e.setCreateDate(new Date());
							insertRecord++;
						}
						String doj = node.getElementsByTagName("doj").item(0).getTextContent();
						if(doj != null && !doj.equalsIgnoreCase("")){
							e.setDoj(MyCalendar.convertStrDateToUtilDateForDoj(doj));
						}
						String company = node.getElementsByTagName("company").item(0).getTextContent();
						if(company != null && company.equalsIgnoreCase("HSIL Limited")){
							e.setOrgId(1000);
						}
						else if(company != null && company.equalsIgnoreCase("Somany Home Innovation Limited")){
							e.setOrgId(2000);
						}
						else if(company != null && company.equalsIgnoreCase("Brilloca Limited")){
							e.setOrgId(4000);
						}
						e.setOrganizationName(company);
						e.setOrganizationNameUpdated(company);
						e.setName(node.getElementsByTagName("user_name").item(0).getTextContent());
						e.setMobile(node.getElementsByTagName("mobile_no").item(0).getTextContent());
						e.setRole(node.getElementsByTagName("role").item(0).getTextContent());
						e.setRoleCode(node.getElementsByTagName("role_code").item(0).getTextContent());
						e.setRmId(node.getElementsByTagName("sup_code").item(0).getTextContent());
						e.setEmailId(node.getElementsByTagName("email").item(0).getTextContent());
						e.setOfficialEmailId(e.getEmailId());
						e.setLocationName(node.getElementsByTagName("location").item(0).getTextContent());
						int sI = e.getLocationName().indexOf("(");
						int eI = e.getLocationName().indexOf(")");
						if(sI >= 0 && eI >= 0){
							String territoryCode = e.getLocationName().substring(sI+1,eI);
							territoryCode = territoryCode.replaceAll(",", "");
							territoryCode = territoryCode.replace(" & ", ",");
							e.setTerritoryCode(territoryCode);
						}
						else{
							e.setTerritoryCode(null);
						}
						e.setLocationCode(node.getElementsByTagName("location_code").item(0).getTextContent());
						e.setGradeTitle(node.getElementsByTagName("grade").item(0).getTextContent());
						e.setDepartmentName(node.getElementsByTagName("function").item(0).getTextContent());
						e.setBussinessUnit(node.getElementsByTagName("bu").item(0).getTextContent());
						e.setDesignation(node.getElementsByTagName("design").item(0).getTextContent());
						e.setStatus(node.getElementsByTagName("status").item(0).getTextContent());
						if(e.getStatus().equalsIgnoreCase("Active")){
							e.setActive(1);
						}
						else{
							e.setActive(0);
						}
						/*if(e.getTerritoryCode() != null){
							e.setActive(1); // this is a field user
						}
						else{
							e.setActive(0); // this is not a field user
						}*/
						save(e);
						totalRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERT_OR_UPDATE", "Record : "+String.valueOf(totalRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(null);
			res.setResponseJson(new SerialClob(CommonFunction.printResponseJson(response).toCharArray()));
			res.setSoapResponse(soapResponse);
			res.setTotalCount(totalRecord);
			res.setInsertCount(insertRecord);
			res.setUpdateCount(updateRecord);
			res.setMethodName("insertOrUpdateEmpByIntergrationOfDate : date "+date);
			res.setCreatedDate(new Date());
			save(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public Response<Map<String, String>> insertDealerInRegistrationByEmpId(String type,String employees) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			List<String> empList = CommonFunction.getListOfUniqueStringTokens(employees, ",");
			for(String empId : empList){
				int insertRecord = 0;
				if(type.equalsIgnoreCase("Dealers")){
					String configSql = "SELECT `Config_Value` FROM `Outstanding_Report_Config` where `Config_Type` = 'distributionChannelExcludeList' ";
					Query configQuery = getEm().createNativeQuery(configSql);
					List<String> distributionChannelExcludeList =  configQuery.getResultList();
					
					if(distributionChannelExcludeList.size() !=0){
						String commaSepareteDistributionChannel = distributionChannelExcludeList.get(0);
						distributionChannelExcludeList = CommonFunction.getListOfUniqueStringTokens(commaSepareteDistributionChannel, ",");
					}
					
					List<String> territoryCodeList = new ArrayList<String>();
					List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					List<String> excludeDealerCodeList = new ArrayList<String>();
					for(Object[] obj : excludeDealerNameCodeList){
						String dmCode = String.valueOf(obj[0]);
						excludeDealerCodeList.add(dmCode);
					}
					
					StringBuilder empRoleType = new StringBuilder();
					String dealerInRegistraion = "";
					territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
					
					if(territoryCodeList.size() !=0){
						for(String terriCode : territoryCodeList){
							//System.out.println(empRoleType+" :: "+terriCode);
							String sql = "SELECT DISTINCT `DEALER_CODE` FROM `Dealer_Master_25Aug` where abs(`TERRITORY`) = '"+terriCode+"' and `DEALER_TYPE_DESC` = 'Dealers'  "
									+ "and `DEALER_CODE` not in (:excludeDealerCodeList) "
									+ "and `CHANNEL_CODE` not in (:distributionChannelExcludeList) "
									+ "and `IS_ACTIVE` = 'X' ";
							Query query = getEm().createNativeQuery(sql);
							query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
							query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
							List<String> dealerResult = query.getResultList();
							
							if(dealerResult.size() !=0){
								for(String dealerCode : dealerResult){
									sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
											+ "and `dealer_code` = '"+dealerCode+"' and `territory_code` = '"+terriCode+"' "
											+ "and `Type` = 'Dealer' and `Is_Active` = 'Y' ";
									query = getEm().createNativeQuery(sql);
									List<String> dealerInRegResult = query.getResultList();
									if(dealerInRegResult.size() == 0){
										sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
												+ "SELECT '"+empId+"',dm.`dealer_code`,'"+terriCode+"','', dm.`DEALER_NAME`,0,0,'Dealer',dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`,'','','','','','','','Y',CURRENT_TIMESTAMP FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
												+ "and abs(dm.`TERRITORY`) = '"+terriCode+"' and dm.`dealer_code` = '"+dealerCode+"' and dm.`IS_ACTIVE` = 'X' "
												+ "GROUP by dm.`dealer_code`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO` ";
										
										int count = getEm().createNativeQuery(sql).executeUpdate();
										insertRecord += count;
									}
									else{
										dealerInRegistraion += dealerCode+", ";
										updateDealerInfoInRegistrationTable(empId,terriCode,dealerCode,successMap,"Dealers");
									}
								}
							}
							else{
								successMap.put("DEALER_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
										"Dealer not exist by terri_code :: "+terriCode+" in Dealer_Master");
								updateDealerInfoInRegistrationTable(empId,terriCode,null,successMap,"Dealers");
							}
							
							if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
								sql = "SELECT DISTINCT `DEALER_CODE` FROM `Dealer_Master_Exclude_List` where `Territory_Code` = '"+terriCode+"' ";
								query = getEm().createNativeQuery(sql);
								List<String> includeDealerCodeList = query.getResultList();
								
								if(includeDealerCodeList.size() != 0){
									for(String dealerCode : includeDealerCodeList){
										sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
												+ "and `dealer_code` = '"+dealerCode+"' and `territory_code` = '"+terriCode+"' "
												+ "and `Type` = 'Dealer' and `Is_Active` = 'Y' " ;
										query = getEm().createNativeQuery(sql);
										List<String> dealerInRegResult = query.getResultList();
										if(dealerInRegResult.size() == 0){
											sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
													+ "SELECT '"+empId+"',dm.`DEALER_CODE`,'"+terriCode+"','', dm.`DEALER_NAME`,0,0,'Dealer',dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`,'','','','','','','','Y',CURRENT_TIMESTAMP FROM `Dealer_Master_Exclude_List` dm where "
													+ "abs(dm.`Territory_Code`) = '"+terriCode+"' and dm.`DEALER_CODE` = '"+dealerCode+"' "
													+ "GROUP by dm.`DEALER_CODE`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO` ";
											
											int count = getEm().createNativeQuery(sql).executeUpdate();
											insertRecord += count;
										}
										else{
											dealerInRegistraion += dealerCode+", ";
											updateDealerInfoInRegistrationTable(empId,terriCode,dealerCode,successMap,"Dealers");
										}
									}
								}
								else{
									successMap.put("EXCLUDE_DEALER_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
											"Dealer not exist by terri_code :: "+terriCode+" in Dealer_Master_Exclude_List");
									updateDealerInfoInRegistrationTable(empId,terriCode,null,successMap,"Dealers");
								}
								
							}
							
						}
						successMap.put("ALREADY_DEALER_IN_REGISTRATION - "+empId, dealerInRegistraion);
						successMap.put("DEALER_IN_REGISTRATION - "+empId, "Record : "+insertRecord);
					}
					else{
						successMap.put("NO_TERRITORY_FOUND",empId);
					}
					
				}
				else if(type.equalsIgnoreCase("Sub Dealers")){
					List<String> territoryCodeList = new ArrayList<String>();
					/*List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					List<String> excludeDealerCodeList = new ArrayList<String>();
					for(Object[] obj : excludeDealerNameCodeList){
						String dmCode = String.valueOf(obj[0]);
						excludeDealerCodeList.add(dmCode);
					}*/
					
					StringBuilder empRoleType = new StringBuilder();
					String subDealerInRegistraion = "";
					territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
					if(territoryCodeList.size() !=0 ){
						for(String terriCode : territoryCodeList){
							String sql = "SELECT DISTINCT `SUB_DEALER_CODE` FROM `Sub_Dealer_Classification` where `TERRITORY_CODE` = '"+terriCode+"'  ";
							Query query = getEm().createNativeQuery(sql);
							List<String> subRealerResult = query.getResultList();
							if(subRealerResult.size() !=0){
								for(String subDealerCode : subRealerResult){
									sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
											+ "and `dealer_code` = '"+subDealerCode+"' and `territory_code` = '"+terriCode+"' "
											+ "and `Type` = 'Sub Dealer' and `Is_Active` = 'Y' ";
									query = getEm().createNativeQuery(sql);
									List<String> subDealerInRegResult = query.getResultList();
									if(subDealerInRegResult.size() == 0){
										sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
												+ "SELECT '"+empId+"',dm.`SUB_DEALER_CODE`,'"+terriCode+"','', dm.`SUB_DEALER_NAME`,0,0,'Sub Dealer','','','','','','','','','','','Y',CURRENT_TIMESTAMP FROM `Sub_Dealer_Classification` dm "
												+ "where abs(dm.`TERRITORY_CODE`) = '"+terriCode+"' "
												+ "GROUP by dm.`SUB_DEALER_CODE`,dm.`SUB_DEALER_NAME` ";
										
										int count = getEm().createNativeQuery(sql).executeUpdate();
										insertRecord += count;
									}
									else{
										subDealerInRegistraion += subDealerCode+", ";
										updateSubdealerInRegistration(empId,terriCode,subDealerCode,successMap);
									}
								}
							}
							else{
								successMap.put("SUB_DEALER_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
										"Sub_Dealer not exist by terri_code :: "+terriCode+" in Sub_Dealer_Classification");
								updateSubdealerInRegistration(empId,terriCode,null,successMap);
							}
						}
						
						successMap.put("ALREADY_SUB_DEALER_IN_REGISTRATION - "+empId, subDealerInRegistraion);
						successMap.put("SUB_DEALER_IN_REGISTRATION - "+empId, "Record : "+insertRecord);
					}
					else{
						successMap.put("NO_TERRITORY_FOUND",empId);
					}
					
				}
				else if(type.equalsIgnoreCase("Distributor")){
					StringBuilder empRoleType = new StringBuilder();
					String distributorInRegistraion = "";
					List<String> territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
					if(territoryCodeList.size() !=0){
						for(String terriCode : territoryCodeList){
							String sql = "SELECT DISTINCT `DEALER_CODE` FROM `Dealer_Master_25Aug` where abs(`TERRITORY`) = '"+terriCode+"' "
									+ "and `DEALER_TYPE_DESC` = 'Distributor'  "
									+ "and `IS_ACTIVE` = 'X' ";
							Query query = getEm().createNativeQuery(sql);
							List<String> dealerResult = query.getResultList();
							if(dealerResult.size() !=0){
								for(String dealerCode : dealerResult){
									sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
											+ "and `dealer_code` = '"+dealerCode+"' and `territory_code` = '"+terriCode+"' "
											+ "and `Type` = 'Distributor' and `Is_Active` = 'Y' ";
									query = getEm().createNativeQuery(sql);
									List<String> distributorInRegResult = query.getResultList();
									if(distributorInRegResult.size() == 0){
										sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
												+ "SELECT '"+empId+"',dm.`dealer_code`,'"+terriCode+"','', dm.`DEALER_NAME`,0,0,'Distributor',dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`,'','','','','','','','Y',CURRENT_TIMESTAMP FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Distributor' "
												+ "and abs(dm.`TERRITORY`) = '"+terriCode+"' and dm.`dealer_code` = '"+dealerCode+"' and dm.`IS_ACTIVE` = 'X' "
												+ "GROUP by dm.`dealer_code`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO` ";
										
										int count = getEm().createNativeQuery(sql).executeUpdate();
										insertRecord += count;
									}
									else{
										distributorInRegistraion += dealerCode+", ";
										updateDealerInfoInRegistrationTable(empId,terriCode,dealerCode,successMap,"Distributor");
									}
								}
							}
							else{
								successMap.put("DISTRIBUTOR_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
										"Distributor not exist by terri_code :: "+terriCode+" in Dealer_Master");
								updateDealerInfoInRegistrationTable(empId,terriCode,null,successMap,"Distributor");
							}
						}
						successMap.put("ALREADY_DISTRIBUTOR_IN_REGISTRATION - "+empId, distributorInRegistraion);
						successMap.put("DISTRIBUTOR_IN_REGISTRATION - "+empId, "Record : "+insertRecord);
					}else{
						successMap.put("NO_TERRITORY_FOUND",empId);
					}
					
					
					/*String sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
							+ "SELECT '"+empId+"',dm.`dealer_code`,dm.`TERRITORY`,'', dm.`DEALER_NAME`,0,0,'Distributor',dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`,'','','','','','','','Y',CURRENT_TIMESTAMP FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Distributor' "
							+ "and abs(dm.`TERRITORY`) in "
							+ "(SELECT emp.`territory_code`  FROM `Employee-Master` emp WHERE emp.`territory_code` is not null and emp.`Emp-id` = '"+empId+"') "
							+ "GROUP by dm.`dealer_code`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`";
					
					int count = getEm().createNativeQuery(sql).executeUpdate();
					insertRecord += count;
					successMap.put("DISTRIBUTOR_IN_REGISTRATION - "+empId, "Record : "+insertRecord);*/
				}
				
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	private void updateSubdealerInRegistration(String empId, String terriCode, String subDealerCode,
			Map<String, String> successMap) {
		try {
			String sql = "Select `SUB_DEALER_CODE`,`SUB_DEALER_NAME` from `Sub_Dealer_Classification` "
					+ "where `TERRITORY_CODE` = '"+terriCode+"' "
							+ "and `SUB_DEALER_CODE` = '"+subDealerCode+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			if(result.size() != 0){
				for(Object [] obj : result){
					//String sdc = obj[0] == null ? "" : String.valueOf(obj[0]);
					String sdn = obj[1] == null ? "" : String.valueOf(obj[1]);
					
					sql = "update `Registration` set `F1` = '"+sdn+"' "
							+ "where `emp_id` = '"+empId+"' "
							+ "and `dealer_code` = '"+subDealerCode+"' "
							+ "and `territory_code` = '"+terriCode+"' "
							+ "and `Type` = 'Sub Dealer' and `Is_Active` = 'Y' ";
					int count = getEm().createNativeQuery(sql).executeUpdate();
					successMap.put("UPDATE_SUBDEALER_IN_REGISTRATION", count+"");
				}
			}
			else{
				sql = "update `Registration` set `Is_Active` = 'N' "
						+ "where `emp_id` = '"+empId+"' "
						+ "and `territory_code` = '"+terriCode+"' "
						+ "and `Type` = 'Sub Dealer'  ";
				int count = getEm().createNativeQuery(sql).executeUpdate();
				successMap.put("DEACTIVATE_SUBDEALER_IN_REGISTRATION", count+"");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in_updateSubdealerInRegistration", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void updateDealerInfoInRegistrationTable(String empId, String terriCode, String dealerCode,
			Map<String, String> successMap,String type) {
		// type = Dealers, Distributor
		String regType = type;
		if(type.equalsIgnoreCase("Dealers")){
			regType = "Dealer";
		}
		
		try {
			if(dealerCode == null){
				dealerCode = "NA";
			}
			String sql = "SELECT dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO` FROM `Dealer_Master_25Aug` dm "
					+ "where dm.`DEALER_TYPE_DESC` = '"+type+"' AND dm.`DEALER_CODE` = :dealerCode "
					+ "and abs(dm.`TERRITORY`) = :terriCode  "
					+ "and dm.`IS_ACTIVE` = 'X' "
					+ "GROUP by dm.`DEALER_CODE`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("dealerCode", dealerCode);
			query.setParameter("terriCode", terriCode);
			List<Object[]> result = query.getResultList();
			if(result.size() != 0){
				for(Object [] obj : result){
					String dm = obj[0] == null ? "" : String.valueOf(obj[0]).replace("'", "");
					String cp = obj[1] == null ? "" : String.valueOf(obj[1]).replace("'", "");
					String peId = obj[2] == null ? "" : String.valueOf(obj[2]).replace("'", "");
					String m = obj[3] == null ? "" : String.valueOf(obj[3]).replace("'", "");
					
					sql = "update `Registration` set `F1` = '"+dm+"', `F2` = '"+cp+"', `F3` = '"+peId+"', `F4` = '"+m+"', `Is_Active` = 'Y'  "
							+ "where `emp_id` = '"+empId+"' and `dealer_code` = '"+dealerCode+"' and `territory_code` = '"+terriCode+"' and `Type` = '"+regType+"' ";
					int count = getEm().createNativeQuery(sql).executeUpdate();
					successMap.put("UPDATE_"+type+"_INFO_IN_REGISTRATION_TABLE", count+" record(s) update in Registration by emp Id : "+empId+", dealer code : "+dealerCode);
				}
			}
			else{
				
				sql = "update `Registration` set `Is_Active` = 'N' "
						+ "where `emp_id` = '"+empId+"' and `territory_code` = '"+terriCode+"' and `Type` = '"+regType+"'  ";
				int count = getEm().createNativeQuery(sql).executeUpdate();
				successMap.put("UPDATE_"+type+"_INFO_IN_REGISTRATION_TABLE", count+" record(s) deactivated in Registration by emp Id : "+empId+", dealer code : "+dealerCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in updateDealerInfoInRegistrationTable ", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	private List<String> prepareTerriCodeByEmpId(String empId,StringBuilder empRoleType) {
		List<String> territoryCodeList = new ArrayList<String>();
		empRoleType.append("");
		try {
			boolean isEmpTM = false;
			String sql = "SELECT DISTINCT `territory_code` FROM `Employee-Master` where `Emp-id` = '"+empId+"' "
					+ "and `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> tmResult = query.getResultList();
			if(tmResult.size() !=0){
				isEmpTM = true;
				territoryCodeList = CommonFunction.getListOfUniqueStringTokens(tmResult.get(0), ",");
				empRoleType.append(Constant.EMPLOYEE_TM);
			}
			
			boolean isEmpSH = false;
			if(isEmpTM == false){
				sql = "SELECT DISTINCT `territory_code` FROM `Territory_SH_RM_ZM_Mapping` where `SH_Code` = '"+empId+"' ";
				query = getEm().createNativeQuery(sql);
				List<String> shTmResult = query.getResultList();
				if(shTmResult.size() !=0){
					isEmpSH = true;
					territoryCodeList = shTmResult;
					empRoleType.append(Constant.EMPLOYEE_SH);
				}
			}
			
			boolean isEmpRM = false;
			if(isEmpTM == false && isEmpSH == false){
				sql = "SELECT DISTINCT `territory_code` FROM `Territory_SH_RM_ZM_Mapping` where `RM_Code` = '"+empId+"' ";
				query = getEm().createNativeQuery(sql);
				List<String> rmTmResult = query.getResultList();
				if(rmTmResult.size() != 0){
					isEmpRM = true;
					territoryCodeList = rmTmResult;
					empRoleType.append(Constant.EMPLOYEE_RM);
				}
			}
			
			if(isEmpTM == false && isEmpSH == false && isEmpRM == false){
				sql = "SELECT DISTINCT `territory_code` FROM `Territory_SH_RM_ZM_Mapping` where `ZM_Code` = '"+empId+"' ";
				query = getEm().createNativeQuery(sql);
				List<String> zmTmResult = query.getResultList();
				if(zmTmResult.size() != 0){
					territoryCodeList = zmTmResult;
					empRoleType.append(Constant.EMPLOYEE_ZM);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return territoryCodeList;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	private String getDealerFromToDate(){
		String fromToDate = "";
		try {
			String sql = "SELECT `From_Date`,`To_Date` FROM `Dealer_Start_End` ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			for(Object[] obj : result){
				fromToDate += String.valueOf(obj[0]);
				fromToDate += ":";
				fromToDate += String.valueOf(obj[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fromToDate;
	}
	
	@Transactional
	public Response<Map<String, String>> getAllDealersOfPeriod() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String fromToDate = getDealerFromToDate();
			//System.out.println("fromToDate => "+fromToDate);
			String fromDate = fromToDate.split(":")[0];
			String toDate = fromToDate.split(":")[1];
			long noOfDay = MyCalendar.getNoOfDaysBetweenTwoStrDates(fromDate, toDate);
			//System.out.println(noOfDay);
			String fDate = fromDate;
			String tDate = "";
			for(int i=0;i<=noOfDay;i++){
				if(i!=0){
					fDate = MyCalendar.getNextDateAsGivenDate(fDate);
				}
				tDate = fDate;
				//System.out.println("day :: "+i+" ::  "+fDate+" :: "+tDate);
				if(!getAllDealersAsPeriod(fDate,tDate,response,successMap)){
					String status = "Something wrong in "+fDate+" date, please re-hit url.. ";
					String sql = "UPDATE `Dealer_Start_End` SET `From_Date`= '"+fDate+"', `Status` = '"+status+"' ";
					int c = getEm().createNativeQuery(sql).executeUpdate();
					System.out.println("update "+c);
					break;
				}
				else if(i == noOfDay){
					String status = "All record successfully inserted from "+fromDate+" to "+toDate+" ";
					String sql = "UPDATE `Dealer_Start_End` SET `From_Date`= '"+fDate+"', `Status` = '"+status+"' ";
					int c = getEm().createNativeQuery(sql).executeUpdate();
					System.out.println("update "+c);
				}
			}
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean getAllDealersAsPeriod(String fromDate,String toDate,Response<Map<String, String>> response,Map<String, String> successMap) {
		boolean returnValue = false;
		try {
			
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_DEALERS> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"         <TB_DEALERS> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <DEALER_NAME></DEALER_NAME> "+
					"               <DEALER_TYPE></DEALER_TYPE> "+
					"               <DEALER_TYPE_DESC></DEALER_TYPE_DESC> "+
					"               <SHOW_ROOM></SHOW_ROOM> "+
					"               <CONT_PERSON></CONT_PERSON> "+
					"               <CONT_ADR1></CONT_ADR1> "+
					"               <CONT_ADR1_PIN></CONT_ADR1_PIN> "+
					"               <PRIM_EMAIL_ID></PRIM_EMAIL_ID> "+
					"               <PRIM_MOBILE_NO></PRIM_MOBILE_NO> "+
					"               <MOBILE_NO></MOBILE_NO> "+
					"               <STREET></STREET> "+
					"               <SALES_OFFICE></SALES_OFFICE> "+
					"               <REGION></REGION> "+
					"               <TERRITORY></TERRITORY> "+
					"               <TER_NAME></TER_NAME> "+
					"               <CITY_CODE></CITY_CODE> "+
					"               <TIN_NO></TIN_NO> "+
					"               <PAN_NO></PAN_NO> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <CHANNEL_CODE></CHANNEL_CODE> "+
					"               <IS_ACTIVE></IS_ACTIVE> "+
					"               <GSTIN></GSTIN> "+
					"               <PRICE_GROUP></PRICE_GROUP> "+
					"				<CUST_STATUS></CUST_STATUS> "+
					"				<CUST_STATUS_DESC></CUST_STATUS_DESC> "+
					"				<SALES_ORG></SALES_ORG> "+
					"				<SALES_ORG_DESC></SALES_ORG_DESC> "+
					"				<ACCOUNT_GROUP></ACCOUNT_GROUP> "+
					"				<ACCOUNT_GROUP_DESC></ACCOUNT_GROUP_DESC> "+
					"            </item> "+
					"         </TB_DEALERS> "+
					"      </urn:Z_SF_GET_ALL_DEALERS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					/*List<BasicDealerInfo> updatedDealerList = new ArrayList<BasicDealerInfo>();
					BasicDealerInfo info = null;*/
					
					NetworkExpansionReportModel model = null;
					List<String> distinctDealerCode = new ArrayList<String>();
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dealerCode = node.getElementsByTagName("DEALER_CODE").item(0).getTextContent();
						String dealerName = node.getElementsByTagName("DEALER_NAME").item(0).getTextContent();
						/*String dealerType = node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent();
						String contPerson = node.getElementsByTagName("CONT_PERSON").item(0).getTextContent();
						String primEmailId = node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent();
						String primMobileNo = node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent();*/
						String terr_code = node.getElementsByTagName("TERRITORY").item(0).getTextContent();
						String terr_name = node.getElementsByTagName("TER_NAME").item(0).getTextContent();
						String cust_status = node.getElementsByTagName("CUST_STATUS").item(0).getTextContent();
						int isActive = cust_status.equalsIgnoreCase("") || cust_status.equalsIgnoreCase("01") ? 1 : 0;
						if(!distinctDealerCode.contains(dealerCode)){
							distinctDealerCode.add(dealerCode);
							
							String sql = "select `DEALER_CODE` from `Network_Expansion_Report` where `DEALER_CODE` = '"+dealerCode+"' ";
							Query query = getEm().createNativeQuery(sql);
							List<String> neResult = query.getResultList();
							if(neResult.size() == 0){
								model = new NetworkExpansionReportModel();
								model.setDealerCode(dealerCode);
								model.setDealerName(dealerName);
								model.setTerritory(terr_code);
								model.setTerName(terr_name);
								model.setIsActive(isActive);
								model.setCreateDate(new Date());
								save(model);
							}
							
							/*if(dealerType.equalsIgnoreCase("Dealers") || dealerType.equalsIgnoreCase("Distributor")){
								info = new BasicDealerInfo();
								info.setDealerCode(dealerCode);
								info.setDealerName(dealerName);
								info.setDealerType(dealerType);
								info.setContPerson(contPerson);
								info.setPrimEmailId(primEmailId);
								info.setPrimMobileNo(primMobileNo);
								info.setTerriCode(terr_code);
								info.setTerriName(terr_name);
								updatedDealerList.add(info);
							}*/
							
						}
					}
					
					/*if(updatedDealerList.size() != 0){
						updateDealerAndDistributorInfoInRegistrationTable(updatedDealerList,successMap);
					}*/
					
					int deleteCount = 0;
					for(String dealerCode : distinctDealerCode){
						String sql = "delete from Dealer_Master_25Aug where DEALER_CODE = '"+dealerCode+"' ";
						deleteCount += getEm().createNativeQuery(sql).executeUpdate();
						//System.out.println("delete count :: "+deleteCount);
					}
					
					
					DealerMasterModel e = null;
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new DealerMasterModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setDealerName(node.getElementsByTagName("DEALER_NAME").item(0).getTextContent());
						e.setDealerType(node.getElementsByTagName("DEALER_TYPE").item(0).getTextContent());
						e.setDealerTypeDesc(node.getElementsByTagName("DEALER_TYPE_DESC").item(0).getTextContent());
						e.setShowRoom(node.getElementsByTagName("SHOW_ROOM").item(0).getTextContent());
						e.setContPerson(node.getElementsByTagName("CONT_PERSON").item(0).getTextContent());
						e.setContAdr1(node.getElementsByTagName("CONT_ADR1").item(0).getTextContent());
						e.setContAdr1Pin(node.getElementsByTagName("CONT_ADR1_PIN").item(0).getTextContent());
						e.setPrimEmailId(node.getElementsByTagName("PRIM_EMAIL_ID").item(0).getTextContent());
						e.setPrimMobileNumber(node.getElementsByTagName("PRIM_MOBILE_NO").item(0).getTextContent());
						e.setMobileNo(node.getElementsByTagName("MOBILE_NO").item(0).getTextContent());
						e.setStreet(node.getElementsByTagName("STREET").item(0).getTextContent());
						e.setSalesOffice(node.getElementsByTagName("SALES_OFFICE").item(0).getTextContent());
						e.setRegion(node.getElementsByTagName("REGION").item(0).getTextContent());
						e.setTerritory(node.getElementsByTagName("TERRITORY").item(0).getTextContent());
						e.setTerName(node.getElementsByTagName("TER_NAME").item(0).getTextContent());
						e.setCityCode(node.getElementsByTagName("CITY_CODE").item(0).getTextContent());
						e.setTinNo(node.getElementsByTagName("TIN_NO").item(0).getTextContent());
						e.setPanNo(node.getElementsByTagName("PAN_NO").item(0).getTextContent());
						e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
						e.setChannelCode(node.getElementsByTagName("CHANNEL_CODE").item(0).getTextContent());
						//e.setIsActive(node.getElementsByTagName("IS_ACTIVE").item(0).getTextContent());
						e.setGstin(node.getElementsByTagName("GSTIN").item(0).getTextContent());
						e.setPriceGroup(node.getElementsByTagName("PRICE_GROUP").item(0).getTextContent());
						e.setCustStatus(node.getElementsByTagName("CUST_STATUS").item(0).getTextContent());
						if(e.getCustStatus().equalsIgnoreCase("") || e.getCustStatus().equalsIgnoreCase("01")){
							e.setIsActive("X"); // active
						}
						else{
							e.setIsActive("XX"); // inactive
						}
						e.setCustStatusDesc(node.getElementsByTagName("CUST_STATUS_DESC").item(0).getTextContent());
						e.setSalesOrg(node.getElementsByTagName("SALES_ORG").item(0).getTextContent());
						e.setSalesOrgDesc(node.getElementsByTagName("SALES_ORG_DESC").item(0).getTextContent());
						e.setAccountGroup(node.getElementsByTagName("ACCOUNT_GROUP").item(0).getTextContent());
						e.setAccountGroupDesc(node.getElementsByTagName("ACCOUNT_GROUP_DESC").item(0).getTextContent());
						e.setDealerInsertDate(fromDate);
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED - "+fromDate, "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
					returnValue = true;
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_DEALER - "+fromDate, ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
					returnValue = true;
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_DEALER - "+fromDate, ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
				returnValue = false;
			}
			response.setErrorsMap(successMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}

	@Transactional
	public Response<Map<String, String>> getAllOutstandings() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = "";
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+ 
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
					"         <IM_DEALER_CODE></IM_DEALER_CODE> "+
					"         <KEY_DATE>"+MyCalendar.getCurrentDate()+"</KEY_DATE> "+
					"         <TB_ALL_OUTSTANDING> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <DIVISION></DIVISION> "+
					"               <OUTSTA_AMOUNT></OUTSTA_AMOUNT> "+
					"               <NOT_DUE></NOT_DUE> "+
					"               <Z0_30></Z0_30> "+
					"               <Z31_60></Z31_60> "+
					"               <Z61_90></Z61_90> "+
					"               <Z91_180></Z91_180> "+
					"               <Z181_365></Z181_365> "+
					"               <Z366_730></Z366_730> "+
					"               <Z731_1095></Z731_1095> "+
					"               <Z1096_ABOVE></Z1096_ABOVE> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <DOC_TYPE></DOC_TYPE> "+
					"               <DOC_NUM></DOC_NUM> "+
					"               <DOC_DATE></DOC_DATE> "+
					"            </item> "+
					"         </TB_ALL_OUTSTANDING> "+
					"      </urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
					
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					List<String> dealerMap = new ArrayList<String>();
					int deleteCount = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dealerCode = node.getElementsByTagName("DEALER_CODE").item(0).getTextContent();
						if(!dealerMap.contains(dealerCode)){
							dealerMap.add(dealerCode);
							String sql = "delete from Outstanding_Master_New where DEALER_CODE = '"+dealerCode+"' ";
							deleteCount += getEm().createNativeQuery(sql).executeUpdate();
							//System.out.println("delete count :: "+deleteCount);
						}
					}
					OutstandingMasterNewModel e = null;
					int successRecord = 0;
					
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new OutstandingMasterNewModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setOutstaAmount(node.getElementsByTagName("OUTSTA_AMOUNT").item(0).getTextContent());
						e.setNotDue(node.getElementsByTagName("NOT_DUE").item(0).getTextContent());
						e.setZ0_30(node.getElementsByTagName("Z0_30").item(0).getTextContent());
						e.setZ31_60(node.getElementsByTagName("Z31_60").item(0).getTextContent());
						e.setZ61_90(node.getElementsByTagName("Z61_90").item(0).getTextContent());
						e.setZ91_180(node.getElementsByTagName("Z91_180").item(0).getTextContent());
						e.setZ181_365(node.getElementsByTagName("Z181_365").item(0).getTextContent());
						e.setZ366_730(node.getElementsByTagName("Z366_730").item(0).getTextContent());
						e.setZ731_1095(node.getElementsByTagName("Z731_1095").item(0).getTextContent());
						e.setZ1096_Above(node.getElementsByTagName("Z1096_ABOVE").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setDocType(node.getElementsByTagName("DOC_TYPE").item(0).getTextContent());
						e.setDocNum(node.getElementsByTagName("DOC_NUM").item(0).getTextContent());
						e.setDocDate(node.getElementsByTagName("DOC_DATE").item(0).getTextContent());
						double z366_Above = Double.parseDouble(e.getZ366_730()) +
								Double.parseDouble(e.getZ731_1095()) +
								Double.parseDouble(e.getZ1096_Above());
						e.setZ366_Above(String.valueOf(z366_Above));
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_OUTSTANDINGS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_OUTSTANDINGS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in All_Outstanding : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getAllOutstandingsOfDate(String keyDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		try {
			Map<String, String> successMap = new HashMap<String, String>();
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = "";
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+ 
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
					"         <IM_DEALER_CODE></IM_DEALER_CODE> "+
					"         <KEY_DATE>"+keyDate+"</KEY_DATE> "+
					"         <TB_ALL_OUTSTANDING> "+
					"            <item> "+
					"               <DEALER_CODE></DEALER_CODE> "+
					"               <DIVISION></DIVISION> "+
					"               <OUTSTA_AMOUNT></OUTSTA_AMOUNT> "+
					"               <NOT_DUE></NOT_DUE> "+
					"               <Z0_30></Z0_30> "+
					"               <Z31_60></Z31_60> "+
					"               <Z61_90></Z61_90> "+
					"               <Z91_180></Z91_180> "+
					"               <Z181_365></Z181_365> "+
					"               <Z366_730></Z366_730> "+
					"               <Z731_1095></Z731_1095> "+
					"               <Z1096_ABOVE></Z1096_ABOVE> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"               <PROFIT_CENTER></PROFIT_CENTER> "+
					"               <DOC_TYPE></DOC_TYPE> "+
					"               <DOC_NUM></DOC_NUM> "+
					"               <DOC_DATE></DOC_DATE> "+
					"            </item> "+
					"         </TB_ALL_OUTSTANDING> "+
					"      </urn:Z_SF_GET_ALL_OUTSTANDINGS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
					
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					List<String> dealerMap = new ArrayList<String>();
					int deleteCount = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dealerCode = node.getElementsByTagName("DEALER_CODE").item(0).getTextContent();
						if(!dealerMap.contains(dealerCode)){
							dealerMap.add(dealerCode);
							String sql = "delete from Outstanding_Master_New where DEALER_CODE = '"+dealerCode+"' ";
							deleteCount += getEm().createNativeQuery(sql).executeUpdate();
							//System.out.println("delete count :: "+deleteCount);
						}
					}
					OutstandingMasterNewModel e = null;
					int successRecord = 0;
					
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new OutstandingMasterNewModel();
						e.setDealerCode(node.getElementsByTagName("DEALER_CODE").item(0).getTextContent());
						e.setDivision(node.getElementsByTagName("DIVISION").item(0).getTextContent());
						e.setOutstaAmount(node.getElementsByTagName("OUTSTA_AMOUNT").item(0).getTextContent());
						e.setNotDue(node.getElementsByTagName("NOT_DUE").item(0).getTextContent());
						e.setZ0_30(node.getElementsByTagName("Z0_30").item(0).getTextContent());
						e.setZ31_60(node.getElementsByTagName("Z31_60").item(0).getTextContent());
						e.setZ61_90(node.getElementsByTagName("Z61_90").item(0).getTextContent());
						e.setZ91_180(node.getElementsByTagName("Z91_180").item(0).getTextContent());
						e.setZ181_365(node.getElementsByTagName("Z181_365").item(0).getTextContent());
						e.setZ366_730(node.getElementsByTagName("Z366_730").item(0).getTextContent());
						e.setZ731_1095(node.getElementsByTagName("Z731_1095").item(0).getTextContent());
						e.setZ1096_Above(node.getElementsByTagName("Z1096_ABOVE").item(0).getTextContent());
						e.setCreditControlArea(node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent());
						e.setProfitCenter(node.getElementsByTagName("PROFIT_CENTER").item(0).getTextContent());
						e.setDocType(node.getElementsByTagName("DOC_TYPE").item(0).getTextContent());
						e.setDocNum(node.getElementsByTagName("DOC_NUM").item(0).getTextContent());
						e.setDocDate(node.getElementsByTagName("DOC_DATE").item(0).getTextContent());
						double z366_Above = Double.parseDouble(e.getZ366_730()) +
								Double.parseDouble(e.getZ731_1095()) +
								Double.parseDouble(e.getZ1096_Above());
						e.setZ366_Above(String.valueOf(z366_Above));
						e.setCreateDate(new Date());
						save(e);
						successRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_OUTSTANDINGS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_OUTSTANDINGS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public Response<SOAPResponse> getTerritoryCodeByEmpId(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse wrappedObj = new SOAPResponse();
		try {
			StringBuilder empRoleType = new StringBuilder();
			List<String> territoryCodeList = prepareTerriCodeByEmpId(jsonData.getEmpId(),empRoleType);
			List<SystemParamDTO> terricodeList = new ArrayList<SystemParamDTO>();
			SystemParamDTO paramObj = null;
			for(String terriCode : territoryCodeList){
				String sql = "SELECT DISTINCT `TER_NAME` FROM `Dealer_Master_25Aug` WHERE abs(`TERRITORY`) = '"+terriCode+"' ";
				Query query = getEm().createNativeQuery(sql);
				List<String> result = query.getResultList();
				String terrName = "";
				if(result.size() == 1){
					terrName = result.get(0);
				}
				else{
					terrName = terriCode;
				}
				
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(terriCode);
				paramObj.setParamDesc(terrName);
				terricodeList.add(paramObj);
			}
			
			/*List<SystemParamDTO> organizationList = new ArrayList<SystemParamDTO>();
			String sql = "SELECT `OrgID`,`OrgnizationName_updated`,`BussinessUnit` FROM `Employee-Master` WHERE `Emp-id` = '"+jsonData.getEmpId()+"' "
					+ "and `territory_code` is not null "
					+ "and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			
			String businessUnit = "";
			for(Object [] obj : result){
				String orgId = obj[0] == null ? "" : String.valueOf(obj[0]);
				String orgName = obj[1] == null ? "" : String.valueOf(obj[1]);
				businessUnit = obj[2] == null ? "" : String.valueOf(obj[2]);
				
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(orgId);
				paramObj.setParamDesc(orgName);
				organizationList.add(paramObj);
			}*/
			
			/*			
			List<SystemParamDTO> distributionChannelList = new ArrayList<SystemParamDTO>();
			sql = "SELECT DISTINCT `Document_Type`.`DISTRIBUTION_CHANNEL`,`Document_Type`.`DISTRIBUTION_CHANNEL_DESC` FROM `Document_Type` WHERE "
					+ "`Document_Type`.`IS_ACTIVE` = 'Y' and "
					+ "`Document_Type`.`SALE_ORG` = (select Distinct `Employee-Master`.`OrgID` from `Employee-Master` "
					+ "where `Employee-Master`.`Emp-id` = '"+jsonData.getEmpId()+"' "
					+ "and `Employee-Master`.`OrgID` is not null "
					+ "and `Employee-Master`.`territory_code` is not null "
					+ "and `Employee-Master`.`Status` = 'Active')";
			Query query1 = getEm().createNativeQuery(sql);
			List<Object[]> result1 = query1.getResultList();
			for(Object [] obj : result1){
				String dc = obj[0] == null ? "" : String.valueOf(obj[0]);
				String dcd = obj[1] == null ? "" : String.valueOf(obj[1]);
				if(!distributionChannelExcludeList.contains(dc)){
					paramObj = new SystemParamDTO();
					paramObj.setParamCode(dc);
					paramObj.setParamDesc(dcd);
					distributionChannelList.add(paramObj);
				}
				
			}
			
			/*wrappedObj.setOrganizationList(organizationList);
			wrappedObj.setDistributionChannelList(distributionChannelList);
			wrappedObj.setDivisionNameList(divisionNameList);*/
			
			/*String fileName = "OrderBooking.json";
			String str = CommonFunction.readJsonFile(fileName);
			JSONObject jsonObject = new JSONObject(str);
			String isSyncButtonShow = jsonObject.getString("isSyncButtonShow");
			wrappedObj.setIsSyncButtonShow(isSyncButtonShow);*/
			
			wrappedObj.setTerricodeList(terricodeList);
			wrappedObj.setIsSyncButtonShow("No");
			
			wrappedList.add(wrappedObj);
			response.setWrappedList(wrappedList);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getPaymentTerms() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_GET_PAYMENT_TERMS> "+
					"         <TB_PAYMENT_TERMS> "+
					"            <item> "+
					"               <PAYMENT_TERM></PAYMENT_TERM> "+
					"               <DESCRIPTION></DESCRIPTION> "+
					"            </item> "+
					"         </TB_PAYMENT_TERMS> "+
					"      </urn:Z_SF_GET_PAYMENT_TERMS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest, successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					String delSql = "delete from `Payment_Terms` ";
					int deleteCount = getEm().createNativeQuery(delSql).executeUpdate();
					
					int successRecord = 0;
					String insertTable = "insert into `Payment_Terms` (`PAYMENT_TERM`,`DESCRIPTION`,`CREATE_DATE`) ";
					String insertValue = "";
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String p = node.getElementsByTagName("PAYMENT_TERM").item(0).getTextContent();
						String d = node.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
						
						insertValue += "('"+p+"','"+d+"',current_timestamp),";
						//successRecord++;
					}
					if(!insertValue.equalsIgnoreCase("")){
						insertValue = insertValue.substring(0, insertValue.length()-1);
						int insertCount = getEm().createNativeQuery(insertTable+" VALUES "+insertValue).executeUpdate();
						successRecord = insertCount;
						//System.out.println("insertCount :: "+insertCount);
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert Count : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("GET_PAYMENT_TERMS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("GET_PAYMENT_TERMS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Get_Payment_Terms : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getAllProducts() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String currentDate = MyCalendar.getCurrentDate();
			
			prepareAllProducts(currentDate, "", successMap);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	public Response<Map<String, String>> getAllStates() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:ZBAPI_SF_GET_ALL_STATES> "+
					"         <TB_STATES> "+
					"            <item> "+
					"               <COUNTRY></COUNTRY> "+
					"               <STATE_CODE></STATE_CODE> "+
					"               <STATE_NAME></STATE_NAME> "+
					"            </item> "+
					"         </TB_STATES> "+
					"      </urn:ZBAPI_SF_GET_ALL_STATES> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest, successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					
					String delSql = "delete from `All_States` ";
					int deleteCount = getEm().createNativeQuery(delSql).executeUpdate();
					
					String insertTable = "insert into `All_States` (`COUNTRY`,`STATE_CODE`,`STATE_NAME`,`CREATE_DATE`) ";
					String insertValue = "";
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String c = node.getElementsByTagName("COUNTRY").item(0).getTextContent();
						String sc = node.getElementsByTagName("STATE_CODE").item(0).getTextContent();
						String sn = node.getElementsByTagName("STATE_NAME").item(0).getTextContent();
						
						insertValue += "('"+c+"','"+sc+"','"+sn+"',current_timestamp),";
						
						//successRecord++;
					}
					if(!insertValue.equalsIgnoreCase("")){
						insertValue = insertValue.substring(0, insertValue.length()-1);
						int insertCount = getEm().createNativeQuery(insertTable+" VALUES "+insertValue).executeUpdate();
						successRecord = insertCount;
						//System.out.println("insertCount :: "+insertCount);
					}
					
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert count : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("GET_ALL_STATES", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("GET_ALL_STATES", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Get_All_States : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getAllDepots() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:ZBAPI_SF_GET_ALL_DEPOTS> "+
					"         <TB_DEPOTS_DATA> "+
					"            <item> "+
					"               <DEPOT_CODE></DEPOT_CODE> "+
					"               <DEPOT_NAME></DEPOT_NAME> "+
					"               <STATE></STATE> "+
					"               <SALES_ORG></SALES_ORG> "+
					"               <SALE_ORG_DESC></SALE_ORG_DESC> "+
					"               <STATE_DESC></STATE_DESC> "+
					"            </item> "+
					"         </TB_DEPOTS_DATA> "+
					"      </urn:ZBAPI_SF_GET_ALL_DEPOTS> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					String delSql = "delete from `All_Depots` ";
					int deleteCount = getEm().createNativeQuery(delSql).executeUpdate();
					
					String insertTable = "insert into `All_Depots` (`DEPOT_CODE`,`DEPOT_NAME`,`STATE`,`SALES_ORG`,`SALE_ORG_DESC`,`STATE_DESC`,`CREATE_DATE`) ";
					String insertValue = "";
					int successRecord = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String dc = node.getElementsByTagName("DEPOT_CODE").item(0).getTextContent();
						String dn = node.getElementsByTagName("DEPOT_NAME").item(0).getTextContent();
						String s = node.getElementsByTagName("STATE").item(0).getTextContent();
						String so = node.getElementsByTagName("SALES_ORG").item(0).getTextContent();
						String sod = node.getElementsByTagName("SALE_ORG_DESC").item(0).getTextContent();
						String sd = node.getElementsByTagName("STATE_DESC").item(0).getTextContent();
						
						insertValue += "('"+dc+"','"+dn+"','"+s+"','"+so+"','"+sod+"','"+sd+"',current_timestamp),";
						
						//successRecord++;
					}
					
					if(!insertValue.equalsIgnoreCase("")){
						insertValue = insertValue.substring(0, insertValue.length()-1);
						int insertCount = getEm().createNativeQuery(insertTable+" VALUES "+insertValue).executeUpdate();
						successRecord = insertCount;
						//System.out.println("insertCount :: "+insertCount);
					}
					
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED", "Insert count : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("GET_ALL_DEPOTS", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("GET_ALL_DEPOTS", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in Get_All_Depots : "+response.getErrorsMap().get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	public Response<Map<String, String>> getDocumentType() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			List<String> orgIdList = new ArrayList<String>();
			orgIdList.add("4000");orgIdList.add("2000");orgIdList.add("1000");
			
			for(String saleOrg : orgIdList){
				prapareDocumentType(saleOrg,successMap);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	private void prapareDocumentType(String saleOrg, Map<String, String> successMap) {
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:ZBAPI_SF_GET_DOCUMENT_TYPES> "+
					"         <IM_SALE_ORG>"+saleOrg+"</IM_SALE_ORG> "+
					"         <TB_DOCUMENT_TYPES> "+
					"            <item> "+
					"               <DOCUMENT_TYPE></DOCUMENT_TYPE> "+
					"               <DESCRIPTION></DESCRIPTION> "+
					"               <SALE_ORG></SALE_ORG> "+
					"               <SALE_ORG_DESC></SALE_ORG_DESC> "+
					"               <DISTRIBUTION_CHANNEL></DISTRIBUTION_CHANNEL> "+
					"               <DISTRIBUTION_CHANNEL_DESC></DISTRIBUTION_CHANNEL_DESC> "+
					"               <DIVISION></DIVISION> "+
					"               <DIVISION_DESC></DIVISION_DESC> "+
					"               <CREDIT_CONTROL_AREA></CREDIT_CONTROL_AREA> "+
					"            </item> "+
					"         </TB_DOCUMENT_TYPES> "+
					"      </urn:ZBAPI_SF_GET_DOCUMENT_TYPES> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					
					String delSql = "delete from `Document_Type` where `SALE_ORG` = '"+saleOrg+"' ";
					int deleteCount = getEm().createNativeQuery(delSql).executeUpdate();
					
					int successRecord = 0;
					/*String insertTable = "insert into `Document_Type` (`DOCUMENT_TYPE`, `DESCRIPTION`, `SALE_ORG`, "
							+ " `SALE_ORG_DESC`, `DISTRIBUTION_CHANNEL`, `DISTRIBUTION_CHANNEL_DESC`, `DIVISION`, "
							+ " `DIVISION_DESC`, `CREDIT_CONTROL_AREA`, `CREATE_DATE`) ";
					String insertValue = "";*/
					DocumentTypeModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						e = new DocumentTypeModel();
						
						String docType = node.getElementsByTagName("DOCUMENT_TYPE").item(0).getTextContent();
						String desc = node.getElementsByTagName("DESCRIPTION").item(0).getTextContent();
						String salesOrg = node.getElementsByTagName("SALE_ORG").item(0).getTextContent();
						String salesOrgDesc = node.getElementsByTagName("SALE_ORG_DESC").item(0).getTextContent();
						String distChannel = node.getElementsByTagName("DISTRIBUTION_CHANNEL").item(0).getTextContent();
						String distChannelDesc = node.getElementsByTagName("DISTRIBUTION_CHANNEL_DESC").item(0).getTextContent();
						String div = node.getElementsByTagName("DIVISION").item(0).getTextContent();
						String divDesc = node.getElementsByTagName("DIVISION_DESC").item(0).getTextContent();
						String creContArea = node.getElementsByTagName("CREDIT_CONTROL_AREA").item(0).getTextContent();
						
						//insertValue += "('"+docType+"','"+desc+"','"+salesOrg+"','"+salesOrgDesc+"','"+distChannel+"','"+distChannelDesc+"','"+div+"','"+divDesc+"','"+creContArea+"',current_timestamp), ";
						
						e.setDocumentType(docType);
						e.setDescription(desc);
						e.setSalesOrg(salesOrg);
						e.setSalesOrgDesc(salesOrgDesc);
						e.setDistributionChannel(distChannel);
						e.setDistributionChannelDesc(distChannelDesc);
						e.setDivision(div);
						e.setDivisionDesc(divDesc);
						e.setCreditControlArea(creContArea);
						e.setIsActive("Y");
						e.setCreateDate(new Date());
						save(e);
						
						successRecord++;
					}
					
					/*if(!insertValue.equalsIgnoreCase("")){
						insertValue = insertValue.substring(0, insertValue.length()-1);
						int insertCount = getEm().createNativeQuery(insertTable+" VALUES "+insertValue).executeUpdate();
						successRecord = insertCount;
						//System.out.println("insertCount :: "+insertCount);
					}*/
					
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED - "+saleOrg, "Insert count : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("GET_DOCUMENT_TYPE - "+saleOrg, ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("GET_DOCUMENT_TYPE  - "+saleOrg, ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Transactional
	public Response<Map<String, String>> getAllProductsOfPeriod() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String fromToDate = getDealerFromToDate();
			//System.out.println("fromToDate => "+fromToDate);
			String fromDate = fromToDate.split(":")[0];
			String toDate = fromToDate.split(":")[1];
			long noOfDay = MyCalendar.getNoOfDaysBetweenTwoStrDates(fromDate, toDate);
			//System.out.println(noOfDay);
			String fDate = fromDate;
			String tDate = "";
			for(int i=0;i<=noOfDay;i++){
				if(i!=0){
					fDate = MyCalendar.getNextDateAsGivenDate(fDate);
				}
				tDate = fDate;
				//System.out.println("day :: "+i+" ::  "+fDate+" :: "+tDate);
				if(!getAllProductsAsPeriod(fDate,tDate,response,successMap)){
					String status = "Something wrong in "+fDate+" date, please re-hit url.. ";
					String sql = "UPDATE `Dealer_Start_End` SET `From_Date`= '"+fDate+"', `Status` = '"+status+"' ";
					int c = getEm().createNativeQuery(sql).executeUpdate();
					System.out.println("update "+c);
					break;
				}
				else if(i == noOfDay){
					String status = "All record successfully inserted from "+fromDate+" to "+toDate+" ";
					String sql = "UPDATE `Dealer_Start_End` SET `From_Date`= '"+fDate+"', `Status` = '"+status+"' ";
					int c = getEm().createNativeQuery(sql).executeUpdate();
					System.out.println("update "+c);
				}
			}
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	private boolean getAllProductsAsPeriod(String fDate, String tDate, Response<Map<String, String>> response, Map<String, String> successMap) {
		boolean returnValue = false;
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_MATERIAL_MASTER> "+
					"         <IM_FROM_DATE>"+fDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+tDate+"</IM_TO_DATE> "+
					"		  <IM_VKORG> "+
					"            <item> "+
					"               <VKORG>4000</VKORG> "+
					"            </item> "+
					/*"            <item> "+
					"               <VKORG>2000</VKORG> "+
					"            </item> "+*/
					"         </IM_VKORG> "+
					"         <TB_PRODUCTS> "+
					"            <item> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PRODUCT_NAME></PRODUCT_NAME> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <CATELOG_NUMBER></CATELOG_NUMBER> "+
					"				<SALES_ORG></SALES_ORG> "+
					"               <DISTRIBUTION_CHANEL></DISTRIBUTION_CHANEL> "+
					"               <PLANT></PLANT> "+
					"               <SALEABLE_ITEM></SALEABLE_ITEM> "+
					"               <BASE_UOM></BASE_UOM> "+
					"               <PROCUREMENT_TYPE></PROCUREMENT_TYPE> "+
					"               <MAT_STS_DIV></MAT_STS_DIV> "+
					"               <MATERIAL_TYPE></MATERIAL_TYPE> "+
					"            </item> "+
					"         </TB_PRODUCTS> "+
					"      </urn:Z_SF_MATERIAL_MASTER> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					List<String> distinctProductCode = new ArrayList<String>();
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String pc = node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent();
						if(!pc.equalsIgnoreCase("") && !distinctProductCode.contains(pc)){
							String delSql = "delete from `All_Products_25Aug` where `PRODUCT_CODE` = '"+pc+"' ";
							int delCount = getEm().createNativeQuery(delSql).executeUpdate();
							System.out.println("Del count : "+delCount+" : "+fDate);
							distinctProductCode.add(pc);
						}
					}
					
					int successRecord = 0;
					AllProductModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String pc = node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent();
						if(!pc.equalsIgnoreCase("")){
							e = new AllProductModel();
							e.setProductCode(pc);
							e.setProductName(node.getElementsByTagName("PRODUCT_NAME").item(0).getTextContent());
							e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
							e.setCatelogNumber(node.getElementsByTagName("CATELOG_NUMBER").item(0).getTextContent());
							if(e.getCatelogNumber() == null || e.getCatelogNumber().equalsIgnoreCase("")){
								e.setCatelogNumber("#N/A");
							}
							e.setSalesOrg(node.getElementsByTagName("SALES_ORG").item(0).getTextContent());
							e.setDistributionChanel(node.getElementsByTagName("DISTRIBUTION_CHANEL").item(0).getTextContent());
							e.setPlant(node.getElementsByTagName("PLANT").item(0).getTextContent());
							e.setSaleableItem(node.getElementsByTagName("SALEABLE_ITEM").item(0).getTextContent());
							e.setProductNameSaleableItem(e.getProductName()+"_"+e.getSaleableItem());
							e.setPcPnCn(e.getProductNameSaleableItem()+" ~ "+e.getCatelogNumber()+" ~ "+e.getProductCode());
							e.setBaseUom(node.getElementsByTagName("BASE_UOM").item(0).getTextContent());
							e.setProcurementType(node.getElementsByTagName("PROCUREMENT_TYPE").item(0).getTextContent());
							e.setMatStsDiv(node.getElementsByTagName("MAT_STS_DIV").item(0).getTextContent());
							e.setMaterialType(node.getElementsByTagName("MATERIAL_TYPE").item(0).getTextContent());
							e.setIsActive(1);
							e.setProductInsertDate(fDate);
							e.setCreateDate(new Date());
							save(e);
							successRecord++;
						}
					}
					
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED : "+fDate, "Record : "+String.valueOf(successRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
					returnValue = true;
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_PRODUCTS : "+fDate, ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
					returnValue = true;
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_PRODUCTS : "+fDate, ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
				returnValue = false;
			}
			response.setErrorsMap(successMap);
		} catch (Exception e) {
			e.printStackTrace();
			returnValue = false;
		}
		return returnValue;
	}

	@Transactional
	public Response<Map<String, String>> insertOrUpdateTmDealerInRegistration() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			updateAlumniInRegistrationTable(successMap);
			
			insertTMDealersInRegistraionTable(successMap);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	private void updateAlumniInRegistrationTable(Map<String, String> successMap) {
		try {
			String sql = "update `Registration` set `Is_Active` = 'N' where `Registration`.`emp_id` in "
					+ "(SELECT DISTINCT `Emp-id` FROM `Employee-Master` where `Status` = 'Alumni') ";
			int count = getEm().createNativeQuery(sql).executeUpdate();
			successMap.put("ALUMNI_IN_ACTIVE_IN_REGISTRATION", "count : "+count);
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in updateAlumniInRegistrationTable", e.toString());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	private void insertTMDealersInRegistraionTable(Map<String, String> successMap) {
		try {
			String configSql = "SELECT `Config_Value` FROM `Outstanding_Report_Config` where `Config_Type` = 'distributionChannelExcludeList' ";
			Query configQuery = getEm().createNativeQuery(configSql);
			List<String> distributionChannelExcludeList =  configQuery.getResultList();
			
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			
			if(distributionChannelExcludeList.size() !=0){
				String commaSepareteDistributionChannel = distributionChannelExcludeList.get(0);
				distributionChannelExcludeList = CommonFunction.getListOfUniqueStringTokens(commaSepareteDistributionChannel, ",");
			}
			
			String sql = "SELECT DISTINCT `Employee-Master`.`Emp-id` FROM `Employee-Master` where "
					+ "`Employee-Master`.`territory_code` is not null and `Employee-Master`.`Status` != 'Alumni' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> result = query.getResultList();
			
			if(result.size() !=0 ){
				for(String empId : result){
					insertTMDealers(empId,distributionChannelExcludeList,excludeDealerCodeList,successMap,"Dealers");
					insertTMDealers(empId,distributionChannelExcludeList,excludeDealerCodeList,successMap,"Distributor");
					//insertTMSubdealers(empId,successMap);
				}
			}
			else{
				successMap.put("NO_EMP_FOUND", "No any employee found for dealer insert in registration table");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in insertTMDealersInRegistraionTable", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void insertTMSubdealers(String empId, Map<String, String> successMap) {
		try {
			int insertRecord = 0;
			List<String> territoryCodeList = new ArrayList<String>();
			StringBuilder empRoleType = new StringBuilder();
			String subDealerInRegistraion = "";
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(territoryCodeList.size() !=0 ){
				for(String terriCode : territoryCodeList){
					String sql = "SELECT DISTINCT `SUB_DEALER_CODE` FROM `Sub_Dealer_Classification` where `TERRITORY_CODE` = '"+terriCode+"'  ";
					Query query = getEm().createNativeQuery(sql);
					List<String> subRealerResult = query.getResultList();
					if(subRealerResult.size() !=0){
						for(String subDealerCode : subRealerResult){
							sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
									+ "and `dealer_code` = '"+subDealerCode+"' and `territory_code` = '"+terriCode+"' "
									+ "and `Type` = 'Sub Dealer' and `Is_Active` = 'Y' ";
							query = getEm().createNativeQuery(sql);
							List<String> subDealerInRegResult = query.getResultList();
							if(subDealerInRegResult.size() == 0){
								sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
										+ "SELECT '"+empId+"',dm.`SUB_DEALER_CODE`,'"+terriCode+"','', dm.`SUB_DEALER_NAME`,0,0,'Sub Dealer','','','','','','','','','','','Y',CURRENT_TIMESTAMP FROM `Sub_Dealer_Classification` dm "
										+ "where abs(dm.`TERRITORY_CODE`) = '"+terriCode+"' "
										+ "GROUP by dm.`SUB_DEALER_CODE`,dm.`SUB_DEALER_NAME` ";
								
								int count = getEm().createNativeQuery(sql).executeUpdate();
								insertRecord += count;
							}
							else{
								subDealerInRegistraion += subDealerCode+", ";
								updateSubdealerInRegistration(empId,terriCode,subDealerCode,successMap);
							}
						}
					}
					else{
						successMap.put("SUB_DEALER_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
								"Sub_Dealer not exist by terri_code :: "+terriCode+" in Sub_Dealer_Classification");
						updateSubdealerInRegistration(empId,terriCode,null,successMap);
					}
				}
				
				successMap.put("ALREADY_SUB_DEALER_IN_REGISTRATION - "+empId, subDealerInRegistraion);
				successMap.put("SUB_DEALER_IN_REGISTRATION - "+empId, "Record : "+insertRecord);
			}
			else{
				successMap.put("NO_TERRITORY_FOUND",empId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_In_insertTMSubdealers", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	@Transactional
	private void insertTMDealers(String empId,List<String> distributionChannelExcludeList, List<String> excludeDealerCodeList,
			Map<String, String> successMap,String type) {
		// type = Dealers, Distributor
		String regType = type;
		if(type.equalsIgnoreCase("Dealers")){
			regType = "Dealer";
		}
		try {
			int insertRecord = 0;
			
			StringBuilder empRoleType = new StringBuilder();
			String dealerInRegistraion = "";
			List<String> territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			
			updateUnknownTerriCodeInRegistration(empId,territoryCodeList,successMap,type);
			
			for(String terriCode : territoryCodeList){
				String sql = "SELECT DISTINCT `DEALER_CODE` FROM `Dealer_Master_25Aug` where abs(`TERRITORY`) = '"+terriCode+"' and `DEALER_TYPE_DESC` = '"+type+"'  "
						+ "and `DEALER_CODE` not in (:excludeDealerCodeList) "
						+ "and `CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and `IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				List<String> dealerResult = query.getResultList();
				
				if(dealerResult.size() !=0){
					for(String dealerCode : dealerResult){
						sql = "SELECT DISTINCT `dealer_code` FROM `Registration` where `emp_id` = '"+empId+"' "
								+ "and `dealer_code` = '"+dealerCode+"' and `territory_code` = '"+terriCode+"' "
								+ "and `Type` = '"+regType+"' and `Is_Active` = 'Y' ";
						query = getEm().createNativeQuery(sql);
						List<String> dealerInRegResult = query.getResultList();
						if(dealerInRegResult.size() == 0){
							sql = "insert into Registration (`emp_id`,`dealer_code`,`territory_code`,`imei`,`F1`,`latitude`,`longitude`,`Type`,`F2`,`F3`,`F4`,`F5`,`F6`,`F7`,`F8`,`F9`,`F10`,`F11`,`Is_Active`,`registered_on`) "
									+ "SELECT '"+empId+"',dm.`dealer_code`,'"+terriCode+"','', dm.`DEALER_NAME`,0,0,'"+regType+"',dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO`,'','','','','','','','Y',CURRENT_TIMESTAMP FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = '"+type+"' "
									+ "and abs(dm.`TERRITORY`) = '"+terriCode+"' and dm.`dealer_code` = '"+dealerCode+"' and dm.`IS_ACTIVE` = 'X' "
									+ "GROUP by dm.`dealer_code`, dm.`DEALER_NAME`,dm.`CONT_PERSON`,dm.`PRIM_EMAIL_ID`,dm.`MOBILE_NO` ";
							
							int count = getEm().createNativeQuery(sql).executeUpdate();
							insertRecord += count;
						}
						else{
							dealerInRegistraion += dealerCode+", ";
							successMap.put("ALREADY_"+type+"_IN_REGISTRATION - "+empId, dealerInRegistraion);
							
							updateDealerInfoInRegistrationTable(empId,terriCode,dealerCode,successMap,type);
						}
					}
				}
				else{
					successMap.put(type+"_IN_REGISTRATION :: empId - "+empId+", terri_code - "+terriCode, 
							type+" not exist by terri_code :: "+terriCode+" in Dealer_Master");
					updateDealerInfoInRegistrationTable(empId,terriCode,null,successMap,type);
				}
				
			}
			successMap.put(type+"_IN_REGISTRATION - "+empId, "Record : "+insertRecord);
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in insertTMDealers", e.toString());
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void updateUnknownTerriCodeInRegistration(String empId, List<String> territoryCodeList,
			Map<String, String> successMap,String type) {
		if(type.equalsIgnoreCase("Dealers")){
			type = "Dealer";
		}
		try {
			String sql = "select `dealer_code` from `Registration` where `emp_id` = '"+empId+"' "
					+ "and `territory_code` not in (:territoryCodeList) and `Type` = '"+type+"' ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("territoryCodeList", territoryCodeList);
			List<String> result = query.getResultList();
			if(result.size() != 0){
				for(String terriCode : territoryCodeList){
					sql = "update `Registration` set `Is_Active` = 'N' where `emp_id` = '"+empId+"' "
							+ "and `territory_code` = '"+terriCode+"' and `Type` = '"+type+"' ";
					int count = getEm().createNativeQuery(sql).executeUpdate();
					
					successMap.put("UNKNOWN_TERRITORY - "+terriCode, "Unknown territory in-active count is "+count);
				}
			}
			else{
				successMap.put("UNKNOWN_TERRITORY", "No unknown territory found of "+empId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("DB_ERROR in updateUnknownTerriCodeInRegistration", e.toString());
		}
		
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object[]> pjpReportData() {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"ORGANIGATION","BUSSINESS_UNIT","EMP_ID", "EMP_NAME","TERRITORY_CODE","TERRITORY_NAME","DEALER_CODE","DEALER_NAME","TYPE",
				"CLASSIFICATION","PJP_CREATION_DATE","VISIT_PLAN_DATE","VISIT_DATE","VISIT_STATUS","VISIT_TYPE","PURPOSE","VISIT_REMARK"});
		try {
			String sql = "SELECT `OrgnizationName`,`BussinessUnit`,`emp_id`,`Name`,`territory_code`,`Territory_Name`,`dealer_code`,`DealerName`,`Type`,"
					+ "`classification`,`PJP_CREATION_DATE`,`VISIT_PLAN_DATE`,`visit_date`,`Visit_Status`,`visit_type`,`purpose`,`visit_remark` FROM `PJP_REPORT`";
			
			//String sql = "select `t1`.`OrgnizationName` AS `OrgnizationName`,`t1`.`BussinessUnit` AS `BussinessUnit`,`t1`.`emp_id` AS `emp_id`,`t1`.`Name` AS `Name`,`t1`.`territory_code` AS `territory_code`,(select distinct `Dealer_Master_25Aug`.`TER_NAME` from `Dealer_Master_25Aug` where (abs(`Dealer_Master_25Aug`.`TERRITORY`) = `t1`.`territory_code`)) AS `territory_name`,`t1`.`dealer_code` AS `dealer_code`,`t1`.`DealerName` AS `DealerName`,`t1`.`Type` AS `Type`,(case when isnull(`Dealer_Classification`.`Rev_Classification`) then 'C' else `Dealer_Classification`.`Rev_Classification` end) AS `classification`,`t1`.`PJP_CREATION_DATE` AS `PJP_CREATION_DATE`,`t1`.`VISIT_PLAN_DATE` AS `VISIT_PLAN_DATE`,`t1`.`visit_date` AS `visit_date`,`t1`.`Visit_Status` AS `Visit_Status`,`t1`.`visit_type` AS `visit_type`,`t1`.`purpose` AS `purpose`,`t1`.`visit_remark` AS `visit_remark` from (((select `Employee-Master`.`OrgnizationName` AS `OrgnizationName`,`Employee-Master`.`BussinessUnit` AS `BussinessUnit`,`t`.`emp_id` AS `emp_id`,`Employee-Master`.`Name` AS `Name`,(case when ((`t`.`Type` = 'Dealer') or (`t`.`Type` = 'Sub Dealer') or (`t`.`Type` = 'Distributor')) then `t`.`territory_code` else `Employee-Master`.`territory_code` end) AS `territory_code`,`t`.`dealer_code` AS `dealer_code`,`t`.`DealerName` AS `DealerName`,`t`.`Type` AS `Type`,`t`.`PJP_CREATION_DATE` AS `PJP_CREATION_DATE`,`t`.`VISIT_PLAN_DATE` AS `VISIT_PLAN_DATE`,(case when (`t`.`mid` is not null) then `t`.`VISIT_PLAN_DATE` end) AS `visit_date`,(case when isnull(`t`.`mid`) then 'OPEN' else 'CLOSED' end) AS `Visit_Status`,`t`.`visit_type` AS `visit_type`,`t`.`purpose` AS `purpose`,`t`.`remark` AS `visit_remark` from (((select `Registration`.`emp_id` AS `emp_id`,`Registration`.`dealer_code` AS `dealer_code`,`Registration`.`F1` AS `DealerName`,`Registration`.`territory_code` AS `territory_code`,`Registration`.`Type` AS `Type`,`visits`.`plan_date` AS `PJP_CREATION_DATE`,`visits`.`date` AS `VISIT_PLAN_DATE`,`visits`.`visit_type` AS `visit_type`,`visits`.`purpose` AS `purpose`,`visits`.`mid` AS `mid`,`visits`.`remark` AS `remark` from (`visits` join `Registration` on((`visits`.`rno` = `Registration`.`reg_id`))) where ((`visits`.`visit_date` >= '2020-05-18') and (`Registration`.`Is_Active` = 'Y')))) `t` join `Employee-Master` on((`t`.`emp_id` = `Employee-Master`.`Emp-id`))))) `t1` left join `Dealer_Classification` on((abs(`t1`.`dealer_code`) = `Dealer_Classification`.`Sold_to_Party`)))";
			
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> dataList =  query.getResultList();
			int i=1;
			for(Object[] obj : dataList){
				i++;
				String orgName = obj[0] == null ? "" : String.valueOf(obj[0]);
				String bu = obj[1] == null ? "" : String.valueOf(obj[1]);
				String empId = String.valueOf(obj[2]);
				String empName = String.valueOf(obj[3]);
				String terriCode = obj[4] == null ? "" : String.valueOf(obj[4]);
				String terriName = obj[5] == null ? "" : String.valueOf(obj[5]);
				String dealerCode = obj[6] == null ? "" : String.valueOf(obj[6]);
				String dealerName = obj[7] == null ? "" : String.valueOf(obj[7]);
				String type = obj[8] == null ? "" : String.valueOf(obj[8]);
				String classification = obj[9] == null ? "" : String.valueOf(obj[9]);
				String pjpCreationDate = obj[10] == null ? "" : String.valueOf(obj[10]).replace(".0", "");
				String visitPlanDate = obj[11] == null ? "" : String.valueOf(obj[11]).replace(".0", "");
				String visitDate = obj[12] == null ? "" : String.valueOf(obj[12]).replace(".0", "");
				String visitStatus = obj[13] == null ? "" : String.valueOf(obj[13]);
				String visitType = obj[14] == null ? "" : String.valueOf(obj[14]);
				String purpose = obj[15] == null ? "" : String.valueOf(obj[15]);
				String visitRemark = obj[16] == null ? "" : String.valueOf(obj[16]);
				
				data.put(String.valueOf(i), new Object[] {orgName, bu, empId, empName, terriCode, terriName, dealerCode, dealerName, type, 
						classification, pjpCreationDate, visitPlanDate, visitDate, visitStatus, visitType, purpose, visitRemark});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return data;
	}

	@Transactional
	public Response<Map<String, String>> getAllProductsOfDate(String fromDate, String toDate) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			
			prepareAllProducts(fromDate, toDate, successMap);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	@Transactional
	private void prepareAllProducts(String fromDate, String toDate, Map<String, String> successMap){
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			String soapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\"> "+
					"   <soap:Header/> "+
					"   <soap:Body> "+
					"      <urn:Z_SF_MATERIAL_MASTER> "+
					"         <IM_FROM_DATE>"+fromDate+"</IM_FROM_DATE> "+
					"         <IM_TO_DATE>"+toDate+"</IM_TO_DATE> "+
					"		  <IM_VKORG> "+
					"            <item> "+
					"               <VKORG>4000</VKORG> "+
					"            </item> "+
//					"            <item> "+
//					"               <VKORG>2000</VKORG> "+
//					"            </item> "+
					"         </IM_VKORG> "+
					"         <TB_PRODUCTS> "+
					"            <item> "+
					"               <PRODUCT_CODE></PRODUCT_CODE> "+
					"               <PRODUCT_NAME></PRODUCT_NAME> "+
					"               <DIVISION_CODE></DIVISION_CODE> "+
					"               <CATELOG_NUMBER></CATELOG_NUMBER> "+
					"				<SALES_ORG></SALES_ORG> "+
					"               <DISTRIBUTION_CHANEL></DISTRIBUTION_CHANEL> "+
					"               <PLANT></PLANT> "+
					"               <SALEABLE_ITEM></SALEABLE_ITEM> "+
					"               <BASE_UOM></BASE_UOM> "+
					"               <PROCUREMENT_TYPE></PROCUREMENT_TYPE> "+
					"               <MAT_STS_DIV></MAT_STS_DIV> "+
					"               <MATERIAL_TYPE></MATERIAL_TYPE> "+
					"            </item> "+
					"         </TB_PRODUCTS> "+
					"      </urn:Z_SF_MATERIAL_MASTER> "+
					"   </soap:Body> "+
					"</soap:Envelope> ";
			String soapResponse = SOAP.method(soapRequest,successMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("item");
				if(nd.getLength() != 0){
					List<String> distinctProductCode = new ArrayList<String>();
					int deleteCount = 0;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String pc = node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent();
						if(!pc.equalsIgnoreCase("") && !distinctProductCode.contains(pc)){
							String delSql = "delete from `All_Products_25Aug` where `PRODUCT_CODE` = '"+pc+"' ";
							int dc = getEm().createNativeQuery(delSql).executeUpdate();
							//System.out.println(delSql+" :: Del count : "+dc+" :: "+fromDate);
							successMap.put("SQL-"+delSql, " :: Del count : "+dc);
							deleteCount += dc;
							distinctProductCode.add(pc);
						}
					}
					
					int successRecord = 0;
					AllProductModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String pc = node.getElementsByTagName("PRODUCT_CODE").item(0).getTextContent();
						if(!pc.equalsIgnoreCase("")){
							e = new AllProductModel();
							e.setProductCode(pc);
							e.setProductName(node.getElementsByTagName("PRODUCT_NAME").item(0).getTextContent());
							e.setDivisionCode(node.getElementsByTagName("DIVISION_CODE").item(0).getTextContent());
							e.setCatelogNumber(node.getElementsByTagName("CATELOG_NUMBER").item(0).getTextContent());
							if(e.getCatelogNumber() == null || e.getCatelogNumber().equalsIgnoreCase("")){
								e.setCatelogNumber("#N/A");
							}
							e.setSalesOrg(node.getElementsByTagName("SALES_ORG").item(0).getTextContent());
							e.setDistributionChanel(node.getElementsByTagName("DISTRIBUTION_CHANEL").item(0).getTextContent());
							e.setPlant(node.getElementsByTagName("PLANT").item(0).getTextContent());
							e.setSaleableItem(node.getElementsByTagName("SALEABLE_ITEM").item(0).getTextContent());
							e.setProductNameSaleableItem(e.getProductName()+"_"+e.getSaleableItem());
							e.setPcPnCn(e.getProductNameSaleableItem()+" ~ "+e.getCatelogNumber()+" ~ "+e.getProductCode());
							e.setBaseUom(node.getElementsByTagName("BASE_UOM").item(0).getTextContent());
							e.setProcurementType(node.getElementsByTagName("PROCUREMENT_TYPE").item(0).getTextContent());
							e.setMatStsDiv(node.getElementsByTagName("MAT_STS_DIV").item(0).getTextContent());
							e.setMaterialType(node.getElementsByTagName("MATERIAL_TYPE").item(0).getTextContent());
							e.setIsActive(1);
							e.setProductInsertDate(fromDate);
							e.setCreateDate(new Date());
							save(e);
							successRecord++;
						}
					}
					
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERTED : "+fromDate, "Insert Record : "+successRecord+", Delete count : "+deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("ALL_PRODUCTS : "+fromDate, ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("ALL_PRODUCTS : "+fromDate, ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			if(soapResponse == null){
				String mobileNumber = CommonFunction.readTextFile("MobileNumber.txt");
				//System.out.println(mobileNumber);
				int isSend = SendSMS.sendSms("Error in All_Products : "+successMap.get("SOAP_API"), mobileNumber);
				System.out.println("isSendSMS :: "+isSend);
			}
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_in_prepareAllProducts", e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getExcludeChannelCodeList(){
		String configSql = "SELECT `Config_Value` FROM `Outstanding_Report_Config` where `Config_Type` = 'distributionChannelExcludeList' ";
		Query configQuery = getEm().createNativeQuery(configSql);
		List<String> distributionChannelExcludeList =  configQuery.getResultList();
		if(distributionChannelExcludeList.size() !=0){
			String commaSepareteDistributionChannel = distributionChannelExcludeList.get(0);
			distributionChannelExcludeList = CommonFunction.getListOfUniqueStringTokens(commaSepareteDistributionChannel, ",");
		}
		return distributionChannelExcludeList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeePerformanceData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			String currentDate = MyCalendar.getCurrentDate();
			String sql = "SELECT `Target_Mapping`.`EMP_CODE`, `Employee-Master`.`territory_code` FROM `Target_Mapping` join `Employee-Master` "
					+ "on `Target_Mapping`.`EMP_CODE` = `Employee-Master`.`Emp-id` where `Employee-Master`.`Status` = 'Active' and `Employee-Master`.`territory_code` is not null";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Performance` where `EmpId` = '"+empId+"' and `TerritoryCode` = '"+territoryCode+"' ").executeUpdate();
				
				insertPerformanceData(distributionChannelExcludeList, empId, territoryCode, "MTD", currentDate,successMap);
				insertPerformanceData(distributionChannelExcludeList, empId, territoryCode, "YTD", currentDate,successMap);
				insertPerformanceData(distributionChannelExcludeList, empId, territoryCode, "QTD", currentDate,successMap);
				i++;
				System.out.println("-saveAllEmployeePerformanceData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}
	
	
	
	@Transactional
	@SuppressWarnings("unchecked")
	private void insertPerformanceData(List<String> distributionChannelExcludeList, String empId, String territoryCode, String period, String currentDate, Map<String, String> successMap){
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
						+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
						+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
						+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				List<Object[]> dcList =  query.getResultList();
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				
				if(dcList.size()!=0){
					List<PerformanceResponse> performance = new ArrayList<PerformanceResponse>();
					List<String> includeDealerCode = new ArrayList<String>();
					for(Object[] dc : dcList){
						if(dc[0] != null){
							String dealerCode = String.valueOf(dc[0]);
							includeDealerCode.add(dealerCode);
						}
					}
					
					if(includeDealerCode.size() != 0){
						Map<String, Integer> monthMap = new HashMap<String, Integer>();
						monthMap.put("Apr", 1);monthMap.put("May", 2);monthMap.put("Jun", 3);
						monthMap.put("Jul", 4);monthMap.put("Aug", 5);monthMap.put("Sep", 6);
						monthMap.put("Oct", 7);monthMap.put("Nov", 8);monthMap.put("Dec", 9);
						monthMap.put("Jan", 10);monthMap.put("Feb", 11);monthMap.put("Mar", 12);
						
						String currentMonthName = MyCalendar.getMonthFromCurrentDate();
						Integer monthNumber = monthMap.get(currentMonthName);
						
						PerformanceResponse per = null;
						float overall_Ybase = 0;
						float overall_TGT = 0;
						float overall_TyAct = 0;
						
						String insertTable = "insert into `All_Employee_Performance` (`EmpId`, `TerritoryCode`, `Period`, `Product`, `LyBase`, `Tgt`, `TyAct`, `GrPer`, `AchPer`, `CurrentDate`) ";
						String insertValue = "";
						sql = "SELECT `Product`,`Column_Name` FROM `Performance_Report_Config` order by `P_Id`";
						query = getEm().createNativeQuery(sql);
						List<Object[]> perConfigList =  query.getResultList();
						for(Object[] obj : perConfigList){
							String product = String.valueOf(obj[0]);
							String columnName = String.valueOf(obj[1]);
							String target = null;
							String hql = "SELECT '"+product+"' as Product, "+columnName+" as Target FROM `Target_Mapping` where EMP_CODE = '"+empId+"' ";
							query = getEm().createNativeQuery(hql);
							List<Object[]> targetList =  query.getResultList();
							if(targetList.size() != 0){
								Object[] targetObj = targetList.get(0);
								if(targetObj[1] != null){
									target = String.valueOf(targetObj[1]);
								}
							}
							per = new PerformanceResponse();
							per.setProduct(product);
							per.setEmpId(empId);
							if(target != null && period.equalsIgnoreCase("MTD")){
								preparePerformanceAsMTD(per,target,monthNumber,includeDealerCode,territoryCodeList);
							}else if(target != null && period.equalsIgnoreCase("QTD")){
								preparePerformanceAsQTD(per,target,monthNumber,includeDealerCode,territoryCodeList);
							}
							else if(target != null && period.equalsIgnoreCase("YTD")){
								preparePerformanceAsYTD(per,target,monthNumber,includeDealerCode,territoryCodeList);
							}
							overall_Ybase += per.getLyBase();
							overall_TGT += per.getTgt();
							overall_TyAct += per.getTyAct();
							performance.add(per);
							
							insertValue+= "('"+empId+"', '"+territoryCode+"', '"+period+"', '"+product+"', "+per.getLyBase()+", "+per.getTgt()+", "+per.getTyAct()+", "+per.getGrPercentate()+", "+per.getAchPercentate()+", '"+currentDate+"'), ";
						}
						per = new PerformanceResponse();
						per.setProduct("Overall");
						per.setLyBase(overall_Ybase);
						DecimalFormat decimalFormat = new DecimalFormat("#.##");
						float twoDigitsF = Float.valueOf(decimalFormat.format(overall_TGT));
						per.setTgt(twoDigitsF);
						per.setTyAct(overall_TyAct);
	
						float achPer = 0;
						if(per.getTgt() != 0){
							achPer = per.getTyAct() / per.getTgt() ;
						}
						achPer = Float.valueOf(decimalFormat.format(achPer));
						per.setAchPercentate(achPer);
						
						float grPer = 0;
						if(per.getLyBase() != 0){
							grPer = per.getTyAct() / (per.getLyBase() - 1 ) ;
						}
						grPer = Float.valueOf(decimalFormat.format(grPer));
						per.setGrPercentate(grPer);
						
						performance.add(per);
						
						insertValue+= "('"+empId+"', '"+territoryCode+"', '"+period+"', 'Overall', "+per.getLyBase()+", "+per.getTgt()+", "+per.getTyAct()+", "+per.getGrPercentate()+", "+per.getAchPercentate()+", '"+currentDate+"') ";
						
						if(!insertValue.equalsIgnoreCase(""))
							getEm().createNativeQuery(insertTable + " VALUES " + insertValue).executeUpdate();
					}
					
				}
				else{
					
				}
			}
			else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_In_insertPerformanceData", e.toString());
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeeOutstandingData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			Date currentDate = new Date();
			
			String sql = "SELECT `Emp-id`, `territory_code` FROM `Employee-Master` where `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Outstanding` where `Emp_id` = '"+empId+"' and `Territory_Code` = '"+territoryCode+"' ").executeUpdate();
				
				insertOutStandingData(distributionChannelExcludeList, empId, territoryCode, successMap, currentDate);
				i++;
				System.out.println("-saveAllEmployeeOutstandingData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
						
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private void insertOutStandingData(List<String> distributionChannelExcludeList, String empId, String territoryCode, Map<String, String> errorMap, Date createDate) {
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
							+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
							+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
							+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
							+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				List<Object[]> dcList =  query.getResultList();
				
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				if(dcList.size()!=0){
					List<OutstandingResponse> outstandingList = new ArrayList<OutstandingResponse>();
					SOAPRequest jsonData1 = null;
					float totalOutstaAmount=0,totalNotdue=0,
					totalZ0_30=0,totalZ31_60=0,totalZ61_90=0,totalZ91_180=0,total181_365=0,totalZ366_Above=0;
					for(Object[] dc : dcList){
						if(dc[0] != null){
							String dealerName = String.valueOf(dc[1]);
							String dealerCode = String.valueOf(dc[0]);
							jsonData1 = new SOAPRequest();
							jsonData1.setDealerCode(dealerCode);
							jsonData1.setIsSyncOutstanding("No");
							
							for(OutstandingResponse out : prepareOutstandingData(jsonData1, errorMap)){
								if(errorMap.size() !=0){
									break;
								}
								out.setDealerName(dealerName);
								outstandingList.add(out);
								
								totalOutstaAmount += Float.parseFloat(out.getOutstaAmount());
								totalNotdue += Float.parseFloat(out.getNotDue());
								totalZ0_30 += Float.parseFloat(out.getZ0_30());
								totalZ31_60 += Float.parseFloat(out.getZ31_60());
								totalZ61_90 += Float.parseFloat(out.getZ61_90());
								totalZ91_180 += Float.parseFloat(out.getZ91_180());
								total181_365 += Float.parseFloat(out.getZ181_365());
								totalZ366_Above += Float.parseFloat(out.getZ366_Above());
							}
							if(errorMap.size() !=0){
								break;
							}
						}
					}
					
					OutstandingResponse om = new OutstandingResponse();
					om.setDealerCode("Total");
					om.setDealerName("");
					om.setDivision("");
					om.setOutstaAmount(String.valueOf(new BigDecimal(String.valueOf(totalOutstaAmount)).setScale(2,0)));
					om.setNotDue(String.valueOf(new BigDecimal(String.valueOf(totalNotdue)).setScale(2,0)));
					om.setZ0_30(String.valueOf(new BigDecimal(String.valueOf(totalZ0_30)).setScale(2,0)));
					om.setZ31_60(String.valueOf(new BigDecimal(String.valueOf(totalZ31_60)).setScale(2,0)));
					om.setZ61_90(String.valueOf(new BigDecimal(String.valueOf(totalZ61_90)).setScale(2,0)));
					om.setZ91_180(String.valueOf(new BigDecimal(String.valueOf(totalZ91_180)).setScale(2,0)));
					om.setZ181_365(String.valueOf(new BigDecimal(String.valueOf(total181_365)).setScale(2,0)));
					om.setZ366_Above(String.valueOf(new BigDecimal(String.valueOf(totalZ366_Above)).setScale(2,0)));
					outstandingList.add(om);
					
					AllEmployeeOutstandingModel e = null;
					for(OutstandingResponse d : outstandingList){
						e = new AllEmployeeOutstandingModel();
						e.setEmpId(empId);
						e.setTerritoryCode(territoryCode);
						e.setDealerCode(d.getDealerCode());
						e.setDealerName(d.getDealerName());
						e.setDivision(d.getDivision());
						e.setOutstaAmount(d.getOutstaAmount());
						e.setNotDue(d.getNotDue());
						e.setZ0_30(d.getZ0_30());
						e.setZ31_60(d.getZ31_60());
						e.setZ61_90(d.getZ61_90());
						e.setZ91_180(d.getZ91_180());
						e.setZ181_365(d.getZ181_365());
						e.setZ366_Above(d.getZ366_Above());
						e.setCreateDate(createDate);
						save(e);
					}
				}
				else{
				}
			}
			else{
			}
		}
		catch(Exception e){
			e.printStackTrace();
			errorMap.put("Error_In_insertOutStandingData", e.toString());
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeeVisitStatusData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			String currentDate = MyCalendar.getCurrentDate();
			String sql = "SELECT `Emp-id`, `territory_code` FROM `Employee-Master` where `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Visit_Status` where `Emp_Id` = '"+empId+"' and `Territory_Code` = '"+territoryCode+"'  ").executeUpdate();
				
				insertVisitStatusData(distributionChannelExcludeList, empId, territoryCode, currentDate, successMap);
				i++;
				System.out.println("-saveAllEmployeeVisitStatusData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
						
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private void insertVisitStatusData(List<String> distributionChannelExcludeList, String empId, String territoryCode, String currentDate, Map<String, String> successMap) {
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
						+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
						+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
						+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				List<Object[]> dcList =  query.getResultList();
				
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				if(dcList.size()!=0){
					List<VisitStatusResponse> visitStatusList = new ArrayList<VisitStatusResponse>();
					List<String> includeDealerCode = new ArrayList<String>();
					
					for(Object[] dc : dcList){
						if(dc[0] != null){
							String dealerCode = String.valueOf(dc[0]);
							includeDealerCode.add(dealerCode);
						}
					}
					
					int subDealerInClassA=0,subDealerVisitDoneClassA = 0;
					int subDealerInClassB=0,subDealerVisitDoneClassB = 0;
					int subDealerInClassC=0,subDealerVisitDoneClassC = 0;
					
					
					sql = "SELECT DISTINCT dm.`SUB_DEALER_CODE`,dm.`SUB_DEALER_NAME`,dm.`CLASSIFICATION` FROM `Sub_Dealer_Classification` dm where abs(dm.`TERRITORY_CODE`) in "
							+ "(SELECT emp.`territory_code`  FROM `Employee-Master` emp WHERE emp.`territory_code` is not null and emp.`Emp-id` = '"+empId+"')";
					query = getEm().createNativeQuery(sql);
					List<Object[]> sdcList =  query.getResultList();
					for(Object[] sdc : sdcList){
						String subDealerCode = String.valueOf(sdc[0]);
						//String subDealerName = String.valueOf(sdc[1]);
						String classification = String.valueOf(sdc[2]);
						int subDealerVisitDone = prepareVisitDoneForAllType(subDealerCode,empId,Constant.SUB_DEALER);
						if(classification == null){
							classification = Constant.DEFAULT_CLASSIFICATION;
							subDealerInClassC++;
							subDealerVisitDoneClassC += subDealerVisitDone;
						}
						else if(classification.equalsIgnoreCase("A")){
							subDealerInClassA++;
							subDealerVisitDoneClassA += subDealerVisitDone;
						}else if(classification.equalsIgnoreCase("B")){
							subDealerInClassB++;
							subDealerVisitDoneClassB += subDealerVisitDone;
						}else if(classification.equalsIgnoreCase("C")){
							subDealerInClassC++;
							subDealerVisitDoneClassC += subDealerVisitDone;
						}
						
					}
					
					if(includeDealerCode.size() != 0){
						prepareVisitStatusByDealerCode(subDealerInClassA,subDealerInClassB,subDealerInClassC,
								subDealerVisitDoneClassA,subDealerVisitDoneClassB,subDealerVisitDoneClassC,visitStatusList,includeDealerCode,empId);
					}
					
					String insertTable = "INSERT INTO `All_Employee_Visit_Status`(`Emp_Id`, `Territory_Code`, `Type`, `Classification`, `No_of_Dealer`, `Visit_Per_Dealer`, `Total_Visit`, `No_of_Visit_Done`, `Adharance_Percentage`, `Bal_to_Visit`, `Create_Date`) ";
					String insertValue = "";
					int i=0;
					for(VisitStatusResponse vs : visitStatusList){
						insertValue += "('"+empId+"', '"+territoryCode+"', '"+vs.getType()+"', '"+vs.getClassification()+"', "+vs.getNoOfDealer()+", "+vs.getVisitPerDealer()+", "+vs.getTotalVisit()+", "+vs.getNoOfVisitDone()+", "+vs.getAdharancePercentage()+", "+vs.getBalOfVisit()+", '"+currentDate+"')";
						
						if(i != visitStatusList.size()-1){
							insertValue += ", ";
						}
						i++;
					}
					if(!insertValue.equalsIgnoreCase(""))
						getEm().createNativeQuery(insertTable + " VALUES " + insertValue).executeUpdate();
					
				}
				else{
				}
			}
			else{
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_In_insertVisitStatusData", e.toString());
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeePendingOrdersData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			Date currentDate = new Date();
			String sql = "SELECT `Emp-id`, `territory_code` FROM `Employee-Master` where `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Pending_Orders` where `Emp_Id` = '"+empId+"' and `Territory_Code` = '"+territoryCode+"'  ").executeUpdate();
				
				insertPendingOrdersData(distributionChannelExcludeList, empId, territoryCode, successMap, currentDate);
				i++;
				System.out.println("-saveAllEmployeePendingOrdersData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
						
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private void insertPendingOrdersData(List<String> distributionChannelExcludeList, String empId, String territoryCode, Map<String, String> errorMap, Date currentDate) {
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
			
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
						+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
						+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
						+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				List<Object[]> dcList =  query.getResultList();
				
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				if(dcList.size()!=0){
					List<PendingOrderStatusResponse> pendingOrderStatusList = new ArrayList<PendingOrderStatusResponse>();
					for(Object[] dc : dcList){
						if(dc[0] != null){
							String dealerName = String.valueOf(dc[1]);
							String dealerCode = String.valueOf(dc[0]);
							
							prepareVisitPendingOrderStatusByDealerCode(dealerName,pendingOrderStatusList,dealerCode,errorMap);
						}
					}
					
					AllEmployeePendingOrdersModel e = null;
					for(PendingOrderStatusResponse p : pendingOrderStatusList){
						e = new AllEmployeePendingOrdersModel();
						e.setEmpId(empId);
						e.setTerritoryCode(territoryCode);
						e.setDealerCode(p.getDealerCode());
						e.setDealerName(p.getDealerName());
						e.setMaterialCode(p.getMaterialCode());
						e.setMaterialDesc(p.getMaterialDesc());
						e.setOrderNo(p.getOrderNo());
						e.setOrderQty(p.getOrderQty());
						e.setBilledQty(p.getBilledQty());
						e.setPendingQty(p.getPendingQty());
						e.setPendingStatus(p.getPendingStatus());
						e.setCreateDate(currentDate);
						save(e);
					}
				}
				else{
				}
			}
			else{
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			errorMap.put("Error_In_insertPendingOrdersData", e.toString());
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeeNonVisitedDealerData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			Date currentDate = new Date();
			
			String sql = "SELECT `Emp-id`, `territory_code` FROM `Employee-Master` where `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Non_Visited_Dealer` where `Emp_Id` = '"+empId+"' and `Territory_Code` = '"+territoryCode+"' ").executeUpdate();
				
				insertNonVisitedDealerData(distributionChannelExcludeList, empId, territoryCode, successMap, currentDate);
				i++;
				System.out.println("-saveAllEmployeeNonVisitedDealerData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
						
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private void insertNonVisitedDealerData(List<String> distributionChannelExcludeList, String empId, String territoryCode, Map<String, String> errorMap, Date currentDate) {
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
						+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
						+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
						+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				List<Object[]> dcList =  query.getResultList();
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				if(dcList.size()!=0){
					/*String financialYearDate = getFinancialYearDate();
					String fromDate = financialYearDate.split(":")[0];
					String toDate = financialYearDate.split(":")[1];*/
					
					List<NonVisitedDealerResponse> nonVisitedDealerList = new ArrayList<NonVisitedDealerResponse>();
					NonVisitedDealerResponse  res = null;
					
					for(Object[] dc : dcList){
						if(dc[0] != null){
							String dealerName = String.valueOf(dc[1]);
							String dealerCode = String.valueOf(dc[0]);
							dealerCode = CommonFunction.removePrefixZero(dealerCode);
							res = new NonVisitedDealerResponse();
							res.setDealerCode(dealerCode);
							res.setDealerName(dealerName);
							String classification = getClassficationByDealerCode(dealerCode);
							if(classification == null){
								classification = Constant.DEFAULT_CLASSIFICATION;
							}
							res.setDealerClass(classification);
							
							//prepareNonVisitedDealer(fromDate, toDate, empId, res, dealerCode, errorMap);
							prepareLastVisitDateForNonVisitedDealer(empId, dealerCode, res);
							nonVisitedDealerList.add(res);
							
						}
					}
					
					AllEmployeeNonVisitedDealerModel e = null;
					for(NonVisitedDealerResponse vs : nonVisitedDealerList){
						e = new AllEmployeeNonVisitedDealerModel();
						e.setEmpId(empId);
						e.setTerritoryCode(territoryCode);
						e.setDealerCode(vs.getDealerCode());
						e.setDealerName(vs.getDealerName());
						e.setDealerClass(vs.getDealerClass());
						e.setYtdSale(vs.getYtdSale());
						e.setLastVisitDate(vs.getLastVisitDate());
						e.setCreateDate(currentDate);
						save(e);
					}
					
				}
				else{
				}
			}
			else{
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			errorMap.put("Error_In_insertNonVisitedDealerData", e.toString());
		}
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> saveAllEmployeeNetworkExpansionData() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String financialYearDate = getFinancialYearDate();
			String fromDate = financialYearDate.split(":")[0];
			String toDate = financialYearDate.split(":")[1];
			
			List<String> distributionChannelExcludeList = getExcludeChannelCodeList();
			
			int deleteCount = 0;
			
			Date currentDate = new Date();
			String sql = "SELECT `Emp-id`, `territory_code` FROM `Employee-Master` where `territory_code` is not null and `Status` = 'Active' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			int i=0;
			for(Object[] obj : result){
				String empId = String.valueOf(obj[0]);
				String territoryCode = String.valueOf(obj[1]);
				
				deleteCount += getEm().createNativeQuery("delete from `All_Employee_Network_Expansion` where `Emp_Id` = '"+empId+"' and `Territory_Code` = '"+territoryCode+"' ").executeUpdate();
				
				insertNetworkExpansionData(distributionChannelExcludeList, empId, territoryCode, fromDate, toDate, currentDate,successMap);
				i++;
				System.out.println("-saveAllEmployeeNetworkExpansionData--Done--"+i+"--"+empId+"--"+territoryCode+"---");
						
			}
			
			endTime = MyCalendar.getCurrentTimestamp();
			successMap.put("DeleteCount", deleteCount+", StartTime : "+startTime+", EndTime : "+endTime);
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	private void insertNetworkExpansionData(List<String> distributionChannelExcludeList, String empId, String territoryCode, String fromDate, String toDate, Date currentDate, Map<String, String> successMap) {
		try {
			List<String> territoryCodeList = new ArrayList<String>();
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			StringBuilder empRoleType = new StringBuilder();
			territoryCodeList = prepareTerriCodeByEmpId(empId,empRoleType);
			if(!territoryCode.equalsIgnoreCase("")){
				territoryCodeList = new ArrayList<String>();
				territoryCodeList.add(territoryCode);
			}
			if(territoryCodeList.size() !=0){
				String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
						+ "and dm.`dealer_code` not in (:excludeDealerCodeList) "
						+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
						+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
						+ "and dm.`IS_ACTIVE` = 'X' ";
				Query query = getEm().createNativeQuery(sql);
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
				query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
				query.setParameter("territoryCodeList", territoryCodeList);
				List<Object[]> dcList =  query.getResultList();
				
				if(!empRoleType.toString().equalsIgnoreCase(Constant.EMPLOYEE_TM)){
					excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
					dcList.addAll(excludeDealerNameCodeList);
				}
				
				
				if(dcList.size()!=0){
					List<NetworkExpansionResponse> networkExpansionList = new ArrayList<NetworkExpansionResponse>();
					List<String> includeDealerCode = new ArrayList<String>();
					
					for(Object[] dc : dcList){
						if(dc[0] != null){
							if(!excludeDealerCodeList.contains(String.valueOf(dc[0]))){
								String dealerCode = String.valueOf(dc[0]);
								includeDealerCode.add(dealerCode);
							}
						}
					}
					
					if(includeDealerCode.size() != 0){
						
						for(String dealerCode : includeDealerCode){
							
							sql = "select nem from NetworkExpansionReportModel nem where nem.dealerCode = '"+dealerCode+"' "
									+ "and nem.isActive = 1 "
									+ "and nem.createDate >= '"+fromDate+"' "
									+ "and nem.createDate <= '"+toDate+"' "
									+ "and nem.ytdBillingValue is null  ";
							query = getEm().createQuery(sql);
							List<NetworkExpansionReportModel> nemList = query.getResultList();
							if(nemList.size() != 0){
								
								NetworkExpansionReportModel nem = nemList.get(0);
								sql = "SELECT DEALER_CODE, sum(INVOICE_VALUE) as invoiceValue, count(BILLING_DATE) as noOfBilling FROM `Sales_Report` where DEALER_CODE = '"+dealerCode+"' GROUP by DEALER_CODE";
								query = getEm().createNativeQuery(sql);
								List<Object[]> rList = query.getResultList();
								if(rList.size() != 0){
									Object[] obj = rList.get(0);
									String invoiceValue = obj[1] == null && String.valueOf(obj[1]).equalsIgnoreCase("") ? "0" : String.valueOf(obj[1]);
									int noOfBilling = obj[2] == null && String.valueOf(obj[2]).equalsIgnoreCase("") ? 0 : Integer.parseInt(String.valueOf(obj[2]));
									
									nem.setYtdBillingValue(String.valueOf(new BigDecimal(invoiceValue).setScale(2,0)));
									nem.setNumOfBilling(noOfBilling);
									save(nem);
								}
							}
							
							sql = "select nem from NetworkExpansionReportModel nem where nem.dealerCode = '"+dealerCode+"' "
									+ "and nem.isActive = 1 "
									+ "and nem.createDate >= '"+fromDate+"' "
									+ "and nem.createDate <= '"+toDate+"' ";
							query = getEm().createQuery(sql);
							nemList = query.getResultList();
							NetworkExpansionResponse nemr = null;
							for(NetworkExpansionReportModel nem : nemList){
								nemr = new NetworkExpansionResponse();
								nemr.setDealerCode(CommonFunction.removePrefixZero(nem.getDealerCode()));
								nemr.setDealerName(nem.getDealerName());
								nemr.setNoOfBilling(String.valueOf(nem.getNumOfBilling()));
								if(nem.getYtdBillingValue() != null && !nem.getYtdBillingValue().equalsIgnoreCase("null")){
									nemr.setYtdBillingValue(String.valueOf(new BigDecimal(nem.getYtdBillingValue()).setScale(2,0)));
								}
								networkExpansionList.add(nemr);
							}
						}
					}
					
					AllEmployeeNetworkExpansionModel e = null;
					for(NetworkExpansionResponse vs : networkExpansionList){
						e = new AllEmployeeNetworkExpansionModel();
						e.setEmpId(empId);
						e.setTerritoryCode(territoryCode);
						e.setDealerCode(vs.getDealerCode());
						e.setDealerName(vs.getDealerName());
						e.setNoOfBilling(vs.getNoOfBilling());
						e.setYtdBillingValue(vs.getYtdBillingValue());
						e.setCreateDate(currentDate);
						save(e);
					}
				}
				else{
				}
			}
			else{
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("Error_In_insertNetworkExpansionData", e.toString());
		}
		
	}

	public Response<Map<String, String>> sendWhatsApp() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> successMap = new HashMap<String, String>();
		try {
			/*String soapRequest = "{"+
										"\"to\":\"919716744965\","+
										"\"type\":\"hsm\","+
										"\"message\":"+
											"{"+
												"\"templateid\":\"pbwa120\" "+
											"},"+
										"\"gotomodule\":0"+
									"}";*/
			
			String soapRequest = "{"+
								"\"to\":\"919716744965\","+
								"\"type\":\"template\","+
								"\"message\":"+
									"{"+
										"\"templateid\":\"pbwa117\","+
										"\"url\":\"https://trinityapplab.com/download.png\","+
										"\"filename\":\"hlr.png\","+
										"\"placeholders\":[]"+
									"},"+
								"\"gotomodule\":0"+
							"}";
			
			/*String soapRequest = "{"+
								"\"to\":\"919716744965\","+
								"\"type\":\"template\","+
								"\"message\":"+
									"{"+
										"\"templateid\":\"pbwa116\","+
										"\"url\":\"https://ucarecdn.com/7fb5c036-d7a8-4f08-843a-dd9157143ab2/\","+
										"\"filename\":\"whatsapp.pdf\","+
										"\"placeholders\":[]"+
									"},"+
								"\"gotomodule\":0"+
							"}";*/
			
			String soapResponse = SOAP.whatsAppApi(soapRequest,successMap);
			
			response.setSoapApiResponse(soapResponse);
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Response<Map<String, String>> insertOrUpdateEmpByReadFile() {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		String soapResponse = null;
		int totalRecord = 0;
		int insertRecord = 0;
		int updateRecord = 0;
		try {
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			Map<String, String> successMap = new HashMap<String, String>();
			// date in ddMMyyyy format
			soapResponse = CommonFunction.readTextFile("Employeewise.txt");
			// System.out.println("soapResponse : " +soapResponse);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("employee");
				if(nd.getLength() != 0){
					EmployeeMasterModel e = null;
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						String empId = node.getElementsByTagName("emp_code").item(0).getTextContent();
						
						String sql = " SELECT emp from EmployeeMasterModel emp where emp.empId = :empId ";
						Query query = getEm().createQuery(sql);
						query.setParameter("empId", empId);
						List<EmployeeMasterModel> empResult = query.getResultList();
						if(empResult.size() !=0){
							e = empResult.get(0);
							//e.seteCode(10000); // change if required
							e.setLastUpdateDate(new Date());
							updateRecord++;
						}
						else{
							e = new EmployeeMasterModel();
							e.setEmpId(empId);
							e.seteCode(10000); // change if required
							e.setPassword("tr"+e.getEmpId());
							e.setCreateDate(new Date());
							insertRecord++;
						}
						String doj = node.getElementsByTagName("doj").item(0).getTextContent();
						if(doj != null && !doj.equalsIgnoreCase("")){
							e.setDoj(MyCalendar.convertStrDateToUtilDateForDoj(doj));
						}
						String company = node.getElementsByTagName("company").item(0).getTextContent();
						if(company != null && company.equalsIgnoreCase("HSIL Limited")){
							e.setOrgId(1000);
						}
						else if(company != null && company.equalsIgnoreCase("Somany Home Innovation Limited")){
							e.setOrgId(2000);
						}
						else if(company != null && company.equalsIgnoreCase("Brilloca Limited")){
							e.setOrgId(4000);
						}
						e.setOrganizationName(company);
						e.setOrganizationNameUpdated(company);
						e.setName(node.getElementsByTagName("user_name").item(0).getTextContent());
						e.setMobile(node.getElementsByTagName("mobile_no").item(0).getTextContent());
						e.setRole(node.getElementsByTagName("role").item(0).getTextContent());
						e.setRoleCode(node.getElementsByTagName("role_code").item(0).getTextContent());
						e.setRmId(node.getElementsByTagName("sup_code").item(0).getTextContent());
						e.setEmailId(node.getElementsByTagName("email").item(0).getTextContent());
						e.setOfficialEmailId(e.getEmailId());
						e.setLocationName(node.getElementsByTagName("location").item(0).getTextContent());
						int sI = e.getLocationName().indexOf("(");
						int eI = e.getLocationName().indexOf(")");
						if(sI >= 0 && eI >= 0){
							String territoryCode = e.getLocationName().substring(sI+1,eI);
							territoryCode = territoryCode.replaceAll(",", "");
							territoryCode = territoryCode.replace(" & ", ",");
							e.setTerritoryCode(territoryCode);
						}
						else{
							e.setTerritoryCode(null);
						}
						e.setLocationCode(node.getElementsByTagName("location_code").item(0).getTextContent());
						e.setGradeTitle(node.getElementsByTagName("grade").item(0).getTextContent());
						e.setDepartmentName(node.getElementsByTagName("function").item(0).getTextContent());
						e.setBussinessUnit(node.getElementsByTagName("bu").item(0).getTextContent());
						e.setDesignation(node.getElementsByTagName("design").item(0).getTextContent());
						e.setStatus(node.getElementsByTagName("status").item(0).getTextContent());
						if(e.getStatus().equalsIgnoreCase("Active")){
							e.setActive(1);
						}
						else{
							e.setActive(0);
						}
						
						save(e);
						totalRecord++;
					}
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("INSERT_OR_UPDATE", "Record : "+String.valueOf(totalRecord)+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				successMap.put("EMP_INTEGRATION", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
			response.setErrorsMap(successMap);
			response.setResponseCode(ReturnsCode.OPERATION_SUCCESSFUL);
			response.setResponseDesc(ReturnsCode.SUCCESSFUL);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseCode(ReturnsCode.GENERIC_DATABASE_ERROR);
			response.setResponseDesc(e.toString());
		}
		
		try {
			ResponseTableModel res = new ResponseTableModel();
			res.setRequestJson(null);
			res.setResponseJson(new SerialClob(CommonFunction.printResponseJson(response).toCharArray()));
			res.setSoapResponse(soapResponse);
			res.setTotalCount(totalRecord);
			res.setInsertCount(insertRecord);
			res.setUpdateCount(updateRecord);
			res.setMethodName("insertOrUpdateEmpByReadFile");
			res.setCreatedDate(new Date());
			save(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return response;
	}

	/*@SuppressWarnings("unchecked")
	public Map<String, Object[]> getAttendanceData(String prevDate) {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"Emp_Code","Roster_Date","In_Time", "Out_Time","In_Out_Man","Working_Hours"});
		try {
			String sql = "SELECT `Emp_Id`, `Date`, `In_Time`, `Out_Time`, "
					+ "(case when `In_Time` = `Out_Time` then 'false' else 'true' end) as `In_Out_Man`, "
					+ "`Working_Hours`  FROM `Attendance_WorkingHours` where `Date` = '"+prevDate+"'";
			
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> dataList =  query.getResultList();
			int i=1;
			for(Object[] obj : dataList){
				i++;
				String empCode = obj[0] == null ? "" : String.valueOf(obj[0]);
				String rosterDate = obj[1] == null ? "" : String.valueOf(obj[1]);
				String inTime = obj[2] == null ? "" : String.valueOf(obj[2]).replace(".0", "");
				String outTime = obj[3] == null ? "" : String.valueOf(obj[3]).replace(".0", "");
				String inOutMan = obj[4] == null ? "" : String.valueOf(obj[4]);
				String workingHours = obj[5] == null ? "" : String.valueOf(obj[5]);
				
				data.put(String.valueOf(i), new Object[] {empCode, rosterDate, inTime, outTime, 
						inOutMan, workingHours});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return data;
	}*/
	
	@SuppressWarnings("unchecked")
	public Map<String, Object[]> getAttendanceData(String prevDate) {
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] {"Date","Emp_Id","Name", "In_Time","Out_Time","Working_Hours"});
		try {
			String sql = "SELECT `Date`, `Emp_Id`, `Name`, `In_Time`, `Out_Time`, "
					+ "`Working_Hours`  FROM `Attendance_WorkingHours` where `Date` = '"+prevDate+"'";
			
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> dataList =  query.getResultList();
			int i=1;
			for(Object[] obj : dataList){
				i++;
				String date = obj[0] == null ? "" : String.valueOf(obj[0]);
				String empCode = obj[1] == null ? "" : String.valueOf(obj[1]);
				String empName = obj[2] == null ? "" : String.valueOf(obj[2]);
				String inTime = obj[3] == null ? "" : String.valueOf(obj[3]).replace(".0", "");
				String outTime = obj[4] == null ? "" : String.valueOf(obj[4]).replace(".0", "");
				String workingHours = obj[5] == null ? "" : String.valueOf(obj[5]);
				
				data.put(String.valueOf(i), new Object[] {date, empCode, empName, inTime, outTime, workingHours});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			getEm().close();
			System.gc();
		}
		return data;
	}

}
