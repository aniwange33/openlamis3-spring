
package org.fhi360.lamis.controller.chart;

import io.swagger.annotations.Api;
import org.fhi360.lamis.config.ContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chart/paginationUtil")
@Api(tags = "PaginationUtil Chart", description = " ")
public class PaginationUtil {

    public PaginationUtil() {
    }

    @GetMapping("/paginateGrid")
    public Map<String, Object> paginateGrid(@RequestParam("page") int page, @RequestParam("rows") int rows,
                                            @RequestParam("table") String table) {
        HttpSession session = null;
        long facilityId = (Long) session.getAttribute("id");

        //calculate the number of rows for the query. We need this for paging the result
        String query = "SELECT COUNT(*) AS count FROM [table] WHERE facility_id = " + facilityId;
        int totalrecords = getCount(query.replace("[table]", table));

        // calculate the total pages for the query
        int totalpages = 0;
        System.out.println("Pages is: " + Math.ceil(totalrecords / rows));
        if (totalrecords > 0) {
            if (totalrecords > rows) {
                if (totalrecords % rows == 0)
                    totalpages = (int) Math.ceil(totalrecords / rows);
                else if (totalrecords % rows > 0)
                    totalpages = (int) Math.ceil(totalrecords / rows) + 1;
            } else {
                totalpages = 1;
            }
        }
        //totalpages = (int) Math.ceil(totalrecords / rows);

        // if for some reasons the requested page is greater than the total set the requested page to total page 
        if (page > totalpages) page = totalpages;

        // calculate the starting position of the rows to be retrieved
        int start = rows * page - rows;

        // if for some reasons start position is negative set it to 0. Typical case is that the user type 0 for the requested page 
        if (start < 0) start = 0;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("start", start);
        map.put("totalpages", totalpages);
        map.put("totalrecords", totalrecords);
        return map;
    }

    private int getCount(String query) {
        Integer count = ContextProvider.getBean(JdbcTemplate.class).queryForObject(query, Integer.class);
        return count != null ? count: 0;
    }
}
