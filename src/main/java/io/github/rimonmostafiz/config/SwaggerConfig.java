package io.github.rimonmostafiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author Rimon Mostafiz
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static String AUTHORIZATION_HEADER = "Authorization";

    public static String DEVELOPER = "Rimon Mostafiz";
    public static String DEVELOPER_EMAIL = "rimonmostafiz@gmail.com";
    public static String DEVELOPER_WEBSITE = "https://rimonmostafiz.github.io";

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(Collections.singletonList(apiKey()))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    public ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Task Manager REST API")
                .version("1.0.0")
                .description("Task Manager REST API")
                .contact(developerContact())
                .license("CC BY-NC-SA 4.0")
                .build();
    }

    public Contact developerContact() {
        return new Contact(DEVELOPER, DEVELOPER_WEBSITE, DEVELOPER_EMAIL);
    }
}
