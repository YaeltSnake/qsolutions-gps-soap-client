package com.qsolutions.gpsclient.util;

import java.time.LocalDateTime;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

/**
 * Utility class for building XMLGregorianCalendar instances
 * required by the QSolutions SOAP service.
 *
 * Provides both manual date construction and automatic
 * timestamp generation for real-time GPS pulse reporting.
 */
public class DateUtils {

    /**
     * Builds an XMLGregorianCalendar from explicit date/time values.
     * Useful for testing with fixed dates.
     */
    public static XMLGregorianCalendar buildXMLGregorianCalendar(
            int year, int month, int day,
            int hour, int minute, int second) {
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.set(year, month - 1, day, hour, minute, second);
            gcal.set(GregorianCalendar.MILLISECOND, 0);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (Exception e) {
            System.err.println("[DateUtils] Error building calendar: " + e.getMessage());
            return null;
        }
    }

    /**
     * Builds an XMLGregorianCalendar from the current system time.
     * This is what should be used in production for real-time GPS pulses.
     */
    public static XMLGregorianCalendar now() {
        LocalDateTime current = LocalDateTime.now();
        return buildXMLGregorianCalendar(
                current.getYear(),
                current.getMonthValue(),
                current.getDayOfMonth(),
                current.getHour(),
                current.getMinute(),
                current.getSecond()
        );
    }
}
