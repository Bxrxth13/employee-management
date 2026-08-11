package com.management.employee.service;

import com.management.employee.dto.EmployeeDto;

import java.util.List;

/**
 * Service interface defining business operations for Employee Management.
 */
public interface EmployeeService {

    /**
     * Create a new employee record.
     *
     * @param employeeDto DTO payload
     * @return Created EmployeeDto
     */
    EmployeeDto createEmployee(EmployeeDto employeeDto);

    /**
     * Retrieve an employee by ID.
     *
     * @param employeeId Unique employee ID
     * @return EmployeeDto
     */
    EmployeeDto getEmployeeById(Long employeeId);

    /**
     * Retrieve all employees.
     *
     * @return List of EmployeeDto objects
     */
    List<EmployeeDto> getAllEmployees();

    /**
     * Update an existing employee record.
     *
     * @param employeeId Unique employee ID to update
     * @param employeeDto Updated details
     * @return Updated EmployeeDto
     */
    EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto);

    /**
     * Delete an employee record by ID.
     *
     * @param employeeId Unique employee ID
     */
    void deleteEmployee(Long employeeId);
}
