package io.github.rimonmostafiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .globalResponses(HttpMethod.GET, getGlobalResponse())
                .globalResponses(HttpMethod.POST, getGlobalResponse())
                .globalResponses(HttpMethod.PUT, getGlobalResponse())
                .globalResponses(HttpMethod.PATCH, getGlobalResponse())
                .globalResponses(HttpMethod.DELETE, getGlobalResponse())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.rimonmostafiz.api"))
                .paths(PathSelectors.any())
                .build();
    }



    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        var authScope = new AuthorizationScope("global", "accessEverything");
        var authorizationScopes = new AuthorizationScope[]{authScope};
        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Task Manager REST API")
                .version("1.0.0")
                .description("Task Manager REST API")
                .contact(developerContact())
                .license("CC BY-NC-SA 4.0")
                .build();
    }

    private Contact developerContact() {
        return new Contact(DEVELOPER, DEVELOPER_WEBSITE, DEVELOPER_EMAIL);
    }

    private List<Response> getGlobalResponse() {
        List<Response> response = new ArrayList<>();
        response.add(new ResponseBuilder().code("200").description("Success").build());
        response.add(new ResponseBuilder().code("201").description("Created").build());
        response.add(new ResponseBuilder().code("400").description("Bad Request").build());
        response.add(new ResponseBuilder().code("401").description("You are not authorized to view the resource").build());
        response.add(new ResponseBuilder().code("403").description("Resource you were trying to access is forbidden").build());
        response.add(new ResponseBuilder().code("404").description("The resource you were trying to reach is not found").build());
        response.add(new ResponseBuilder().code("500").description("Internal Server Error").build());
        return response;
    }
}
