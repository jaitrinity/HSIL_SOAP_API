package com.hsil.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.hsil.dto.SystemParamDTO;
import com.hsil.entity.AllProductModel;
import com.hsil.entity.OrderBookingDetModel;
import com.hsil.entity.OrderBookingHdrModel;
import com.hsil.generic.common.GenericDaoImpl;
import com.hsil.request.OrderBookingRequest;
import com.hsil.request.SOAPRequest;
import com.hsil.response.OrderBookingAddedItems;
import com.hsil.response.OrderBookingSummaryResponse;
import com.hsil.response.OrderItemsList;
import com.hsil.response.SOAPResponse;

public class OrderBookingDaoImpl<T> extends GenericDaoImpl<Object> implements OrderBookingDao {
	
	@Transactional
	@SuppressWarnings("unchecked")
	public Response<SystemParamDTO> getDealerByTerritoryCode(SOAPRequest jsonData) {
		Response<SystemParamDTO> response = new Response<SystemParamDTO>();
		List<SystemParamDTO> wrappedList = new ArrayList<SystemParamDTO>();
		SystemParamDTO wrappedObj = null;
		try {
			List<String> territoryCodeList = CommonFunction.getListOfUniqueStringTokens(jsonData.getTerritoryCode(), ",");
			List<Object[]> excludeDealerNameCodeList = getExcludeDealerCodeList(territoryCodeList);
			List<String> excludeDealerCodeList = new ArrayList<String>();
			for(Object[] obj : excludeDealerNameCodeList){
				String dmCode = String.valueOf(obj[0]);
				excludeDealerCodeList.add(dmCode);
			}
			
			List<String> distributionChannelExcludeList = getDistributionChannelExcludeList();
			
			String sql = "SELECT DISTINCT dm.`dealer_code`,dm.`dealer_name` FROM `Dealer_Master_25Aug` dm where dm.`DEALER_TYPE_DESC` = 'Dealers' "
					+ "and dm.`CHANNEL_CODE` not in (:distributionChannelExcludeList) "
					+ "and abs(dm.`TERRITORY`) in (:territoryCodeList) "
					+ "and dm.`IS_ACTIVE` = 'X' ";
			if(excludeDealerCodeList.size() != 0){
				sql+= "and dm.`dealer_code` not in (:excludeDealerCodeList) ";
			}
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("distributionChannelExcludeList", distributionChannelExcludeList);
			query.setParameter("territoryCodeList", territoryCodeList);
			if(excludeDealerCodeList.size() != 0){
				query.setParameter("excludeDealerCodeList", excludeDealerCodeList);
			}
			List<Object[]> result = query.getResultList();
			for(Object[] obj : result){
				String dc = obj[0] == null ? "" : String.valueOf(obj[0]);
				String dn = obj[1] == null ? "" : String.valueOf(obj[1]);
				wrappedObj = new SystemParamDTO();
				wrappedObj.setParamCode(dc);
				wrappedObj.setParamDesc(dn);
				wrappedList.add(wrappedObj);
			}
			
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
	@SuppressWarnings("unchecked")
	private List<String> getDistributionChannelExcludeList() {
		try {
			String sql = "SELECT `Config_Value` FROM `Outstanding_Report_Config` where `Config_Type` = 'distributionChannelExcludeList' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> distributionChannelExcludeList =  query.getResultList();
			
			if(distributionChannelExcludeList.size() !=0){
				String commaSepareteDistributionChannel = distributionChannelExcludeList.get(0);
				distributionChannelExcludeList = CommonFunction.getListOfUniqueStringTokens(commaSepareteDistributionChannel, ",");
				return distributionChannelExcludeList;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
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

	@Transactional
	public Response<SOAPResponse> getProductDetails(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse wrappedObj = new SOAPResponse();
		try {
			if(jsonData.getSearchBy().equalsIgnoreCase("")){
				//List<SystemParamDTO> materialCodeList = prepareMaterialCodeList(jsonData);
				List<SystemParamDTO> materialDescList = prepareMaterialDescList(jsonData);
				//List<SystemParamDTO> catelogNumberListList = prepareCatelogNumList(jsonData);
				
				//wrappedObj.setMaterialCodeList(materialCodeList);
				wrappedObj.setMaterialDescList(materialDescList);
				//wrappedObj.setCatelogNumberListList(catelogNumberListList);
			}
			else if(jsonData.getSearchBy().equalsIgnoreCase(Constant.MATERIAL_CODE)){
				if(jsonData.getActionDoneOnMatDesc().equalsIgnoreCase("No")){
					List<SystemParamDTO> materialDescList = prepareMaterialDescList(jsonData);
					wrappedObj.setMaterialDescList(materialDescList);
				}
				if(jsonData.getActionDoneOnCatNum().equalsIgnoreCase("No")){
					List<SystemParamDTO> catelogNumberListList = prepareCatelogNumList(jsonData);
					wrappedObj.setCatelogNumberListList(catelogNumberListList);
				}
			}
			else if(jsonData.getSearchBy().equalsIgnoreCase(Constant.MATERIAL_DESC)){
				/*if(jsonData.getActionDoneOnMatCode().equalsIgnoreCase("No")){
					List<SystemParamDTO> materialCodeList = prepareMaterialCodeList(jsonData);
					wrappedObj.setMaterialCodeList(materialCodeList);
				}*/
				
				if(jsonData.getActionDoneOnCatNum().equalsIgnoreCase("No")){
					List<SystemParamDTO> catelogNumberListList = prepareCatelogNumList(jsonData);
					wrappedObj.setCatelogNumberListList(catelogNumberListList);
				}
				
			}
			else if(jsonData.getSearchBy().equalsIgnoreCase(Constant.CATELOG_NUMBER)){
				if(jsonData.getActionDoneOnMatCode().equalsIgnoreCase("No")){
					List<SystemParamDTO> materialCodeList = prepareMaterialCodeList(jsonData);
					wrappedObj.setMaterialCodeList(materialCodeList);
				}
				/*if(jsonData.getActionDoneOnMatDesc().equalsIgnoreCase("No")){
					List<SystemParamDTO> materialDescList = prepareMaterialDescList(jsonData);
					wrappedObj.setMaterialDescList(materialDescList);
				}*/
				
			}
			
			if(jsonData.getActionDoneOnMatCode().equalsIgnoreCase("Yes") && 
					jsonData.getActionDoneOnMatDesc().equalsIgnoreCase("Yes") && 
					jsonData.getActionDoneOnCatNum().equalsIgnoreCase("Yes")){
				
				List<SystemParamDTO> divisionCodeList = prepareDivisionCodeList(jsonData);
				wrappedObj.setDivisionCodeList(divisionCodeList);
			}
			
			
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
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> prepareDivisionCodeList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			String sql = "SELECT DISTINCT `DIVISION_CODE` FROM `All_Products` "
					+ "where `CREATE_DATE` is not null and `MAT_STS_DC_X` = '' ";
			
				sql += " and `PRODUCT_CODE` = '"+jsonData.getMatCode()+"' ";
				sql += " and `PRODUCT_NAME_SALEABLE_ITEM` = '"+jsonData.getMatDesc()+"' ";
				sql += " and `CATELOG_NUMBER` = '"+jsonData.getCatNum()+"' ";
				//sql += " LIMIT 100 ";
				
			Query query = getEm().createNativeQuery(sql);
			List<String> result = query.getResultList();
			for(String matDesc : result){
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(matDesc);
				paramObj.setParamDesc(matDesc+" ");
				paramList.add(paramObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> prepareMaterialDescList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			List<String> divisionCodeList = getDivisionCodeList(jsonData);
			
			String sql = "SELECT DISTINCT `PRODUCT_NAME_SALEABLE_ITEM` FROM `All_Products` "
					+ "where `CREATE_DATE` is not null and `MAT_STS_DC_X` = '' ";
			if(jsonData.getActionDoneOnMatCode().equalsIgnoreCase("Yes")){
				sql += " and `PRODUCT_CODE` = '"+jsonData.getMatCode()+"' ";
			}
			if(jsonData.getActionDoneOnCatNum().equalsIgnoreCase("Yes")){
				sql += " and `CATELOG_NUMBER` = '"+jsonData.getCatNum()+"' ";
			}
			sql += " and `DIVISION_CODE` in (:divisionCodeList) ";
			//sql += " LIMIT 100 ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("divisionCodeList", divisionCodeList);
			List<String> result = query.getResultList();
			for(String matDesc : result){
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(matDesc);
				paramObj.setParamDesc(matDesc+" ");
				paramList.add(paramObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<String> getDivisionCodeList(SOAPRequest jsonData) {
		try {
			/*String sql = "SELECT DISTINCT `DIVISION`  FROM `Document_Type` WHERE `SALE_ORG` = '"+jsonData.getSalesOrg()+"' "
					+ "AND `DISTRIBUTION_CHANNEL` = '"+jsonData.getDistributionChannel()+"' ";*/
			
			String sql = "select DISTINCT `Division_Code` from `Division_Payment_Terms` "
					+ "where `Division_Name` = '"+jsonData.getDivisionName()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> prepareCatelogNumList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			List<String> divisionCodeList = getDivisionCodeList(jsonData);
			
			String sql = "SELECT DISTINCT `CATELOG_NUMBER` FROM `All_Products` "
					+ "where `CREATE_DATE` is not null and `MAT_STS_DC_X` = '' ";
			if(jsonData.getActionDoneOnMatCode().equalsIgnoreCase("Yes")){
				sql += " and `PRODUCT_CODE` = '"+jsonData.getMatCode()+"' ";
			}
			if(jsonData.getActionDoneOnMatDesc().equalsIgnoreCase("Yes")){
				sql += " and `PRODUCT_NAME_SALEABLE_ITEM` = '"+jsonData.getMatDesc()+"' ";
			}
			sql += " and `DIVISION_CODE` in (:divisionCodeList) ";
			//sql += " LIMIT 100 ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("divisionCodeList", divisionCodeList);
			List<String> result = query.getResultList();
			for(String catNum : result){
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(catNum);
				paramObj.setParamDesc(catNum+" ");
				paramList.add(paramObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> prepareMaterialCodeList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			List<String> divisionCodeList = getDivisionCodeList(jsonData);
			
			String sql = "SELECT DISTINCT `PRODUCT_CODE` FROM `All_Products` "
					+ "where `CREATE_DATE` is not null and `MAT_STS_DC_X` = '' ";
			if(jsonData.getActionDoneOnMatDesc().equalsIgnoreCase("Yes")){
				sql += " and `PRODUCT_NAME_SALEABLE_ITEM` = '"+jsonData.getMatDesc()+"' ";
			}
			if(jsonData.getActionDoneOnCatNum().equalsIgnoreCase("Yes")){
				sql += " and `CATELOG_NUMBER` = '"+jsonData.getCatNum()+"' ";
			}
			sql += " and `DIVISION_CODE` in (:divisionCodeList) ";
			//sql += " LIMIT 100 ";
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("divisionCodeList", divisionCodeList);
			List<String> result = query.getResultList();
			for(String matCode : result){
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(matCode);
				paramObj.setParamDesc(matCode+" ");
				paramList.add(paramObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	public Response<SOAPResponse> getOtherFieldData(SOAPRequest jsonData) {
		Response<SOAPResponse> response = new Response<SOAPResponse>();
		List<SOAPResponse> wrappedList = new ArrayList<SOAPResponse>();
		SOAPResponse wrappedObj = new SOAPResponse();
		try {
			List<SystemParamDTO> documentTypeList = prepareDocumentTypeList(jsonData);
			List<SystemParamDTO> plantList = preparePlantList(jsonData);
			List<SystemParamDTO> paymentTermList = preparePaymentTermList(jsonData);
			
			wrappedObj.setDocumentTypeList(documentTypeList);
			wrappedObj.setPlantList(plantList);
			wrappedObj.setPaymentTermList(paymentTermList);
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
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> preparePaymentTermList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			String sql = "SELECT DISTINCT `Payment_Term` FROM `Division_Payment_Terms` "
					//+ "where `Division_Code` = 'All' or `Division_Code` in (:diviList) ";
					+ "where `Division_Code` in (:diviList) ";
			List<String> diviList = CommonFunction.getListOfUniqueStringTokens(jsonData.getDivisionCode(), ",");
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("diviList", diviList);
			List<String> result = query.getResultList();
			if(result.size() !=0){
				for(String pt : result){
					List<String> ptList = CommonFunction.getListOfUniqueStringTokens(pt, ",");
					for(String obj : ptList){
						paramObj = new SystemParamDTO();
						paramObj.setParamCode(obj);
						paramObj.setParamDesc(obj+" ");
						paramList.add(paramObj);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> preparePlantList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			List<String> diviList = CommonFunction.getListOfUniqueStringTokens(jsonData.getDivisionCode(), ",");
			for(String diviCode : diviList){
				if(diviCode.equalsIgnoreCase("10") || diviCode.equalsIgnoreCase("11") || diviCode.equalsIgnoreCase("12") ||
					diviCode.equalsIgnoreCase("45") || diviCode.equalsIgnoreCase("46")){

					List<String> stateList = getStateByTerritoryCode(jsonData.getTerritoryCode());
					stateList.add("All");
					
					for(String st : stateList){
						String sql = "SELECT DISTINCT `Plant_Code`,`Plant` FROM `Plant_Division_Mapping` "
								+ "where find_in_set('"+st+"',`Type`) <> 0  ";
						
						Query query = getEm().createNativeQuery(sql);
						List<Object[]> result = query.getResultList();
						for(Object [] obj : result){
							String dc = obj[0] == null ? "" : String.valueOf(obj[0]);
							String dn = obj[1] == null ? "" : String.valueOf(obj[1]);
			 				
							paramObj = new SystemParamDTO();
							paramObj.setParamCode(dc);
							paramObj.setParamDesc(dn);
							paramList.add(paramObj);
						}
						
					}
				}
				else{
					String sql = "SELECT DISTINCT `Plant_Code`,`Plant` FROM `Plant_Division_Mapping` "
							+ "where find_in_set('"+diviCode+"',`Division_Code`) <> 0 ";
					
					Query query = getEm().createNativeQuery(sql);
					List<Object[]> result = query.getResultList();
					for(Object [] obj : result){
						String dc = obj[0] == null ? "" : String.valueOf(obj[0]);
						String dn = obj[1] == null ? "" : String.valueOf(obj[1]);
		 				
						paramObj = new SystemParamDTO();
						paramObj.setParamCode(dc);
						paramObj.setParamDesc(dn);
						paramList.add(paramObj);
					}
				}
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<String> getStateByTerritoryCode(String territoryCode) {
		try {
			String sql = "SELECT distinct `State` FROM `Territory_State_Mapping` where `Territory_Code` = '"+territoryCode+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<String> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	private List<SystemParamDTO> prepareDocumentTypeList(SOAPRequest jsonData) {
		List<SystemParamDTO> paramList = new ArrayList<SystemParamDTO>();
		SystemParamDTO paramObj = null;
		try {
			String sql = "SELECT DISTINCT `DOCUMENT_TYPE`,`DESCRIPTION` FROM `Document_Type` "
					+ "where `SALE_ORG` = '"+jsonData.getSalesOrg()+"' and `DIVISION` in (:diviList) ";
			
			List<String> diviList = CommonFunction.getListOfUniqueStringTokens(jsonData.getDivisionCode(), ",");
			Query query = getEm().createNativeQuery(sql);
			query.setParameter("diviList", diviList);
			List<Object[]> result = query.getResultList();
			for(Object [] obj : result){
				String dt = obj[0] == null ? "" : String.valueOf(obj[0]);
				String desc = obj[1] == null ? "" : String.valueOf(obj[1]);
 				
				paramObj = new SystemParamDTO();
				paramObj.setParamCode(dt);
				paramObj.setParamDesc(dt+"/"+desc);
				paramList.add(paramObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	@Transactional
	public Response<Map<String, String>> submitOrderBookingData(OrderBookingRequest jsonData) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		
		try {
			if(jsonData.getIsNewTransaction().equalsIgnoreCase("Y")){
				OrderBookingHdrModel h = new OrderBookingHdrModel();
				h.setTransactionId(jsonData.getTransactionId());
				h.setEmpId(jsonData.getEmpId());
				h.setTerritoryCode(jsonData.getTerritoryCode());
				h.setTerritoryName(jsonData.getTerritoryName());
				h.setDealerCode(jsonData.getDealerCode());
				h.setDealerName(jsonData.getDealerName());
				h.setDivisionName(jsonData.getDivisionName());
				h.setOrganizationId(jsonData.getOrgId());
				h.setOrganizationName(jsonData.getOrgName());
				h.setDistributionChannel(jsonData.getDisttibutionChannel());
				h.setDistributionChannelDesc(jsonData.getDisttibutionChannelDesc());
				h.setDocumentType(jsonData.getDocumentType());
				h.setPlant(jsonData.getPlant());
				h.setPayementTerm(jsonData.getPaymentTerm());
				h.setCreateDate(new Date());
				
				OrderBookingHdrModel h1 = (OrderBookingHdrModel) save(h);
				
				OrderBookingDetModel d = null;
				for(OrderBookingAddedItems items : jsonData.getAddItemsList()){
					d = new OrderBookingDetModel();
					d.setTransactionId(h1.getTransactionId());
					d.setMaterialDesc(items.getMaterialDesc());
					d.setCatelogNumber(items.getCatelogNumber());
					d.setMaterialCode(items.getMaterialCode());
					d.setDivisionCode(items.getDivisionCode());
					d.setQuantity(items.getQuantity());
					d.setCreateDate(new Date());
					save(d);
				}
			
			}
			else{
				String trId = jsonData.getTransactionId();
				if(jsonData.getAlreadyExistItemList().size() != 0){
					for(OrderBookingAddedItems items : jsonData.getAlreadyExistItemList()){
						String sql = "update `Order_Booking_Det` set"
								//+ " `Material_Desc` = '"+items.getMaterialDesc()+"',"
								//+ " `Catelog_Number` = '"+items.getCatelogNumber()+"',"
								//+ " `Material_Code` = '"+items.getMaterialCode()+"',"
								//+ " `Division_Code` = '"+items.getDivisionCode()+"',"
								+ " `Quantity` = '"+items.getQuantity()+"' where `Transaction_Id` = '"+trId+"' and `Id` = '"+items.getId()+"' ";
						int c = getEm().createNativeQuery(sql).executeUpdate();
						System.out.println("update in det "+c);
					}
				}
				else{
					String delSql = "DELETE from `Order_Booking_Det` where `Transaction_Id` = '"+trId+"' ";
					int c = getEm().createNativeQuery(delSql).executeUpdate();
					System.out.println("delete in det "+c);
				}
				
				
				OrderBookingDetModel d = null;
				for(OrderBookingAddedItems items : jsonData.getNewAddItemsList()){
					d = new OrderBookingDetModel();
					d.setTransactionId(trId);
					d.setMaterialDesc(items.getMaterialDesc());
					d.setCatelogNumber(items.getCatelogNumber());
					d.setMaterialCode(items.getMaterialCode());
					d.setDivisionCode(items.getDivisionCode());
					d.setQuantity(items.getQuantity());
					d.setCreateDate(new Date());
					save(d);
				}
			}
			
			if(jsonData.getSubmitType().equalsIgnoreCase("place")){
				placeOrderBookingItems(jsonData,response);
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
	
	private void placeOrderBookingItems(OrderBookingRequest jsonData, Response<Map<String, String>> response){
		Map<String, String> errorsMap = new HashMap<String, String>();
		StringBuilder soapRequest = new StringBuilder();
		String soapResponse = null;
		try {
			List<OrderBookingAddedItems> orderItemList = new ArrayList<OrderBookingAddedItems>();
			if(jsonData.getAlreadyExistItemList() != null){
				for(OrderBookingAddedItems already : jsonData.getAlreadyExistItemList()){
					orderItemList.add(already);
				}
			}
			
			if(jsonData.getNewAddItemsList() != null){
				for(OrderBookingAddedItems newAdd : jsonData.getNewAddItemsList()){
					orderItemList.add(newAdd);
				}
			}
			
			
			if(jsonData.getIsNewTransaction().equalsIgnoreCase("N")){
				jsonData.setAddItemsList(orderItemList);
			}
			
			String startTime = MyCalendar.getCurrentTimestamp();
			String endTime = null;
			
			String headerData = "<DOC_TYPE>"+jsonData.getDocumentType()+"</DOC_TYPE>"
					+ "<SAL_ORG>"+jsonData.getOrgId()+"</SAL_ORG>"
					+ "<DIS_CHANL>"+jsonData.getDisttibutionChannel()+"</DIS_CHANL>"
					+ "<DIVISION>"+jsonData.getAddItemsList().get(0).getDivisionCode()+"</DIVISION>"
					+ "<CUSTOMER>"+jsonData.getDealerCode()+"</CUSTOMER>"
					+ "<PO_NO>"+jsonData.getTransactionId()+"</PO_NO>"
					+ "<PO_DATE></PO_DATE>";
			
			String itemData = "";
			for(OrderBookingAddedItems added : jsonData.getAddItemsList()){
				itemData += "<item>";
					itemData += "<MATERIAL>"+added.getMaterialCode()+"</MATERIAL>";
					itemData += "<PLANT>"+jsonData.getPlant()+"</PLANT>";
					itemData += "<ORD_QTY>"+added.getQuantity()+".000</ORD_QTY>";
				itemData += "</item>";
			}
			
			
			soapRequest.append("<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">"
					+ "<soap:Header/>"
					+ "<soap:Body>"
					+ "<urn:Z_SD_SF_SALES_ORD_CRT>"
					+ "");
			soapRequest.append("<I_HEADER>");
			soapRequest.append(headerData);
			soapRequest.append("</I_HEADER>");
			
			soapRequest.append("<I_ITEM>");
			soapRequest.append(itemData);
			soapRequest.append("</I_ITEM>");
			
			soapRequest.append("<I_SIMULATE></I_SIMULATE>");
			soapRequest.append("</urn:Z_SD_SF_SALES_ORD_CRT>"
					+ "</soap:Body>"
					+ "</soap:Envelope>");
			
			soapResponse = SOAP.orderBookingMethod(soapRequest.toString(),errorsMap);
			if(soapResponse != null){
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource src = new InputSource();
				src.setCharacterStream(new StringReader(soapResponse));
				Document doc = builder.parse(src);
				NodeList nd = doc.getElementsByTagName("E_SO_DETAILS");
				NodeList nd2 = doc.getElementsByTagName("RETURN");
				if(nd.getLength() != 0){
					String salDoc="",docDate="",netValue="",currency="",
							type="",id="",number="",message="",logNo="",logMsgNo="",messageV1="",messageV2="",messageV3="",messageV4="",
							parameter="",row="",field="",system="";
					for (int i = 0; i < nd.getLength(); i++) {
						Element node = (Element) nd.item(i);
						
						salDoc = node.getElementsByTagName("SAL_DOC").item(0).getTextContent();
						docDate = node.getElementsByTagName("DOC_DATE").item(0).getTextContent();
						netValue = node.getElementsByTagName("NET_VALUE").item(0).getTextContent();
						currency = node.getElementsByTagName("CURRENCY").item(0).getTextContent();
					}
					
					for (int i = 0; i < nd2.getLength(); i++) {
						Element node = (Element) nd2.item(i);
						
						type = node.getElementsByTagName("TYPE").item(0).getTextContent();
						id = node.getElementsByTagName("ID").item(0).getTextContent();
						number = node.getElementsByTagName("NUMBER").item(0).getTextContent();
						message = node.getElementsByTagName("MESSAGE").item(0).getTextContent();
						logNo = node.getElementsByTagName("LOG_NO").item(0).getTextContent();
						logMsgNo = node.getElementsByTagName("LOG_MSG_NO").item(0).getTextContent();
						messageV1 = node.getElementsByTagName("MESSAGE_V1").item(0).getTextContent();
						messageV2 = node.getElementsByTagName("MESSAGE_V2").item(0).getTextContent();
						messageV3 = node.getElementsByTagName("MESSAGE_V3").item(0).getTextContent();
						messageV4 = node.getElementsByTagName("MESSAGE_V4").item(0).getTextContent();
						parameter = node.getElementsByTagName("PARAMETER").item(0).getTextContent();
						row = node.getElementsByTagName("ROW").item(0).getTextContent();
						field = node.getElementsByTagName("FIELD").item(0).getTextContent();
						system = node.getElementsByTagName("SYSTEM").item(0).getTextContent();
					}
					
					String sql="update Order_Booking_Hdr set"
							+ " `SAL_DOC` = '"+salDoc+"',"
							+ " `DOC_DATE` = '"+docDate+"',"
							+ " `NET_VALUE` = '"+netValue+"', "
							+ " `CURRENCY` = '"+currency+"', "
							+ " `TYPE` = '"+type+"', "
							+ " `IDD` = '"+id+"', "
							+ " `NUMBER` = '"+number+"', "
							+ " `MESSAGE` = '"+message+"', "
							+ " `LOG_NO` = '"+logNo+"', "
							+ " `LOG_MSG_NO` = '"+logMsgNo+"', "
							+ " `MESSAGE_V1` = '"+messageV1+"', "
							+ " `MESSAGE_V2` = '"+messageV2+"', "
							+ " `MESSAGE_V3` = '"+messageV3+"', "
							+ " `MESSAGE_V4` = '"+messageV4+"', "
							+ " `PARAMETER` = '"+parameter+"', "
							+ " `ROW` = '"+row+"', "
							+ " `FIELD` = '"+field+"', "
							+ " `SYSTEM` = '"+system+"' "
							+ " where `Transaction_Id` = '"+jsonData.getTransactionId()+"' ";
					
					int count = getEm().createNativeQuery(sql).executeUpdate();
					System.out.println("update "+count);
					
					endTime = MyCalendar.getCurrentTimestamp();
					errorsMap.put("SUCCESS - "+jsonData.getTransactionId(), "Record : "+salDoc+", StartTime : "+startTime+", EndTime : "+endTime);
					
				}
				else{
					endTime = MyCalendar.getCurrentTimestamp();
					errorsMap.put("SALES_ORD_CRT", ReturnsCode.SOAP_API_NO_RECORD_FOUND+", StartTime : "+startTime+", EndTime : "+endTime);
				}
			}
			else{
				endTime = MyCalendar.getCurrentTimestamp();
				errorsMap.put("SALES_ORD_CRT", ReturnsCode.SOAP_API_ERROR+", StartTime : "+startTime+", EndTime : "+endTime);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorsMap.put("ERROR_IN_placeOrderBookingItems", e.toString());
		}
		
		response.setErrorsMap(errorsMap);
		response.setSoapApiRequest(soapRequest.toString());
		response.setSoapApiResponse(soapResponse);
		
	}

	@SuppressWarnings("unchecked")
	public Response<OrderBookingSummaryResponse> getOrderSummary(SOAPRequest jsonData) {
		Response<OrderBookingSummaryResponse> response = new Response<OrderBookingSummaryResponse>();
		List<OrderBookingSummaryResponse> wrappedList = new ArrayList<OrderBookingSummaryResponse>();
		OrderBookingSummaryResponse wrappedObj = null;
		try {
			String sql = "SELECT `Transaction_Id`, `Territory_Code`, `Territory_Name`, `Dealer_Code`, `Dealer_Name`,"
					+ " `Division_Name`, `Distribution_Channel`, `Distribution_Channel_Desc`, `Document_Type`, `Plant`,"
					+ " `Payment_Term`, `SAL_DOC`, `DOC_DATE`, `NET_VALUE`, `TYPE`,`Organization_Id`, "
					+ " `Create_Date` "
					+ " FROM `Order_Booking_Hdr` "
					+ " where `Transaction_Id` is not null "
					+ " and `Emp_Id` = '"+jsonData.getEmpId()+"' ";
			if(!jsonData.getDealerCode().equalsIgnoreCase("")){
				sql += "and `Dealer_Code` =  '"+jsonData.getDealerCode()+"' ";
			}
			if(!jsonData.getFromDate().equalsIgnoreCase("")){
				sql += "and date(`Create_Date`) >= '"+jsonData.getFromDate()+"' ";
			}
			if(!jsonData.getToDate().equalsIgnoreCase("")){
				sql += "and date(`Create_Date`) <=  '"+jsonData.getToDate()+"' ";
			}
			
			
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			for(Object [] obj : result){
				wrappedObj = new OrderBookingSummaryResponse();
				wrappedObj.setTransactionId(String.valueOf(obj[0]));
				wrappedObj.setTerritoryCode(obj[1] == null ? "" : String.valueOf(obj[1]));
				wrappedObj.setTerritoryName(obj[2] == null ? "" : String.valueOf(obj[2]));
				wrappedObj.setDealerCode(obj[3] == null ? "" : String.valueOf(obj[3]));
				wrappedObj.setDealerName(obj[4] == null ? "" : String.valueOf(obj[4]));
				wrappedObj.setDivisionName(obj[5] == null ? "" : String.valueOf(obj[5]));
				wrappedObj.setDistChannel(obj[6] == null ? "" : String.valueOf(obj[6]));
				wrappedObj.setDistChannelDesc(obj[7] == null ? "" : String.valueOf(obj[7]));
				wrappedObj.setDocumentType(obj[8] == null ? "" : String.valueOf(obj[8]));
				wrappedObj.setPlant(obj[9] == null ? "" : String.valueOf(obj[9]));
				wrappedObj.setPaymentTerm(obj[10] == null ? "" : String.valueOf(obj[10]));
				wrappedObj.setSalDoc(obj[11] == null ? "" : String.valueOf(obj[11]));
				wrappedObj.setDocDate(obj[12] == null ? "" : String.valueOf(obj[12]));
				wrappedObj.setNetValue(obj[13] == null ? "" : String.valueOf(obj[13]));
				wrappedObj.setType(obj[14] == null ? "" : String.valueOf(obj[14]));
				wrappedObj.setOrganizationId(obj[15] == null ? "" : String.valueOf(obj[15]));
				wrappedObj.setCreateDate(obj[16] == null ? "" : String.valueOf(obj[16]).replace(".0", ""));
				
				prepareOrderDet(wrappedObj);
				
				wrappedList.add(wrappedObj);
			}
			
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

	@SuppressWarnings("unchecked")
	private void prepareOrderDet(OrderBookingSummaryResponse wrappedObj) {
		List<OrderItemsList> orderItemsList = new ArrayList<OrderItemsList>();
		OrderItemsList oi = null;
		try {
			String sql = "select `id`,`Material_Desc`, `Catelog_Number`, `Material_Code`, `Division_Code`, `Quantity` from"
					+ " `Order_Booking_Det` where `Transaction_Id` = '"+wrappedObj.getTransactionId()+"' ";
			Query query = getEm().createNativeQuery(sql);
			List<Object[]> result = query.getResultList();
			for(Object [] obj : result){
				oi = new OrderItemsList();
				oi.setId(String.valueOf(obj[0]));
				oi.setMaterialDesc(obj[1] == null ? "" : String.valueOf(obj[1]));
				oi.setCatelogNumber(obj[2] == null ? "" : String.valueOf(obj[2]));
				oi.setMaterialCode(obj[3] == null ? "" : String.valueOf(obj[3]));
				oi.setDivisionCode(obj[4] == null ? "" : String.valueOf(obj[4]));
				oi.setQuantity(obj[5] == null ? "" : String.valueOf(obj[5]));
				orderItemsList.add(oi);
			}
			wrappedObj.setOrderItemsList(orderItemsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Transactional
	public Response<Map<String, String>> deleteTransactionAndDetails(OrderBookingRequest jsonData) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			if(jsonData.getTransactionDetId().equalsIgnoreCase("")){
				String sql = "delete from `Order_Booking_Hdr` where `Transaction_Id` = '"+jsonData.getTransactionId()+"' ";
				int del = getEm().createNativeQuery(sql).executeUpdate();
				System.out.println("del : "+del);
				if(del!=0){
					String sql1 = "delete from `Order_Booking_Det` where `Transaction_Id` = '"+jsonData.getTransactionId()+"' ";
					int del1 = getEm().createNativeQuery(sql1).executeUpdate();
					System.out.println("del1 : "+del1);
				}
			}
			else{
				String sql1 = "delete from `Order_Booking_Det` where `id` = "+jsonData.getTransactionDetId()+""
						+ " and `Transaction_Id` = '"+jsonData.getTransactionId()+"' ";
				int del1 = getEm().createNativeQuery(sql1).executeUpdate();
				System.out.println("del1 : "+del1);
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

	/*@Override
	public Response<Map<String, String>> importData(SOAPRequest jsonData) {
		Response<Map<String, String>> response = new Response<Map<String,String>>();
		try {
			DataFormatter formatter = new DataFormatter();
			FileInputStream fis = new FileInputStream(new File(jsonData.getExcelBase64Str()));
			XSSFWorkbook excelWorkBook = new XSSFWorkbook(fis);
		 	XSSFSheet exelWorkSheet = excelWorkBook.getSheetAt(0);
		 	int recordInExcel = exelWorkSheet.getLastRowNum();
		 	
		 	AllProductModel e = null;
		 	for (int count = 0; count <= recordInExcel; count++){
		 		Row row = exelWorkSheet.getRow(count);
		    	if(row.getRowNum() == 0){
		        	continue;
		        }
		    	e = new AllProductModel();
		    	e.setProductCode(formatter.formatCellValue(row.getCell(0)));
		    	e.setProductName(formatter.formatCellValue(row.getCell(1)));
		    	e.setProductGroupCode(formatter.formatCellValue(row.getCell(2)));
		    	e.setDivisionCode(formatter.formatCellValue(row.getCell(3)));
		    	e.setCatelogNumber(formatter.formatCellValue(row.getCell(4)));
		    	e.setSalesOrg(formatter.formatCellValue(row.getCell(5)));
		    	e.setDistributionChanel(formatter.formatCellValue(row.getCell(6)));
		    	e.setPlant(formatter.formatCellValue(row.getCell(7)));
		    	e.setStorageLocation(formatter.formatCellValue(row.getCell(8)));
		    	e.setStorageLocationDesc(formatter.formatCellValue(row.getCell(9)));
		    	e.setProfitCenter(formatter.formatCellValue(row.getCell(10)));
		    	e.setProfitCenterDesc(formatter.formatCellValue(row.getCell(11)));
		    	e.setProductModel(formatter.formatCellValue(row.getCell(12)));
		    	e.setProductModelDesc(formatter.formatCellValue(row.getCell(13)));
		    	e.setProductRange(formatter.formatCellValue(row.getCell(14)));
		    	e.setProductRangeDesc(formatter.formatCellValue(row.getCell(15)));
		    	e.setProductCategory(formatter.formatCellValue(row.getCell(16)));
		    	e.setProductCategoryDesc(formatter.formatCellValue(row.getCell(17)));
		    	e.setMaterialType(formatter.formatCellValue(row.getCell(18)));
		    	e.setMaterialTypeDesc(formatter.formatCellValue(row.getCell(19)));
		    	e.setSaleableItem(formatter.formatCellValue(row.getCell(20)));
		    	e.setProductNameSaleableItem(e.getProductName()+"_"+e.getSaleableItem());
		    	e.setBaseUom(formatter.formatCellValue(row.getCell(21)));
		    	e.setMatStsDiv(formatter.formatCellValue(row.getCell(22)));
		    	e.setMatStsDivDesc(formatter.formatCellValue(row.getCell(23)));
		    	e.setMatStsDc(formatter.formatCellValue(row.getCell(24)));
		    	e.setMatStsDcFrom(formatter.formatCellValue(row.getCell(25)));
		    	e.setMatStsDcX(formatter.formatCellValue(row.getCell(26)));
		    	e.setMatStsDcXfrom(formatter.formatCellValue(row.getCell(27)));
		    	e.setMatGroup1(formatter.formatCellValue(row.getCell(28)));
		    	e.setMatGroup1Desc(formatter.formatCellValue(row.getCell(29)));
		    	e.setMatGroup2(formatter.formatCellValue(row.getCell(30)));
		    	e.setMatGroup2Desc(formatter.formatCellValue(row.getCell(31)));
		    	e.setMatGroup3(formatter.formatCellValue(row.getCell(32)));
		    	e.setMatGroup3Desc(formatter.formatCellValue(row.getCell(33)));
		    	e.setMatGroup4(formatter.formatCellValue(row.getCell(34)));
		    	e.setMatGroup4Desc(formatter.formatCellValue(row.getCell(35)));
		    	e.setMatGroup5(formatter.formatCellValue(row.getCell(36)));
		    	e.setMatGroup5Desc(formatter.formatCellValue(row.getCell(37)));
		    	e.setBatchTick(formatter.formatCellValue(row.getCell(38)));
		    	e.setHsnCode(formatter.formatCellValue(row.getCell(39)));
		    	e.setMrpContr(formatter.formatCellValue(row.getCell(40)));
		    	e.setMrpContrDesc(formatter.formatCellValue(row.getCell(41)));
		    	e.setProcurementType(formatter.formatCellValue(row.getCell(42)));
		    	e.setIsActive(1);
		    	e.setProductInsertDate("2020-08-19");
		    	e.setCreateDate(new Date());
		    	save(e);
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
	}*/
}
