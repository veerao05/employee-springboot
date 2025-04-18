package com.employee.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.employee.TestDataFactory;
import com.employee.dao.Employee;
import com.employee.dto.EmployeeDto;
import com.employee.response.EmployeeResponse;
import com.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto validEmployeeDto;
    private Employee existingEmployee;

    @BeforeEach
    void setUp() {

        validEmployeeDto = TestDataFactory.buildTestEmployeeDtoWithFullData();
        existingEmployee = employeeService.saveEmployee(validEmployeeDto);
    }

    @Test
    void getAllEmployees_ShouldReturnPaginatedResults() throws Exception {
        // Create test data
        for (int i = 0; i < 15; i++) {
            EmployeeDto dto = TestDataFactory.buildTestEmployeeDtoWithFullData();
            dto.setFirstName("Employee-" + i);
            dto.setEmail("employee" + i + "@example.com");
            employeeService.saveEmployee(dto);
        }

        // Test pagination parameters
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .param("offset", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "firstName")
                        .param("dir", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        EmployeeResponse<Employee> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new com.fasterxml.jackson.core.type.TypeReference<>() {
                });

        assertThat(response.getData()).hasSize(5);
        assertThat(response.getPagination().getPage()).isEqualTo(1);
        assertThat(response.getPagination().getTotalPages()).isEqualTo(5); // 16 total / 5 per page
        assertThat(response.getData().get(0).getFirstName()).startsWith("Employee-");
    }

    @Test
    void getEmployeeById_ShouldReturnEmployeeWhenExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", existingEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingEmployee.getId()))
                .andExpect(jsonPath("$.firstName").value(validEmployeeDto.getFirstName()));
    }

    @Test
    void getEmployeeById_ShouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employees/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        EmployeeDto newEmployee = TestDataFactory.buildTestEmployeeDtoWithFullData();
        newEmployee.setEmail("new.employee@example.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("new.employee@example.com"));
    }

    @Test
    void createEmployee_ShouldReturnValidationErrors() throws Exception {
        EmployeeDto invalidEmployee = EmployeeDto.builder().build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.firstName").value(containsString("First name is required")))
                .andExpect(jsonPath("$.email").value(containsString("Email is required")))
                .andExpect(jsonPath("$.employmentType").value(containsString("Employment type is required")))
                .andExpect(jsonPath("$.emergencyContact").value(containsString("Emergency contact is required")))
                .andExpect(jsonPath("$.jobTitle").value(containsString("Job title is required")))
                .andExpect(jsonPath("$.department").value(containsString("Department is required")))
                .andExpect(jsonPath("$.phoneNo").value(containsString("Phone number is required")))
                .andReturn();
    }

    @Test
    void updateEmployee_ShouldUpdateExistingEmployee() throws Exception {
        EmployeeDto updateDto = TestDataFactory.buildTestEmployeeDtoWithFullData();
        updateDto.setFirstName("UpdatedFirstName");
        updateDto.setLastName("UpdatedLastName");

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/{id}", existingEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedFirstName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLastName"));
    }

    @Test
    void updateEmployee_ShouldReturn404ForNonExistingId() throws Exception {
        EmployeeDto updateDto = TestDataFactory.buildTestEmployeeDtoWithFullData();

        mockMvc.perform(MockMvcRequestBuilders.put("/employees/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", existingEmployee.getId()))
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteEmployee_ShouldReturn404ForNonExistingId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/9999"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllEmployees_ShouldReturnEmptyListWhenNoRecords() throws Exception {
        // Delete the existing employee
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", existingEmployee.getId()))
                .andExpect(status().isNoContent());

        // Verify that no employees exist
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        EmployeeResponse<Employee> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new com.fasterxml.jackson.core.type.TypeReference<>() {
                });

        assertThat(response.getData()).isEmpty();
        assertThat(response.getPagination().getTotalResults()).isZero();
    }}
