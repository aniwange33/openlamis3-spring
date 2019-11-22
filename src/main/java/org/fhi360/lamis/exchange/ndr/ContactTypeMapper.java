/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

import org.fhi360.lamis.exchange.ndr.schema.ContactType;

/**
 *
 * @author user1
 */
public class ContactTypeMapper {
    public ContactType contactType(long patientId) {        
        ContactType contact = new ContactType();
        return contact;
    }
}
