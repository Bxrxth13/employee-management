package com.management.employee.controller;

import com.management.employee.dto.EmployeeDto;
import com.management.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller providing CRUD endpoints for managing Employees.
 */
@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Controller", description = "REST APIs for Employee Management operations")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructor injection for EmployeeService.
     *
     * @param employeeService Business logic service dependency
     */
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Create a new Employee.
     *
     * @param employeeDto Input employee details
     * @return Created EmployeeDto with 201 Created status
     */
    @Operation(summary = "Create Employee", description = "Add a new employee record to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payload or duplicate email"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    /**
     * Get list of all Employees.
     *
     * @return List of EmployeeDto objects with 200 OK status
     */
    @Operation(summary = "Get All Employees", description = "Fetch all employee records from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * Get Employee by ID.
     *
     * @param id Unique employee ID
     * @return EmployeeDto with 200 OK status
     */
    @Operation(summary = "Get Employee By ID", description = "Fetch a single employee record by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(
            @Parameter(description = "Employee ID", example = "1") @PathVariable("id") Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Update existing Employee by ID.
     *
     * @param id Unique employee ID
     * @param employeeDto Updated details
     * @return Updated EmployeeDto with 200 OK status
     */
    @Operation(summary = "Update Employee", description = "Update an existing employee record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payload or email conflict"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @Parameter(description = "Employee ID", example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * Delete Employee by ID.
     *
     * @param id Unique employee ID
     * @return Success message with 200 OK status
     */
    @Operation(summary = "Delete Employee", description = "Delete an employee record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @Parameter(description = "Employee ID", example = "1") @PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully with ID: " + id);
    }
}
