package com.springprojects.banking_application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title="Banking Application",
				description = "Backend Banking application using Java, Spring Boot, Spring Data JPA, PostgreSQL DB, API documentation with Swagger",
				version = "v1.0",
				contact = @Contact(
						name ="Divya B",
						email="divyabalagopal2000@gmail.com",
						url="https://github.com/divyabalagopal/BankingSystemApplication"
				),
				license = @License(
						name="Banking Application",
						url = "https://github.com/divyabalagopal/BankingSystemApplication"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Backend Banking application using Java, Spring Boot, Spring Data JPA, MySQL DB, API documentation with Swagger",
				url = "https://github.com/divyabalagopal/BankingSystemApplication"
		)
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
