package org.fhi360.lamis.utility;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;

public class WKHtmlWrapperConfig extends WrapperConfig {
    public String findExecutable() {
        return "C:/LAMIS2/wkhtmltopdf";
    }
}
