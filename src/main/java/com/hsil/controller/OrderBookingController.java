package com.hsil.controller;

import java.sql.Clob;
import java.util.Map;

import javax.sql.rowset.serial.SerialClob;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hsil.constant.CommonFunction;
import com.hsil.constant.Response;
import com.hsil.dao.OrderBookingDao;
import com.hsil.dto.SystemParamDTO;
import com.hsil.request.OrderBookingRequest;
import com.hsil.request.SOAPRequest;
import com.hsil.response.OrderBookingAddedItems;
import com.hsil.response.OrderBookingSummaryResponse;
import com.hsil.response.SOAPResponse;
import com.hsil.service.HsilService;

@CrossOrigin
@RestController
@RequestMapping(value = "/hsil")
public class OrderBookingController {
	@Autowired
	OrderBookingDao orderBookingDao;
	
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
	
	@RequestMapping(value ="/getDealerByTerritoryCode",method = RequestMethod.POST)
	public Response<SystemParamDTO> getDealerByTerritoryCode(@RequestBody SOAPRequest jsonData) {
		
		Response<SystemParamDTO> response = null;
		try {
			response = orderBookingDao.getDealerByTerritoryCode(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getDealerByTerritoryCode");
		return response;
	}
	
	@RequestMapping(value ="/getProductDetails",method = RequestMethod.POST)
	public Response<SOAPResponse> getProductDetails(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = orderBookingDao.getProductDetails(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getTerritoryCodeByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/getOtherFieldData",method = RequestMethod.POST)
	public Response<SOAPResponse> getOtherFieldData(@RequestBody SOAPRequest jsonData) {
		Response<SOAPResponse> response = null;
		try {
			response = orderBookingDao.getOtherFieldData(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(null, response, "getTerritoryCodeByEmpId :: empId - "+jsonData.getEmpId());
		return response;
	}
	
	@RequestMapping(value ="/submitOrderBookingData",method = RequestMethod.POST)
	public Response<Map<String, String>> submitOrderBookingData(@RequestBody OrderBookingRequest jsonData) {
		Response<Map<String, String>> response = null;
		try {
			response = orderBookingDao.submitOrderBookingData(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveResponseInTable(jsonData, response, "submitOrderBookingData");
		return response;
	}
	
	@RequestMapping(value ="/getOrderSummary",method = RequestMethod.POST)
	public Response<OrderBookingSummaryResponse> getOrderSummary(@RequestBody SOAPRequest jsonData) {
		Response<OrderBookingSummaryResponse> response = null;
		try {
			response = orderBookingDao.getOrderSummary(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//saveResponseInTable(jsonData, response, "getOrderSummary");
		return response;
	}
	
	@RequestMapping(value ="/deleteTransactionAndDetails",method = RequestMethod.POST)
	public Response<Map<String, String>> deleteTransactionAndDetails(@RequestBody OrderBookingRequest jsonData) {
		Response<Map<String, String>> response = null;
		try {
			response = orderBookingDao.deleteTransactionAndDetails(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		saveResponseInTable(jsonData, response, "deleteTransaction");
		return response;
	}
	
	/*@RequestMapping(value ="/importData",method = RequestMethod.POST)
	public Response<Map<String, String>> importData(@RequestBody SOAPRequest jsonData) {
		
		Response<Map<String, String>> response = null;
		try {
			String u = CommonFunction.convertExcelBase64ToFile(jsonData.getOperator(), jsonData.getExcelBase64Str(), jsonData.getType());
			//System.out.println(u);
			jsonData.setExcelBase64Str(u);
			response = orderBookingDao.importData(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}*/

}
