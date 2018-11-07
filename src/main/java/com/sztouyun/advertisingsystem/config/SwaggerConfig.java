package com.sztouyun.advertisingsystem.config;

import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import com.sztouyun.advertisingsystem.web.security.JwtTokenUtil;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig  extends WebMvcConfigurerAdapter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EnvironmentConfig environmentConfig;

    @Value("${internalapi.header.key}")
    private String internalApiHeadKey;

    @Value("${internalapi.header.value}")
    private String internalApiHeadValue;

    @Value("${openapi.header.key}")
    private String openApiHeadKey;

    @Value("${openapi.header.value}")
    private String openApiHeadValue;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars*")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    @Profile({"local","dev","test"})
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        if(environmentConfig.isDev() || environmentConfig.isTest()){
            var authorizationCode="Bearer "+jwtTokenUtil.generateToken(AuthenticationService.getAdminUser());
            parameterBuilder.name("Authorization").description("Authorization").defaultValue(authorizationCode).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
            parameters.add(parameterBuilder.build());
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("广告系统API接口"))
                .globalOperationParameters(parameters)
                .groupName("1.PortalAPI")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sztouyun.advertisingsystem.api"))
                .paths(PathSelectors.any())
                .build();
    }
	
	@Bean
    @Profile({"local","dev","test","stage"})
    public Docket createOpenApi() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name(openApiHeadKey).description(openApiHeadKey).defaultValue(openApiHeadValue).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("广告系统OpenAPI接口"))
                .globalOperationParameters(parameters)
                .groupName("3.OpenAPI")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sztouyun.advertisingsystem.openapi"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @Profile({"local","dev","test","stage"})
    public Docket createInternalAPI() {
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder.name(internalApiHeadKey).description(internalApiHeadKey).defaultValue(internalApiHeadValue).modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("广告系统InternalAPI接口"))
                .globalOperationParameters(parameters)
                .groupName("2.InternalAPI")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sztouyun.advertisingsystem.internalapi"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(String title) {
        return new ApiInfoBuilder()
                .title(title)
                .license("")
                .contact("")
                .description("")
                .version("1.0")
                .build();
    }
}
