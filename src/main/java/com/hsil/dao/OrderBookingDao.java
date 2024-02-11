package com.hsil.dao;

import java.util.Map;

import com.hsil.constant.Response;
import com.hsil.dto.SystemParamDTO;
import com.hsil.request.OrderBookingRequest;
import com.hsil.request.SOAPRequest;
import com.hsil.response.OrderBookingAddedItems;
import com.hsil.response.OrderBookingSummaryResponse;
import com.hsil.response.SOAPResponse;

public interface OrderBookingDao {
	Response<SystemParamDTO> getDealerByTerritoryCode(SOAPRequest jsonData);

	Response<SOAPResponse> getProductDetails(SOAPRequest jsonData);

	Response<SOAPResponse> getOtherFieldData(SOAPRequest jsonData);

	Response<Map<String, String>> submitOrderBookingData(OrderBookingRequest jsonData);

	Response<OrderBookingSummaryResponse> getOrderSummary(SOAPRequest jsonData);

	Response<Map<String, String>> deleteTransactionAndDetails(OrderBookingRequest jsonData);

	//Response<Map<String, String>> importData(SOAPRequest jsonData);

}
