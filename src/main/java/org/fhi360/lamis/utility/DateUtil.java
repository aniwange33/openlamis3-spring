/**
 *
 * @author aalozie
 */
package org.fhi360.lamis.utility;

import org.joda.time.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateUtil {

    public static java.time.LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date addYearMonthDay(Date currentDate, int number, String unit){
        Date date = null;
        Calendar calendar = Calendar.getInstance();   
        calendar.setTime(currentDate);
        if(unit.equalsIgnoreCase("year(s)") || unit.equalsIgnoreCase("YEAR")) {
            calendar.add(Calendar.YEAR, number);               
        }
        else {
            if(unit.equalsIgnoreCase("month(s)") || unit.equalsIgnoreCase("MONTH")) {
                calendar.add(Calendar.MONTH, number);                               
            }
            else {
                if(unit.equalsIgnoreCase("day(s)") || unit.equalsIgnoreCase("DAY")) {
                    calendar.add(Calendar.DATE, number);                                       
                }
            }
        }
        try {
            date = calendar.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }  

    public static Date addYear(Date currentDate, int number){
        Date date = null;
        Calendar calendar = Calendar.getInstance();   
        calendar.setTime(currentDate);
        calendar.add(Calendar.YEAR, number);                               
        try {
            date = calendar.getTime();     //date = (Date) dateFormat.parse(dateFormat.format(calendar.getTime()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }  

    public static Date addMonth(Date currentDate, int number){
        Date date = null;
        Calendar calendar = Calendar.getInstance();   
        calendar.setTime(currentDate);
        calendar.add(Calendar.MONTH, number);                               
        try {
            date = calendar.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }  
    
    public static Date addDay(Date currentDate, int number){
        Date date = null;
        Calendar calendar = Calendar.getInstance();   
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, number);                               
        try {
            date = calendar.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }  

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }


    public static java.time.LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    public static Map getAge(Date dateBirth, Date dateRef) {
        Map<String, Object> map = new HashMap<String, Object>();
        LocalDate birthDate = new LocalDate(formatDate(dateBirth, "MM/dd/yyyy"));
        LocalDate refDate = new LocalDate(formatDate(dateRef, "MM/dd/yyyy"));       
        Period period = new Period(birthDate, refDate, PeriodType.yearMonthDay());

        int age = 0;
        String ageUnit = "";
        if(period.getYears() != 0) {
           age = period.getYears();
           ageUnit = "Year(s)";
        }
        else {
            if(period.getMonths() != 0) {
               age = period.getMonths();
               ageUnit = "Month(s)";
            }
            else {
               age = period.getDays();                
               ageUnit = "Days(s)";
            }          
        }
        map.put("age", age);
        map.put("ageUnit",ageUnit); 
        System.out.println("Age" + age + ".."+ageUnit);
        return map;    
    }
    
    public static int getMonth(String month) {
        int number = 0;
        month = month.toUpperCase();
        if(month.equals("JANUARY")) {
            number = 1;
        }
        if(month.equals("FEBRUARY")) {
            number = 2;
        }
        if(month.equals("MARCH")) {
            number = 3;
        }
        if(month.equals("APRIL")) {
            number = 4;
        }
        if(month.equals("MAY")) {
            number = 5;
        }
        if(month.equals("JUNE")) {
            number = 6;
        }
        if(month.equals("JULY")) {
            number = 7;
        }
        if(month.equals("AUGUST")) {
            number = 8;
        }
        if(month.equals("SEPTEMBER")) {
            number = 9;
        }
        if(month.equals("OCTOBER")) {
            number = 10;
        }
        if(month.equals("NOVEMBER")) {
            number = 11;
        }
        if(month.equals("DECEMBER")) {
            number = 12;
        }
        return number;
    }

    public static String getMonth(int i) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};        
        String string = "";
        
        if(i > 0 && i <= 12) {
            string =  months[i-1];
        }
        return string;
    }
    
    public static Date getLastDateOfMonth(int year, int month) {
        Date date = null;
        Calendar calendar = new GregorianCalendar(year, month - 1, Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        try {
            date = calendar.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }
    
    public static Date getFirstDateOfMonth(int year, int month) {
        Date date = null;
        Calendar calendar = new GregorianCalendar(year, month - 1, Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));        
        try {
            date = calendar.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return date;
    }

    public static Date parseStringToDate(String dateString, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            if(!dateString.equals("") && !dateString.isEmpty()) {
                date = dateFormat.parse(dateString);
            }
        } catch (ParseException ignored) {
        }
        return date;
    }
    
    public static java.sql.Date parseStringToSqlDate(String dateString, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            if(!dateString.equals("") && !dateString.isEmpty()) {
                date = (Date) dateFormat.parse(dateString);                 
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new java.sql.Date(date.getTime());
    }
    public static Date convertToDateViaSqlDate(java.time.LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String parseDateToString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = "";
        try {
            dateString = (date == null? "" : dateFormat.format(date));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return dateString;
    }
    
    public static String getDateAsString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
        return dateFormat.format(cal.getTime());
    }
    
    //format a date from one date format to another and return a java date
    public static Date formatDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date datefmt = null;
        try {           
            String dateString = dateFormat.format(date);
            datefmt = (Date) dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return datefmt;
    }

    //format a date from one date format to another and return a sql date
    public static java.sql.Date formatSqlDate(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date datefmt = null;
        try {           
            String dateString = dateFormat.format(date);
            datefmt = (Date) dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(datefmt.getTime());
    }
    
    //change a date string from one date format to another
    public static String formatDateString(String dateString, String startFormat, String endFormat) {
        DateFormat dateFormat = new SimpleDateFormat(startFormat);   
        try {           
            //parse the date string into Date object
            Date date = dateFormat.parse(dateString);
            dateFormat = new SimpleDateFormat(endFormat);
            //format the date into another format
            dateString = dateFormat.format(date);
                                     
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    //format a date string from one date format to another and return a java date
    public static Date formatDateStringToDate(String dateString, String startFormat, String endFormat) {
        Date date = null;
        try {           
            DateFormat dateFormat = new SimpleDateFormat(startFormat);
            //DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"); //eg String dateStr = "Tue Sep 23 16:47:00 WAT 2014";

            //parse the date string into Date object
            date = dateFormat.parse(dateString);
            dateFormat = new SimpleDateFormat(endFormat);
                                     
            //format the date into another format
            dateString = dateFormat.format(date);
            date = (Date) dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //format a date string from one date format to another and return a sql date
    public static java.sql.Date formatDateStringToSqlDate(String dateString, String startFormat, String endFormat) {
        Date date = null;
        try {           
            DateFormat dateFormat = new SimpleDateFormat(startFormat);
            //DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"); //eg String dateStr = "Tue Sep 23 16:47:00 WAT 2014";

            //parse the date string into Date object
            date = dateFormat.parse(dateString);
            dateFormat = new SimpleDateFormat(endFormat);
                                     
            //format the date into another format
            dateString = dateFormat.format(date);
            date = (Date) dateFormat.parse(dateString);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(date.getTime());
    }
       
    public static String getDateWords(Date date) {
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String dateWords = "";
        try {
            String dateString = (date == null? "" : dateFormat.format(date));
            if(!dateString.isEmpty()) {
                int i = Integer.parseInt(dateString.substring(0,2));
                if(i > 0 && i < 12) {
                    dateWords = dateString.substring(3,5) + " " + months[i-1] + " " + dateString.substring(6);            
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return dateWords;
    }

   public static boolean isBetween(Date date, Date dateBegin, Date dateEnd) {
       return !date.before(dateBegin) && !date.after(dateEnd); //includes the end points
       //return date.after(dateBegin) && date.before(dateEnd); //excludes the end points
   }


    public static int daysBetween(Date startDate, Date endDate) {
        DateMidnight theStartDate = new DateMidnight(startDate);
        DateMidnight theEndDate = new DateMidnight(endDate);
        Days d = Days.daysBetween(theStartDate, theEndDate);
        return d.getDays();
    }
	
    public static int monthsBetweenIgnoreDays(Date startDate, Date endDate) {
        LocalDate start = new LocalDate(startDate);
        LocalDate end = new LocalDate(endDate);
        return Months.monthsBetween(start, end).getMonths();
    }

    public static int yearsBetweenIgnoreDays(Date startDate, Date endDate) {
        LocalDate start = new LocalDate(startDate);
        LocalDate end = new LocalDate(endDate);
        return Years.yearsBetween(start, end).getYears();
    }

    public static String getReportingPeriodAsString(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        DateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
        return dateFormat.format(cal.getTime());
    }
    
    public static Date getDate(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Map<String, Object> getPeriod(Date date, int increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, increment);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        DateFormat dateFormat = new SimpleDateFormat("MMM ''yy");
        String periodLabel = dateFormat.format(cal.getTime());

        Map<String, Object>map = new HashMap<String, Object>();
        map.put("year", year);
        map.put("month", month+1);
        map.put("periodLabel", periodLabel);
        return map;
    }
    
    //Utility methods to format Date to XMLGregorianCalendar
    public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    public static XMLGregorianCalendar getXmlDateTime(Date date) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date));
    }
    
    public static Timestamp getSqlTimeStamp(String time) {
        Timestamp timeStampDate = null;
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = (Date) formatter.parse(time);
            timeStampDate = new Timestamp(date.getTime());
            System.out.println(timeStampDate);

        } catch (ParseException e) {
          System.out.println("Exception :" + e);
        }
        return timeStampDate;
    }
    
    
    //Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    //Convert Calendar to Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    //Get week of the year from date
    public static int getWeekYear(Date date) {
        int week= 0;
        try{
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            week = cal.get(Calendar.WEEK_OF_YEAR);  
        } catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        
        return week;
    }

    public static Date getWeekStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        try{
             calendar = dateToCalendar(date);

            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DATE, -1);
            }
        } catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return calendar.getTime();
    }

    public static Date getWeekEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        try{
            calendar = dateToCalendar(date);

            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DATE, 1);
            }
            
        } catch(Exception e){
            System.out.println("Exception: " + e);
        }
        return calendar.getTime();
    }
    
}
