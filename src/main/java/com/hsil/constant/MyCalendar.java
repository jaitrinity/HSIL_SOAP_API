package com.hsil.constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyCalendar {
	
	public static String getCurrentTimestamp()
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
		Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static int getYearFromCurrentDate(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		return year;
	}
	
	public static int getMonthNumberFromCurrentDate(){
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(java.util.Calendar.MONTH);
		return month+1;
	}
	
	public static String getMonthFromCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("MMM");
		Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getCurrentDateForExcel(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getCurrentDateInDdMMyyyy(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
    	return dateFormat.format(date);
	}
	
	public static String getFirstDateOfCurrentMonth(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date date = cal.getTime();
		return dateFormat.format(date);
	}
	
	public static String getPreviousMonthFirstDateInStr(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH,-1);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		Date firstDateOfPreviousMonth = (Date) aCalendar.getTime();
		return dateFormat.format(firstDateOfPreviousMonth);
	}
	
	public static String getEightDateOfMonthInStr(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.DATE, 8);
		Date eightDateOfMonth = (Date) aCalendar.getTime();
		return dateFormat.format(eightDateOfMonth);
	}
	
	/* 
	 * it return +value if date1 > date2,
	 * it return -value if date1 < date2,
	 * it return 0 if both date in equal........... */
	public static int compareTwoStrDate(String date1, String date2){
		return date1.compareTo(date2);
	}
	
	public static Date convertStrDateToUtilDate(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dates = new Date();
		try {
			dates = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dates;
	}
	
	public static String getPreviousDateFromCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	public static String getPreviousDateFromCurrentDateInDdMmYyyy(){
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	public static String getNextDateAsGivenDate(String date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(convertStrDateToUtilDate(date));
		cal.add(Calendar.DATE, 1);
		return dateFormat.format(cal.getTime());
	}
	
	/* date2 is greater than date1 */
	public static long getNoOfDaysBetweenTwoStrDates(String date1, String date2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(convertStrDateToUtilDate(date1));
		cal2.setTime(convertStrDateToUtilDate(date2));
		
		long miliSecondForDate1 = cal1.getTimeInMillis();
		long miliSecondForDate2 = cal2.getTimeInMillis();
		
		long diffInMilis = miliSecondForDate2 - miliSecondForDate1;
		
		long diffDays = diffInMilis / (24 * 60 * 60 * 1000);
		return diffDays;
	}
	
	public static Date convertStrDateToUtilDateForDoj(String date){
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date outputDate = null;
		try {
			// converting the input string in date
			Date varDate = inputFormat.parse(date);
			// changing the format of date and storing it in string
			String formateDate = outputFormat.format(varDate);
			// converting string date in date
			outputDate = outputFormat.parse(formateDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outputDate;
	}
	
	
}
