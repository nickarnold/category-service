package com.levelsbeyond.category.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Function;

public class ViewUtils {
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	private static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM-dd-yyyy hh:mm a z");
	
	private static Function<Date, String> formatDate = new Function<Date, String>() {
		public String apply(Date date) {
			return (date != null ? DATE_FORMAT.format(date) : null);
		}
	};

	private static Function<String, String> formatDateTime = new Function<String, String>() {
		public String apply(String date) {
			return (date != null ? DATE_TIME_FORMAT.format(date) : null);
		}
	};
	
	public static Function<Date, String> formatDate() {
		return formatDate;
	}
	
	public static Function<String, String> formatDateTime() {
		return formatDateTime;	
	}

}
