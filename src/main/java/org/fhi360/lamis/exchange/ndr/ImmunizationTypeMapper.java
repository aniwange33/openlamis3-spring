/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.fhi360.lamis.exchange.ndr.schema.ImmunizationType;
/**
 *
 * @author user1
 */
public class ImmunizationTypeMapper {
    public ImmunizationType immunizationType(long patientId) {
        ImmunizationType immunization = new ImmunizationType();
        return immunization;                
    }
}
