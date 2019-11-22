
package org.fhi360.lamis.exchange.nigqual;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.fhi360.lamis.config.ContextProvider;
import org.fhi360.lamis.config.ApplicationProperties;
import org.fhi360.lamis.utility.FileUtil;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
@Component
public class DocumentWriter {
    private static final ApplicationProperties lamisProperties = ContextProvider.getBean(ApplicationProperties.class);

    public static void writeXmlToFile(Document document, String table) {
        String contextPath = lamisProperties.getContextPath();
        String fileName = contextPath + "transfer/nigqual/" + table + ".xml";
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(fileName));
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(outputStream, format);
            writer.write(document);
            writer.flush();
            writer.close();
            outputStream.close();
            //Copy files to secondary locations
            FileUtil fileUtil = new FileUtil();
            fileUtil.makeDir(contextPath + "transfer/nigqual/");
            fileUtil.makeDir(lamisProperties.getContextPath() + "/transfer/nigqual/");
            fileName = table + ".xml";
            //for servlets in the default(root) context, copy file to the transfer folder in root
            if (!contextPath.equalsIgnoreCase(lamisProperties.getContextPath()))
                fileUtil.copyFile(fileName, contextPath + "transfer/nigqual/", lamisProperties.getContextPath() + "/transfer/nigqual/");

        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }

    }

}
