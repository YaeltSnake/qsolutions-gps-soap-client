package com.qsolutions.gpsclient;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

public class DateUtils {
    // Recibira año, mes, dia, hora, minuto, seguni
    public static XMLGregorianCalendar buiXMLGregorianCalendar(
            int year, int month, int day,
            int hour, int minute, int second){
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.set(year, month - 1, day, hour, minute, second);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
    }
    
    
}
