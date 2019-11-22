/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;
import java.math.BigDecimal;
import java.util.Date;
import org.fhi360.lamis.exchange.ndr.schema.FacilityType;
import org.fhi360.lamis.exchange.ndr.schema.MessageHeaderType;
import org.fhi360.lamis.utility.DateUtil;
import org.springframework.stereotype.Component;

/**
 *
 * @author user1
 */
@Component
public class MessageHeaderTypeMapper {
    public MessageHeaderType messageHeaderType(long patientId) {
        MessageHeaderType header = new MessageHeaderType();
        try {
            //Set the Header Information
            header.setMessageCreationDateTime(DateUtil.getXmlDateTime(new Date()));
            header.setMessageSchemaVersion(new BigDecimal("1.2"));

            //Set the Sending Organization in the Header
            //In this scenario we are using a fictional IP
            FacilityType sendingOrganization = new FacilityType();
            sendingOrganization.setFacilityName("Family Health International (360)");
            sendingOrganization.setFacilityID("FHI360");
            sendingOrganization.setFacilityTypeCode("IP");
            header.setMessageSendingOrganization(sendingOrganization);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return header;                
    }
}
