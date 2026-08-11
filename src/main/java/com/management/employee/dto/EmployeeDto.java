package com.management.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object for Employee API requests and responses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee Data Transfer Object")
public class EmployeeDto {

    @Schema(description = "Unique identifier of the employee", example = "1")
    private Long employeeId;

    @Schema(description = "First name of the employee", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Last name of the employee", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Email address of the employee", example = "john.doe@company.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Department of the employee", example = "Engineering", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Department is required")
    private String department;

    @Schema(description = "Designation or title of the employee", example = "Senior Software Engineer")
    private String designation;

    @Schema(description = "Annual salary of the employee", example = "95000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private Double salary;

    @Schema(description = "Joining date of the employee", example = "2024-01-15")
    private LocalDate joiningDate;
}
