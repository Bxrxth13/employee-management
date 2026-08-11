package com.management.employee.repository;

import com.management.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository interface for Employee entity operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Check if an employee with the given email exists.
     *
     * @param email Email to search
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find an employee by email.
     *
     * @param email Email to search
     * @return Optional containing Employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Find employees by department.
     *
     * @param department Department to search
     * @return List of employees in that department
     */
    List<Employee> findByDepartment(String department);

    /**
     * Check if an employee with the given email exists excluding a specific employee ID (used during updates).
     *
     * @param email Email to search
     * @param employeeId Employee ID to exclude
     * @return true if email exists for another employee
     */
    boolean existsByEmailAndEmployeeIdNot(String email, Long employeeId);
}
