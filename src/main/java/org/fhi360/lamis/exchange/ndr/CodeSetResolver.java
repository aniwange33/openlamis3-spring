/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.exchange.ndr;

/**
 * @author user1
 */

import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.exchange.ndr.schema.CodedSimpleType;
import org.fhi360.lamis.exchange.ndr.schema.FacilityType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeSetResolver {
    private static JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);
    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate = ContextProvider.getBean(NamedParameterJdbcTemplate.class);

    public CodeSetResolver() {
    }

    public static CodedSimpleType getRegimen(String regimen) {
        final CodedSimpleType cst = new CodedSimpleType();
        String query = "SELECT composition, regimentype_id FROM regimen WHERE description = ?";
        jdbcTemplate.query(query, rs -> {
            String regimentypeId = Long.toString(rs.getLong("regimentype_id"));
            String regimen1 = rs.getString("composition").trim() + "_" + regimentypeId;
            String query1 = "SELECT * FROM ndrcodeset WHERE sys_description = ?";
            jdbcTemplate.query(query1, rs1 -> {
                cst.setCode(rs1.getString("code"));
                cst.setCodeDescTxt(rs1.getString("code_description"));
            }, regimen1);
            if (cst.getCode() == null) {
                String regimen2 = "Others_" + regimentypeId;
                String query2 = "SELECT * FROM ndrcodeset WHERE sys_description = ?";
                jdbcTemplate.query(query2, rs2 -> {
                    cst.setCode(rs2.getString("code"));
                    cst.setCodeDescTxt(rs2.getString("code_description"));
                }, regimen2);
            }
        }, regimen);
        return cst;
    }

    public static CodedSimpleType getRegimenById(long regimenId) {
        final CodedSimpleType cst = new CodedSimpleType();
        String query1 = "SELECT composition, regimentype_id FROM regimen WHERE regimen_id = " + regimenId;
        jdbcTemplate.query(query1, rs -> {
            String regimentypeId = Long.toString(rs.getLong("regimentype_id"));
            String regimen = rs.getString("composition").trim() + "_" + regimentypeId;
            String query2 = "SELECT * FROM ndrcodeset WHERE sys_description = ?";
            jdbcTemplate.query(query2, rs1 -> {
                cst.setCode(rs1.getString("code"));
                cst.setCodeDescTxt(rs1.getString("code_description"));
            }, regimen);
            if (cst.getCode() == null) {
                String regimen2 = "Others_" + regimentypeId;
                String query3 = "SELECT * FROM ndrcodeset WHERE sys_description = ?";
                jdbcTemplate.query(query3, rs2 -> {
                    cst.setCode(rs2.getString("code"));
                    cst.setCodeDescTxt(rs2.getString("code_description"));
                }, regimen2);
            }
        });
        return cst;
    }

    public static CodedSimpleType getCodedSimpleType(String codeSetNm, String description) {
        CodedSimpleType cst = new CodedSimpleType();
        Map<String, Object> params = new HashMap<>();
        String query = "SELECT * FROM ndrcodeset WHERE code_set_nm = :code AND sys_description = :desc";
        params.put("code", codeSetNm);
        params.put("desc", description);
        if (codeSetNm.contains(",")) {
            String[] codes = codeSetNm.split(",");
            params.put("code", Arrays.asList(codes));
            query = "SELECT * FROM ndrcodeset WHERE code_set_nm IN (:code) AND sys_description = :desc";
        }
        namedParameterJdbcTemplate.query(query, params, rs -> {
            cst.setCode(rs.getString("code"));
            cst.setCodeDescTxt(rs.getString("code_description"));
        });
        return cst;
    }

    public static String getCode(String codeSetNm, String description) {
        String id = "";
        String query = "SELECT code FROM ndrcodeset WHERE code_set_nm = ? AND sys_description = ?";
        List<String> codes = jdbcTemplate.queryForList(query, String.class, codeSetNm, description);
        if (!codes.isEmpty()) {
            return codes.get(0);
        }
        return id;
    }


    //This method retrieves the FMoH assigned facility ID from the NDR codeset table
    //This ID is used in place of the internal facility ID
    public static FacilityType getFacility(long facilityId) {
        FacilityType facility = new FacilityType();
        String description = Long.toString(facilityId);
        String query = "SELECT * FROM ndrcodeset WHERE sys_description = ?";
        jdbcTemplate.query(query, rs -> {
            facility.setFacilityName(rs.getString("code_description"));
            facility.setFacilityID(rs.getString("code"));
            facility.setFacilityTypeCode("FAC");
        }, description);
        return facility;
    }
}
