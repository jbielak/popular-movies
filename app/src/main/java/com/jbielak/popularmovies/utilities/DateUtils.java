package com.jbielak.popularmovies.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class for handling date conversions that are useful for  Popular Movies.
 */
public abstract class DateUtils {

    private static final String INCOMING_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DISPLAY_DATE_FORMAT = "dd-MM-yyyy";

    /**
     * Helper method to convert JSON string representation of the date into
     * Date object.
     *
     * @param date  String received in JSON
     * @return  Date object
     */
    public static Date getDate(String date) {
        Date parsedDate = null;
        DateFormat format = new SimpleDateFormat(INCOMING_DATE_FORMAT, Locale.getDefault());
        try {
            parsedDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDate;
    }

    /**
     * Helper method  to convert Date object to date string that will be displayed to user.
     *
     * @param date  Date object
     * @return A user friendly representation of date, eg "10-09-2014"
     */
    public static String getFriendlyDateString(Date date) {
        if (date != null) {
            DateFormat dateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
            return dateFormat.format(date);
        }
        return null;
    }


    /**
     * Given a Date object, return year string.
     *
     * @param date  Date object
     * @return Year
     */
    public static String getYearString(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return String.valueOf(cal.get(Calendar.YEAR));
        }
        return null;
    }
}
