/**
 *
 * @author aalozie
 */
package org.fhi360.lamis.utility;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChartUtil {
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ChartUtil() {
    }
    
    public String getReportingPeriodAsString(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        DateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy");
        return dateFormat.format(cal.getTime());
    }
    
    public Date getDate(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return cal.getTime();
    }

    public Map<String, Object> getPeriod(Date date, int increment) {
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
    
     public Map<String, Object> getDayPeriod(Date date, int increment) {
        
        Calendar cal = Calendar.getInstance();
       // cal.setTime(DateUtil.formatDate(date, "dd/MM/yyyy"));
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, increment);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DateFormat dateFormat = new SimpleDateFormat("MMM ''dd");
        String periodLabel = dateFormat.format(cal.getTime());

        Map<String, Object>map = new HashMap<String, Object>();
        map.put("year", year);
        map.put("month", month);
        map.put("day", day+1);
        map.put("periodLabel", periodLabel);
        return map;
    }
     

    public double getPercentage(String query) {
       double percentage  = 0.0;
       try {
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                int numerator = resultSet.getInt("numerator");
                int denominator = resultSet.getInt("denominator");
                percentage = numerator > 0 ? 100.0 * numerator / denominator : 0.0;
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return Math.round(percentage * 10.0) / 10.0; // round percentage to 1 decimal point
    }      

    public double getPercentage(int numerator, int denominator) {
        double percentage  = numerator > 0 ? 100.0 * numerator / denominator : 0.0;
        return Math.round(percentage * 10.0) / 10.0; // round percentage to 1 decimal point
    }      
    
    public int getCount(String query) {
       int count  = 0;
       try {
           jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                count = resultSet.getInt("count");
            }
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }
        return count;
    }      

    public void executeUpdate(String query) {
        try {
            jdbcUtil = new JDBCUtil();
            preparedStatement = jdbcUtil.getStatement(query);
            preparedStatement.executeUpdate();
        }
        catch (Exception exception) {
            jdbcUtil.disconnectFromDatabase();  //disconnect from database
        }        
    }        

}
