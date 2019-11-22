/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

/**
 * @author user1
 */

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.AddressType;
import org.springframework.jdbc.core.JdbcTemplate;

public class AddressTypeMapper {
    private static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    public AddressTypeMapper() {
    }

    public AddressType addressType(long patientId) {
        AddressType address = new AddressType();
        try {
            address.setAddressTypeCode("H");
            address.setCountryCode("NGA");

            String query = "SELECT state, lga, address FROM patient WHERE patient_id = " + patientId;
            jdbcTemplate.query(query, rs -> {
                String state = rs.getString("state") == null ? "" : rs.getString("state");
                String lga = rs.getString("lga") == null ? "" : rs.getString("lga");
                //String home = rs.getString("address") == null ? "" : new Scrambler().unscrambleCharacters(rs.getString("address"));
                //If the state of origin of a client is not know don't set other data elements in address schema
                if (!state.isEmpty()) {
                    address.setStateCode(CodeSetResolver.getCode("STATES", state));
                    if (!lga.isEmpty()) address.setLGACode(CodeSetResolver.getCode("LGA", lga));
                    //if(!home.isEmpty()) address.setOtherAddressInformation(home);
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return address;
    }

}
