package com.versionone.selenium;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Collection of static date parsing functions.
 */
public class V1DateParser {

	/**
	 * Converts a Java Date to a short date format like "mm/dd/yyyy".
	 *
	 * @param dateToParse
	 *            The Date value to parse.
	 * @return String in the "mm/dd/yyyy" format.
	 */
	public static String getShortDateStringFromDate(Date dateToParse) {
		if (null == dateToParse) return "";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(dateToParse);
	}

	/**
	 * Converts a string with time to a Java Date format like "mm/dd/yyyy" with no time.
	 *
	 * @param dateToParse
	 *            The string value to parse.
	 * @return Date in the "mm/dd/yyyy" format.
	 * @throws ParseException
	 */
	public static Date getShortDateFromStringWithTime(String dateToParse) throws ParseException {
		if (null == dateToParse) return null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDateString = dateToParse.substring(0, dateToParse.indexOf("T"));
		return formatter.parse(formattedDateString);
	}
	
	/**
	 * Converts a string with time to a Java Date format like "mm/dd/yyyy" with no time adjusting by the specified days.
	 * 
	 * @param dateToParse
	 * @param daysToAdjust
	 * @return Date
	 */
	public static Date getShortDateFromStringWithTimeAdjustedByDay(String dateToParse, int daysToAdjust) {
		if (null == dateToParse) return null;
		DateTime currentDate = new DateTime(dateToParse);
		return currentDate.plusDays(daysToAdjust).toDate();
	}
	
	/**
	 * Converts a Date with time to a string format like "mm/dd/yyyy" with no time adjusting by the specified days.
	 * 
	 * @param dateToParse
	 * @param daysToAdjust
	 * @return String
	 */
	public static String getShortDateStringFromDateWithTimeAdjustedByDay(Date dateToParse, int daysToAdjust) {
		if (null == dateToParse) return "";
		DateTime currentDate = new DateTime(dateToParse);
		String formatter = "MM/dd/yyyy";
		return currentDate.plusDays(daysToAdjust).toString(formatter);
	}

	/**
	 * Converts a string to a Date with a given format.
	 *
	 * @param date
	 * @param format
	 * @return DateTime object.
	 * @throws ParseException
	 */
	public static DateTime parseDate(String date, String format) throws ParseException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(format).withOffsetParsed().withZoneUTC();
		
		// Adding "withOffsetParsed()" means "set new DateTime's time zone offset to match input string".
		DateTime dateTime = formatter.withOffsetParsed().parseDateTime(date);
		return dateTime;
	}

}
