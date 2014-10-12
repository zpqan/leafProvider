package com.leaf.leafData.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LeafTimeUtil {
	public static String getCurrentTime() {
		return getPastTime(0);
		
	}
	public static String getPreviousDay() {
		return getPastTime(3600*24);
	}
	public static String getPastTime(long seconds) {
		SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+4"));
		String s = gmtDateFormat.format(new Date(System.currentTimeMillis()- seconds*1000));
		//Current Date Time in GMT
		System.out.println("Current Date and Time in GMT time zone: " + s);
		return s;	
	}
	
	
}
