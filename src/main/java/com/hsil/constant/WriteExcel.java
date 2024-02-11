package com.hsil.constant;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {
	public static String writeExcel(Map<String, Object[]> data,String fileName){
		 String returnString = null;
		//Blank workbook
       XSSFWorkbook workbook = new XSSFWorkbook(); 
        
       //Create a blank sheet
       XSSFSheet sheet = workbook.createSheet("Employee Data");
         
       //Iterate over data and write to sheet
       Set<String> keyset = data.keySet();
       int rownum = 0;
       for (String key : keyset)
       {
           Row row = sheet.createRow(rownum++);
           Object [] objArr = data.get(key);
           int cellnum = 0;
           for (Object obj : objArr)
           {
              Cell cell = row.createCell(cellnum++);
              if(obj instanceof String)
                   cell.setCellValue((String)obj);
               else if(obj instanceof Integer)
                   cell.setCellValue((Integer)obj);
           }
       }
       try
       {
           //Write the workbook in file system
           FileOutputStream out = new FileOutputStream(new File(fileName));
           workbook.write(out);
           out.close();
           returnString = fileName+" written successfully on disk.";
           //System.out.println(returnString);
       } 
       catch (Exception e) 
       {
           e.printStackTrace();
       }
       return returnString;
	}
	
	public static String writeAttendanceExcel(Map<String, Object[]> data,String fileName){
		String returnString = null;
		// Blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("Employee Data");

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (cellnum == 1 && rownum != 1) {
					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd").parse((String) obj);
						cell.setCellValue(date);
						cell.setCellStyle(cellStyle);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (rownum != 1 && (cellnum == 4 || cellnum == 5)) {
					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
					try {
						Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((String) obj);
						cell.setCellValue(date);
						cell.setCellStyle(cellStyle);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					cell.setCellValue((String) obj);
				}
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
			returnString = fileName + " written successfully.";
			// System.out.println(returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnString;
	}

}
