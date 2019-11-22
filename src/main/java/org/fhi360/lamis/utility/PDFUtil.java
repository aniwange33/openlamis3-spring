package org.fhi360.lamis.utility;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import org.apache.commons.io.IOUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class PDFUtil {

    public static byte[] generate(String template, Map<String, Object> parameters, Object dataSource, boolean landscape) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setCacheable(true);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        context.setVariables(parameters);
        context.setVariable("dataSource", dataSource);

        InputStream is = PDFUtil.class.getClassLoader().getResourceAsStream("style.css");
        String tmpDir = System.getProperty("java.io.tmpdir");
        try {
            if (is != null) {
                IOUtils.copy(is, new FileOutputStream(new File(tmpDir + "/style.css")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String content = templateEngine.process(template, context);
        WrapperConfig wrapperConfig = new WKHtmlWrapperConfig();
        Pdf pdf = new Pdf(wrapperConfig);

        pdf.addPageFromString(content);
        if (landscape) {
            pdf.addParam(new Param("-O", "Landscape"));
        }
        byte[] data = null;
        try {
            data = pdf.getPDF();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    static {
        try {
            SevenZ.decompress(PDFUtil.class.getClassLoader()
                    .getResource("wkhtmltopdf.7z").getPath(), new File("c:/lamis2/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
