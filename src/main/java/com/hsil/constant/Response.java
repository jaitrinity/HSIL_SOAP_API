package com.hsil.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response<T> {
	private List<T> wrappedList = new ArrayList<T>();
	private Map<String, String> errorsMap = new HashMap<String, String>();
	private String responseCode;
	private String responseDesc;
	private String soapApiRequest;
	private String soapApiResponse;
	private Long count;

	public Response() {
	}

	public Response(List<T> wrappedList) {
		this.wrappedList = wrappedList;
	}

	public Response(List<T> wrappedList, Map<String, String> errorsMap) {
		this.wrappedList = wrappedList;
		this.errorsMap = errorsMap;
	}

	public Response(List<T> wrappedList, Map<String, String> errorsMap, String responseCode, String responseDesc) {
		this.wrappedList = wrappedList;
		this.errorsMap = errorsMap;
		this.responseCode = responseCode;
		this.responseDesc = responseDesc;
	}

	public List<T> getWrappedList() {
		return this.wrappedList;
	}

	public void setWrappedList(List<T> wrappedList) {
		this.wrappedList = wrappedList;
	}

	public Map<String, String> getErrorsMap() {
		return this.errorsMap;
	}

	public void setErrorsMap(Map<String, String> errorsMap) {
		this.errorsMap = errorsMap;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return this.responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getSoapApiResponse() {
		return soapApiResponse;
	}

	public void setSoapApiResponse(String soapApiResponse) {
		this.soapApiResponse = soapApiResponse;
	}

	public String getSoapApiRequest() {
		return soapApiRequest;
	}

	public void setSoapApiRequest(String soapApiRequest) {
		this.soapApiRequest = soapApiRequest;
	}
}
