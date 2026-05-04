package com.equabli.collectprism.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.equabli.domain.helpers.CommonConstants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

//    public static final Contact DEFAULT_CONTACT = new Contact(CommonConstants.CONTACT_INFO_NAME, CommonConstants.CONTACT_INFO_URL, CommonConstants.CONTACT_INFO_EMAIL);
//
//    private static final String API_VERSION = "1.0";
//
//    private static final String API_TITLE = "Data Scrubbing API v" + API_VERSION + " - REST";
//
//    private static final String API_DESC = "This document provides the API details for data scrubbing services";
//
//    private static final String API_TOS = "https://www.equabli.com/privacy-policy";
//
//    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(API_TITLE, API_DESC, API_VERSION, API_TOS, DEFAULT_CONTACT,
//            "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Arrays.asList());
//
//    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json"));
//
//    private static final List<SecurityScheme> securitySchemes = Arrays.asList(new ApiKey(AUTHORIZATION, AUTHORIZATION, HEADER.getIn()), new ApiKey(REQUEST_ORIGIN, REQUEST_ORIGIN, HEADER.getIn()));
//
//    private static final List<SecurityReference> securityReferences = Arrays.asList(new SecurityReference(AUTHORIZATION, new AuthorizationScope[0]), new SecurityReference(REQUEST_ORIGIN, new AuthorizationScope[0]));
//
//    private static final List<SecurityContext> securityContexts = Arrays.asList(SecurityContext.builder().securityReferences(securityReferences).build());
//
//    @Bean
//    @DependsOn("serviceHostUrls")
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .securitySchemes(securitySchemes)
//                .securityContexts(securityContexts)
//                .host(formatHosting(dataScrubbingServiceHost))
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.equabli.datascrubbing.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(DEFAULT_API_INFO)
//                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
//                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
//    }
	
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String JWT = "JWT";
	private static final String API_KEY = "apiKey";
	
	@Bean
	public OpenAPI customOpenAPI() {
		 Contact DEFAULT_CONTACT = new Contact();
		 DEFAULT_CONTACT.setEmail(CommonConstants.CONTACT_INFO_EMAIL);
		 DEFAULT_CONTACT.setUrl(CommonConstants.CONTACT_INFO_URL);
		 DEFAULT_CONTACT.setName(CommonConstants.CONTACT_INFO_NAME);

		return new OpenAPI()
				.components(new Components().addSecuritySchemes(AUTHORIZATION, authorizationSecuritySchema())
						//.addSecuritySchemes(API_KEY, apiKeySecuritySchema())
						)
				.addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION).addList(API_KEY));
	}

	public SecurityScheme authorizationSecuritySchema() {
		return new SecurityScheme().name("bearerAuth").description(JWT).in(SecurityScheme.In.HEADER)
				.description(AUTHORIZATION).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat(JWT);
	}
	
} 