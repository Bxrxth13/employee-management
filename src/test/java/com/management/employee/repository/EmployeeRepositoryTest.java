package com.management.employee.repository;

import com.management.employee.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@company.com")
                .department("Finance")
                .designation("Financial Analyst")
                .salary(88000.00)
                .joiningDate(LocalDate.of(2023, 3, 1))
                .build();
    }

    @Test
    @DisplayName("Repository - Save & Find By ID")
    void givenEmployee_whenSave_thenReturnSavedEmployee() {
        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getEmployeeId()).isNotNull();

        Optional<Employee> found = employeeRepository.findById(savedEmployee.getEmployeeId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alice.smith@company.com");
    }

    @Test
    @DisplayName("Repository - Exists By Email")
    void givenEmployeeEmail_whenExistsByEmail_thenReturnTrue() {
        employeeRepository.save(employee);

        boolean exists = employeeRepository.existsByEmail("alice.smith@company.com");
        boolean notExists = employeeRepository.existsByEmail("unknown@company.com");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Repository - Find By Email")
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findByEmail("alice.smith@company.com");

        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Alice");
    }
}
