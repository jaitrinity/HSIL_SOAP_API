package com.hsil.constant;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

public class SOAP {
	/*private static final String AUTHENTICATE = "sfcomuser:Welcome@123";*/
	
	/*private static final String AUTHENTICATE = "sfcomuser:Welcome123";*/
	
	private static final String AUTHENTICATE = "sfcomuser:Welcome@123";
	private static final String BASIC_AUTH = "Basic " + new String(Base64.getEncoder().encode(AUTHENTICATE.getBytes()));
	
	public static String method(String soapRequest,Map<String, String> successMap){
		String soapResponse = null;
		try {
			//String URL = "http://hsildvr.hindwarebathrooms.com:8000/sap/bc/srt/rfc/sap/zsd_sf/100/zsd_sf/zsd_sf";
			//String URL = "http://hsilqar.hindwarebathrooms.com:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf/zsd_sf";
			//String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_wf/zsd_wf";
			//String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new/zsd_sf_new";
			//String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new1/zsd_sf_new1"; 
			//String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new2/zsd_sf_new2";
			//String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new3/zsd_sf_new3"; 
			String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new4/zsd_sf_new4"; // prod WSDL
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			con.setRequestProperty("Authorization", BASIC_AUTH);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(soapRequest);
			wr.flush();
			wr.close();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
			//System.out.println(soapResponse);
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("SOAP_API", e.toString());
		}
		return soapResponse;
	}
	
	public static String empIntegration(int dataType, String date,Map<String, String> errorsMap){
		// dataType = 0 for all emp data
		// dataType = 1, for add  employee respective date
		// dataType = 2, for update employee respective date
		// date = ddMMyyyy, ex if 13-Fed-2020 then date = 13022020 
		String soapResponse = null;
		try {
			String apiKey = "9061915e-fc18-48f5-ac85-65b2febbf3bd";
			String url = "https://www.myemploywise.com/asperm/services/employees?api_key="+apiKey+"&data_type="+dataType;
			if(dataType == 1 || dataType == 2){
				url += "&start="+date;
			}
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setDoInput(true);
		    con.setDoOutput(true);
		    con.setRequestMethod("GET");
		    con.connect();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
			//System.out.println(soapResponse);
		} catch (Exception e) {
			e.printStackTrace();
			errorsMap.put("EMPLOYEEWISE_SOAP_API_ERROR", e.toString());
		}
		return soapResponse;
	}
	
	public static String empAttendanceIntigration(String soapRequest,Map<String, String> errorMap){
		String soapResponse = null;
		try {
			String apiKey = "9acd6012-0144-4a9e-85cb-60e0e62a62a4";
			String url = "https://www.myemploywise.com/asperm/services/attendance?api_key="+apiKey;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/xml"); // Request
			con.setRequestProperty("Content-Type","application/xml"); // Response
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(soapRequest);
			wr.flush();
			wr.close();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
			//System.out.println(soapResponse);
		} catch (Exception e) {
			e.printStackTrace();
			errorMap.put("ATTENDANCE_SOAP_API_ERROR", e.toString());
		}
		return soapResponse;
		
	}
	
	/*public static String soapMethod(String soapRequest){
		String soapResponse = null;
		try {
			String URL = "http://hsprdci.hsil.in:8000/sap/bc/srt/rfc/sap/zsd_sf/300/zsd_sf_new/zsd_sf_new";
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			con.setRequestProperty("Authorization", BASIC_AUTH);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(soapRequest);
			wr.flush();
			wr.close();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
			//System.out.println(soapResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return soapResponse;
	}*/
	
	public static String orderBookingMethod(String soapRequest,Map<String, String> successMap){
		String soapResponse = null;
		try {
			String URL = "http://hsildvr.hindwarebathrooms.com:8000/sap/bc/srt/rfc/sap/zsales_force_mobile_app/100/zsales_order/zsales";
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
			con.setRequestProperty("Authorization", BASIC_AUTH);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(soapRequest);
			wr.flush();
			wr.close();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("SOAP_ORDER_BOOKING_API", e.toString());
		}
		return soapResponse;
	}
	
	public static String whatsAppApi(String soapRequest, Map<String, String> successMap){
		String soapResponse = null;
		try {
			String URL = "https://whatsapp.pinnacle.in/wamessage/v1/send";
			String whatsAppKey = "6e59cb76-d4d0-47fd-90b6-71dabb7f5bba";
			URL obj = new URL(URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type","application/json; charset=utf-8");
			con.setRequestProperty("apikey", whatsAppKey);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(soapRequest);
			wr.flush();
			wr.close();
			String responseStatus = con.getResponseMessage();
			System.out.println(responseStatus);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer sbresponse = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				sbresponse.append(inputLine);
			}
			in.close();
			soapResponse = sbresponse.toString();
		} catch (Exception e) {
			e.printStackTrace();
			successMap.put("WhatsApp_API", e.toString());
		}
		return soapResponse;
	}
	
	

}
