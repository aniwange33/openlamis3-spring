package org.fhi360.lamis.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.fhi360.lamis.controller.AncController;
import org.fhi360.lamis.controller.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@EnableSwagger2
@ComponentScan(basePackageClasses = AncController.class)
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).ignoredParameterTypes(HttpSession.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.fhi360.lamis")).build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfo("LAMIS", "", "1.0",
                "Terms of Service", new Contact("Health Informatics",
                "http://lamis3.sidhas.org:8080", "alozie@fhi360,org"), "", "",
                new ArrayList<>());
    }
}
