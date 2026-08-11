package com.management.employee.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger UI configuration bean.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System REST API")
                        .description("Production-Ready Spring Boot REST API for managing employees with PostgreSQL, Docker, and Jenkins CI/CD pipeline integration.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DevOps Team")
                                .email("devops@company.com")
                                .url("https://github.com/company/employee-management"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
