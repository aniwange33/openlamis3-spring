/**
 * @author AALOZIE
 */

package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.utility.builder.DevolveListBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chart/devolve")
@Api(tags = "Devolve Grid Chart", description = " ")
public class DevolveGridAction {
    private final JdbcTemplate jdbcTemplate = ContextProvider.getBean(JdbcTemplate.class);

    @GetMapping("/grid")
    public ResponseEntity devolveGrid(@RequestParam("id") Long patientId) {
        String query = "SELECT devolve.*, communitypharm.* FROM devolve LEFT JOIN communitypharm ON " +
                "devolve.communitypharm_id = communitypharm.communitypharm_id WHERE patient_id = ? ORDER BY date_devolved DESC";
        List devolveList = jdbcTemplate.query(query, resultSet-> {
            new DevolveListBuilder().buildDevolveList(resultSet);
            return new DevolveListBuilder().retrieveDevolveList();
        }, patientId);
        return ResponseEntity.ok(devolveList);
    }
}
