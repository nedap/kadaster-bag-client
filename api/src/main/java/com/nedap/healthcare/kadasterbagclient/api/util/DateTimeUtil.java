package com.nedap.healthcare.kadasterbagclient.api.util;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtil {

    /**
     * Return date formatter for kadaster bag service.
     * 
     * @return date formatter for pattern 'yyyyMMddhhmmss' and 'UTC' locale
     */
    private static DateTimeFormatter getDateFormater() {

        return DateTimeFormat.forPattern("yyyyMMdd").withLocale(new Locale("UTC"));
    }

    /**
     * Parse dateTime using format 'yyyyMMddhhmmss' and 'UTC' locale
     * 
     * @param dateTime
     *            string date time
     * @return parsed DateTime object
     */
    public static DateTime parse(final String dateTime) {
        return DateTime.parse(dateTime.substring(0, 8), getDateFormater());
    }
}
