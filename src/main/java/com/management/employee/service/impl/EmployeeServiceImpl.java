package com.management.employee.service.impl;

import com.management.employee.dto.EmployeeDto;
import com.management.employee.entity.Employee;
import com.management.employee.exception.DuplicateEmailException;
import com.management.employee.exception.EmployeeNotFoundException;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EmployeeService interface providing core business logic.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Constructor Injection enforcing SOLID Dependency Inversion Principle.
     *
     * @param employeeRepository JPA Repository dependency
     */
    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new DuplicateEmailException("Employee already exists with email: " + employeeDto.getEmail());
        }

        Employee employee = mapToEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToDto(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        return mapToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (employeeRepository.existsByEmailAndEmployeeIdNot(employeeDto.getEmail(), employeeId)) {
            throw new DuplicateEmailException("Email " + employeeDto.getEmail() + " is already in use by another employee");
        }

        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setEmail(employeeDto.getEmail());
        existingEmployee.setDepartment(employeeDto.getDepartment());
        existingEmployee.setDesignation(employeeDto.getDesignation());
        existingEmployee.setSalary(employeeDto.getSalary());
        existingEmployee.setJoiningDate(employeeDto.getJoiningDate());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return mapToDto(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        employeeRepository.delete(employee);
    }

    /**
     * Helper method to map EmployeeDto to Employee JPA Entity.
     */
    private Employee mapToEntity(EmployeeDto dto) {
        return Employee.builder()
                .employeeId(dto.getEmployeeId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .designation(dto.getDesignation())
                .salary(dto.getSalary())
                .joiningDate(dto.getJoiningDate())
                .build();
    }

    /**
     * Helper method to map Employee JPA Entity to EmployeeDto.
     */
    private EmployeeDto mapToDto(Employee entity) {
        return EmployeeDto.builder()
                .employeeId(entity.getEmployeeId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .department(entity.getDepartment())
                .designation(entity.getDesignation())
                .salary(entity.getSalary())
                .joiningDate(entity.getJoiningDate())
                .build();
    }
}
