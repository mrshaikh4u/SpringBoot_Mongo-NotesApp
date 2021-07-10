package com.mycomp.notesApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This is Application's configuration class serves centralized management of
 * confs
 * 
 * @author Rahil
 *
 */

@Configuration
@EnableTransactionManagement
@EnableSwagger2
public class ApplicationConfiguration {

	/**
	 * Used to enable swagger to document REST APIs
	 * 
	 * @return
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
		                                              .apis(RequestHandlerSelectors.basePackage(
		                                                      "com.mycomp.notesApp.controller"))
		                                              .paths(PathSelectors.regex("/.*"))
		                                              .build()
		                                              .apiInfo(apiEndPointsInfo());
													 
	}

	/**
	 * Swagger REST APIs
	 * 
	 * @return
	 */
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("REST APIs Documentation")
		                           .description(
		                                   "This page describes REST endpoints used in notes application and short description for each APIs including input and outputs for more details feel free to reach @ below contact")
		                           .contact(new Contact("Rahil Shaikh", "https://www.test.com/en/",
		                                   "mohamedrshaikh@gmail.com"))
		                           .build();
	}

	/**
	 * Used to enable CORS in application
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				        .allowedOrigins("http://localhost");
			}

		};
	}

}