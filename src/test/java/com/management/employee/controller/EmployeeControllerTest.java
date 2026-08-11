package com.management.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.employee.dto.EmployeeDto;
import com.management.employee.exception.EmployeeNotFoundException;
import com.management.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
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
    @DisplayName("POST /employees - Create Employee Success")
    void givenEmployeeDto_whenCreateEmployee_thenReturnCreated() throws Exception {
        given(employeeService.createEmployee(any(EmployeeDto.class))).willReturn(employeeDto);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@company.com")));
    }

    @Test
    @DisplayName("POST /employees - Validation Error Bad Request")
    void givenInvalidEmployeeDto_whenCreateEmployee_thenReturnBadRequest() throws Exception {
        EmployeeDto invalidDto = EmployeeDto.builder()
                .firstName("") // Empty first name
                .lastName("Doe")
                .email("invalid-email") // Invalid email pattern
                .department("Engineering")
                .salary(-100.0) // Negative salary
                .build();

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Failed")));
    }

    @Test
    @DisplayName("GET /employees - Get All Employees")
    void whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        given(employeeService.getAllEmployees()).willReturn(List.of(employeeDto));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    @DisplayName("GET /employees/{id} - Get Employee By ID")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        given(employeeService.getEmployeeById(1L)).willReturn(employeeDto);

        mockMvc.perform(get("/employees/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    @DisplayName("GET /employees/{id} - Not Found")
    void givenInvalidId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        given(employeeService.getEmployeeById(99L)).willThrow(new EmployeeNotFoundException(99L));

        mockMvc.perform(get("/employees/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /employees/{id} - Update Employee")
    void givenEmployeeIdAndDto_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        given(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).willReturn(employeeDto);

        mockMvc.perform(put("/employees/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    @DisplayName("DELETE /employees/{id} - Delete Employee")
    void givenEmployeeId_whenDeleteEmployee_thenReturnOkMessage() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/employees/{id}", 1L))
                .andExpect(status().isOk());
    }
}
