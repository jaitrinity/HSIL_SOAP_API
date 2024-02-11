package com.hsil.constant;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSMS {
	
	public static int sendSms(String message, String toMobile){
		int responseCode = 0;
		//String username = "trinitymobile"; 
		//String password = "123456";
		String apiKey = "ae6fa4-5cab56-4bc26d-caa56f-b27aab";
		String senderId = "TRIAPP";
		String messageType = "UNI";
		try{
			//String POST_URL = "http://www.smsjust.com/sms/user/urlsms.php?username="+username+"&pass="+password+"&senderid="+senderId+"&dest_mobileno="+toMobile+"&msgtype="+ messageType+"&message="+URLEncoder.encode(message, "UTF-8")+"&response=Y";
			String POST_URL = "http://www.smsjust.com/sms/user/urlsms.php?apikey="+apiKey+"&senderid="+senderId+"&dest_mobileno="+toMobile+"&msgtype="+ messageType+"&message="+URLEncoder.encode(message, "UTF-8")+"&response=Y";
			//System.out.println("POST_URL : "+POST_URL);
			URL url = new URL(POST_URL);
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    con.setDoInput(true);
		    con.setDoOutput(true);
		    con.setRequestMethod("POST");
		    con.connect();
		    responseCode = con.getResponseCode();
		    System.out.println("Conn status:- " +responseCode);
		}catch(Exception e){
			e.printStackTrace();
		}
		return responseCode;
	}

}
