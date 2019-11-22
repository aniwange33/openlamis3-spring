package org.fhi360.lamis.model.dto;

import lombok.Data;
import org.fhi360.lamis.utility.DateUtil;

@Data
public class NigqualServiceDTO {
    private  String reportingMonthBegin ;
    private  String reportingYearBegin ;
    private  String  reportingMonthEnd ;
    private  String  reportingYearEnd;
    private  String reviewPeriodId;
    private  String portalId;
    private  String rnl;
}
