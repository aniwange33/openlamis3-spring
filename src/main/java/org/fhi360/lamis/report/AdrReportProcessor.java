/**
 *
 * @author AALOZIE
 */

package org.fhi360.lamis.report;

import java.util.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.fhi360.lamis.utility.JDBCUtil;


public class AdrReportProcessor {
    private String reportingMonth;
    private String reportingYear;
    private String patientId;
    private HashMap parameterMap;
    
    private HttpServletRequest request;
    private HttpSession session;
    private String query;
    private JDBCUtil jdbcUtil;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public AdrReportProcessor() {
    } 
    
    public void adrReport() {
        
    }
    
    public HashMap getReportParameters(){
        parameterMap = new HashMap();
        //reportingMonth = DateUtil.getMonthNumber(request.getParameter("reportingMonth"));
        reportingYear = request.getParameter("reportingYear");
        return parameterMap;
    }    
}
