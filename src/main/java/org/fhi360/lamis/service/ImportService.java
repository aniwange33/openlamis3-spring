/**
 * @author aalozie
 */

package org.fhi360.lamis.service;

import lombok.extern.slf4j.Slf4j;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.Constants;
import org.fhi360.lamis.utility.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class ImportService {
    private final ApplicationProperties applicationProperties;
    private final XmlParserDelegator xmlParserDelegator;

    public ImportService(ApplicationProperties applicationProperties, XmlParserDelegator xmlParserDelegator) {
        this.applicationProperties = applicationProperties;
        this.xmlParserDelegator = xmlParserDelegator;
    }


    public void processXml(String fileName) {
        String contextPath = applicationProperties.getContextPath();
        //session.setAttribute("isImport", true);
        FileUtil fileUtil = new FileUtil();

        String directory = contextPath + "transfer/";
        fileUtil.makeDir(directory);

        String zipFile = directory + fileName;
        try {
            fileUtil.unzip(zipFile);
            String[] tables = Constants.Tables.TRANSACTION_TABLES.split(",");
            for (String table : tables) {
                LOG.info("Processing {} ...", table);
                //session.setAttribute("processingStatus", table);
                String filePath = contextPath + "exchange/" + table + ".xml";
                File file = new File(filePath);
                if (file.exists()) {
                    org.fhi360.lamis.service.parser.xml.XmlParserDelegator xmlParserDelegator = new org.fhi360.lamis.service.parser.xml.XmlParserDelegator();
                    xmlParserDelegator.delegate(table, filePath, null);
                    file.delete();
                }
            }
        } catch (Exception ex) {
            //session.setAttribute("processingStatus", "terminated");
            ex.printStackTrace();
        }
        //session.setAttribute("isImport", false);
        //session.setAttribute("processingStatus", "completed");
    }
}
