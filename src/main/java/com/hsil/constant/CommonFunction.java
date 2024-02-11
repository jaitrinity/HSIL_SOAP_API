package com.hsil.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.codehaus.jackson.map.ObjectMapper;

public class CommonFunction {
	public static List<String> getListOfUniqueStringTokens(String str, String del) {
		String[] strArray = str.split(del);
		List<String> list = new ArrayList<String>();
		for (String s : strArray) {
			if (null != s && (!s.equalsIgnoreCase("")) && (!list.contains(s.trim()))) {
				list.add(s.trim());
			}
		}
		return list;
	}
	
	public static String printResponseJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;

	}
	
	public static String removePrefixZero(String str) 
    { 
        // Count leading zeros 
        int i = 0; 
        while (i < str.length() && str.charAt(i) == '0') 
            i++; 
  
        // Convert str into StringBuffer as Strings 
        // are immutable. 
        StringBuffer sb = new StringBuffer(str); 
  
        // The  StringBuffer replace function removes 
        // i characters from given index (0 here) 
        sb.replace(0, i, ""); 
  
        return sb.toString();  // return in String 
    }
	
	public static String readTextFile(String txtFileName){
		String filePath = Constant.DAILY_REPORT_FOLDER;
		String fileName = txtFileName;
		String line = null;
		try {
			File file = new File(filePath+fileName);
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        
	        //System.out.println("Reading text file using FileReader");
	        /*while((line = br.readLine()) != null){
	            //process the line
	            System.out.println(line);
	        }*/
	        line = br.readLine();
	        br.close();
	        fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return line;
	}
	
	/*public static String convertExcelBase64ToFile(String operator, String base64Code, String attachType){
		String ext = null;
		String base64 [] = base64Code.split(",");
		String fileName = "Upload_"+operator+"_"+attachType;
		try {
			switch (base64[0] ) {//check image's extension
				case "data:application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;base64":
	            	ext = ".xlsx";
	                break;
	            default://should write cases for more images types
	            	ext = ".xlsx";
	                break;
	        }
			fileName += ext;
			byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64[1]);
			String imgPath = Constant.DAILY_REPORT_FOLDER+fileName;
			//String imgPath = "D:/jpm/myFolder/" + fileName;
			writeFile(imageBytes, imgPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String url = Constant.DAILY_REPORT_FOLDER + fileName;
		//String url = "D:/jpm/myFolder/" + fileName;
		return url;
	}
	
	
	private static void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		Runtime.getRuntime().exec("chmod 664 -R "+filename+"");
		fop.flush();
		fop.close();
	}*/
	
	public static String readJsonFile(String fileName){
		String filePath = Constant.DAILY_REPORT_FOLDER;
		String lines = "";
		try {
			File file = new File(filePath+fileName);
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        
	        //System.out.println("Reading text file using FileReader");
	        String line = "";
	        while((line = br.readLine()) != null){
	            //process the line
	        	lines += line;
	            //System.out.println(line);
	        }
	        
	        br.close();
	        fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return lines;
	}
}
