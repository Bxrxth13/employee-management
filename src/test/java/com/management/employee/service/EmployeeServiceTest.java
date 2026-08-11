package com.management.employee.service;

import com.management.employee.dto.EmployeeDto;
import com.management.employee.entity.Employee;
import com.management.employee.exception.DuplicateEmailException;
import com.management.employee.exception.EmployeeNotFoundException;
import com.management.employee.repository.EmployeeRepository;
import com.management.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@company.com")
                .department("Engineering")
                .designation("Senior Engineer")
                .salary(95000.00)
                .joiningDate(LocalDate.of(2024, 1, 15))
                .build();

        employeeDto = EmployeeDto.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@company.com")
                .department("Engineering")
                .designation("Senior Engineer")
                .salary(95000.00)
                .joiningDate(LocalDate.of(2024, 1, 15))
                .build();
    }

    @Test
    @DisplayName("Create Employee - Success")
    void givenEmployeeDto_whenCreateEmployee_thenReturnSavedEmployeeDto() {
        given(employeeRepository.existsByEmail(anyString())).willReturn(false);
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        EmployeeDto savedDto = employeeService.createEmployee(employeeDto);

        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getEmployeeId()).isEqualTo(1L);
        assertThat(savedDto.getEmail()).isEqualTo("john.doe@company.com");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Create Employee - Duplicate Email Exception")
    void givenExistingEmail_whenCreateEmployee_thenThrowDuplicateEmailException() {
        given(employeeRepository.existsByEmail(employeeDto.getEmail())).willReturn(true);

        assertThrows(DuplicateEmailException.class, () -> employeeService.createEmployee(employeeDto));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Get Employee By ID - Success")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeDto() {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        EmployeeDto foundDto = employeeService.getEmployeeById(1L);

        assertThat(foundDto).isNotNull();
        assertThat(foundDto.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("Get Employee By ID - Not Found Exception")
    void givenInvalidId_whenGetEmployeeById_thenThrowEmployeeNotFoundException() {
        given(employeeRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(99L));
    }

    @Test
    @DisplayName("Get Employee By Email - Success")
    void givenEmail_whenGetEmployeeByEmail_thenReturnEmployeeDto() {
        given(employeeRepository.findByEmail("john.doe@company.com")).willReturn(Optional.of(employee));

        EmployeeDto foundDto = employeeService.getEmployeeByEmail("john.doe@company.com");

        assertThat(foundDto).isNotNull();
        assertThat(foundDto.getEmail()).isEqualTo("john.doe@company.com");
    }

    @Test
    @DisplayName("Get Employees By Department - Success")
    void givenDepartment_whenGetEmployeesByDepartment_thenReturnEmployeeDtoList() {
        Employee secondEmployee = Employee.builder()
                .employeeId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@company.com")
                .department("Engineering")
                .designation("HR Manager")
                .salary(85000.00)
                .joiningDate(LocalDate.of(2023, 5, 10))
                .build();

        given(employeeRepository.findByDepartment("Engineering")).willReturn(List.of(employee, secondEmployee));

        List<EmployeeDto> employeeList = employeeService.getEmployeesByDepartment("Engineering");

        assertThat(employeeList).hasSize(2);
        assertThat(employeeList).extracting(EmployeeDto::getDepartment).containsOnly("Engineering");
    }

    @Test
    @DisplayName("Get All Employees - Success")
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeDtoList() {
        Employee secondEmployee = Employee.builder()
                .employeeId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@company.com")
                .department("HR")
                .designation("HR Manager")
                .salary(85000.00)
                .joiningDate(LocalDate.of(2023, 5, 10))
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, secondEmployee));

        List<EmployeeDto> employeeList = employeeService.getAllEmployees();

        assertThat(employeeList).hasSize(2);
        assertThat(employeeList.get(0).getFirstName()).isEqualTo("John");
        assertThat(employeeList.get(1).getFirstName()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("Update Employee - Success")
    void givenUpdatedDetails_whenUpdateEmployee_thenReturnUpdatedEmployeeDto() {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        given(employeeRepository.existsByEmailAndEmployeeIdNot("john.updated@company.com", 1L)).willReturn(false);
        given(employeeRepository.save(any(Employee.class))).willReturn(employee);

        employeeDto.setEmail("john.updated@company.com");
        employeeDto.setSalary(105000.00);

        EmployeeDto updatedDto = employeeService.updateEmployee(1L, employeeDto);

        assertThat(updatedDto).isNotNull();
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("Delete Employee - Success")
    void givenEmployeeId_whenDeleteEmployee_thenRepositoryDeleteIsCalled() {
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }
}
